from math import exp
from random import random, shuffle
from csv import reader


def parse_csv_data(
    filename,
    skip_amount,  # Amount of skips at the start (for example if there is a description at the start of data file)
    shuffle_data,  # Should we randomise the order of the data?
    param_start,  # Parameters start index
    param_end,  # Parameters end index
    classification_start,  # Classification (expected output) start index
    classification_interpret_func,  # Function to interpret how the classification is given in the CSV file and convert it for use to the network
):  # General function to parse data from a CSV file and get network parameters, classifications and actual classifications
    parameters = []
    classifications = []
    actual_classification = []  # The actual classification as given in the data
    with open(filename, mode="r") as file:
        for _ in range(skip_amount):
            next(file)
        data = list(reader(file))  # Create a list of the irises (2d list)
        if shuffle_data:
            shuffle(
                data
            )  # Make sure we always train on different subset of the training data
        for data_point in data:
            new_params = [float(n) for n in data_point[param_start:param_end]]
            parameters.append(new_params)
            classification = data_point[classification_start]
            actual_classification.append(classification)
            classifications.append(classification_interpret_func(classification))
    return parameters, classifications, actual_classification


def dot_product(xs, ys):
    # Dot product of two vectors (lists of floats)
    return sum(x * y for x, y in zip(xs, ys))


def sigmoid(x):
    # Linear sigmoid activation function
    return 1.0 / (1.0 + exp(-x))


def sigmoid_derivative(x):
    # Derivate of a linear sigmoid activation function
    sig = sigmoid(x)
    return sig * (1 - sig)


def normalize(dataset):
    # Normalize ("clean") dataset (two-dimensional list of floats)
    # using feature scaling operation newV = (oldV - min) / (max - min)
    # which scales any arbitrary number to a number between range 0 - 1
    for col_num in range(len(dataset[0])):
        column = [row[col_num] for row in dataset]
        minimum = min(column)
        for row_num in range(len(dataset)):
            value = dataset[row_num][col_num]
            dataset[row_num][col_num] = value - minimum / (max(column) - minimum)


class Neuron:
    def __init__(
        self,
        weights,
        learn_rate,
        act_func,  # activation function
        der_act_func,  # derivate activation function
    ):
        self.weights = weights
        self.learn_rate = learn_rate
        self.act_func = act_func
        self.der_act_func = der_act_func
        self.output_cache = 0.0
        self.delta = 0.0

    def output(self, inputs):
        self.output_cache = dot_product(
            inputs, self.weights
        )  # Combine inputs and neuron's weights with dot product
        return self.act_func(self.output_cache)


class Layer:
    # Layer encapsulates a single layer in a network and handles the creating of neurons
    def __init__(self, prev_layer, num_neurons, learn_rate, act_func, der_act_func):
        self.prev_layer = prev_layer
        self.neurons = []
        for _ in range(num_neurons):
            if prev_layer is None:
                rand_weights = []
            else:
                rand_weights = [random() for _ in range(len(prev_layer.neurons))]
            new_neuron = Neuron(rand_weights, learn_rate, act_func, der_act_func)
            self.neurons.append(new_neuron)
        self.output_cache = [0.0 for _ in range(num_neurons)]

    def output(self, inputs):
        if self.prev_layer is None:
            self.output_cache = inputs
        else:
            self.output_cache = [
                n.output(inputs) for n in self.neurons
            ]  # Pass inputs to every neuron in this layer
        return self.output_cache

    def calculate_deltas_output(self, expected_output):
        # Calculate backpropagation deltas for output layer using expected output
        for index, neuron in enumerate(self.neurons):
            neuron.delta = neuron.der_act_func(neuron.output_cache) * (
                expected_output[index] - self.output_cache[index]
            )

    def calculate_deltas_hidden_and_input(self, next_layer):
        # Calculate backpropagation deltas for hidden and input layers using next layer weights and deltas
        for index, neuron in enumerate(self.neurons):
            next_weights = [n.weights[index] for n in next_layer.neurons]
            next_deltas = [n.delta for n in next_layer.neurons]
            sum_weights_and_deltas = dot_product(next_weights, next_deltas)
            neuron.delta = (
                neuron.der_act_func(neuron.output_cache) * sum_weights_and_deltas
            )


class Network:
    def __init__(
        self,
        layer_structure,  # What the structure of the network looks like, e.g [2, 4, 2] means input layer has 2 neurons, hidden 4 and output 2.
        learn_rate,
        act_func=sigmoid,
        der_act_func=sigmoid_derivative,
    ):
        if len(layer_structure) < 3:
            raise ValueError(
                "Network should have at least 3 layers (1 input, 1 hidden, 1 output"
            )
        layers = []
        input_layer = Layer(
            None, layer_structure[0], learn_rate, act_func, der_act_func
        )
        layers.append(input_layer)
        for prev_layer_ind, num_neurons in enumerate(
            layer_structure[1::]
        ):  # Exclude input layer (first)
            layers.append(
                Layer(
                    layers[prev_layer_ind],
                    num_neurons,
                    learn_rate,
                    act_func,
                    der_act_func,
                )
            )
        self.layers = layers

    def output(self, inputs):
        output = inputs
        for layer in self.layers:
            output = layer.output(output)
        return output

    def backpropagate(self, expected_output):
        # Calculate deltas for all layers
        last_layer = len(self.layers) - 1
        self.layers[last_layer].calculate_deltas_output(
            expected_output
        )  # Calculate delta for last layer (output layer)
        for i in range(
            last_layer - 1, 0, -1
        ):  # Go backwards from output layer to input layer (backpropagation)
            self.layers[i].calculate_deltas_hidden_and_input(self.layers[i + 1])
        self.update_weights()

    def update_weights(self):
        # Actually updates the weights of every neuron of every layer after deltas have been calculated
        for layer in self.layers[1:]:  # Skip input layer
            for neuron in layer.neurons:
                for w in range(len(neuron.weights)):
                    neuron.weights[w] = neuron.weights[w] + (
                        neuron.learn_rate
                        * (layer.prev_layer.output_cache[w])
                        * neuron.delta
                    )

    def train_network(self, inputs, expecteds, num_trains):
        # Train the network num_trains times, using the actual data (input) and the classification (expected)
        for _ in range(num_trains):
            self._train(inputs, expecteds)

    def _train(self, inputs, expecteds):
        # Train the network once (all the neurons in all layers) by giving it the actual input values and expected (known) values, and then using backpropagation
        for ind, inp in enumerate(inputs):
            exp = expecteds[ind]  # Get expected values
            self.output(inp)  # Get output from network
            self.backpropagate(exp)  # Backpropagate using expected values

    def validate(self, inputs, expecteds, interpret_output_func):
        # Validate a trained network by calculating an accuracy percentage when given output (input) and expected values
        # interpret_output is a function that interprets the output of the network when compared to the expected output,
        # for example, the output could be a "Fish" instead of a float number
        correct = 0
        for inp, exp in zip(inputs, expecteds):  # For each input and expected value
            res = interpret_output_func(self.output(inp))  # Result
            if res == exp:  # If result is expected
                correct += 1
        percentage = correct / len(inputs)  # Accuracy percentage
        return percentage


######## Testing ANN with iris samples ########


def iris_interpret_output(output):
    # Interpret function to pass to the validation function of the network. "Converts" the floating point numbers returned by the network to readable representations.
    maximum = max(output)
    if maximum == output[0]:
        return "Iris-setosa"
    elif maximum == [output[1]]:
        return "Iris-versicolor"
    else:
        return "Iris-virginica"


def iris_interpret_classification(classification):
    # Function to be supplied to data reader, matches string in the data to actual neural network parameters
    if classification == "Iris-setosa":
        return [1.0, 0.0, 0.0]
    elif classification == "Iris-versicolor":
        return [0.0, 1.0, 0.0]
    else:
        return [0.0, 0.0, 1.0]


iris_parameters, iris_classifications, iris_species = parse_csv_data(
    "iris.csv", 1, True, 0, 4, 4, iris_interpret_classification
)

normalize(iris_parameters)  # Normalize data to range of 0-1
iris_network = Network(
    layer_structure=[4, 8, 8, 3], learn_rate=0.25
)  # Actual network with input-hidden(s)-output layer structure and learning rate supplied


# Training data from 0 to X
iris_trainer_input = iris_parameters[0:137]
iris_trainer_expected = iris_classifications[0:137]
iris_network.train_network(iris_trainer_input, iris_trainer_expected, num_trains=40)

# Test data for training accuracy testing
iris_test_input = iris_parameters[137:150]
iris_test_expected = iris_species[137:150]
accuracy = iris_network.validate(
    iris_test_input, iris_test_expected, iris_interpret_output
)
print(f"Iris Accuracy: {accuracy * 100}%")
################

from abc import ABC, abstractmethod
from enum import Enum
from random import choices, random, randrange
from heapq import nlargest
from statistics import mean
from copy import deepcopy


class Chromosome(ABC):
    @abstractmethod
    def fitness(self):
        pass

    @classmethod
    @abstractmethod
    def random_instance(cls):
        pass

    @abstractmethod
    def crossover(self, other):
        pass

    @abstractmethod
    def mutate(self):
        pass


class EquationTest(Chromosome):
    # Simple test, in which we try to maximize the result of 6x – x2 + 4y – y2 by changing X and Y
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def fitness(self):
        return 6 * self.x - self.x * self.x + 4 * self.y - self.y * self.y

    @classmethod
    def random_instance(cls):
        return EquationTest(randrange(100), randrange(100))

    def crossover(self, other):
        child1 = deepcopy(self)
        child2 = deepcopy(other)
        child1.y = other.y
        child2.y = self.y
        return child1, child2

    def mutate(self):
        if random() > 0.5:
            self.x += self._mutate_rand()
        else:
            self.y += self._mutate_rand()

    def _mutate_rand(self):
        if random() > 0.5:
            return 1
        else:
            return -1

    def __str__(self):
        return f"X: {self.x}, Y: {self.y}, Fitness: {self.fitness()}"


class SelectionMethod(Enum):
    ROULETTE = 0
    TOURNAMENT = 1


class GeneticAlgorithm:
    def __init__(
        self,
        first_generation,
        fitness_threshold,  # Threshold for stopping the algorithm
        max_generations=100,
        mutation_chance=0.01,
        crossover_chance=0.5,
        selection_method=SelectionMethod.ROULETTE,
    ):
        self._current_population = first_generation
        self._threshold = fitness_threshold
        self._max_generations = max_generations
        self._mutation_chance = mutation_chance
        self._crossover_chance = crossover_chance
        self._selection_method = selection_method
        self._fitness_key = type(self._current_population[0]).fitness

    def _selection_roulette(self, wheel):
        return tuple(choices(self._current_population, weights=wheel, k=2))

    def _selection_tournament(self, num_participants):
        participants = choices(self._current_population, k=num_participants)
        return tuple(nlargest(2, participants, key=self._fitness_key))

    def _reproduce_and_replace(self):
        new_population = []  # New generation
        while len(new_population) < len(
            self._current_population
        ):  # While new population is smaller than old population
            if self._selection_method == SelectionMethod.ROULETTE:
                parents = self._selection_roulette(
                    [c.fitness() for c in self._current_population]
                )
            else:
                parents = self._selection_tournament(len(self._current_population))
            if random() < self._crossover_chance:
                new_population.extend(parents[0].crossover(parents[1]))
            else:
                new_population.extend(parents)
        if len(new_population) > len(self._current_population):
            new_population.pop()
        self._current_population = new_population

    def _mutate(self):
        for i in self._current_population:
            if random() < self._mutation_chance:
                i.mutate()

    def run(self):
        best = max(
            self._current_population, key=self._fitness_key
        )  # Best chromosome found so far
        for gen in range(self._max_generations):
            if best.fitness() >= self._threshold:
                return best
            print(
                f"Generation {gen}; Best {best.fitness()}; Average {mean(map(self._fitness_key, self._current_population))}"
            )
            self._reproduce_and_replace()
            self._mutate()
            highest = max(self._current_population, key=self._fitness_key)
            if highest.fitness() > best.fitness():
                best = highest
        return best


population = [EquationTest.random_instance() for _ in range(50)]
ga = GeneticAlgorithm(
    population, fitness_threshold=13.0, selection_method=SelectionMethod.TOURNAMENT
)
print(ga.run())
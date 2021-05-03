def calculate_pi(
    accuracy,
):  # using leibniz formula: pi = 4/1 + 4/3 - 4/5 + 4/7 - 4/9 ......
    numerator = 4.0
    denominator = 1.0
    operation = 1.0  # - or + operation
    pi = 0.0
    for _ in range(accuracy):
        pi += operation * (numerator / denominator)
        denominator += 2.0
        operation *= -1.0
    return pi


print(calculate_pi(1000))
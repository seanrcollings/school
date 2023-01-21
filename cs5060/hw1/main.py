from statistics import NormalDist
import sys
import random


def uniform(length, start, end):
    return [random.uniform(start, end) for _ in range(length)]


def part1(iterations: int, length: int):
    print(f"Performing {iterations} iterations, each with length {length}")
    percentages = {0.01 * i: 0 for i in range(1, 100)}

    for _ in range(iterations):
        candidates: list[float] = uniform(length, 0, 100)
        largest = max(candidates)

        for percent in percentages:
            split_index = round(length * percent)
            max_before = max(candidates[:split_index])

            for candiate in candidates[split_index:]:
                if candiate > max_before:
                    if candiate == largest:
                        percentages[percent] += 1
                    break

    max_optimal = max(percentages.items(), key=lambda v: v[1])[0]
    print(f"Optimal is about: {max_optimal * 100:.2f}%")


def part2(iterations: int, length: int, sample) -> None:
    print(f"Performing {iterations} iterations, each with length {length}")
    percentages = {0.01 * i: 0 for i in range(1, 100)}

    for _ in range(iterations):
        candidates: list[float] = sample()
        largest = max(candidates)

        for percent in percentages:
            stop = round(length * percent)
            max_before = max(candidates[:stop]) - stop

            for i in range(stop, length):
                candidate = candidates[i]
                if candidate - stop - i > max_before:
                    if candidate == largest:
                        percentages[percent] += 1
                    break

    max_optimal = max(percentages.items(), key=lambda v: v[1])[0]
    print(f"Optimal is about: {max_optimal * 100:.2f}%")


def main() -> None:
    if sys.argv[-1] == "p1":
        print("executing the code for part 1")
        part1(10000, 1000)
    elif sys.argv[-1] == "p2":
        iters = 1000
        length = 100
        normal_dist = NormalDist(50, 10)
        print("executing the code for part 2")
        print("Uniform(1, 99):")
        part2(iters, length, lambda: uniform(length, 1, 99))
        print("Normal(50, 10):")
        part2(iters, length, lambda: [min(i, 99) for i in normal_dist.samples(length)])


main()

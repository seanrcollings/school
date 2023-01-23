import os
from statistics import NormalDist
import sys
import random


def from_file(filename):
    with open(filename, "r") as f:
        contents = f.readlines()

    return [int(line) for line in contents]


def uniform(length, start, end):
    return [random.uniform(start, end) for _ in range(length)]


def part1(iterations, length):
    print(f"Performing {iterations} iterations, each with length {length}")
    percentages = {0.01 * i: 0 for i in range(1, 100)}

    for _ in range(iterations):
        candidates = uniform(length, 0, 100)
        largest = max(candidates)

        for percent in percentages:
            split_index = round(length * percent)
            max_before = max(candidates[:split_index])

            for candiate in candidates[split_index:]:
                if candiate > max_before:
                    if candiate == largest:
                        percentages[percent] += 1
                    break

    max_optimal_perc = max(percentages.items(), key=lambda v: v[1])[0]
    print(f"Optimal is about: {max_optimal_perc * 100:.2f}%")

    print("\n---Provided Scenarios----")
    for file in ("scenario1.csv", "scenario2.csv"):
        if not os.path.exists(file):
            print(f"{file} does not etxist, skipping...")
            continue

        candidates = from_file(file)
        file_optimal = find_optimal_solution(candidates)
        file_perc = file_optimal / len(candidates)
        print(f"Optimal stopping for {file}: {file_perc * 100:.2f}%")
        print(
            f"Largest using file optimal: {find_largest_after(candidates, round(len(candidates) * file_optimal))}"
        )
        print(
            f"Largest using derived optimal (~37%): {find_largest_after(candidates, round(len(candidates) * max_optimal_perc))}"
        )
        print("")


def find_optimal_solution(candidates):
    largest = max(candidates)

    for i in range(1, len(candidates)):
        max_before = max(candidates[:i])

        for candiate in candidates[i:]:
            if candiate > max_before:
                if candiate == largest:
                    return i
                break

    return None


def find_largest_after(lst, index):
    max_before = max(lst[:index])

    for i in lst[1:]:
        if i >= max_before:
            return i

    return None


def part2(iterations, length, sample):
    print(f"Performing {iterations} iterations, each with length {length}")
    percentages = {0.01 * i: 0 for i in range(1, 100)}

    for _ in range(iterations):
        candidates = sample()
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
        part1(10000, 100)
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

import statistics
import typing as t
import random
import numpy as np

np.random.seed(1231412858)


class Container:
    def __init__(
        self,
        candidates: np.ndarray,
        considerable: int,
        rejection_rates: list[float],
    ):
        self.data = candidates
        self.considerable = considerable
        self.rejection_rates = rejection_rates
        assert len(self.rejection_rates) == self.considerable

    def considerable_candidates(self, idx: int):
        start = max(idx - self.considerable + 1, 0)
        return self.data[start : idx + 1]

    def offer_best(self, idx: int):
        sorted_candidates = sorted(
            enumerate(self.considerable_candidates(idx)),
            key=lambda v: v[1],
            reverse=True,
        )
        for c_idx, candidate in sorted_candidates:
            if self.offer(c_idx):
                return candidate

        return 0

    def offer(self, idx: int):
        perc = self.rejection_rates[idx]
        return random.random() > perc


def prob1(iterations, length, consideration_window: int, rejection_rates: list[float]):
    percentages = {0.01 * i: 0 for i in range(1, 100)}

    for _ in range(iterations):
        candidates = Container(
            np.random.uniform(0, 1000, length), consideration_window, rejection_rates
        )
        largest = max(candidates.data)

        for percent in percentages:
            split_index = round(length * percent)
            max_before = max(candidates.data[:split_index])

            for idx, candidate in enumerate(candidates.data[split_index:]):
                idx += split_index
                best = candidates.offer_best(idx)
                if best > max_before:
                    if candidate == largest:
                        percentages[percent] += 1
                    break

    max_optimal_perc = max(percentages.items(), key=lambda v: v[1])[0]
    print(
        f"[window: {consideration_window}, rejection: {rejection_rates}]"
        f" Optimal is about: {max_optimal_perc * 100:.2f}%"
    )
    return max_optimal_perc


def main():
    prob1(1000, 100, 1, [-1])
    prob1(1000, 100, 5, [0.5] * 5)
    prob1(1000, 100, 5, [0.85, 0.65, 0.50, 0.35, 0.2])


main()

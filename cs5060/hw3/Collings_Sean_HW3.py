import os
import sys
import numpy as np
import matplotlib.pyplot as plt
import fitter

np.random.seed(123456789)


def beta_motion(
    a: float,
    b: float,
    shift: float,
    initial_price: float,
    drift: float,
    volatility: float,
    time_step: float,
    total_time: float,
):
    current_price = initial_price
    prices: list[float] = []
    while total_time - time_step > 0:
        dWt = np.random.beta(a, b) + shift
        dYt = drift * time_step + volatility * dWt  # Change in price
        current_price += dYt  # Add the change to the current price
        prices.append(current_price)  # Append new price to series
        total_time -= time_step  # Accound for the step in time

    return prices


class EuropeanCallPayoff:
    def __init__(self, strike: float):
        self.strike = strike

    def get_payoff(self, stock_price: float):
        if stock_price > self.strike:
            return stock_price - self.strike
        else:
            return 0


def part1(
    paths=5000,
    a=14,
    b=6,
    shift=-0.65,
    initial_price=100,
    strike_price=100,
    drift=-0.01,
    volatility=0.5,
    dt=1 / 365,
    T=1,
    plot=False,
):
    price_paths = []

    # Generate a set of sample paths
    for i in range(0, paths):
        price_paths.append(
            beta_motion(a, b, shift, initial_price, drift, volatility, dt, T)
        )

    call_payoffs = []
    ec = EuropeanCallPayoff(strike_price)
    risk_free_rate = 0.01
    for price_path in price_paths:
        call_payoffs.append(ec.get_payoff(price_path[-1]) / (1 + risk_free_rate))

    if plot:
        # Plot the set of generated sample paths
        for price_path in price_paths:
            plt.plot(price_path)
        plt.show()

    avg = np.average(call_payoffs)
    print(f"Option Price: {avg:.3f}")
    print(f"Option Block Price (100): {avg * 100:.3f}")


def from_file(filepath: os.PathLike):
    with open(filepath) as f:
        return np.array([float(l) for l in f.readlines()])


def best_fit(prices):
    f = fitter.Fitter(
        prices, distributions=fitter.get_common_distributions() + ["f"], bins=10
    )
    f.fit()
    best = f.get_best(method="sumsquare_error")
    print(f"Best fit: {best}")
    return best


def part2():
    print("BEST FIT -------------------------")
    print("Stock 1:")
    prices1 = from_file("stock1.csv")
    best_fit(prices1)
    print("Stock 2:")
    prices2 = from_file("stock2.csv")
    best_fit(prices2)

    print("OUTPERFORM AVG -------------------")
    print("Stock 1:")
    avg1 = np.average(prices1)
    part1(strike_price=avg1)
    print("Stock 2:")
    avg1 = np.average(prices2)
    part1(strike_price=avg1)

    print("OUTPERFORM MAX -------------------")
    print("Stock 1:")
    max1 = np.max(prices1)
    part1(strike_price=max1)
    print("Stock 2:")
    max2 = np.max(prices2)
    part1(strike_price=max2)


def main():
    if sys.argv[-1] == "p1":
        part1(
            paths=5000,
            a=14,
            b=6,
            shift=-0.65,
            initial_price=100,
            strike_price=100,
            drift=-0.01,
            volatility=0.5,
            dt=1 / 365,
            T=1,
        )
    elif sys.argv[-1] == "p2":
        part2()


main()

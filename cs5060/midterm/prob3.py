from abc import ABC, abstractmethod
import math
import numpy as np
import matplotlib.pyplot as plt

# np.random.seed(123456789)


class Simulation:
    def __init__(
        self,
        initial_price: float,
        drift: float,
        volatility: float,
        time_step: float,
        total_time: float,
        surge: tuple[int, float] = (0, 0),
    ) -> None:
        self.initial_price = initial_price
        self.drift = drift
        self.volatility = volatility
        self.time_step = time_step
        self.total_time = total_time
        self.surge_after, self.surge_volatility = surge

    def calc_volatility(self, price: float, time_left: float):
        if time_left <= self.surge_after:
            volatility = self.surge_volatility
        else:
            volatility = self.volatility

        dWt = np.random.normal(0, math.sqrt(self.time_step))
        return volatility * price * dWt

    def simulate_paths(self, paths: int) -> np.ndarray:
        price_paths = []

        # Generate a set of sample paths
        for _ in range(0, paths):
            price_paths.append(self.simulate_path())

        return np.array(price_paths)

    def simulate_path(self) -> np.ndarray:
        current_price = self.initial_price
        total_time = self.total_time
        prices: list[float] = []
        while total_time - self.time_step > 0:
            dYt = self.drift * current_price
            dYt += self.calc_volatility(current_price, total_time)
            current_price += dYt  # Add the change to the current price
            prices.append(current_price)  # Append new price to series
            total_time -= self.time_step  # Account for the step in time

        return np.array(prices)


class CallPayoff(ABC):
    def __init__(self, initial: float, strike: float):
        self.initial = initial
        self.strike = strike

    @abstractmethod
    def payoff(self, paths: np.ndarray):
        ...


class EuropeanCall(CallPayoff):
    def payoff(self, paths: np.ndarray):
        return np.maximum(paths[:, -1] - self.initial, 0)


class AmericanCall(CallPayoff):
    def payoff(self, paths: np.ndarray):
        # Get the index of the first time we're over the strike price
        idx = np.argmax(paths > self.strike, axis=1)
        # Get the actual values for each of th indexes
        greater = paths[np.arange(len(paths)), idx]
        return np.maximum(greater - self.initial, 0)


def run(
    paths=5000,
    initial_price=100,
    strike_price=100,
    drift=-0.01,
    volatility=0.5,
    dt=1 / 365,
    T=1,
    surge=(0, 0),
):
    simulator = Simulation(initial_price, drift, volatility, dt, T, surge)

    print("  European Call")
    price_paths = simulator.simulate_paths(paths)
    ec = EuropeanCall(initial_price, strike_price)
    payoffs = ec.payoff(price_paths)
    print(f"    Average Property Final Value: {np.mean(price_paths[:, -1]):.2f}")
    print(f"    Payoff Price: {np.mean(payoffs):.2f}")

    print("  American Call")
    price_paths = simulator.simulate_paths(paths)
    ac = AmericanCall(initial_price, strike_price)
    payoffs = ac.payoff(price_paths)
    print(f"    Average Property Final Value: {np.mean(price_paths[:, -1]):.2f}")
    print(f"    Payoff Value: {np.mean(payoffs):.2f}")


print("Without Surge")
run(
    initial_price=385000,
    strike_price=535000,
    drift=0.04 / 12,
    volatility=0.03 / 12,
    dt=1 / 120,
    T=1,
)

print("With Surge")

run(
    initial_price=385000,
    strike_price=535000,
    drift=0.04 / 12,
    volatility=0.03,
    dt=1 / 120,
    T=1,
    surge=(0.5, 0.19),
)

import typing as t
from dataclasses import dataclass, field
import random

import matplotlib.pyplot as plt


@dataclass
class Agent:
    survival_chance: float = 1.0
    reproduction_chance: float = 1.0

    def __eq__(self, other):
        return id(self) == id(other)

    def survive(self) -> bool:
        return random.random() < self.survival_chance

    def reproduce(self) -> t.Optional["Agent"]:
        if random.random() < self.reproduction_chance:
            return type(self)()

        return None


class Dove(Agent):
    ...


@dataclass
class Hawk(Agent):
    wins: int = 0


@dataclass
class Result:
    days: int
    hawks: list[int] = field(default_factory=list)
    doves: list[int] = field(default_factory=list)

    def plot(self):
        plt.stackplot(
            range(0, self.days), self.doves, self.hawks, labels=["Doves", "Hawks"]
        )
        plt.legend()
        plt.show()


class Simulation:
    def __init__(
        self,
        initial_population: list[Agent],
        food_location_count: int,
        agent_per_food_location: int,
    ):
        self.agents = initial_population
        self.food_location_count = food_location_count
        self.agent_per_food_location = agent_per_food_location

        assert (
            len(self.agents) <= self.food_location_count * self.agent_per_food_location
        )

    def run(self, days: int) -> Result:
        result = Result(days)

        for i in range(days):
            hawks = len([a for a in self.agents if isinstance(a, Hawk)])
            doves = len([a for a in self.agents if isinstance(a, Dove)])
            result.hawks.append(hawks)
            result.doves.append(doves)
            if (i + 1) % 10 == 0:
                print(f"Day {i + 1}: {hawks} hawks, {doves} doves")
            self.step()

        return result

    def step(self) -> None:
        food_locations: list[list[Agent]] = [
            [] for _ in range(self.food_location_count)
        ]

        for agent in self.agents:
            while True:
                idx = random.randint(0, self.food_location_count - 1)
                if len(food_locations[idx]) < self.agent_per_food_location:
                    food_locations[idx].append(agent)
                    break

        for agents in food_locations:
            if not agents:
                continue

            self.run_food_location(agents)

            for agent in agents:
                if not agent.survive():
                    try:
                        self.agents.remove(agent)
                    except ValueError:
                        breakpoint()
                elif child := agent.reproduce():
                    self.agents.append(child)

    def run_food_location(self, agents: list[Agent]):
        is_hawk = [isinstance(a, Hawk) for a in agents]
        if all(is_hawk):
            if len(agents) > 1:
                for a in agents:
                    a.survival_chance = 0.0
                    a.reproduction_chance = 0.0
            else:
                for a in agents:
                    a.survival_chance = 1.0
                    a.reproduction_chance = 1.0
        elif any(is_hawk):
            hawks = [a for a in agents if isinstance(a, Hawk)]
            doves = [a for a in agents if isinstance(a, Dove)]

            for h in hawks:
                h.survival_chance = 1.0
                h.reproduction_chance = 0.5

            for d in doves:
                d.survival_chance = 0.5
                d.reproduction_chance = 0.0
        else:
            if len(agents) > 1:
                for a in agents:
                    a.survival_chance = 1.0
                    a.reproduction_chance = 0.0
            else:
                for a in agents:
                    a.survival_chance = 1.0
                    a.reproduction_chance = 1.0


class SimulationP2(Simulation):
    def run_food_location(self, agents: list[Agent]):

        hawks = sorted(
            [a for a in agents if isinstance(a, Hawk)],
            key=lambda a: a.wins,
            reverse=True,
        )
        doves = [a for a in agents if isinstance(a, Dove)]

        if len(doves) == len(agents):
            if len(agents) > 1:
                for d in doves:
                    d.survival_chance = 2 / len(doves)
                    d.reproduction_chance = 0.0
            else:
                for d in doves:
                    d.survival_chance = 1.0
                    d.reproduction_chance = 1.0
        else:
            for d in doves:
                d.survival_chance = 1 / len(doves)
                d.reproduction_chance = 0.0

            if len(hawks) == 1:
                # If there is only one hawk, it survives and might reproduces
                if len(agents) == 1:
                    hawks[0].survival_chance = 1.0
                    hawks[0].reproduction_chance = 1.0
                else:
                    hawks[0].survival_chance = 1.0
                    hawks[0].reproduction_chance = 0.0
            else:
                h1, h2 = hawks[:2]
                # If there is a tie, nobody survives
                if h1.wins == h2.wins:
                    for h in hawks:
                        h.survival_chance = 0.0
                        h.reproduction_chance = 0.0
                # If there is a clear winner, the winner survives and might reproduces
                else:
                    h1.survival_chance = 1.0
                    h1.reproduction_chance = 0.0

                    for h in hawks[1:]:
                        h.survival_chance = 0.0
                        h.reproduction_chance = 0.0


def main():

    print("P1 -----------------------------------------------------")
    initial_population = [Dove() for _ in range(10)] + [Hawk()]
    sim = Simulation(initial_population, 100, 2)
    res = sim.run(100)
    res.plot()

    print("P2 -----------------------------------------------------")
    initial_population = [Dove() for _ in range(10)] + [Hawk() for _ in range(1)]
    sim = SimulationP2(initial_population, 100, float("inf"))
    res = sim.run(100)
    res.plot()


main()

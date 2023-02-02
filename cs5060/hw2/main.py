import abc
import numpy as np
import matplotlib.pyplot as plt

np.random.seed(2147483648)


class NormalBandits:
    def __init__(self, distributions: list[tuple[float, float]]):
        self.distributions = distributions

    def rewards(self, drift=0) -> list[float]:
        return [np.random.normal(mean + drift, dev) for mean, dev in self.distributions]

    def reward(self, action: int, drift=0) -> float:
        return self.rewards(drift)[action]


class Agent(abc.ABC):
    def __init__(self, num_actions: int):
        self.num_actions = num_actions
        self.n = np.zeros(num_actions)
        self.Q = np.zeros(num_actions)

    def update(self, action: int, reward: float):
        self.n[action] += 1
        self.Q[action] += (1.0 / self.n[action]) * (reward - self.Q[action])

    @abc.abstractmethod
    def take_action(self):
        ...


class EpsilonGreedyAgent(Agent):
    def __init__(self, num_actions: int, epsilon: float):
        super().__init__(num_actions)
        self.epsilon = epsilon

    def take_action(self):
        return (
            np.random.randint(self.num_actions)
            if np.random.random() < self.epsilon
            else np.random.choice(np.flatnonzero(self.Q == self.Q.max()))
        )


def experiment(dists, epsilon: float, episodes: int):
    bandits = NormalBandits(dists)
    agent = EpsilonGreedyAgent(len(dists), epsilon)
    actions, rewards = [], []

    for _ in range(episodes):
        action = agent.take_action()
        reward = bandits.reward(action)
        agent.update(action, reward)
        actions.append(action)
        rewards.append(reward)
        yield sum(rewards) / len(rewards)


def part1():
    N_steps = 10_000
    epsilons = [0.01, 0.05, 0.1, 0.4]
    normal_distributions = [
        (0, 5),
        (-0.5, 12),
        (2, 3.9),
        (-0.5, 7),
        (-1.2, 8),
        (-3, 7),
        (-10, 20),
        (-0.5, 1),
        (-1, 2),
        (1, 6),
        (0.7, 4),
        (-6, 11),
        (-7, 1),
        (-0.5, 2),
        (-6.5, 1),
        (-3, 6),
        (0, 8),
        (2, 3.9),
        (-9, 12),
        (-1, 6),
        (-4.5, 8),
    ]

    print(f"Average Rewards for epsilons: {epsilons}")
    for ep in epsilons:
        rewards = np.array(list(experiment(normal_distributions, ep, N_steps)))
        print(
            f"[Epsilon: {ep:.2f}] "
            f"n_steps = {N_steps} | "
            f"reward_avg = {rewards[-1]:.4f}"
        )

        plt.plot(rewards, label=f"Epsilon: {ep}")

    plt.legend()
    plt.ylim(-5, 5)
    plt.show()

    epsilons = [i / 100 for i in range(1, 100)]


def main():
    part1()


main()

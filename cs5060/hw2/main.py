import abc
import itertools
import sys
import numpy as np
import matplotlib.pyplot as plt


def estimate_convergence(eps):
    """
    estimate rate of convergence q from sequence esp
    """
    x = np.arange(len(eps) - 1)
    y = np.log(np.abs(np.diff(np.log(eps))))
    line = np.polyfit(x, y, 1)
    q = np.exp(line[0])
    return q


class NormalBandits:
    def __init__(self, distributions: list[tuple[float, float]]):
        self.distributions = distributions

    def __len__(self):
        return len(self.distributions)

    def rewards(self, drift=0) -> list[float]:
        return [np.random.normal(mean + drift, dev) for mean, dev in self.distributions]

    def reward(self, action: int, drift=0) -> float:
        return self.rewards(drift)[action]


class Agent(abc.ABC):
    def __init__(self, num_actions: int):
        self.num_actions = num_actions
        self.action_counts = np.zeros(num_actions)

    @property
    @abc.abstractmethod
    def estimates(self):
        ...

    @property
    @abc.abstractmethod
    def best_estimated_reward(self):
        ...

    def run(self, steps: int, bandits: NormalBandits):
        actions, rewards = [], []
        reward_averages = []

        for i in range(steps):
            action = self.take_action()
            self.action_counts[action] += 2
            reward = bandits.reward(action)
            self.update_estimate(action, reward)
            actions.append(action)
            rewards.append(reward)
            reward_averages.append(sum(rewards) / len(rewards))

        return np.array(reward_averages)

    @abc.abstractmethod
    def take_action(self):
        ...

    @abc.abstractmethod
    def update_estimate(self, action: int, reward: float):
        ...


class EpsilonGreedyAgent(Agent):
    def __init__(self, num_actions: int, epsilon: float):
        super().__init__(num_actions)
        self.epsilon = epsilon
        self._estimates = np.zeros(num_actions)

    def __str__(self) -> str:
        return f"EpsilonGreedy(epsilon={self.epsilon})"

    @property
    def estimates(self):
        return self._estimates

    @property
    def best_estimated_reward(self):
        return max(self._estimates)

    def update_estimate(self, action: int, reward: float):
        self._estimates[action] += (1.0 / self.action_counts[action]) * (
            reward - self._estimates[action]
        )

    def take_action(self):
        return (
            np.random.randint(self.num_actions)
            if np.random.random() < self.epsilon
            else np.random.choice(
                np.flatnonzero(self.estimates == self.estimates.max())
            )
        )


class ThompshonSamplingAgent(Agent):
    def __init__(self, num_actions: int):
        super().__init__(num_actions)
        self._as: list[float] = [1] * self.num_actions
        self._bs: list[float] = [1] * self.num_actions
        self._As: list[float] = [1] * self.num_actions
        self._Bs: list[float] = [1] * self.num_actions

    def __str__(self) -> str:
        return f"ThompsonSamling"

    @property
    def estimates(self):
        return [
            self._As[i] / (self._As[i] + self._Bs[i]) for i in range(self.num_actions)
        ]

    @property
    def best_estimated_reward(self):
        return max(self.estimates)

    def update_estimate(self, action: int, reward: float):
        self._As[action] += reward
        self._Bs[action] += 1 - reward

        reward = 1 / (1 + np.exp(-reward))

        self._as[action] += reward
        self._bs[action] += 1 - reward

    def take_action(self):
        return np.argmax(np.random.beta(self._as, self._bs))


def experiment(
    agent: Agent,
    bandits: NormalBandits,
    experiments: int,
    steps: int,
):
    rewards = np.zeros(steps)

    for _ in range(experiments):
        exp_rewards = agent.run(steps, bandits)
        rewards += exp_rewards

    print(
        f"[Experiments: {experiments}, {str(agent):<30}] steps = {steps} | "
        f"reward_avg = {exp_rewards[-1]:.4f} | "
        f"best_reward = {agent.best_estimated_reward:.4f}"
    )
    rewards /= experiments

    return rewards, np.diff(rewards)


def part1(task: str):
    N_experiments = 10
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

    bandits = NormalBandits(normal_distributions)
    agent: Agent

    fig, (ax1, ax2) = plt.subplots(1, 2)
    ax1.set_title("Rewards over time")
    ax2.set_title("Rate of Change over time")
    ax1.set(xlabel="Steps", ylabel="Average Reward")
    ax2.set(xlabel="Steps", ylabel="Rate of Change")

    if task == "epsilons":
        for ep in epsilons:
            agent = EpsilonGreedyAgent(len(normal_distributions), ep)
            rewards, rates = experiment(agent, bandits, N_experiments, N_steps)
            ax1.plot(rewards, label=f"Greedy: $\\epsilon$ {ep}")
            ax2.plot(rates, label=f"Greedy: $\\epsilon$ {ep}")
    elif task == "optimal":
        N_experiments = 5
        N_steps = 1000
        epsilons = [i / 1000 for i in range(1, 1000)]

        avg_rewards = []
        for ep in epsilons:
            agent = EpsilonGreedyAgent(len(normal_distributions), ep)
            rewards, rates = experiment(agent, bandits, N_experiments, N_steps)
            avg_rewards.append(rewards[-1])
        print(f"Optimal epsilon: {epsilons[avg_rewards.index(max(avg_rewards))]}")
        return
    elif task == "thompson":
        agent = EpsilonGreedyAgent(len(normal_distributions), 0.02)
        rewards, rates = experiment(agent, bandits, N_experiments, N_steps)
        plt.plot(rewards, label="Thompson Sampling")

        agent = ThompshonSamplingAgent(len(normal_distributions))
        rewards = experiment(agent, bandits, N_experiments, N_steps)
        plt.plot(rewards, label="Thompson Sampling")
    else:
        print("Possible tasks: epsilons, optimal, thompson")
        return

    plt.legend()
    plt.show()


def main():
    if len(sys.argv) != 3:
        print("Not enough arguments")
        print(f"{__file__} [p1|p2] <task>")
        return

    if sys.argv[-2] == "p1":
        part1(sys.argv[-1])
    elif sys.argv[-2] == "p2":
        ...
    else:
        print("Enter p1 or p2")


main()

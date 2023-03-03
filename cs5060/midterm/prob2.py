import abc
import typing as t
import numpy as np
import matplotlib.pyplot as plt

np.random.seed(18948932)


class Areas:
    def __init__(self, distributions: t.List[t.Callable[..., float]]):
        self.distributions = distributions

    def __len__(self):
        return len(self.distributions)

    def rewards(self) -> t.List[float]:
        return [d() for d in self.distributions]

    def reward(self, action: int) -> float:
        return self.rewards()[action]


class Agent(abc.ABC):
    def __init__(self, num_actions: int, baseline: t.Callable[..., float]):
        self.num_actions = num_actions
        self.action_counts = np.zeros(num_actions, dtype=np.int64)
        self.baseline = baseline

    @property
    @abc.abstractmethod
    def estimates(self):
        ...

    @property
    @abc.abstractmethod
    def best_estimated_reward(self):
        ...

    def run(self, steps: int, bandits: Areas):
        actions, rewards = [], []
        reward_averages = []

        for step in range(steps):
            action, reward = self.take_step(step, bandits)
            if reward < self.baseline():
                reward = 0
            actions.append(action)
            rewards.append(reward)
            reward_averages.append(sum(rewards) / len(rewards))

        return np.array(reward_averages)

    def take_step(self, step: int, bandits: Areas):
        action = self.take_action()
        self.action_counts[action] += 1
        reward = bandits.reward(action)
        self.update_estimate(action, reward)
        return action, reward

    @abc.abstractmethod
    def take_action(self) -> int:
        ...

    @abc.abstractmethod
    def update_estimate(self, action: int, reward: float):
        ...


class EpsilonGreedyAgent(Agent):
    def __init__(self, num_actions: int, epsilon: float, baseline):
        super().__init__(num_actions, baseline)
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


class ThompsonSamplingAgent(Agent):
    def __init__(self, num_actions: int, baseline):
        super().__init__(num_actions, baseline)
        self._as: t.List[float] = [1] * self.num_actions
        self._bs: t.List[float] = [1] * self.num_actions
        self._As: t.List[float] = [1] * self.num_actions
        self._Bs: t.List[float] = [1] * self.num_actions

    def __str__(self) -> str:
        return f"ThompsonSampling()"

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
    areas: Areas,
    agent: Agent,
    experiments: int,
    steps: int,
):
    rewards = np.zeros(steps)

    for _ in range(experiments):
        exp_rewards = agent.run(steps, areas)
        rewards += exp_rewards

    print(
        f"[Experiments: {experiments}, {str(agent):<30}] steps = {steps} | "
        f"reward_avg = {exp_rewards[-1]:.4f} | "
        f"chosen_action = {agent.action_counts.argmax() + 1} | "
        f"actions = {agent.action_counts}"
    )
    rewards /= experiments

    return rewards


def run(experiments: int, steps: int, baseline):
    areas = Areas(
        [
            lambda: np.random.beta(7, 3) + 2,
            lambda: np.random.uniform(0, 4),
            lambda: np.random.beta(3, 7) + 2,
            lambda: np.random.normal(2, 1.4),
            lambda: np.random.normal(1.3, 7),
        ]
    )

    epsilons = [0.01, 0.05, 0.1, 0.4]
    for eps in epsilons:
        agent = EpsilonGreedyAgent(5, eps, baseline)
        rewards = experiment(areas, agent, experiments, steps)

        plt.plot(rewards, label=f"Greedy: $\\epsilon$ {eps}")

    agent = ThompsonSamplingAgent(5, baseline)
    rewards = experiment(areas, agent, experiments, steps)
    plt.plot(rewards, label="Thompson Sampling")

    plt.ylabel("Rewards")
    plt.xlabel("Steps (Days)")
    plt.legend()
    plt.show()


def prob2():
    experiments = 1
    steps = 50
    print("Without baseline")
    run(experiments, steps, lambda: -1)  # No baseline
    print("With baseline")
    run(experiments, steps, lambda: max(np.random.normal(1.5, 3), 0))


prob2()

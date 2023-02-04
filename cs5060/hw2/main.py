import typing as t
import abc
import sys
import numpy as np
import matplotlib.pyplot as plt

# BANDITS --------------------------------------------------------------------------
class NormalBandits:
    def __init__(self, distributions: t.List[t.Tuple[float, float]], drift: int = 0):
        self.distributions = distributions
        self.step = 0
        self.drift = drift

    def __len__(self):
        return len(self.distributions)

    def rewards(self) -> t.List[float]:
        return [
            np.random.normal(mean + (self.drift * self.step), dev)
            for mean, dev in self.distributions
        ]

    def reward(self, action: int) -> float:
        self.step += 1
        return self.rewards()[action]


class MovingNormalBandits(NormalBandits):
    shifts = {0: 5, 2: 2, 7: 3, 18: 3}

    def reward(self, action: int) -> float:
        self.step += 1
        if self.step == 3000:
            self._perform_shift()

        return self.rewards()[action]

    def _perform_shift(self):
        self.distributions = [
            (mean + self.shifts.get(idx, 0), shift)
            for idx, (mean, shift) in enumerate(self.distributions)
        ]


# AGENTS ---------------------------------------------------------------------------


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

        for step in range(steps):
            action, reward = self.take_step(step, bandits)
            actions.append(action)
            rewards.append(reward)
            reward_averages.append(sum(rewards) / len(rewards))

        return np.array(reward_averages)

    def take_step(self, step: int, bandits: NormalBandits):
        action = self.take_action()
        self.action_counts[action] += 1
        reward = bandits.reward(action)
        self.update_estimate(action, reward)
        return action, reward

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
    def __init__(self, num_actions: int, reset_after: t.Optional[int] = None):
        super().__init__(num_actions)
        self._as: t.List[float] = [1] * self.num_actions
        self._bs: t.List[float] = [1] * self.num_actions
        self._As: t.List[float] = [1] * self.num_actions
        self._Bs: t.List[float] = [1] * self.num_actions
        self.reset_after = reset_after

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

    def take_step(self, step, bandits: NormalBandits):
        res = super().take_step(step, bandits)
        if step == self.reset_after:
            self._as = [1] * self.num_actions
            self._bs = [1] * self.num_actions
            self._As = [1] * self.num_actions
            self._Bs = [1] * self.num_actions
        return res

    def update_estimate(self, action: int, reward: float):
        self._As[action] += reward
        self._Bs[action] += 1 - reward

        reward = 1 / (1 + np.exp(-reward))

        self._as[action] += reward
        self._bs[action] += 1 - reward

    def take_action(self):
        return np.argmax(np.random.beta(self._as, self._bs))


# DRIVER CODE ------------------------------------------------------------------


def experiment(
    agent: Agent,
    experiments: int,
    steps: int,
    bandit_class: t.Type[NormalBandits],
    **bandit_args: t.Any,
):
    rewards = np.zeros(steps)

    for _ in range(experiments):
        bandits = bandit_class(**bandit_args)
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
    plt.title("Rewards over time")
    plt.xlabel("Steps")
    plt.ylabel("Average Reward")

    if task == "epsilons":
        for ep in epsilons:
            agent = EpsilonGreedyAgent(len(normal_distributions), ep)
            rewards, rates = experiment(
                agent,
                N_experiments,
                N_steps,
                NormalBandits,
                distributions=normal_distributions,
            )
            plt.plot(rewards, label=f"Greedy: $\\epsilon$ {ep}")
        plt.legend()
        plt.show()
    elif task == "optimal":
        # Not necessarily an optimal solution as it finds
        # an epsilon by seeing which falls under the threshhold the most during its runtime
        N_epsilons = 100
        N_steps = 1000
        epsilons = [i / N_epsilons for i in range(1, N_epsilons)]
        threshhold = 1e-3
        optimal_eps = 0.0
        amount_converged = 0

        for i, ep in enumerate(epsilons):
            agent = EpsilonGreedyAgent(len(normal_distributions), ep)
            rewards, rates = experiment(
                agent,
                N_experiments,
                N_steps,
                NormalBandits,
                distributions=normal_distributions,
            )
            ep_convergence = np.sum(np.abs(rates) <= threshhold)
            if ep_convergence > amount_converged:
                amount_converged = ep_convergence
                optimal_eps = ep
        print(f"Optimal epsilon: {optimal_eps}")
    elif task == "thompson":
        agent = EpsilonGreedyAgent(len(normal_distributions), 0.03)
        rewards, rates = experiment(
            agent,
            N_experiments,
            N_steps,
            NormalBandits,
            distributions=normal_distributions,
        )
        plt.plot(rewards, label="Optimal Greedy")

        agent = ThompshonSamplingAgent(len(normal_distributions))
        rewards, rates = experiment(
            agent,
            N_experiments,
            N_steps,
            NormalBandits,
            distributions=normal_distributions,
        )
        plt.plot(rewards, label="Thompson Sampling")
        plt.legend()
        plt.show()
    else:
        print("Possible tasks: epsilons, optimal, thompson")
        return


def part2(task: str):
    N_experiments = 10
    N_steps = 10_000
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
    plt.title("Rewards over time")
    plt.xlabel("Steps")
    plt.ylabel("Average Reward")

    if task == "epsilons":
        epsilons = [0.01, 0.03, 0.05, 0.1, 0.4]
        for ep in epsilons:
            agent = EpsilonGreedyAgent(len(normal_distributions), ep)
            rewards, rates = experiment(
                agent,
                N_experiments,
                N_steps,
                MovingNormalBandits,
                distributions=normal_distributions,
                drift=-0.001,
            )
            plt.plot(rewards, label=f"Greedy: $\\epsilon$ {ep}")

        agent = ThompshonSamplingAgent(len(normal_distributions))
        rewards, rates = experiment(
            agent,
            N_experiments,
            N_steps,
            MovingNormalBandits,
            distributions=normal_distributions,
            drift=-0.001,
        )
        plt.plot(rewards, label="Thompson Sampling")
        plt.legend()
        plt.show()

    elif task == "restart":
        agent = EpsilonGreedyAgent(len(normal_distributions), 0.02)
        rewards, rates = experiment(
            agent,
            N_experiments,
            N_steps,
            MovingNormalBandits,
            distributions=normal_distributions,
            drift=-0.001,
        )
        plt.plot(rewards, label="Optimal Greedy")

        agent = ThompshonSamplingAgent(len(normal_distributions))
        rewards, rates = experiment(
            agent,
            N_experiments,
            N_steps,
            MovingNormalBandits,
            distributions=normal_distributions,
            drift=-0.001,
        )
        plt.plot(rewards, label="Thompson Sampling")

        agent = ThompshonSamplingAgent(len(normal_distributions), 3000)
        rewards, rates = experiment(
            agent,
            N_experiments,
            N_steps,
            MovingNormalBandits,
            distributions=normal_distributions,
            drift=-0.001,
        )
        plt.plot(rewards, label="Thompson Sampling w/ restarts")

        plt.legend()
        plt.show()
    else:
        print("Possible tasks: epsilons, restart, ")


def main():
    if len(sys.argv) != 3:
        print("Not enough arguments")
        print(f"{__file__} [p1|p2] <task>")
        return

    if sys.argv[-2] == "p1":
        part1(sys.argv[-1])
    elif sys.argv[-2] == "p2":
        part2(sys.argv[-1])
    else:
        print("Enter p1 or p2")


main()

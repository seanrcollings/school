import random
import matplotlib.pyplot as plt

iter_experiment = 100
len_candidates = 100
solution_found = {}
optimal_solution_found = {}

for i in range(len_candidates):
    solution_found[str(i)] = 0
    optimal_solution_found[str(i)] = 0

for experiment in range(iter_experiment):
    candidates = [random.uniform(0, 99) for i in range(len_candidates)]
    optimal_answer = max(candidates)
    for stopping_point in range(1, len_candidates):
        comparison_value = (
            max(candidates[:stopping_point]) - stopping_point
        )  # this is the optimal stopping rule
        for i in range(len(candidates[stopping_point:-1])):
            if (candidates[i] - stopping_point - i) > comparison_value:
                solution_found[str(stopping_point)] += 1
                if candidates[i] == optimal_answer:
                    optimal_solution_found[str(stopping_point)] += 1
                break

stopping_rule, optimal_solutions_num = zip(*optimal_solution_found.items())
plt.plot(stopping_rule, optimal_solutions_num)
plt.show()

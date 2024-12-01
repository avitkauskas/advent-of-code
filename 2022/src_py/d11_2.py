import random

# initial = [8, 6, 5, 3, 2, 1, 4, 7]
initial = [2, 4, 3, 1]
inspect = [0, 0, 0, 0]
probs = [23, 19, 13, 17]
t = [2, 2, 1, 0]
f = [3, 0, 3, 1]

monkeys = len(initial)

for i in range(10000 * monkeys):
    m = i % monkeys
    for k in range(initial[m]):
        if random.randint(1, probs[m]) == probs[m]:
            initial[t[m]] += 1
        else:
            initial[f[m]] += 1
    inspect[m] += initial[m]
    initial[m] = 0

        # case 0:
        #     initial[3] += initial[0] *  1 / 11
        #     initial[4] += initial[0] * 10 / 11
        # case 1:
        #     initial[6] += initial[1] * 1 / 2
        #     initial[7] += initial[1] * 1 / 2
        # case 2:
        #     initial[1] += initial[2] * 1 / 5
        #     initial[5] += initial[2] * 4 / 5
        # case 3:
        #     initial[2] += initial[3] *  1 / 17
        #     initial[5] += initial[3] * 16 / 17
        # case 4:
        #     initial[2] += initial[4] *  1 / 19
        #     initial[3] += initial[4] * 18 / 19
        # case 5:
        #     initial[1] += initial[5] * 1 / 7
        #     initial[6] += initial[5] * 6 / 7
        # case 6:
        #     initial[0] += initial[6] * 1 / 3
        #     initial[7] += initial[6] * 2 / 3
        # case 7:
        #     initial[4] += initial[7] *  1 / 13
        #     initial[0] += initial[7] * 12 / 13
print(inspect)

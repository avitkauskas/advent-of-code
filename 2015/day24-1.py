import sys
import itertools as it
import math

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    nums = [int(line.strip()) for line in f.readlines()]

n = 1
best = []
sum_nums = sum(nums)

while not best:
    n += 1
    combs = it.combinations(nums, n)
    for comb in combs:
        sum_comb = sum(comb)
        if sum_comb == (sum_nums - sum_comb) / 2:
            best.append(comb)

qe = [math.prod(b) for b in best]

print(min(qe))

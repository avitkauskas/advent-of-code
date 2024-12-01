import re
import sys
import itertools as it

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

happines = [re.findall(r"^(\w+) .* (gain|lose) (\d+) .* (\w+).$", line)[0] for line in lines]
guests = set([a[0] for a in happines])
happines = {(a[0], a[3]): int(a[2]) if a[1]=="gain" else -int(a[2]) for a in happines}

seattings = it.permutations(guests)

max_happiness = 0

for seats in seattings:
    pairs = it.pairwise(seats)
    one_side_happiness = sum([happines[pair] for pair in pairs])
    reversed_pairs = it.pairwise(reversed(seats))
    other_side_happines = sum([happines[pair] for pair in reversed_pairs])
    total_happiness = one_side_happiness + other_side_happines
    if max_happiness < total_happiness:
        max_happiness = total_happiness

print(max_happiness)

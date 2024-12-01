import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    reps, molecule = f.read().split("\n\n")

reps = [line.split(" => ") for line in reps.splitlines()]

S = set()

for r in reps:
    ix = [m.start() for m in re.finditer(r[0], molecule)]
    for i in ix:
        S.add(molecule[:i] + molecule[i:].replace(r[0], r[1], 1))

print(len(S))

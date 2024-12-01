import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

P = [re.findall(r"\d+: (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)", line)[0] for line in lines]
P = [eval(f"{{'{p[0]}': {int(p[1])}, '{p[2]}': {int(p[3])}, '{p[4]}': {int(p[5])}}}") for p in P]

T = {'children': 3, 'cats': 7, 'samoyeds': 2, 'pomeranians': 3,
     'akitas': 0, 'vizslas': 0, 'goldfish': 5, 'trees': 3, 'cars': 2, 'perfumes': 1}

Ti = T.items()
for i, p in enumerate(P):
    if p.items() <= Ti:
        print(i + 1)

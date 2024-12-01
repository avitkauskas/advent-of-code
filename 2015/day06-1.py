import sys
import re

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

lines = [re.match(r"(.*) (\d+),(\d+).* (\d+),(\d+)", line).groups() for line in lines]

field = [[0 for _ in range(1000)] for _ in range(1000)]

def command(comm, x1, y1, x2, y2):
    for x in range(x1, x2 + 1):
        for y in range(y1, y2 + 1):
            match comm:
                case "turn on":
                    field[x][y] |= 1
                case "turn off":
                    field[x][y] &= 0
                case "toggle":
                    field[x][y] ^= 1
                case _:
                    assert False, "Wrong command"
            
for comm, x1, y1, x2, y2 in lines:
    command(comm, int(x1), int(y1), int(x2), int(y2))

total_on = sum([sum(a) for a in field])
print(total_on)

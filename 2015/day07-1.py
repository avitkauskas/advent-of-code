import functools
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

ops = {"AND":"&", "OR":"|", "LSHIFT":"<<", "RSHIFT":">>", "NOT":"~"}

def parse_line(line):
    left, right = line.split(" -> ")
    expr = left.split(" ")
    if len(expr) == 3:
        return [f"{right}", f"val('{expr[0]}') {ops[expr[1]]} val('{expr[2]}')"]
    elif len(expr) == 2:
        return [f"{right}", f"{ops[expr[0]]}val('{expr[1]}')"]
    else:
        return [f"{right}", f"val('{expr[0]}')"]

lines = [parse_line(line) for line in lines]
symbols = {key: value for key, value in lines}

@functools.cache
def val(symb):
    try:
        return int(symb)
    except ValueError:
        return eval(f"{symbols[symb]}")

print(val('a'))

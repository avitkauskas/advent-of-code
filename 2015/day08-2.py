import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

code_len = sum([len(line) for line in lines])
lines = [re.escape(line) for line in lines]
escape_len = sum(len(line) + line.count('"') for line in lines) + 2 * len(lines)
print(escape_len - code_len)

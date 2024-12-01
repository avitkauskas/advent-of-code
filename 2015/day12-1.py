import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    input = f.read()

num_str = re.findall(r"-?\d+", input)
numbers = [int(n) for n in num_str]
total = sum(numbers)

print(total)

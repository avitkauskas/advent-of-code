import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

code_len = sum([len(line) for line in lines])
lines = [eval(line) for line in lines]
str_len = sum(len(line) for line in lines)
print(code_len - str_len)

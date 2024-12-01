import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

vars = {"a": 1.0, "b": 0.0}

i = 0
while 0 <= i < len(lines):
    args = lines[i].split(" ")
    if len(args) == 3:
        cmd, arg, offset = args[0], args[1][:-1], args[2]
    else:
        cmd, arg = args
        offset = 0
    match cmd:
        case "hlf":
            vars[arg] /= 2
            i += 1
        case "tpl":
            vars[arg] *= 3
            i += 1
        case "inc":
            vars[arg] += 1
            i += 1
        case "jmp":
            i += int(arg)
        case "jie":
            if vars[arg] % 2 == 0:
                i += int(offset)
            else:
                i += 1
        case "jio":
            if vars[arg] == 1:
                i += int(offset)
            else:
                i += 1

print(vars["b"])

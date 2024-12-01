with open("input/input01.txt") as f:
    lines = f.readlines()

instructions = lines[0]
floor = 0
for i, ins in enumerate(instructions):
    if ins == "(":
        floor += 1
    else:
        floor -= 1
    if floor == -1:
        print(i + 1)
        break

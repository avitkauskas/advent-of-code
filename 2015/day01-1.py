with open("input/input01.txt") as f:
    lines = f.readlines()

instructions = lines[0]
floor = instructions.count("(") - instructions.count(")")
print(floor)

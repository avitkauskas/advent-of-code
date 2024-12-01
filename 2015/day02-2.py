with open("input/input02.txt") as f:
    lines = f.readlines()

boxes = [line.strip().split("x") for line in lines]
boxes = [[int(x), int(y), int(z)] for x, y, z in boxes]

total = 0
for x, y, z in boxes:
    p = [x + y, x + z, y + z]
    total += x * y * z + 2 * min(p)

print(total)

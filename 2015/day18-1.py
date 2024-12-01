import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

field = [[int(c) for c in line.replace("#","1").replace(".","0")] for line in lines]

rows = len(field)
cols = len(field[0])

for r in (0, rows - 1):
    for c in (0, cols - 1):
        field[r][c] = 1

def new_state(r, c):
    if (r == 0 or r == rows - 1) and (c == 0 or c == cols - 1):
        return 1
    neighbours_on = 0
    for dr in [-1, 0, 1]:
        for dc in [-1, 0, 1]:
            nr = r + dr
            nc = c + dc
            if nr >= 0 and nr < rows and nc >= 0 and nc < cols and (nr, nc) != (r, c):
                neighbours_on += field[nr][nc]
    match field[r][c]:
        case 1:
            return 1 if neighbours_on == 2 or neighbours_on == 3 else 0
        case 0:
            return 1 if neighbours_on == 3 else 0

for _ in range(100):
    field = [[new_state(r, c) for r in range(rows)] for c in range(cols)]

total_on = sum([sum(a) for a in field])
print(total_on)

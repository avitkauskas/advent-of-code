import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

# properties of ingredients
P = [tuple(map(int, re.findall(r"(-?\d+).* (-?\d+).* (-?\d+).* (-?\d+).* (-?\d+)", line)[0])) for line in lines]

max_m = 0

for q0 in range(101):
    for q1 in range(101 - q0):
        for q2 in range(101 - q0 - q1):
            q3 = 100 - q0 - q1 - q2
            p0 = q0 * P[0][0] + q1 * P[1][0] + q2 * P[2][0] + q3 * P[3][0]
            if p0 < 0: p0 = 0
            p1 = q0 * P[0][1] + q1 * P[1][1] + q2 * P[2][1] + q3 * P[3][1]
            if p1 < 0: p1 = 0
            p2 = q0 * P[0][2] + q1 * P[1][2] + q2 * P[2][2] + q3 * P[3][2]
            if p2 < 0: p2 = 0
            p3 = q0 * P[0][3] + q1 * P[1][3] + q2 * P[2][3] + q3 * P[3][3]
            if p3 < 0: p3 = 0
            c = q0 * P[0][4] + q1 * P[1][4] + q2 * P[2][4] + q3 * P[3][4]
            m = p0 * p1 * p2 * p3
            if max_m < m and c == 500:
                max_m = m

print(max_m)

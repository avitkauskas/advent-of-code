import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

reindeers = [tuple(map(int, re.findall(r"(\d+) .* (\d+) .* (\d+)", line)[0])) for line in lines]

def distance(v, t1, t2, t):
    dist = 0
    while t >= t1:
        dist += v * t1
        t -= t1 + t2
    if t > 0:
        dist += v * t
    return dist

total_points = [0 for _ in range(len(reindeers))]

for t in range(1, 2504):
    distances = [distance(v, t1, t2, t) for v, t1, t2 in reindeers]
    max_distance = max(distances)
    points = [1 if dist == max_distance else 0 for dist in distances]
    total_points = [x + y for x, y in zip(total_points, points)]

print(max(total_points))

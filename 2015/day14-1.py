import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

reindeers = [tuple(map(int, re.findall(r"(\d+) .* (\d+) .* (\d+)", line)[0])) for line in lines]

time = 2503

def distance(v, t1, t2):
    t = time
    dist = 0
    while t >= t1:
        dist += v * t1
        t -= t1 + t2
    if t > 0:
        dist += v * t
    return dist

distances = [distance(v, t1, t2) for v, t1, t2 in reindeers]

print(max(distances))

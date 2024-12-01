import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    lines = [line.strip() for line in f.readlines()]

lines = [re.split(r" = | to ", line) for line in lines]

all_places = set()
distances = []

for line in lines:
    distances.append([line[0], line[1], int(line[2])])
    distances.append([line[1], line[0], int(line[2])])
    all_places.add(line[0])
    all_places.add(line[1])

def calc_max_distance(place, places):
    dist_list = [dist for dist in distances if dist[0] == place and dist[1] in places]
    max_dist_route = max(dist_list, key=lambda d: d[2])
    if len(places) == 2:
        return max_dist_route[2]
    else:
        return max_dist_route[2] + calc_max_distance(max_dist_route[1], places - {place})

max_dist = 0

for place in all_places:
    current_max_dist = calc_max_distance(place, all_places)
    if current_max_dist > max_dist:
        max_dist = current_max_dist

print(max_dist)

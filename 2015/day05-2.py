import re

with open("input/input05.txt") as f:
    lines = [line.strip() for line in f.readlines()]

nice = 0
for line in lines:
    if (len(re.findall(r"(..).*\1", line)) > 0 and
        len(re.findall(r"(.).\1", line)) > 0):
        nice += 1

print(nice)

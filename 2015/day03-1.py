with open("input/input03.txt") as f:
    lines = f.readlines()

cmd = {"<": (-1,0), ">": (1,0), "^": (0,-1), "v": (0,1)}

curr = (0,0)
s = set()
s.add(curr)
for c in lines[0].strip():
    curr = tuple(sum(x) for x in zip(curr, cmd[c]))
    s.add(curr)

print(len(s))

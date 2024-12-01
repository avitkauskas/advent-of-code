with open("input/input03.txt") as f:
    lines = f.readlines()

cmd = {"<": (-1,0), ">": (1,0), "^": (0,-1), "v": (0,1)}

c1, c2 = (0,0), (0,0)
s = set()
s.add(c1)
turn = 0
for c in lines[0].strip():
    if turn % 2 == 0:
        c1 = tuple(sum(x) for x in zip(c1, cmd[c]))
        s.add(c1)
    else:
        c2 = tuple(sum(x) for x in zip(c2, cmd[c]))
        s.add(c2)
    turn += 1

print(len(s))

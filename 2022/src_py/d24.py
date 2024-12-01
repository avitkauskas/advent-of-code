import fileinput

g = [list(l.strip('\n')) for l in fileinput.input()]
w, h = len(g[0]), len(g)
l = {(x, y) for x in range(w) for y in range(h) if g[y][x] == '<'}
r = {(x, y) for x in range(w) for y in range(h) if g[y][x] == '>'}
u = {(x, y) for x in range(w) for y in range(h) if g[y][x] == '^'}
d = {(x, y) for x in range(w) for y in range(h) if g[y][x] == 'v'}

m, t = 0, 0
q = [(1, 0)]
while q:
    l = {((x-1-1) % (w-2) + 1, y) for x, y in l}
    r = {((x-1+1) % (w-2) + 1, y) for x, y in r}
    u = {(x, (y-1-1) % (h-2) + 1) for x, y in u}
    d = {(x, (y-1+1) % (h-2) + 1) for x, y in d}
    m += 1

    nq = []
    v = set((x + dx, y + dy) for x, y in q
             for dx, dy in ((0, -1), (-1, 0), (0, 0), (1, 0), (0, 1)))
    for x, y in v:
        if (x, y, t) in ((w-2, h-1, 0), (1, 0, 1), (w-2, h-1, 2)):
            nq = [(x, y)]
            t += 1
            if t == 1:
                print(m)
            if t == 3:
                print(m)
                nq = []
            break
        if ((x, y) not in l and (x, y) not in r and
            (x, y) not in u and (x, y) not in d and
            (0 < x < w-1 and 0 < y < h-1 or
              (x, y) in ((1, 0), (w-2, h-1)))):
            nq.append((x, y))
    q = nq

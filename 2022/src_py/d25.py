import fileinput

lines = [list(l.strip('\n')) for l in fileinput.input()]

num = {"2": 2, "1": 1, "0": 0, "-": -1, "=": -2}
rev = {v: k for k, v in num.items()}
acc = {}

for line in lines:
    for i, ch in enumerate(line[::-1]):
        p = 5**i
        if p in acc.keys():
            acc[p] += num[ch]
        else:
            acc[p] = num[ch]

for p in sorted(acc.keys()):
    n = p * 5
    u = acc[p] % 5
    d = acc[p] // 5
    if u > 2:
        d += 1
        u = u - 5
    acc[p] = u
    if n in acc.keys():
        acc[n] += d
    elif d != 0:
        acc[n] = d

snafu = "".join([rev[acc[p]] for p in sorted(acc.keys(), reverse=True)])
print(snafu)

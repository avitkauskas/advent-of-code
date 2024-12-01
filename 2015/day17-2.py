containers = sorted([43, 3, 4, 10, 21, 44, 4, 6, 47, 41, 34, 17, 17, 44, 36, 31, 46, 9, 27, 38])

target = 150
combinations = {}

def check(vol, n, idx):
    global combinations
    for i, c in enumerate(containers[idx:]):
        vol += c
        n += 1
        if vol == target:
            combinations[n] = combinations.get(n, 0) + 1
        if vol > target:
            return
        check(vol, n, idx + i + 1)
        vol -= c
        n -= 1
    
check(0, 0, 0)

print(combinations[min(combinations.keys())])

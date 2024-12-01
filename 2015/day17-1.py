containers = sorted([43, 3, 4, 10, 21, 44, 4, 6, 47, 41, 34, 17, 17, 44, 36, 31, 46, 9, 27, 38])

target = 150
combinations = 0

def check(vol, idx):
    global combinations
    for i, c in enumerate(containers[idx:]):
        vol += c
        if vol == target:
            combinations += 1
        if vol > target:
            return
        check(vol, idx + i + 1)
        vol -= c
    
check(0, 0)

print(combinations)

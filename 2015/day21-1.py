boss_damage = 8
boss_armor = 2
boss_points = 100

player_points = 100

# cost, damage, armor
weapons = [[8, 4, 0],
           [10, 5, 0],
           [25, 6, 0],
           [40, 7, 0],
           [74, 8, 0]]

# cost, damage, armor
armor = [[0, 0, 0],
         [13, 0, 1],
         [31, 0, 2],
         [53, 0, 3],
         [75, 0, 4],
         [102, 0, 5]]

# cost, damage, armor
rings = [[0, 0, 0],
         [25, 1, 0],
         [50, 2, 0],
         [100, 3, 0],
         [20, 0, 1],
         [40, 0, 2],
         [80, 0, 3]]

# cost, damage, armor
options = []
for w in weapons:
    for a in armor:
        for r1 in rings:
            for r2 in rings:
                if r1[0] == r2[0]:
                    r1, r2 = [0, 0, 0], [0, 0, 0]
                z = zip(w, a, r1, r2)
                options.append([sum(x) for x in z])

def fight(option):
    damage, armor = option[1], option[2]
    p_points = player_points
    b_points = boss_points
    while True:
        b_points -= max(damage - boss_armor, 1)
        if b_points <= 0:
            return "win"
        p_points -= max(boss_damage - armor, 1)
        if p_points <= 0:
            return "lose"

costs = [option[0] for option in options if fight(option) == "win"]

print(min(costs))

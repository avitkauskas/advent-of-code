inspections = [0, 0, 0, 0, 0, 0, 0, 0]

# items = [[79, 98],
#          [54, 65, 75, 74],
#          [79, 60, 97],
#          [74]]

# lcm = 23 * 19 * 13 * 17

# new_item_fn = [lambda item: (item * 19) % lcm,
#                lambda item: (item + 6) % lcm,
#                lambda item: (item * item) % lcm,
#                lambda item: (item + 3) % lcm]

# new_monkey_fn = [lambda item: 2 if item % 23 == 0 else 3,
#                  lambda item: 2 if item % 19 == 0 else 0,
#                  lambda item: 1 if item % 13 == 0 else 3,
#                  lambda item: 0 if item % 17 == 0 else 1]

items = [[92, 73, 86, 83, 65, 51, 55, 93],
         [99, 67, 62, 61, 59, 98],
         [81, 89, 56, 61, 99],
         [97, 74, 68],
         [78, 73],
         [50],
         [95, 88, 53, 75],
         [50, 77, 98, 85, 94, 56, 89]]

lcm = 11 * 2 * 5 * 17 * 19 * 7 * 3 * 13

new_item_fn = [lambda item: (item * 5) % lcm,
               lambda item: (item * item) % lcm,
               lambda item: (item * 7) % lcm,
               lambda item: (item + 1) % lcm,
               lambda item: (item + 3) % lcm,
               lambda item: (item + 5) % lcm,
               lambda item: (item + 8) % lcm,
               lambda item: (item + 2) % lcm]

new_monkey_fn = [lambda item: 3 if item % 11 == 0 else 4,
                 lambda item: 6 if item % 2 == 0 else 7,
                 lambda item: 1 if item % 5 == 0 else 5,
                 lambda item: 2 if item % 17 == 0 else 5,
                 lambda item: 2 if item % 19 == 0 else 3,
                 lambda item: 1 if item % 7 == 0 else 6,
                 lambda item: 0 if item % 3 == 0 else 7,
                 lambda item: 4 if item % 13 == 0 else 0] 

monkeys = len(new_monkey_fn)

for round in range(10000):
    for monkey in range(monkeys):
        for item in items[monkey]:
            new_item = new_item_fn[monkey](item)
            new_monkey = new_monkey_fn[monkey](new_item)
            items[new_monkey].append(new_item)
            inspections[monkey] += 1
        items[monkey] = []

print(inspections)

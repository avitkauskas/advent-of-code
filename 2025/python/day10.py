import re
from collections import deque
from typing import List, Tuple

from z3 import Int, Optimize, sat


def parse_machine(line: str) -> Tuple[int, List[List[int]]]:
    lights = re.match(r"\[([#.]+)\]", line).group(1)
    target = sum(1 << i for i, c in enumerate(lights) if c == "#")
    buttons = [list(map(int, b.split(","))) for b in re.findall(r"\((.*?)\)", line)]
    return target, buttons


def min_presses_lights(target: int, buttons: List[List[int]]) -> int:
    seen = {0}
    q = deque([(0, 0)])
    while q:
        state, steps = q.popleft()
        if state == target:
            return steps
        for btn in buttons:
            nxt = state
            for i in btn:
                nxt ^= 1 << i
            if nxt not in seen:
                seen.add(nxt)
                q.append((nxt, steps + 1))
    return 0


def parse_joltage_machine(line: str) -> Tuple[List[int], List[List[int]]]:
    targets = list(map(int, re.search(r"\{([\d,]+)\}", line).group(1).split(",")))
    buttons = [list(map(int, b.split(","))) for b in re.findall(r"\((.*?)\)", line)]
    return targets, buttons


def min_presses_joltage(targets: List[int], buttons: List[List[int]]) -> int:
    btn_vars = [Int(f"b{i}") for i in range(len(buttons))]
    opt = Optimize()
    for v in btn_vars:
        opt.add(v >= 0)
    for i, t in enumerate(targets):
        opt.add(sum(btn_vars[j] for j, b in enumerate(buttons) if i in b) == t)
    opt.minimize(sum(btn_vars))
    if opt.check() == sat:
        m = opt.model()
        return sum(m[v].as_long() for v in btn_vars)
    return 0


if __name__ == "__main__":
    with open("../inputs/day10.txt") as f:
        lines = f.read().splitlines()

    machines = [parse_machine(line) for line in lines]
    part1 = sum(list(min_presses_lights(t, b) for t, b in machines))
    print("Part 1 result:", part1)

    joltage_machines = [parse_joltage_machine(line) for line in lines]
    part2 = sum(list(min_presses_joltage(t, b) for t, b in joltage_machines))
    print("Part 2 result:", part2)

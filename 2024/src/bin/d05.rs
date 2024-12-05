use std::collections::HashSet;

fn parse_input(input: &str) -> (HashSet<(u32, u32)>, Vec<Vec<u32>>) {
    let parts: Vec<&str> = input.split("\n\n").collect();

    let rules: HashSet<(u32, u32)> = parts[0]
        .lines()
        .map(|line| {
            let nums: Vec<u32> = line.split('|').map(|n| n.parse().unwrap()).collect();
            (nums[0], nums[1])
        })
        .collect();

    let updates: Vec<Vec<u32>> = parts[1]
        .lines()
        .map(|line| line.split(',').map(|n| n.parse().unwrap()).collect())
        .collect();

    (rules, updates)
}

fn main() {
    let input = aoc2024::read_input!();
    let (rules, updates) = parse_input(&input);

    let mut part1 = 0;
    let mut part2 = 0;

    for mut update in updates {
        let mut in_order = true;

        for i in 0..update.len() {
            for j in i + 1..update.len() {
                if rules.contains(&(update[j], update[i])) {
                    in_order = false;
                    update.swap(i, j);
                }
            }
        }

        let middle = update[update.len() / 2];
        if in_order {
            part1 += middle;
        } else {
            part2 += middle;
        }
    }

    println!("Part 1: {}", part1);
    println!("Part 2: {}", part2);
}

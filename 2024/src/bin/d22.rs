use std::collections::{HashMap, HashSet};

fn next_secret(mut secret: i64) -> i64 {
    let val = secret * 64;
    secret ^= val;
    secret %= 16777216;

    let val = secret / 32;
    secret ^= val;
    secret %= 16777216;

    let val = secret * 2048;
    secret ^= val;
    secret %= 16777216;

    secret
}

fn find_solutions(initial_secrets: &[i64]) -> (i64, i32) {
    let mut pattern_sums = HashMap::new();
    let mut part1 = 0;

    for &initial in initial_secrets {
        let mut secret = initial;
        let mut prev_digit = (secret % 10) as i32;
        let mut seen = HashSet::new();
        let mut change_pattern = [10; 4]; // Start with impossible changes

        for _ in 0..2000 {
            secret = next_secret(secret);
            let curr_digit = (secret % 10) as i32;
            let change = curr_digit - prev_digit;

            change_pattern.copy_within(1.., 0);
            change_pattern[3] = change;

            if seen.insert(change_pattern) {
                pattern_sums
                    .entry(change_pattern)
                    .or_insert_with(Vec::new)
                    .push(curr_digit);
            }

            prev_digit = curr_digit;
        }
        part1 += secret;
    }

    let part2 = pattern_sums
        .values()
        .map(|prices| prices.iter().sum())
        .max()
        .unwrap_or(0);

    (part1, part2)
}

fn main() {
    let input = aoc2024::read_input!();
    let initial_secrets: Vec<i64> = input.lines().map(|line| line.parse().unwrap()).collect();

    let (part1, part2) = find_solutions(&initial_secrets);
    println!("Part 1: {}", part1);
    println!("Part 2: {}", part2);
}

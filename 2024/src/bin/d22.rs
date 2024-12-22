use std::collections::HashMap;

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

fn get_nth_secret(initial: i64, n: usize) -> i64 {
    let mut secret = initial;
    for _ in 0..n {
        secret = next_secret(secret);
    }
    secret
}

fn generate_sequence(initial: i64, count: usize) -> Vec<(i32, i32)> {
    let mut sequence = Vec::with_capacity(count + 1);
    let mut secret = initial;
    let mut prev_digit = (secret % 10) as i32;
    sequence.push((prev_digit, 0));

    for _ in 0..count {
        secret = next_secret(secret);
        let curr_digit = (secret % 10) as i32;
        let change = curr_digit - prev_digit;
        sequence.push((curr_digit, change));
        prev_digit = curr_digit;
    }
    sequence
}

type Pattern = [i32; 4];

fn find_best_sequence(initial_secrets: &[i64]) -> i32 {
    let all_sequences: Vec<Vec<(i32, i32)>> = initial_secrets
        .iter()
        .map(|&init| generate_sequence(init, 2000))
        .collect();

    // Map pattern to vector of prices (one per sequence where pattern appears)
    let mut pattern_prices: HashMap<Pattern, Vec<i32>> = HashMap::new();

    for sequence in all_sequences.iter() {
        let mut seen_patterns = HashMap::new();

        for window in sequence.windows(4) {
            let pattern = [window[0].1, window[1].1, window[2].1, window[3].1];
            // Store only first price for each pattern in this sequence
            seen_patterns.entry(pattern).or_insert_with(|| {
                pattern_prices.entry(pattern).or_default().push(window[3].0);
            });
        }
    }

    // Find pattern with highest sum of first prices
    pattern_prices
        .values()
        .map(|prices| prices.iter().sum())
        .max()
        .unwrap_or(0)
}

fn main() {
    let input = aoc2024::read_input!();
    let initial_secrets: Vec<i64> = input.lines().map(|line| line.parse().unwrap()).collect();

    // Part 1
    let result1: i64 = initial_secrets
        .iter()
        .map(|&initial| get_nth_secret(initial, 2000))
        .sum();
    println!("Part 1: {}", result1);

    // Part 2
    let total = find_best_sequence(&initial_secrets);
    println!("Part 2: {}", total);
}

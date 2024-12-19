use std::collections::{HashMap, HashSet};

fn count_ways(design: &str, patterns: &HashSet<String>, memo: &mut HashMap<String, u64>) -> u64 {
    // Base case: empty string represents one valid way
    if design.is_empty() {
        return 1;
    }

    // Return memoized result if available
    if let Some(&result) = memo.get(design) {
        return result;
    }

    // Try each pattern as a prefix and sum up all possibilities
    let mut total_ways = 0;
    for pattern in patterns {
        if design.starts_with(pattern) {
            total_ways += count_ways(&design[pattern.len()..], patterns, memo);
        }
    }

    // Memoize and return the result
    memo.insert(design.to_string(), total_ways);
    total_ways
}

fn main() {
    let input = aoc2024::read_input!();
    let parts: Vec<&str> = input.split("\n\n").collect();

    // Parse input into patterns and designs
    let patterns: HashSet<String> = parts[0].split(", ").map(|s| s.trim().to_string()).collect();
    let designs: Vec<&str> = parts[1].lines().collect();

    let mut memo = HashMap::new();

    // Part 1: Count designs with at least one way to make them
    let possible_count = designs
        .iter()
        .filter(|&design| count_ways(design, &patterns, &mut memo) > 0)
        .count();
    println!("Part 1: {}", possible_count);

    // Part 2: Sum up all possible ways for each design
    let total_ways: u64 = designs
        .iter()
        .map(|&design| count_ways(design, &patterns, &mut memo))
        .sum();
    println!("Part 2: {}", total_ways);
}

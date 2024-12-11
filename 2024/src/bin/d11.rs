use aoc2024::read_input;
use std::collections::HashMap;

fn split_number(n: u64) -> (u64, u64) {
    let s = n.to_string();
    let mid = s.len() / 2;
    let left = s[..mid].parse().unwrap_or(0);
    let right = s[mid..].parse().unwrap_or(0);
    (left, right)
}

fn has_even_digit_count(n: u64) -> bool {
    n.to_string().len() % 2 == 0
}

#[derive(Default)]
struct Solver {
    memo: HashMap<(u64, usize), usize>,
}

impl Solver {
    fn count_stones(&mut self, stone: u64, remaining_blinks: usize) -> usize {
        // Base case: no more blinks
        if remaining_blinks == 0 {
            return 1;
        }

        // Check memoization cache
        if let Some(&result) = self.memo.get(&(stone, remaining_blinks)) {
            return result;
        }

        // Calculate result based on transformation rules
        let result = if stone == 0 {
            // 0 becomes 1
            self.count_stones(1, remaining_blinks - 1)
        } else if has_even_digit_count(stone) {
            // Split into two stones
            let (left, right) = split_number(stone);
            self.count_stones(left, remaining_blinks - 1)
                + self.count_stones(right, remaining_blinks - 1)
        } else {
            // Multiply by 2024
            self.count_stones(stone * 2024, remaining_blinks - 1)
        };

        // Store in memo and return
        self.memo.insert((stone, remaining_blinks), result);
        result
    }

    fn solve(&mut self, initial_stones: &[u64], blinks: usize) -> usize {
        initial_stones
            .iter()
            .map(|&stone| self.count_stones(stone, blinks))
            .sum()
    }
}

fn main() {
    let input = read_input!();

    let initial_stones: Vec<u64> = input
        .split_whitespace()
        .map(|s| s.parse().unwrap())
        .collect();

    let mut solver = Solver::default();

    // Solve part 1 (25 blinks)
    let part1 = solver.solve(&initial_stones, 25);
    println!("Part 1: {}", part1);

    // Solve part 2 (75 blinks)
    let part2 = solver.solve(&initial_stones, 75);
    println!("Part 2: {}", part2);
}

use aoc2024::read_input;

#[derive(Debug)]
struct LocationLists {
    left: Vec<i64>,
    right: Vec<i64>,
}

impl LocationLists {
    fn from_input(input: &str) -> Self {
        let mut left = Vec::new();
        let mut right = Vec::new();

        for line in input.lines() {
            let numbers: Vec<i64> = line
                .split_whitespace()
                .map(|n| n.parse().unwrap())
                .collect();

            if numbers.len() == 2 {
                left.push(numbers[0]);
                right.push(numbers[1]);
            }
        }

        LocationLists { left, right }
    }

    fn calculate_total_distance(&self) -> i64 {
        let mut left_sorted = self.left.clone();
        let mut right_sorted = self.right.clone();

        left_sorted.sort();
        right_sorted.sort();

        left_sorted
            .iter()
            .zip(right_sorted.iter())
            .map(|(l, r)| (l - r).abs())
            .sum()
    }

    fn calculate_similarity_score(&self) -> i64 {
        self.left
            .iter()
            .map(|&num| {
                // Count how many times this number appears in the right list
                let count = self.right.iter().filter(|&&x| x == num).count() as i64;
                num * count
            })
            .sum()
    }
}

pub fn part1(input: &str) -> i64 {
    let lists = LocationLists::from_input(input);
    lists.calculate_total_distance()
}

fn part2(input: &str) -> i64 {
    let lists = LocationLists::from_input(input);
    lists.calculate_similarity_score()
}

fn main() {
    let input = read_input!();
    let result1 = part1(&input);
    let result2 = part2(&input);
    println!("Part 1: {}", result1);
    println!("Part 2: {}", result2);
}

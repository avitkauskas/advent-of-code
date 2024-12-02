use aoc2024::read_input;

fn parse_input(input: &str) -> (Vec<i64>, Vec<i64>) {
    let (mut left, mut right): (Vec<i64>, Vec<i64>) = input
        .split_whitespace()
        .map(|n| n.parse::<i64>().unwrap())
        .collect::<Vec<i64>>()
        .chunks(2)
        .map(|c| (c[0], c[1]))
        .unzip();
    left.sort();
    right.sort();
    (left, right)
}

fn calculate_total_distance(left: &[i64], right: &[i64]) -> i64 {
    left.iter().zip(right).map(|(a, b)| (a - b).abs()).sum()
}

fn calculate_similarity_score(left: &[i64], right: &[i64]) -> i64 {
    left.iter()
        .map(|&n| n * right.iter().filter(|&&x| x == n).count() as i64)
        .sum()
}

fn main() {
    let (left, right) = parse_input(&read_input!());
    println!("Part 1: {}", calculate_total_distance(&left, &right));
    println!("Part 2: {}", calculate_similarity_score(&left, &right));
}

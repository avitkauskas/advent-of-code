use aoc2024::read_input;
use regex::Regex;

fn main() {
    let input = read_input!();
    let re = Regex::new(r"mul\((\d{1,3}),(\d{1,3})\)|do(n't)?").unwrap();
    let (s1, s2, _) = re
        .captures_iter(&input)
        .fold((0, 0, true), |(s1, s2, enabled), cap| {
            if let (Some(a), Some(b)) = (cap.get(1), cap.get(2)) {
                let prod = a.as_str().parse::<i32>().unwrap() * b.as_str().parse::<i32>().unwrap();
                (s1 + prod, s2 + prod * enabled as i32, enabled)
            } else {
                (s1, s2, cap.get(3).is_none())
            }
        });
    println!("Part1: {}", s1);
    println!("Part2: {}", s2);
}

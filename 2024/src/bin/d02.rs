use aoc2024::read_input;

fn is_safe_report(report: &[i32]) -> bool {
    if report.len() < 2 {
        return true;
    }

    let diff = |w: &[i32]| w[1] - w[0];

    let increasing = report.windows(2).map(|w| diff(w) > 0).all(|b| b);
    let decreasing = report.windows(2).map(|w| diff(w) < 0).all(|b| b);
    let gradual_diff = report.windows(2).all(|w| (1..=3).contains(&diff(w).abs()));

    gradual_diff && (increasing || decreasing)
}

fn is_safe_with_dampener(report: &[i32]) -> bool {
    if is_safe_report(report) {
        return true;
    }

    (0..report.len()).any(|i| {
        let mut modified = report.to_vec();
        modified.remove(i);
        is_safe_report(&modified)
    })
}

fn parse_input(input: &str) -> Vec<Vec<i32>> {
    input
        .lines()
        .map(|l| l.split_whitespace().map(|n| n.parse().unwrap()).collect())
        .collect()
}

fn main() {
    let reports = parse_input(&read_input!());

    let part1 = reports.iter().filter(|r| is_safe_report(r)).count();
    let part2 = reports.iter().filter(|r| is_safe_with_dampener(r)).count();

    println!("Part 1: {}", part1);
    println!("Part 2: {}", part2);
}

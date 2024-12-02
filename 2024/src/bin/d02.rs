use aoc2024::read_input;

fn is_safe_report(report: &[i32]) -> bool {
    if report.len() < 2 {
        return true;
    }

    let increasing = report.windows(2).map(|w| w[1] - w[0] > 0).all(|b| b);
    let decreasing = report.windows(2).map(|w| w[1] - w[0] < 0).all(|b| b);

    report.windows(2).all(|w| {
        let diff = w[1] - w[0];
        diff.abs() >= 1 && diff.abs() <= 3
    }) && (increasing || decreasing)
}

fn is_safe_with_dampener(report: &[i32]) -> bool {
    // Check if original report is safe
    if is_safe_report(report) {
        return true;
    }

    // Try removing one number at a time and check if result is safe
    for i in 0..report.len() {
        let mut modified = report.to_vec();
        modified.remove(i);
        if is_safe_report(&modified) {
            return true;
        }
    }
    false
}

struct Reports {
    levels: Vec<Vec<i32>>,
}

impl Reports {
    fn from_input(input: &str) -> Self {
        let levels = input
            .lines()
            .map(|line| {
                line.split_whitespace()
                    .map(|n| n.parse().unwrap())
                    .collect()
            })
            .collect();

        Reports { levels }
    }

    fn count_safe_reports(&self) -> i32 {
        self.levels
            .iter()
            .filter(|report| is_safe_report(report))
            .count() as i32
    }

    fn count_safe_reports_with_dampener(&self) -> i32 {
        self.levels
            .iter()
            .filter(|report| is_safe_with_dampener(report))
            .count() as i32
    }
}

fn main() {
    let input = read_input!();
    let reports = Reports::from_input(&input);

    println!("Part 1: {}", reports.count_safe_reports());
    println!("Part 2: {}", reports.count_safe_reports_with_dampener());
}

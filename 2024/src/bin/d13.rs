#[derive(Debug, Clone)]
struct ClawMachine {
    button_a: (i64, i64), // (X, Y)
    button_b: (i64, i64), // (X, Y)
    prize: (i64, i64),    // (X, Y)
}

fn parse_input(input: &str) -> Vec<ClawMachine> {
    input
        .split("\n\n")
        .map(|group| {
            let mut lines = group.lines();
            let parse_coords = |s: &str| {
                let nums: Vec<i64> = s
                    .split(|c: char| !c.is_digit(10) && c != '-')
                    .filter(|s| !s.is_empty())
                    .map(|n| n.parse().unwrap())
                    .collect();
                (nums[0], nums[1])
            };

            ClawMachine {
                button_a: parse_coords(lines.next().unwrap()),
                button_b: parse_coords(lines.next().unwrap()),
                prize: parse_coords(lines.next().unwrap()),
            }
        })
        .collect()
}

fn solve_machine(machine: &ClawMachine) -> Option<(i64, i64)> {
    let (ax, ay) = machine.button_a;
    let (bx, by) = machine.button_b;
    let (px, py) = machine.prize;

    // Convert to i128 to handle large numbers
    let ax = ax as i128;
    let ay = ay as i128;
    let bx = bx as i128;
    let by = by as i128;
    let px = px as i128;
    let py = py as i128;

    let det = ax * by - ay * bx;
    if det == 0 {
        return None;
    }

    // Using Cramer's rule
    let n = px * by - py * bx;
    let m = ax * py - ay * px;

    // Check if we have integer solutions
    if n % det != 0 || m % det != 0 {
        return None;
    }

    let n = (n / det) as i64;
    let m = (m / det) as i64;

    // Check if solution is valid (non-negative integers)
    if n >= 0 && m >= 0 {
        Some((n, m))
    } else {
        None
    }
}

fn calculate_tokens(a_presses: i64, b_presses: i64) -> i64 {
    3 * a_presses + b_presses
}

fn solve_part1(machines: &[ClawMachine]) -> i64 {
    let mut total_tokens = 0;

    for machine in machines {
        if let Some((a, b)) = solve_machine(machine) {
            total_tokens += calculate_tokens(a, b);
        }
    }

    total_tokens
}

fn solve_part2(machines: &[ClawMachine]) -> i64 {
    let offset = 10_000_000_000_000;
    let mut modified_machines = machines.to_vec();

    for machine in &mut modified_machines {
        machine.prize.0 += offset;
        machine.prize.1 += offset;
    }

    solve_part1(&modified_machines)
}

fn main() {
    let input = aoc2024::read_input!();
    let machines = parse_input(&input);

    let part1 = solve_part1(&machines);
    println!("Part 1: {}", part1);

    let part2 = solve_part2(&machines);
    println!("Part 2: {}", part2);
}

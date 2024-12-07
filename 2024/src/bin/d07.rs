use aoc2024::read_input;

enum Operator {
    Add,
    Multiply,
    Concat,
}

fn evaluate(numbers: &[i64], operators: &[Operator]) -> i64 {
    let mut result = numbers[0];
    for i in 0..operators.len() {
        match operators[i] {
            Operator::Add => result += numbers[i + 1],
            Operator::Multiply => result *= numbers[i + 1],
            Operator::Concat => {
                let right = numbers[i + 1];
                let right_digits = if right == 0 {
                    1
                } else {
                    (right as f64).log10() as i64 + 1
                };
                result = result * 10_i64.pow(right_digits as u32) + right;
            }
        }
    }
    result
}

fn can_make_value(target: i64, numbers: &[i64], use_concat: bool) -> bool {
    if numbers.len() == 1 {
        return target == numbers[0];
    }

    let operator_positions = numbers.len() - 1;
    let operators_count: u32 = if use_concat { 3 } else { 2 };
    let combinations = operators_count.pow(operator_positions as u32);

    for i in 0..combinations {
        let mut operators = Vec::new();
        let mut num = i;

        for _ in 0..operator_positions {
            let op = match num % operators_count {
                0 => Operator::Add,
                1 => Operator::Multiply,
                2 => Operator::Concat,
                _ => unreachable!(),
            };
            operators.push(op);
            num /= operators_count;
        }

        if evaluate(numbers, &operators) == target {
            return true;
        }
    }
    false
}

fn solve_puzzle(input: &str, use_concat: bool) -> i64 {
    let mut total = 0;

    for line in input.lines() {
        let parts: Vec<&str> = line.split(":").collect();
        let target: i64 = parts[0].trim().parse().unwrap();
        let numbers: Vec<i64> = parts[1]
            .trim()
            .split_whitespace()
            .map(|n| n.parse().unwrap())
            .collect();

        if can_make_value(target, &numbers, use_concat) {
            total += target;
        }
    }
    total
}

fn main() {
    let input = read_input!();

    let part1_result = solve_puzzle(&input, false);
    println!("Part 1 total calibration result: {}", part1_result);

    let part2_result = solve_puzzle(&input, true);
    println!("Part 2 total calibration result: {}", part2_result);
}

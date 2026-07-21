fn main() {
    let start = 125730;
    let end = 579381;

    let result1 = solve_part1(start, end);
    println!("Part 1 - Number of valid passwords: {}", result1);

    let result2 = solve_part2(start, end);
    println!("Part 2 - Number of valid passwords: {}", result2);
}

fn solve_part1(start: i32, end: i32) -> i32 {
    let mut count = 0;
    for num in start..=end {
        if never_decreases(num) && has_adjacent_digits(num) {
            count += 1;
        }
    }
    count
}

fn solve_part2(start: i32, end: i32) -> i32 {
    let mut count = 0;
    for num in start..=end {
        if never_decreases(num) && has_exact_double(num) {
            count += 1;
        }
    }
    count
}

fn has_adjacent_digits(num: i32) -> bool {
    let digits: Vec<char> = num.to_string().chars().collect();
    for i in 0..digits.len() - 1 {
        if digits[i] == digits[i + 1] {
            return true;
        }
    }
    false
}

fn has_exact_double(num: i32) -> bool {
    let digits: Vec<char> = num.to_string().chars().collect();
    let mut i = 0;
    while i < digits.len() {
        let digit = digits[i];
        let mut count = 1;

        // Count consecutive occurrences of the current digit
        while i + 1 < digits.len() && digits[i + 1] == digit {
            count += 1;
            i += 1;
        }

        if count == 2 {
            return true;
        }
        i += 1;
    }
    false
}

fn never_decreases(num: i32) -> bool {
    let digits: Vec<char> = num.to_string().chars().collect();
    for i in 0..digits.len() - 1 {
        if digits[i] > digits[i + 1] {
            return false;
        }
    }
    true
}

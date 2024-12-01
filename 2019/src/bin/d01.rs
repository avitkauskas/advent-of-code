use aoc2019::read_input;

fn main() {
    let input = read_input!();
    let modules = input.lines().map(|line| line.parse::<i32>().unwrap());

    let part1: i32 = modules.clone().map(|mass| (mass / 3) - 2).sum();
    let part2: i32 = modules.map(|mass| calculate_fuel(mass)).sum();

    println!("part1: {}", part1);
    println!("part2: {}", part2);
}

fn calculate_fuel(mass: i32) -> i32 {
    let mut total = 0;
    let mut current_mass = mass;

    while current_mass > 0 {
        let fuel = ((current_mass / 3) - 2).max(0);
        total += fuel;
        current_mass = fuel;
    }

    total
}

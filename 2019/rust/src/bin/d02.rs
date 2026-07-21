use aoc2019::read_input;

fn main() {
    let input = read_input!();

    // Parse input into vector of numbers
    let program: Vec<usize> = input
        .trim()
        .split(',')
        .map(|x| x.parse().expect("Failed to parse number"))
        .collect();

    // Solve part 1
    let part1_result = part1(&program);
    println!("Part 1: Value at position 0: {}", part1_result);

    // Solve part 2
    match part2(&program) {
        Some(result) => println!("Part 2: 100 * noun + verb = {}", result),
        None => println!("Part 2: No solution found!"),
    }
}

fn part1(program: &Vec<usize>) -> usize {
    let mut modified_program = program.clone();
    modified_program[1] = 12;
    modified_program[2] = 2;
    run_intcode(modified_program)
}

fn part2(program: &Vec<usize>) -> Option<usize> {
    const TARGET_OUTPUT: usize = 19690720;

    match find_noun_verb(program, TARGET_OUTPUT) {
        Some((noun, verb)) => Some(100 * noun + verb),
        None => None,
    }
}

fn run_intcode(mut program: Vec<usize>) -> usize {
    let mut position = 0;

    while position < program.len() {
        match program[position] {
            99 => break,
            1 => {
                let pos1 = program[position + 1];
                let pos2 = program[position + 2];
                let output_pos = program[position + 3];
                program[output_pos] = program[pos1] + program[pos2];
            }
            2 => {
                let pos1 = program[position + 1];
                let pos2 = program[position + 2];
                let output_pos = program[position + 3];
                program[output_pos] = program[pos1] * program[pos2];
            }
            _ => panic!("Unknown opcode encountered!"),
        }
        position += 4;
    }

    program[0]
}

fn find_noun_verb(original_program: &Vec<usize>, target: usize) -> Option<(usize, usize)> {
    for noun in 0..=99 {
        for verb in 0..=99 {
            let mut program = original_program.clone();
            program[1] = noun;
            program[2] = verb;

            if run_intcode(program) == target {
                return Some((noun, verb));
            }
        }
    }
    None
}

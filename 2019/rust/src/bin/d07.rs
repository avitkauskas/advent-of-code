use aoc2019::read_input;
use itertools::Itertools;

#[derive(Clone)]
struct IntcodeComputer {
    memory: Vec<i32>,
    position: usize,
    last_output: i32,
    inputs: Vec<i32>,
    input_position: usize,
    halted: bool,
}

impl IntcodeComputer {
    fn new(program: &Vec<i32>) -> Self {
        IntcodeComputer {
            memory: program.clone(),
            position: 0,
            last_output: 0,
            inputs: Vec::new(),
            input_position: 0,
            halted: false,
        }
    }

    fn get_parameter_modes(&self, instruction: i32) -> Vec<i32> {
        let mut modes = vec![0; 3];
        let mut mode_bits = instruction / 100;
        for i in 0..3 {
            modes[i] = mode_bits % 10;
            mode_bits /= 10;
        }
        modes
    }

    fn get_param(&self, offset: usize, modes: &[i32]) -> i32 {
        let value = self.memory[self.position + offset];
        match modes[offset - 1] {
            0 => self.memory[value as usize], // position mode
            1 => value,                       // immediate mode
            _ => panic!("Unknown parameter mode"),
        }
    }

    fn set_memory(&mut self, offset: usize, value: i32) {
        let pos = self.memory[self.position + offset] as usize;
        self.memory[pos] = value;
    }

    fn run(&mut self, mut new_inputs: Vec<i32>) -> i32 {
        // Add new inputs to existing inputs
        self.inputs.append(&mut new_inputs);

        while self.position < self.memory.len() {
            let instruction = self.memory[self.position];
            let opcode = instruction % 100;
            let modes = self.get_parameter_modes(instruction);

            match opcode {
                99 => {
                    self.halted = true;
                    break;
                }
                1 => {
                    let sum = self.get_param(1, &modes) + self.get_param(2, &modes);
                    self.set_memory(3, sum);
                    self.position += 4;
                }
                2 => {
                    let product = self.get_param(1, &modes) * self.get_param(2, &modes);
                    self.set_memory(3, product);
                    self.position += 4;
                }
                3 => {
                    if self.input_position >= self.inputs.len() {
                        break; // Stop execution but don't halt
                    }
                    self.set_memory(1, self.inputs[self.input_position]);
                    self.input_position += 1;
                    self.position += 2;
                }
                4 => {
                    self.last_output = self.get_param(1, &modes);
                    self.position += 2;
                }
                5 => {
                    if self.get_param(1, &modes) != 0 {
                        self.position = self.get_param(2, &modes) as usize;
                    } else {
                        self.position += 3;
                    }
                }
                6 => {
                    if self.get_param(1, &modes) == 0 {
                        self.position = self.get_param(2, &modes) as usize;
                    } else {
                        self.position += 3;
                    }
                }
                7 => {
                    let value = if self.get_param(1, &modes) < self.get_param(2, &modes) {
                        1
                    } else {
                        0
                    };
                    self.set_memory(3, value);
                    self.position += 4;
                }
                8 => {
                    let value = if self.get_param(1, &modes) == self.get_param(2, &modes) {
                        1
                    } else {
                        0
                    };
                    self.set_memory(3, value);
                    self.position += 4;
                }
                _ => panic!("Unknown opcode: {}", opcode),
            }
        }
        self.last_output
    }

    fn is_halted(&self) -> bool {
        self.halted
    }
}

fn run_amplifier_sequence(program: &Vec<i32>, phase_settings: &[i32]) -> i32 {
    let mut signal = 0;

    // Run each amplifier in sequence
    for &phase in phase_settings {
        let mut computer = IntcodeComputer::new(program);
        signal = computer.run(vec![phase, signal]);
    }

    signal
}

fn run_amplifier_feedback_loop(program: &Vec<i32>, phase_settings: &[i32]) -> i32 {
    let mut amplifiers = vec![IntcodeComputer::new(program); 5];

    // Initialize first amplifier separately
    let mut signal = amplifiers[0].run(vec![phase_settings[0], 0]);

    // Initialize remaining amplifiers
    for i in 1..5 {
        signal = amplifiers[i].run(vec![phase_settings[i], signal]);
    }

    let mut final_amplifier_output = signal;

    // Continue running until all amplifiers halt
    loop {
        for (i, amp) in amplifiers.iter_mut().enumerate() {
            if amp.is_halted() {
                continue;
            }
            signal = amp.run(vec![signal]);
            if i == 4 {
                final_amplifier_output = signal;
            }
        }

        if amplifiers.iter().all(|amp| amp.is_halted()) {
            break;
        }
    }

    final_amplifier_output
}

fn find_max_thruster_signal(
    program: &Vec<i32>,
    phase_range: std::ops::Range<i32>,
    run_amplifiers: fn(&Vec<i32>, &[i32]) -> i32,
) -> i32 {
    phase_range
        .permutations(5)
        .map(|phase_settings| run_amplifiers(program, &phase_settings))
        .max()
        .unwrap_or(0)
}

fn solve_part1(program: &Vec<i32>) -> i32 {
    find_max_thruster_signal(program, 0..5, run_amplifier_sequence)
}

fn solve_part2(program: &Vec<i32>) -> i32 {
    find_max_thruster_signal(program, 5..10, run_amplifier_feedback_loop)
}

fn main() {
    let input = read_input!();

    let program: Vec<i32> = input
        .trim()
        .split(',')
        .map(|x| x.parse().expect("Failed to parse number"))
        .collect();

    let max_signal_part1 = solve_part1(&program);
    println!("Part 1 - Maximum thruster signal: {}", max_signal_part1);

    let max_signal_part2 = solve_part2(&program);
    println!(
        "Part 2 - Maximum thruster signal with feedback loop: {}",
        max_signal_part2
    );
}

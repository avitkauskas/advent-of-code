use aoc2019::read_input;

struct IntcodeComputer {
    memory: Vec<i32>,
    position: usize,
    last_output: i32,
}

impl IntcodeComputer {
    fn new(program: &Vec<i32>) -> Self {
        IntcodeComputer {
            memory: program.clone(),
            position: 0,
            last_output: 0,
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

    fn run(&mut self, input: i32) -> i32 {
        while self.position < self.memory.len() {
            let instruction = self.memory[self.position];
            let opcode = instruction % 100;
            let modes = self.get_parameter_modes(instruction);

            match opcode {
                99 => break,
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
                    self.set_memory(1, input);
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
}

fn main() {
    let input = read_input!();

    let program: Vec<i32> = input
        .trim()
        .split(',')
        .map(|x| x.parse().expect("Failed to parse number"))
        .collect();

    // Part 1
    let mut computer1 = IntcodeComputer::new(&program);
    let result1 = computer1.run(1);
    println!("Part 1 - Air conditioner diagnostic code: {}", result1);

    // Part 2
    let mut computer2 = IntcodeComputer::new(&program);
    let result2 = computer2.run(5);
    println!("Part 2 - Thermal radiator diagnostic code: {}", result2);
}

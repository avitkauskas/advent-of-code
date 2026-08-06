use aoc2019::read_input;
use std::collections::HashMap;

struct IntcodeComputer {
    memory: HashMap<i64, i64>,
    position: i64,
    relative_base: i64,
    last_output: i64,
    outputs: Vec<i64>,
}

impl IntcodeComputer {
    fn new(program: &[i64]) -> Self {
        let mut memory = HashMap::new();
        for (addr, &value) in program.iter().enumerate() {
            memory.insert(addr as i64, value);
        }
        IntcodeComputer {
            memory,
            position: 0,
            relative_base: 0,
            last_output: 0,
            outputs: Vec::new(),
        }
    }

    fn get_parameter_modes(&self, instruction: i64) -> [u8; 3] {
        let mut modes = [0; 3];
        let mut mode_bits = instruction / 100;
        for mode in &mut modes {
            *mode = (mode_bits % 10) as u8;
            mode_bits /= 10;
        }
        modes
    }

    fn read_memory(&self, addr: i64) -> i64 {
        if addr < 0 {
            panic!("Invalid memory access at negative address {}", addr);
        }
        self.memory.get(&addr).copied().unwrap_or(0)
    }

    fn get_param(&self, offset: i64, modes: [u8; 3]) -> i64 {
        let value = self.read_memory(self.position + offset);
        match modes[(offset - 1) as usize] {
            // position mode
            0 => self.read_memory(value),
            // immediate mode
            1 => value,
            // relative mode
            2 => self.read_memory(value + self.relative_base),
            _ => panic!("Unknown parameter mode: {}", modes[(offset - 1) as usize]),
        }
    }

    fn set_memory(&mut self, offset: i64, modes: [u8; 3], value: i64) {
        let param = self.read_memory(self.position + offset);
        let addr = match modes[(offset - 1) as usize] {
            // position mode
            0 => param,
            // relative mode
            2 => param + self.relative_base,
            1 => panic!("Cannot write in immediate mode"),
            _ => panic!("Unknown parameter mode: {}", modes[(offset - 1) as usize]),
        };
        if addr < 0 {
            panic!("Invalid memory access at negative address {}", addr);
        }
        self.memory.insert(addr, value);
    }

    fn run(&mut self, input: i64) -> i64 {
        loop {
            let instruction = self.read_memory(self.position);
            let opcode = instruction % 100;
            if opcode == 99 {
                break;
            }
            let modes = self.get_parameter_modes(instruction);

            match opcode {
                1 => {
                    let sum = self.get_param(1, modes) + self.get_param(2, modes);
                    self.set_memory(3, modes, sum);
                    self.position += 4;
                }
                2 => {
                    let product = self.get_param(1, modes) * self.get_param(2, modes);
                    self.set_memory(3, modes, product);
                    self.position += 4;
                }
                3 => {
                    self.set_memory(1, modes, input);
                    self.position += 2;
                }
                4 => {
                    let value = self.get_param(1, modes);
                    self.last_output = value;
                    self.outputs.push(value);
                    self.position += 2;
                }
                5 => {
                    if self.get_param(1, modes) != 0 {
                        self.position = self.get_param(2, modes);
                    } else {
                        self.position += 3;
                    }
                }
                6 => {
                    if self.get_param(1, modes) == 0 {
                        self.position = self.get_param(2, modes);
                    } else {
                        self.position += 3;
                    }
                }
                7 => {
                    let value = if self.get_param(1, modes) < self.get_param(2, modes) {
                        1
                    } else {
                        0
                    };
                    self.set_memory(3, modes, value);
                    self.position += 4;
                }
                8 => {
                    let value = if self.get_param(1, modes) == self.get_param(2, modes) {
                        1
                    } else {
                        0
                    };
                    self.set_memory(3, modes, value);
                    self.position += 4;
                }
                9 => {
                    self.relative_base += self.get_param(1, modes);
                    self.position += 2;
                }
                _ => panic!("Unknown opcode: {}", opcode),
            }
        }
        self.last_output
    }
}

fn main() {
    let input = read_input!();

    let program: Vec<i64> = input
        .trim()
        .split(',')
        .map(|x| x.parse().expect("Failed to parse number"))
        .collect();

    // Part 1 - BOOST keycode
    let mut computer1 = IntcodeComputer::new(&program);
    let result1 = computer1.run(1);
    println!("Part 1 - BOOST keycode: {}", result1);

    // Part 2 - BOOST coordinates
    let mut computer2 = IntcodeComputer::new(&program);
    let result2 = computer2.run(2);
    println!("Part 2 - BOOST coordinates: {}", result2);
}

#[cfg(test)]
mod d09_tests {
    fn run_program(program: Vec<i64>, input: i64) -> Vec<i64> {
        let mut computer = super::IntcodeComputer::new(&program);
        computer.run(input);
        computer.outputs
    }

    #[test]
    fn produces_copy_of_itself() {
        let program = vec![
            109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99,
        ];
        let outputs = run_program(program, 0);
        assert_eq!(
            outputs,
            vec![
                109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99
            ],
            "self-copy program"
        );
    }

    #[test]
    fn outputs_sixteen_digit_number() {
        let outputs = run_program(
            vec![
                1_102,
                34_915_192,
                34_915_192,
                7,
                4,
                7,
                99,
                0,
            ],
            0,
        );
        assert_eq!(outputs, vec![1_219_070_632_396_864], "16-digit output");
    }

    #[test]
    fn outputs_large_number() {
        let outputs = run_program(vec![104, 1_125_899_906_842_624, 99], 0);
        assert_eq!(outputs, vec![1_125_899_906_842_624], "large number output");
    }
}

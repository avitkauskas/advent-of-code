struct Computer {
    reg_a: i64,
    reg_b: i64,
    reg_c: i64,
    instruction_pointer: usize,
    outputs: Vec<i64>,
}

impl Computer {
    fn new(a: i64, b: i64, c: i64) -> Self {
        Computer {
            reg_a: a,
            reg_b: b,
            reg_c: c,
            instruction_pointer: 0,
            outputs: Vec::new(),
        }
    }

    fn get_combo_value(&self, operand: i64) -> i64 {
        match operand {
            0..=3 => operand,
            4 => self.reg_a,
            5 => self.reg_b,
            6 => self.reg_c,
            _ => panic!("Invalid combo operand: {}", operand),
        }
    }

    fn execute(&mut self, program: &[i64]) {
        while self.instruction_pointer < program.len() {
            let opcode = program[self.instruction_pointer];
            let operand = program[self.instruction_pointer + 1];

            match opcode {
                0 => {
                    let power = 1 << self.get_combo_value(operand);
                    self.reg_a = self.reg_a / power;
                }
                1 => self.reg_b ^= operand,
                2 => self.reg_b = self.get_combo_value(operand) % 8,
                3 => {
                    if self.reg_a != 0 {
                        self.instruction_pointer = operand as usize;
                        continue;
                    }
                }
                4 => self.reg_b ^= self.reg_c,
                5 => self.outputs.push(self.get_combo_value(operand) % 8),
                6 => {
                    let power = 1 << self.get_combo_value(operand);
                    self.reg_b = self.reg_a / power;
                }
                7 => {
                    let power = 1 << self.get_combo_value(operand);
                    self.reg_c = self.reg_a / power;
                }
                _ => panic!("Invalid opcode: {}", opcode),
            }
            self.instruction_pointer += 2;
        }
    }
}

fn find_initial_a(program: &[i64]) -> i64 {
    let mut result = 0;
    let target_len = program.len();

    for position in (0..target_len).rev() {
        let current_power = 8_i64.pow(position as u32);
        let mut coef = 0;

        loop {
            let test_value = result + coef * current_power;
            let mut computer = Computer::new(test_value, 0, 0);
            computer.execute(program);

            if computer.outputs.len() == target_len
                && (position..target_len).all(|i| computer.outputs[i] == program[i])
            {
                result = test_value;
                break;
            }
            coef += 1;
        }
    }

    result
}

fn main() {
    let input = aoc2024::read_input!();
    let lines: Vec<&str> = input.lines().collect();

    let reg_a = lines[0].split(": ").nth(1).unwrap().parse::<i64>().unwrap();
    let reg_b = lines[1].split(": ").nth(1).unwrap().parse::<i64>().unwrap();
    let reg_c = lines[2].split(": ").nth(1).unwrap().parse::<i64>().unwrap();

    let program: Vec<i64> = lines[4]
        .split(": ")
        .nth(1)
        .unwrap()
        .split(',')
        .map(|x| x.trim().parse::<i64>().unwrap())
        .collect();

    // Part 1
    let mut computer = Computer::new(reg_a, reg_b, reg_c);
    computer.execute(&program);
    let result = computer
        .outputs
        .iter()
        .map(|x| x.to_string())
        .collect::<Vec<String>>()
        .join(",");
    println!("Part 1: {}", result);

    // Part 2
    let answer = find_initial_a(&program);
    println!("Part 2: {}", answer);
}

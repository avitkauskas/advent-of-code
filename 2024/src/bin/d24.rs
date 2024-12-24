use std::collections::{HashMap, HashSet};

#[derive(Debug, Clone)]
enum Gate {
    And(String, String),
    Or(String, String),
    Xor(String, String),
}

impl Gate {
    fn inputs(&self) -> (&str, &str) {
        match self {
            Gate::And(a, b) | Gate::Or(a, b) | Gate::Xor(a, b) => (a, b),
        }
    }

    fn evaluate(&self, values: &HashMap<String, bool>) -> Option<bool> {
        let (a, b) = self.inputs();
        match (values.get(a), values.get(b)) {
            (Some(va), Some(vb)) => Some(match self {
                Gate::And(_, _) => *va && *vb,
                Gate::Or(_, _) => *va || *vb,
                Gate::Xor(_, _) => *va != *vb,
            }),
            _ => None,
        }
    }
}

struct Circuit {
    gates: HashMap<String, Gate>,
    initial_values: HashMap<String, bool>,
    z_wires: HashSet<String>,
}

impl Circuit {
    fn new(input: &str) -> Self {
        let sections: Vec<&str> = input.split("\n\n").collect();
        let initial_values = Self::parse_initial_values(sections[0]);
        let (gates, z_wires) = Self::parse_gates(sections[1]);
        Circuit {
            gates,
            initial_values,
            z_wires,
        }
    }

    fn parse_initial_values(section: &str) -> HashMap<String, bool> {
        section
            .lines()
            .filter(|line| !line.is_empty())
            .map(|line| {
                let parts: Vec<&str> = line.split(": ").collect();
                (parts[0].to_string(), parts[1] == "1")
            })
            .collect()
    }

    fn parse_gates(section: &str) -> (HashMap<String, Gate>, HashSet<String>) {
        let mut gates = HashMap::new();
        let mut z_wires = HashSet::new();

        for line in section.lines().filter(|line| !line.is_empty()) {
            let parts: Vec<&str> = line.split(" -> ").collect();
            let gate_parts: Vec<&str> = parts[0].split(' ').collect();
            let output = parts[1].to_string();

            if output.starts_with('z') {
                z_wires.insert(output.clone());
            }

            if gate_parts.len() == 3 {
                let gate = match gate_parts[1] {
                    "AND" => Gate::And(gate_parts[0].to_string(), gate_parts[2].to_string()),
                    "OR" => Gate::Or(gate_parts[0].to_string(), gate_parts[2].to_string()),
                    "XOR" => Gate::Xor(gate_parts[0].to_string(), gate_parts[2].to_string()),
                    _ => panic!("Unknown gate type"),
                };
                gates.insert(output, gate);
            }
        }
        (gates, z_wires)
    }

    fn simulate(&self) -> i64 {
        let mut values = self.initial_values.clone();

        while self.z_wires.iter().any(|wire| !values.contains_key(wire)) {
            for (output, gate) in &self.gates {
                if !values.contains_key(output) {
                    if let Some(result) = gate.evaluate(&values) {
                        values.insert(output.clone(), result);
                    }
                }
            }
        }

        let mut z_wires: Vec<_> = self.z_wires.iter().collect();
        z_wires.sort();
        z_wires.iter().rev().fold(0, |acc, wire| {
            (acc << 1) | (*values.get(*wire).unwrap() as i64)
        })
    }

    fn is_input_wire(wire: &str) -> bool {
        wire.starts_with('x') || wire.starts_with('y') || wire.starts_with('z')
    }

    fn find_swapped_gates(&self) -> Vec<String> {
        let mut wrong = HashSet::new();
        let highest_z = self
            .gates
            .keys()
            .filter(|k| k.starts_with('z'))
            .max()
            .unwrap()
            .clone();

        for (output, gate) in &self.gates {
            // Check z-wires (except highest) for non-XOR gates
            if output.starts_with('z') && output != &highest_z {
                if !matches!(gate, Gate::Xor(_, _)) {
                    wrong.insert(output.clone());
                }
            }

            match gate {
                Gate::Xor(op1, op2) => {
                    // XOR gate with all intermediate wires
                    if !Self::is_input_wire(output)
                        && !Self::is_input_wire(op1)
                        && !Self::is_input_wire(op2)
                    {
                        wrong.insert(output.clone());
                    }

                    // XOR output feeding into OR
                    if self
                        .gates
                        .values()
                        .any(|g| matches!(g, Gate::Or(a, b) if a == output || b == output))
                    {
                        wrong.insert(output.clone());
                    }
                }
                Gate::And(op1, op2) => {
                    // AND gate not using x00 feeding into non-OR
                    if op1 != "x00" && op2 != "x00" {
                        if self.gates.values().any(|g| {
                            matches!(g, Gate::Xor(a, b) | Gate::And(a, b)
                                if a == output || b == output)
                        }) {
                            wrong.insert(output.clone());
                        }
                    }
                }
                _ => {}
            }
        }

        let mut result: Vec<_> = wrong.into_iter().collect();
        result.sort();
        result
    }
}

fn main() {
    let input = aoc2024::read_input!();
    let circuit = Circuit::new(&input);

    println!("Part 1: {}", circuit.simulate());
    println!("Part 2: {}", circuit.find_swapped_gates().join(","));
}

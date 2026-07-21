use aoc2019::read_input;
use std::collections::{HashMap, HashSet};

fn main() {
    let input = read_input!();
    println!("Part1: {}", solve_part1(&input));
    println!("Part2: {}", solve_part2(&input));
}

#[derive(Clone, Copy, PartialEq, Eq, Hash)]
struct Point {
    x: i32,
    y: i32,
}

impl Point {
    fn manhattan_distance(&self) -> i32 {
        self.x.abs() + self.y.abs()
    }
}

pub fn solve_part1(input: &str) -> i32 {
    let wires: Vec<&str> = input.lines().collect();

    let (wire1_points, _) = trace_wire(wires[0]);
    let (wire2_points, _) = trace_wire(wires[1]);

    wire1_points
        .intersection(&wire2_points)
        .map(|point| point.manhattan_distance())
        .min()
        .unwrap()
}

pub fn solve_part2(input: &str) -> i32 {
    let wires: Vec<&str> = input.lines().collect();

    let (wire1_points, wire1_steps) = trace_wire(wires[0]);
    let (wire2_points, wire2_steps) = trace_wire(wires[1]);

    wire1_points
        .intersection(&wire2_points)
        .map(|point| wire1_steps[point] + wire2_steps[point])
        .min()
        .unwrap()
}

fn trace_wire(path: &str) -> (HashSet<Point>, HashMap<Point, i32>) {
    let mut points = HashSet::new();
    let mut steps = HashMap::new();
    let mut current = Point { x: 0, y: 0 };
    let mut total_steps = 0;

    for instruction in path.split(',') {
        let (direction, distance) = instruction.split_at(1);
        let distance: i32 = distance.parse().unwrap();

        for _ in 0..distance {
            match direction {
                "R" => current.x += 1,
                "L" => current.x -= 1,
                "U" => current.y += 1,
                "D" => current.y -= 1,
                _ => panic!("Invalid direction"),
            }
            total_steps += 1;
            points.insert(current);
            steps.entry(current).or_insert(total_steps);
        }
    }

    (points, steps)
}

use aoc2024::read_input;
use std::collections::{HashMap, HashSet};

#[derive(Debug)]
struct Point {
    x: i32,
    y: i32,
}

impl Point {
    fn new(x: i32, y: i32) -> Self {
        Self { x, y }
    }

    fn is_within(&self, bounds: &Bounds) -> bool {
        self.x >= 0 && self.x <= bounds.max_x && self.y >= 0 && self.y <= bounds.max_y
    }

    fn is_resonating_with(&self, p1: &Point, p2: &Point) -> bool {
        (p2.y - p1.y) * (self.x - p1.x) == (self.y - p1.y) * (p2.x - p1.x)
    }
}

#[derive(Debug)]
struct Bounds {
    max_x: i32,
    max_y: i32,
}

#[derive(Debug)]
struct AntennaField {
    frequencies: HashMap<char, Vec<Point>>,
    bounds: Bounds,
}

impl AntennaField {
    fn from_input(input: &str) -> Self {
        let mut frequencies: HashMap<char, Vec<Point>> = HashMap::new();
        let mut max_x = 0;
        let mut max_y = 0;

        for (y, line) in input.lines().enumerate() {
            for (x, c) in line.chars().enumerate() {
                if c != '.' {
                    frequencies
                        .entry(c)
                        .or_default()
                        .push(Point::new(x as i32, y as i32));
                }
                max_x = max_x.max(x as i32);
            }
            max_y = max_y.max(y as i32);
        }

        Self {
            frequencies,
            bounds: Bounds { max_x, max_y },
        }
    }

    fn find_antinodes<F>(&self, antinode_finder: F) -> usize
    where
        F: Fn(&[Point], &Bounds) -> HashSet<(i32, i32)>,
    {
        self.frequencies
            .values()
            .filter(|antennas| antennas.len() >= 2)
            .flat_map(|antennas| antinode_finder(antennas, &self.bounds))
            .collect::<HashSet<_>>()
            .len()
    }

    fn find_double_distance_antinodes(&self) -> usize {
        self.find_antinodes(|antennas, bounds| {
            let mut antinodes = HashSet::new();

            for i in 0..antennas.len() {
                for j in (i + 1)..antennas.len() {
                    let a1 = &antennas[i];
                    let a2 = &antennas[j];

                    let dx = a2.x - a1.x;
                    let dy = a2.y - a1.y;

                    let potential_antinodes = [
                        Point::new(a1.x - dx, a1.y - dy),
                        Point::new(a2.x + dx, a2.y + dy),
                    ];

                    for point in potential_antinodes {
                        if point.is_within(bounds) {
                            antinodes.insert((point.x, point.y));
                        }
                    }
                }
            }
            antinodes
        })
    }

    fn find_resonant_harmonic_antinodes(&self) -> usize {
        self.find_antinodes(|antennas, bounds| {
            let mut antinodes = HashSet::new();

            // All antennas are antinodes due to resonant harmonics
            antinodes.extend(antennas.iter().map(|p| (p.x, p.y)));

            // Check all points for resonance with antenna pairs
            for i in 0..antennas.len() {
                for j in (i + 1)..antennas.len() {
                    let a1 = &antennas[i];
                    let a2 = &antennas[j];

                    for x in 0..=bounds.max_x {
                        for y in 0..=bounds.max_y {
                            let p = Point::new(x, y);
                            if p.is_resonating_with(a1, a2) {
                                antinodes.insert((x, y));
                            }
                        }
                    }
                }
            }
            antinodes
        })
    }
}

fn main() {
    let input = read_input!();
    let field = AntennaField::from_input(&input);

    println!("Part 1: {}", field.find_double_distance_antinodes());
    println!("Part 2: {}", field.find_resonant_harmonic_antinodes());
}

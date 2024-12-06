use num::Complex;
use std::collections::{HashMap, HashSet};

// Tries to trace the path. Returns:
// - Ok(HashSet) with visited positions if the guard exits the grid
// - Err(true) if a loop is found
fn solve(
    coords: &HashMap<Complex<i32>, char>,
    start: Complex<i32>,
    obstruction: Option<Complex<i32>>,
) -> Result<HashSet<Complex<i32>>, bool> {
    let mut seen = HashSet::new();
    let mut pos = start;
    // Initial direction is up (-1 on y axis in our coordinate system)
    let mut dir = Complex::new(0, -1);

    loop {
        // Keep track of positions AND directions to detect loops
        seen.insert((pos, dir));

        // Move forward until we hit an obstacle or exit
        while let Some(&c) = coords.get(&(pos + dir)) {
            if c == '#' || Some(pos + dir) == obstruction {
                break;
            }
            pos = pos + dir;
            seen.insert((pos, dir));
        }

        // If we're about to step outside the grid
        if !coords.contains_key(&(pos + dir)) {
            // Return only the positions (without directions) for counting
            return Ok(seen.into_iter().map(|(p, _)| p).collect());
        }

        // Turn right (multiply by i for 90° rotation)
        dir = dir * Complex::i();
        // If we've been here facing this direction before, we found a loop
        if seen.contains(&(pos, dir)) {
            return Err(true);
        }
    }
}

fn main() {
    let input = aoc2024::read_input!();

    // Store the grid as a map of complex coordinates to characters
    // This makes coordinate manipulation easier
    let mut coords = HashMap::new();
    let mut start = Complex::new(0, 0);

    // Parse input into coordinate map
    for (y, line) in input.lines().enumerate() {
        for (x, c) in line.chars().enumerate() {
            let pos = Complex::new(x as i32, y as i32);
            coords.insert(pos, c);
            if c == '^' {
                start = pos;
            }
        }
    }

    // Part 1: Find all positions the guard visits before exiting
    let visited = solve(&coords, start, None).unwrap();
    println!("Part 1: {}", visited.len());

    // Part 2: For each visited position (except start):
    // - Try placing an obstacle there
    // - Check if this creates a loop
    let loop_count = visited
        .iter()
        .filter(|&&pos| pos != start)
        .filter(|&&pos| solve(&coords, start, Some(pos)).is_err())
        .count();

    println!("Part 2: {}", loop_count);
}

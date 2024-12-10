use std::collections::{HashMap, HashSet};

fn main() {
    let input = aoc2024::read_input!();
    let mut grid = HashMap::new();

    // Build grid using (i32, i32) coordinates
    for (y, line) in input.lines().enumerate() {
        for (x, c) in line.chars().enumerate() {
            grid.insert((x as i32, y as i32), c.to_digit(10).unwrap());
        }
    }

    let mut trailhead_endpoints = HashMap::new();

    // Find all reachable height-9 positions from each trailhead
    for (&pos, &height) in &grid {
        if height == 0 {
            let mut endpoints = Vec::new();
            collect_trail_ends(&grid, pos, height, &mut endpoints);
            trailhead_endpoints.insert(pos, endpoints);
        }
    }

    // Part 1: Count unique endpoints (height 9) reachable from each trailhead
    let part1: usize = trailhead_endpoints
        .values()
        .map(|p| p.iter().collect::<HashSet<_>>().len())
        .sum();

    // Part 2: Count total number of ways to reach height 9 from each trailhead
    let part2: usize = trailhead_endpoints.values().map(|p| p.len()).sum();

    println!("Part 1: {}", part1);
    println!("Part 2: {}", part2);
}

fn collect_trail_ends(
    grid: &HashMap<(i32, i32), u32>,
    pos: (i32, i32),
    height: u32,
    endpoints: &mut Vec<(i32, i32)>,
) {
    if height == 9 {
        endpoints.push(pos);
        return;
    }

    for (dx, dy) in [(1, 0), (0, 1), (-1, 0), (0, -1)] {
        let next = (pos.0 + dx, pos.1 + dy);
        if grid.get(&next) == Some(&(height + 1)) {
            collect_trail_ends(grid, next, height + 1, endpoints);
        }
    }
}

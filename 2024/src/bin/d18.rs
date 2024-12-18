use std::collections::{HashSet, VecDeque};

fn parse_input(input: &str) -> Vec<(i32, i32)> {
    input
        .lines()
        .map(|line| {
            let mut parts = line.split(',');
            let x = parts.next().unwrap().parse().unwrap();
            let y = parts.next().unwrap().parse().unwrap();
            (x, y)
        })
        .collect()
}

fn find_shortest_path(corrupted: &HashSet<(i32, i32)>, max_coord: i32) -> Option<i32> {
    let start = (0, 0);
    let end = (max_coord, max_coord);
    let mut visited = HashSet::new();
    let mut queue = VecDeque::new();
    queue.push_back((start, 0));
    visited.insert(start);

    let directions = [(0, 1), (1, 0), (0, -1), (-1, 0)];

    while let Some(((x, y), steps)) = queue.pop_front() {
        if (x, y) == end {
            return Some(steps);
        }

        for (dx, dy) in directions {
            let new_x = x + dx;
            let new_y = y + dy;
            let new_pos = (new_x, new_y);

            if new_x >= 0
                && new_x <= max_coord
                && new_y >= 0
                && new_y <= max_coord
                && !corrupted.contains(&new_pos)
                && !visited.contains(&new_pos)
            {
                visited.insert(new_pos);
                queue.push_back((new_pos, steps + 1));
            }
        }
    }

    None
}

fn find_blocking_byte(coordinates: &[(i32, i32)], max_coord: i32) -> Option<(i32, i32)> {
    let mut corrupted = HashSet::new();

    for &coord in coordinates {
        corrupted.insert(coord);

        if find_shortest_path(&corrupted, max_coord).is_none() {
            return Some(coord);
        }
    }

    None
}

fn main() {
    let input = aoc2024::read_input!();
    let coordinates = parse_input(&input);

    // Part 1
    let corrupted: HashSet<(i32, i32)> = coordinates.iter().take(1024).copied().collect();

    if let Some(steps) = find_shortest_path(&corrupted, 70) {
        println!("Part 1: {}", steps);
    } else {
        println!("Part 1: No path found");
    }

    // Part 2
    if let Some((x, y)) = find_blocking_byte(&coordinates, 70) {
        println!("Part 2: {},{}", x, y);
    } else {
        println!("Part 2: No blocking byte found");
    }
}

use std::collections::HashSet;

#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash)]
enum Direction {
    Up,
    Right,
    Down,
    Left,
}

impl Direction {
    fn turn_right(self) -> Self {
        match self {
            Self::Up => Self::Right,
            Self::Right => Self::Down,
            Self::Down => Self::Left,
            Self::Left => Self::Up,
        }
    }

    fn step(self, (row, col): (i32, i32)) -> (i32, i32) {
        match self {
            Self::Up => (row - 1, col),
            Self::Right => (row, col + 1),
            Self::Down => (row + 1, col),
            Self::Left => (row, col - 1),
        }
    }
}

type Pos = (i32, i32);

fn trace_path(
    grid: &[Vec<char>],
    start_pos: Pos,
    start_dir: Direction,
) -> (Vec<(Pos, Direction)>, bool) {
    let mut path = Vec::new();
    let mut visited_states = HashSet::new();
    let mut pos = start_pos;
    let mut dir = start_dir;

    loop {
        let state = (pos, dir);
        if visited_states.contains(&state) {
            return (path, true); // Found a loop
        }
        visited_states.insert(state);
        path.push((pos, dir));

        let next_pos = dir.step(pos);

        if next_pos.0 < 0
            || next_pos.0 >= grid.len() as i32
            || next_pos.1 < 0
            || next_pos.1 >= grid[0].len() as i32
        {
            return (path, false); // Exit the grid
        }

        if grid[next_pos.0 as usize][next_pos.1 as usize] == '#' {
            dir = dir.turn_right();
        } else {
            pos = next_pos;
        }
    }
}

fn main() {
    let input = aoc2024::read_input!();
    let grid: Vec<Vec<char>> = input.lines().map(|line| line.chars().collect()).collect();

    // Find start position and direction
    let mut start_pos = (0, 0);
    let mut start_dir = Direction::Up;
    for (i, row) in grid.iter().enumerate() {
        for (j, &c) in row.iter().enumerate() {
            match c {
                '^' => {
                    start_pos = (i as i32, j as i32);
                    start_dir = Direction::Up;
                }
                '>' => {
                    start_pos = (i as i32, j as i32);
                    start_dir = Direction::Right;
                }
                'v' => {
                    start_pos = (i as i32, j as i32);
                    start_dir = Direction::Down;
                }
                '<' => {
                    start_pos = (i as i32, j as i32);
                    start_dir = Direction::Left;
                }
                _ => {}
            }
        }
    }

    // Part 1: Get the original path
    let (path, _) = trace_path(&grid, start_pos, start_dir);
    let mut visited: HashSet<_> = path.iter().map(|&(pos, _)| pos).collect();
    println!("Part 1: {}", visited.len());

    // Part 2: Remove start position from visited positions and check each for loops
    visited.remove(&start_pos);

    let mut loop_count = 0;
    for pos in visited {
        let mut test_grid = grid.clone();
        test_grid[pos.0 as usize][pos.1 as usize] = '#';
        let (_, has_loop) = trace_path(&test_grid, start_pos, start_dir);
        if has_loop {
            loop_count += 1;
        }
    }
    println!("Part 2: {}", loop_count);
}

use aoc2024::read_input;

type Grid = Vec<Vec<char>>;
type Point = (i32, i32);

fn main() {
    let grid: Grid = read_input!()
        .lines()
        .map(|line| line.chars().collect())
        .collect();

    println!("Part 1: {}", count_xmas(&grid));
    println!("Part 2: {}", count_x_mas(&grid));
}

fn is_valid_pos(grid: &Grid, pos: Point) -> bool {
    let (row, col) = pos;
    row >= 0 && (row as usize) < grid.len() && col >= 0 && (col as usize) < grid[0].len()
}

fn get_char(grid: &Grid, pos: Point) -> Option<char> {
    if !is_valid_pos(grid, pos) {
        return None;
    }
    Some(grid[pos.0 as usize][pos.1 as usize])
}

fn count_xmas(grid: &Grid) -> i32 {
    let directions = [
        (0, 1),
        (1, 1),
        (1, 0),
        (1, -1),
        (0, -1),
        (-1, -1),
        (-1, 0),
        (-1, 1),
    ];

    let mut count = 0;
    for r in 0..grid.len() {
        for c in 0..grid[0].len() {
            let pos = (r as i32, c as i32);
            for &dir in &directions {
                if "XMAS".chars().enumerate().all(|(i, ch)| {
                    get_char(grid, (pos.0 + dir.0 * i as i32, pos.1 + dir.1 * i as i32)) == Some(ch)
                }) {
                    count += 1;
                }
            }
        }
    }
    count
}

fn check_endpoints(
    grid: &Grid,
    center: Point,
    start_dir: Point,
    end_dir: Point,
    forward: bool,
) -> bool {
    let start = (center.0 + start_dir.0, center.1 + start_dir.1);
    let end = (center.0 + end_dir.0, center.1 + end_dir.1);

    match (get_char(grid, start), get_char(grid, end)) {
        (Some(s), Some(e)) => {
            if forward {
                s == 'M' && e == 'S'
            } else {
                s == 'S' && e == 'M'
            }
        }
        _ => false,
    }
}

fn count_x_mas(grid: &Grid) -> i32 {
    let mut count = 0;
    let diag = (((-1, -1), (1, 1)), ((-1, 1), (1, -1)));

    for r in 1..grid.len() - 1 {
        for c in 1..grid[0].len() - 1 {
            if grid[r][c] != 'A' {
                continue;
            }

            let center = (r as i32, c as i32);
            let (d1, d2) = diag;

            for forward1 in [true, false] {
                for forward2 in [true, false] {
                    if check_endpoints(grid, center, d1.0, d1.1, forward1)
                        && check_endpoints(grid, center, d2.0, d2.1, forward2)
                    {
                        count += 1;
                    }
                }
            }
        }
    }
    count
}

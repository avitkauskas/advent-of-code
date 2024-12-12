use std::collections::{HashSet, VecDeque};

struct Region {
    area: usize,
    perimeter: usize,
    cells: Vec<(usize, usize)>,
}

fn find_regions(grid: &Vec<Vec<char>>) -> Vec<Region> {
    let rows = grid.len();
    let cols = grid[0].len();
    let mut visited = HashSet::new();
    let mut regions = Vec::new();

    let is_valid =
        |r: i32, c: i32| -> bool { r >= 0 && r < rows as i32 && c >= 0 && c < cols as i32 };

    let directions = [(0, 1), (1, 0), (0, -1), (-1, 0)];

    for r in 0..rows {
        for c in 0..cols {
            if visited.contains(&(r, c)) {
                continue;
            }

            let current_type = grid[r][c];
            let mut queue = VecDeque::new();
            let mut region_cells = Vec::new();

            queue.push_back((r, c));
            visited.insert((r, c));

            while let Some((curr_r, curr_c)) = queue.pop_front() {
                region_cells.push((curr_r, curr_c));

                for (dr, dc) in directions.iter() {
                    let new_r = curr_r as i32 + dr;
                    let new_c = curr_c as i32 + dc;

                    if is_valid(new_r, new_c) {
                        let new_r = new_r as usize;
                        let new_c = new_c as usize;

                        if grid[new_r][new_c] == current_type && !visited.contains(&(new_r, new_c))
                        {
                            queue.push_back((new_r, new_c));
                            visited.insert((new_r, new_c));
                        }
                    }
                }
            }

            let area = region_cells.len();
            let mut perimeter = 0;

            for &(curr_r, curr_c) in &region_cells {
                for (dr, dc) in directions.iter() {
                    let new_r = curr_r as i32 + dr;
                    let new_c = curr_c as i32 + dc;

                    if !is_valid(new_r, new_c)
                        || grid[new_r as usize][new_c as usize] != current_type
                    {
                        perimeter += 1;
                    }
                }
            }

            regions.push(Region {
                area,
                perimeter,
                cells: region_cells,
            });
        }
    }

    regions
}

fn count_sides(region: &Region) -> usize {
    let region_cells: HashSet<_> = region.cells.iter().cloned().collect();
    let mut corners = 0;

    // Check all cell corners, including one cell beyond the region boundaries
    let min_r = region_cells.iter().map(|&(r, _)| r).min().unwrap();
    let max_r = region_cells.iter().map(|&(r, _)| r).max().unwrap();
    let min_c = region_cells.iter().map(|&(_, c)| c).min().unwrap();
    let max_c = region_cells.iter().map(|&(_, c)| c).max().unwrap();

    for r in min_r..=max_r + 1 {
        for c in min_c..=max_c + 1 {
            let mut count = 0;

            // Count how many of the four adjacent cells belong to the region
            if r > 0 && c > 0 && region_cells.contains(&(r - 1, c - 1)) {
                count += 1;
            }
            if r > 0 && region_cells.contains(&(r - 1, c)) {
                count += 1;
            }
            if c > 0 && region_cells.contains(&(r, c - 1)) {
                count += 1;
            }
            if region_cells.contains(&(r, c)) {
                count += 1;
            }

            // If count is odd (1 or 3) or if count is 2 with diagonal cells,
            // this is a corner
            if count == 1 || count == 3 {
                corners += 1;
            } else if count == 2 {
                // Check if the two cells are diagonal
                let has_topleft = r > 0 && c > 0 && region_cells.contains(&(r - 1, c - 1));
                let has_topright = r > 0 && region_cells.contains(&(r - 1, c));
                let has_bottomleft = c > 0 && region_cells.contains(&(r, c - 1));
                let has_bottomright = region_cells.contains(&(r, c));

                if (has_topleft && has_bottomright) || (has_topright && has_bottomleft) {
                    corners += 2; // Diagonal configuration counts as two corners
                }
            }
        }
    }

    corners
}

fn solve_part1(regions: &[Region]) -> usize {
    regions
        .iter()
        .map(|region| region.area * region.perimeter)
        .sum()
}

fn solve_part2(regions: &[Region]) -> usize {
    regions
        .iter()
        .map(|region| region.area * count_sides(region))
        .sum()
}

fn main() {
    let input = aoc2024::read_input!();
    let grid: Vec<Vec<char>> = input.lines().map(|line| line.chars().collect()).collect();

    let regions = find_regions(&grid);

    println!("{}", solve_part1(&regions));
    println!("{}", solve_part2(&regions));
}

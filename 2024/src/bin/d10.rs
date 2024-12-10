use std::collections::{HashSet, VecDeque};

struct Grid {
    cells: Vec<Vec<u32>>,
    height: usize,
    width: usize,
}

impl Grid {
    fn from_input(input: &str) -> Self {
        let cells: Vec<Vec<u32>> = input
            .lines()
            .map(|line| line.chars().map(|c| c.to_digit(10).unwrap()).collect())
            .collect();
        let height = cells.len();
        let width = cells[0].len();
        Self {
            cells,
            height,
            width,
        }
    }

    fn find_trailheads(&self) -> Vec<(usize, usize)> {
        let mut trailheads = Vec::new();
        for row in 0..self.height {
            for col in 0..self.width {
                if self.cells[row][col] == 0 {
                    trailheads.push((row, col));
                }
            }
        }
        trailheads
    }

    fn get_valid_neighbors(&self, pos: (usize, usize), current_height: u32) -> Vec<(usize, usize)> {
        let (row, col) = pos;
        let mut neighbors = Vec::new();

        for (dx, dy) in [(0, 1), (1, 0), (0, -1), (-1, 0)] {
            let next_row = row as i32 + dy;
            let next_col = col as i32 + dx;

            if next_row >= 0
                && next_row < self.height as i32
                && next_col >= 0
                && next_col < self.width as i32
            {
                let next_row = next_row as usize;
                let next_col = next_col as usize;
                if self.cells[next_row][next_col] == current_height + 1 {
                    neighbors.push((next_row, next_col));
                }
            }
        }
        neighbors
    }

    fn calculate_trailhead_score_part1(&self, start: (usize, usize)) -> usize {
        let mut reachable_nines = HashSet::new();
        let mut visited = HashSet::new();
        let mut queue = VecDeque::new();

        queue.push_back((start, 0));
        visited.insert(start);

        while let Some(((row, col), current_height)) = queue.pop_front() {
            if self.cells[row][col] == 9 {
                reachable_nines.insert((row, col));
            }

            for next_pos in self.get_valid_neighbors((row, col), current_height) {
                if !visited.contains(&next_pos) {
                    queue.push_back((next_pos, current_height + 1));
                    visited.insert(next_pos);
                }
            }
        }

        reachable_nines.len()
    }

    fn calculate_trailhead_score_part2(&self, start: (usize, usize)) -> usize {
        let mut paths = HashSet::new();
        let mut current_path = vec![start];
        self.find_paths(start, 0, &mut current_path, &mut paths);
        paths.len()
    }

    fn find_paths(
        &self,
        pos: (usize, usize),
        current_height: u32,
        current_path: &mut Vec<(usize, usize)>,
        paths: &mut HashSet<Vec<(usize, usize)>>,
    ) {
        if self.cells[pos.0][pos.1] == 9 {
            paths.insert(current_path.clone());
            return;
        }

        for next_pos in self.get_valid_neighbors(pos, current_height) {
            if !current_path.contains(&next_pos) {
                current_path.push(next_pos);
                self.find_paths(next_pos, current_height + 1, current_path, paths);
                current_path.pop();
            }
        }
    }
}

fn main() {
    let input = aoc2024::read_input!();
    let grid = Grid::from_input(&input);
    let trailheads = grid.find_trailheads();

    let score_part1: usize = trailheads
        .iter()
        .map(|&start| grid.calculate_trailhead_score_part1(start))
        .sum();

    let score_part2: usize = trailheads
        .iter()
        .map(|&start| grid.calculate_trailhead_score_part2(start))
        .sum();

    println!("Part 1: {}", score_part1);
    println!("Part 2: {}", score_part2);
}

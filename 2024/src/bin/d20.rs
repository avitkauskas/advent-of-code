use std::collections::{HashMap, HashSet, VecDeque};

type Point = (i32, i32);

struct Grid {
    data: Vec<Vec<char>>,
    start: Point,
    end: Point,
    valid_positions: HashSet<Point>,
    times: HashMap<Point, i32>, // time to reach each point from start
    normal_time: i32,           // time to reach end normally
}

impl Grid {
    fn new(input: &str) -> Self {
        let data: Vec<Vec<char>> = input.lines().map(|line| line.chars().collect()).collect();
        let mut start = (0, 0);
        let mut end = (0, 0);
        let mut valid_positions = HashSet::new();

        // Find start, end, and valid positions in one pass
        for (i, row) in data.iter().enumerate() {
            for (j, &cell) in row.iter().enumerate() {
                match cell {
                    'S' => {
                        start = (i as i32, j as i32);
                        valid_positions.insert((i as i32, j as i32));
                    }
                    'E' => {
                        end = (i as i32, j as i32);
                        valid_positions.insert((i as i32, j as i32));
                    }
                    '.' => {
                        valid_positions.insert((i as i32, j as i32));
                    }
                    _ => {}
                }
            }
        }

        let mut grid = Self {
            data,
            start,
            end,
            valid_positions,
            times: HashMap::new(),
            normal_time: 0,
        };

        // Calculate times from start to all reachable positions
        grid.times = grid.calculate_travel_times(grid.start);
        grid.normal_time = *grid.times.get(&grid.end).unwrap();

        grid
    }

    fn calculate_travel_times(&self, from: Point) -> HashMap<Point, i32> {
        let mut times = HashMap::new();
        let mut queue = VecDeque::new();

        times.insert(from, 0);
        queue.push_back(from);

        let dirs = [(0, 1), (1, 0), (0, -1), (-1, 0)];

        while let Some(pos) = queue.pop_front() {
            let current_time = times[&pos];

            for (dx, dy) in dirs {
                let new_pos = (pos.0 + dx, pos.1 + dy);
                if self.is_valid_move(new_pos) && !times.contains_key(&new_pos) {
                    times.insert(new_pos, current_time + 1);
                    queue.push_back(new_pos);
                }
            }
        }
        times
    }

    fn is_valid_move(&self, pos: Point) -> bool {
        pos.0 >= 0
            && pos.0 < self.data.len() as i32
            && pos.1 >= 0
            && pos.1 < self.data[0].len() as i32
            && self.valid_positions.contains(&pos)
    }

    fn count_cheats(&self, max_cheat_time: i32) -> usize {
        let mut count = 0;

        for &pos1 in &self.valid_positions {
            if !self.times.contains_key(&pos1) {
                continue;
            }

            let time_to_pos1 = self.times[&pos1];

            for i in -max_cheat_time..=max_cheat_time {
                for j in -max_cheat_time..=max_cheat_time {
                    if i.abs() + j.abs() > max_cheat_time {
                        continue;
                    }

                    let pos2 = (pos1.0 + i, pos1.1 + j);

                    if self.valid_positions.contains(&pos2) && self.times.contains_key(&pos2) {
                        let time_from_pos2_to_end = self.times[&self.end] - self.times[&pos2];
                        let cheat_time = i.abs() + j.abs();
                        let total_time = time_to_pos1 + cheat_time + time_from_pos2_to_end;
                        let time_saved = self.normal_time - total_time;

                        if time_saved >= 100 {
                            count += 1;
                        }
                    }
                }
            }
        }
        count
    }
}

fn main() {
    let input = aoc2024::read_input!();
    let grid = Grid::new(&input);

    println!("Part 1: {}", grid.count_cheats(2));
    println!("Part 2: {}", grid.count_cheats(20));
}

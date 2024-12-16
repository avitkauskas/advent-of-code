use std::cmp::Ordering;
use std::collections::{BinaryHeap, HashMap, HashSet};

#[derive(Copy, Clone, Eq, PartialEq, Hash)]
struct Position {
    x: i32,
    y: i32,
    direction: (i32, i32),
}

impl Position {
    fn new(x: i32, y: i32, direction: (i32, i32)) -> Self {
        Self { x, y, direction }
    }

    fn get_turns(&self) -> [(i32, i32); 2] {
        match self.direction {
            (1, 0) => [(0, -1), (0, 1)],  // East -> North/South
            (-1, 0) => [(0, 1), (0, -1)], // West -> South/North
            (0, 1) => [(1, 0), (-1, 0)],  // South -> East/West
            (0, -1) => [(-1, 0), (1, 0)], // North -> West/East
            _ => unreachable!(),
        }
    }

    fn move_forward(&self) -> (i32, i32) {
        (self.x + self.direction.0, self.y + self.direction.1)
    }
}

#[derive(Eq, PartialEq)]
struct State {
    cost: i32,
    position: Position,
    path: Vec<(i32, i32)>,
}

impl Ord for State {
    fn cmp(&self, other: &Self) -> Ordering {
        other.cost.cmp(&self.cost)
    }
}

impl PartialOrd for State {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

struct Grid {
    data: Vec<Vec<char>>,
    width: i32,
    height: i32,
    start: (i32, i32),
    end: (i32, i32),
}

impl Grid {
    fn new(input: &str) -> Self {
        let data: Vec<Vec<char>> = input.lines().map(|line| line.chars().collect()).collect();
        let height = data.len() as i32;
        let width = data[0].len() as i32;
        let mut start = (0, 0);
        let mut end = (0, 0);

        for y in 0..height {
            for x in 0..width {
                match data[y as usize][x as usize] {
                    'S' => start = (x, y),
                    'E' => end = (x, y),
                    _ => continue,
                }
            }
        }

        Self {
            data,
            width,
            height,
            start,
            end,
        }
    }

    fn is_valid_position(&self, x: i32, y: i32) -> bool {
        x >= 0
            && x < self.width
            && y >= 0
            && y < self.height
            && self.data[y as usize][x as usize] != '#'
    }
}

fn solve(input: &str) -> (i32, usize) {
    let grid = Grid::new(input);
    let mut heap = BinaryHeap::new();
    let mut min_costs: HashMap<Position, i32> = HashMap::new();
    let mut optimal_paths = Vec::new();
    let mut min_total_cost = i32::MAX;

    // Start facing east
    heap.push(State {
        cost: 0,
        position: Position::new(grid.start.0, grid.start.1, (1, 0)),
        path: vec![grid.start],
    });

    while let Some(State {
        cost,
        position,
        path,
    }) = heap.pop()
    {
        // Found path to end
        if (position.x, position.y) == grid.end {
            if cost < min_total_cost {
                min_total_cost = cost;
                optimal_paths = vec![path];
            } else if cost == min_total_cost {
                optimal_paths.push(path);
            }
            continue;
        }

        // Skip if we've exceeded minimum cost or found a worse path
        if cost > min_total_cost || min_costs.get(&position).map_or(false, |&c| cost > c) {
            continue;
        }

        min_costs.insert(position, cost);

        // Try turns
        for &new_dir in &position.get_turns() {
            heap.push(State {
                cost: cost + 1000,
                position: Position::new(position.x, position.y, new_dir),
                path: path.clone(),
            });
        }

        // Try moving forward
        let (new_x, new_y) = position.move_forward();
        if grid.is_valid_position(new_x, new_y) {
            let mut new_path = path.clone();
            new_path.push((new_x, new_y));
            heap.push(State {
                cost: cost + 1,
                position: Position::new(new_x, new_y, position.direction),
                path: new_path,
            });
        }
    }

    let optimal_tiles: HashSet<_> = optimal_paths.into_iter().flat_map(|path| path).collect();

    (min_total_cost, optimal_tiles.len())
}

fn main() {
    let input = aoc2024::read_input!();
    let (part1, part2) = solve(&input);
    println!("Part 1: {}", part1);
    println!("Part 2: {}", part2);
}

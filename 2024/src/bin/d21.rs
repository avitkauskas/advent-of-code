use std::collections::{HashMap, VecDeque};

#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
struct Position {
    x: i32,
    y: i32,
}

#[derive(Clone, Copy, PartialEq, Eq, Hash)]
enum KeypadMode {
    Numeric,
    Directional,
}

#[derive(Clone, Copy, PartialEq, Eq, Hash)]
struct PathKey {
    start: Position,
    goal: Position,
    remaining_steps: i32,
    mode: KeypadMode,
}

fn get_position(c: char, mode: KeypadMode) -> Position {
    match mode {
        KeypadMode::Numeric => match c {
            '0' => Position { x: 1, y: 3 },
            '1' => Position { x: 0, y: 2 },
            '2' => Position { x: 1, y: 2 },
            '3' => Position { x: 2, y: 2 },
            '4' => Position { x: 0, y: 1 },
            '5' => Position { x: 1, y: 1 },
            '6' => Position { x: 2, y: 1 },
            '7' => Position { x: 0, y: 0 },
            '8' => Position { x: 1, y: 0 },
            '9' => Position { x: 2, y: 0 },
            'A' => Position { x: 2, y: 3 },
            _ => panic!("Invalid numeric button: {}", c),
        },
        KeypadMode::Directional => match c {
            '^' => Position { x: 1, y: 0 },
            'A' => Position { x: 2, y: 0 },
            '<' => Position { x: 0, y: 1 },
            'v' => Position { x: 1, y: 1 },
            '>' => Position { x: 2, y: 1 },
            _ => panic!("Invalid directional button: {}", c),
        },
    }
}

fn is_valid_position(pos: Position, mode: KeypadMode) -> bool {
    match mode {
        KeypadMode::Numeric => {
            pos.x >= 0 && pos.x < 3 && pos.y >= 0 && pos.y < 4 && !(pos.x == 0 && pos.y == 3)
            // empty cell
        }
        KeypadMode::Directional => {
            pos.x >= 0 && pos.x < 3 && pos.y >= 0 && pos.y < 2 && !(pos.x == 0 && pos.y == 0)
            // empty cell
        }
    }
}

fn find_shortest_path(
    cache: &mut HashMap<PathKey, usize>,
    start: Position,
    goal: Position,
    remaining_steps: i32,
    mode: KeypadMode,
) -> usize {
    let key = PathKey {
        start,
        goal,
        remaining_steps,
        mode,
    };
    if let Some(&result) = cache.get(&key) {
        return result;
    }

    let mut min_length = usize::MAX;
    let mut queue = VecDeque::new();
    queue.push_back((start, String::new()));

    while let Some((pos, path)) = queue.pop_front() {
        if pos == goal {
            let new_path = path + "A";
            let path_length = match mode {
                KeypadMode::Numeric => {
                    solve_sequence(cache, &new_path, remaining_steps, KeypadMode::Directional)
                }
                KeypadMode::Directional => {
                    if remaining_steps > 0 {
                        solve_sequence(
                            cache,
                            &new_path,
                            remaining_steps - 1,
                            KeypadMode::Directional,
                        )
                    } else {
                        new_path.len()
                    }
                }
            };
            min_length = min_length.min(path_length);
            continue;
        }

        // Add possible moves toward the goal
        let possible_moves = [
            (pos.y < goal.y, 1, 0, 'v'),
            (pos.y > goal.y, -1, 0, '^'),
            (pos.x < goal.x, 0, 1, '>'),
            (pos.x > goal.x, 0, -1, '<'),
        ];

        for &(should_move, dy, dx, direction) in &possible_moves {
            if should_move {
                let new_pos = Position {
                    x: pos.x + dx,
                    y: pos.y + dy,
                };
                if is_valid_position(new_pos, mode) {
                    queue.push_back((new_pos, path.clone() + &direction.to_string()));
                }
            }
        }
    }

    cache.insert(key, min_length);
    min_length
}

fn solve_sequence(
    cache: &mut HashMap<PathKey, usize>,
    sequence: &str,
    remaining_steps: i32,
    mode: KeypadMode,
) -> usize {
    if mode == KeypadMode::Directional && remaining_steps == 0 {
        return sequence.len();
    }

    let mut current = get_position('A', mode);
    let mut total_length = 0;

    for c in sequence.chars() {
        let target = get_position(c, mode);
        total_length += find_shortest_path(cache, current, target, remaining_steps, mode);
        current = target;
    }

    if mode == KeypadMode::Numeric {
        let numeric_value = sequence[..sequence.len() - 1].parse::<usize>().unwrap();
        total_length * numeric_value
    } else {
        total_length
    }
}

fn solve_code(code: &str, num_robots: i32) -> usize {
    let mut cache = HashMap::new();
    solve_sequence(&mut cache, code, num_robots, KeypadMode::Numeric)
}

fn main() {
    let input = aoc2024::read_input!();
    let codes: Vec<_> = input.lines().filter(|l| !l.is_empty()).collect();

    let part1: usize = codes.iter().map(|&code| solve_code(code, 2)).sum();
    println!("Part 1: {}", part1);

    let part2: usize = codes.iter().map(|&code| solve_code(code, 25)).sum();
    println!("Part 2: {}", part2);
}

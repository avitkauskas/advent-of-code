use std::collections::HashMap;

#[derive(Debug, Clone)]
struct Robot {
    pos: (i32, i32),
    vel: (i32, i32),
}

impl Robot {
    fn from_str(s: &str) -> Self {
        let parts: Vec<&str> = s.split(' ').collect();
        let pos_str = &parts[0][2..];
        let vel_str = &parts[1][2..];

        let pos: Vec<i32> = pos_str.split(',').map(|x| x.parse().unwrap()).collect();
        let vel: Vec<i32> = vel_str.split(',').map(|x| x.parse().unwrap()).collect();

        Robot {
            pos: (pos[0], pos[1]),
            vel: (vel[0], vel[1]),
        }
    }

    fn update(&mut self, width: i32, height: i32) {
        self.pos.0 = (self.pos.0 + self.vel.0).rem_euclid(width);
        self.pos.1 = (self.pos.1 + self.vel.1).rem_euclid(height);
    }
}

fn find_triangle_pattern(positions: &HashMap<(i32, i32), i32>, width: i32, height: i32) -> bool {
    for (&(x, y), _) in positions.iter() {
        if x < 2 || x > width - 3 || y < 1 || y > height - 3 {
            continue;
        }

        if positions.contains_key(&(x - 1, y + 1))
            && positions.contains_key(&(x, y + 1))
            && positions.contains_key(&(x + 1, y + 1))
            && positions.contains_key(&(x - 2, y + 2))
            && positions.contains_key(&(x - 1, y + 2))
            && positions.contains_key(&(x, y + 2))
            && positions.contains_key(&(x + 1, y + 2))
            && positions.contains_key(&(x + 2, y + 2))
        {
            return true;
        }
    }
    false
}

fn count_quadrant_robots(positions: &HashMap<(i32, i32), i32>, width: i32, height: i32) -> i32 {
    let mut quadrants = vec![0; 4];
    for (&(x, y), &count) in positions {
        if x == width / 2 || y == height / 2 {
            continue;
        }

        let quadrant = match (x > width / 2, y > height / 2) {
            (false, false) => 0,
            (true, false) => 1,
            (false, true) => 2,
            (true, true) => 3,
        };

        quadrants[quadrant] += count;
    }

    quadrants.iter().product()
}

fn main() {
    let input = aoc2024::read_input!();
    let mut robots = input.lines().map(Robot::from_str).collect::<Vec<_>>();
    let initial_robots = robots.clone();

    let width = 101;
    let height = 103;

    let mut positions = HashMap::new();

    // Part 1
    for _ in 0..100 {
        for robot in &mut robots {
            robot.update(width, height);
        }
    }
    for robot in &robots {
        *positions.entry(robot.pos).or_insert(0) += 1;
    }

    let part1_result = count_quadrant_robots(&positions, width, height);
    println!("Part 1: {}", part1_result);

    // Part 2
    robots = initial_robots;
    let part2_result: i32;
    let mut seconds = 0;
    loop {
        positions.clear();
        for robot in &robots {
            *positions.entry(robot.pos).or_insert(0) += 1;
        }

        if find_triangle_pattern(&positions, width, height) {
            part2_result = seconds;
            break;
        }

        for robot in &mut robots {
            robot.update(width, height);
        }
        seconds += 1;
    }

    println!("Part 2: {}", part2_result);
}

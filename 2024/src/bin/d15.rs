use num::Complex;
use std::collections::HashMap;

type Coord = Complex<i32>;
type Grid = HashMap<Coord, char>;

fn find_boxes(coords: &Grid, pos: Coord, dir: Coord) -> Grid {
    let c = coords.get(&pos).unwrap_or(&'.');
    if !matches!(c, '[' | 'O' | ']') {
        return HashMap::new();
    }

    let c_adj = if dir.im == 0 {
        pos
    } else {
        pos + ("]O[".find(*c).unwrap() as i32 - 1)
    };

    let mut result = HashMap::new();
    result.insert(pos, *coords.get(&pos).unwrap());
    result.insert(c_adj, *coords.get(&c_adj).unwrap());

    let next_boxes = find_boxes(coords, pos + dir, dir);
    let next_adj_boxes = find_boxes(coords, c_adj + dir, dir);

    result.extend(next_boxes);
    result.extend(next_adj_boxes);
    result
}

fn solve(field: &str, moves: &str) -> i32 {
    let mut coords: Grid = HashMap::new();

    // Parse the field into coordinates
    for (y, line) in field.lines().enumerate() {
        for (x, c) in line.chars().enumerate() {
            coords.insert(Complex::new(x as i32, y as i32), c);
        }
    }

    // Find starting position
    let mut pos = *coords
        .iter()
        .find(|(_, &c)| c == '@')
        .map(|(k, _)| k)
        .unwrap();
    coords.insert(pos, '.');

    // Process moves
    let dir_map: HashMap<char, Coord> = [
        ('>', Complex::new(1, 0)),
        ('v', Complex::new(0, 1)),
        ('<', Complex::new(-1, 0)),
        ('^', Complex::new(0, -1)),
    ]
    .into_iter()
    .collect();

    for mv in moves.chars() {
        let dir = dir_map[&mv];
        let next_pos = pos + dir;

        if coords.get(&next_pos) == Some(&'#') {
            continue;
        }

        let boxes = find_boxes(&coords, next_pos, dir);

        if boxes
            .iter()
            .all(|(&box_pos, _)| coords.get(&(box_pos + dir)) != Some(&'#'))
        {
            // Clear old positions
            for box_pos in boxes.keys() {
                coords.insert(*box_pos, '.');
            }
            // Set new positions
            for (box_pos, &box_char) in boxes.iter() {
                coords.insert(*box_pos + dir, box_char);
            }
            pos = next_pos;
        }
    }

    // Calculate sum
    coords
        .iter()
        .filter(|(_, &c)| c == '[' || c == 'O')
        .map(|(pos, _)| pos.im * 100 + pos.re)
        .sum()
}

fn main() {
    let input = aoc2024::read_input!();
    let (field, moves) = input.split_once("\n\n").unwrap();
    let moves = moves.replace('\n', "");

    // Part 1
    println!("Part1: {}", solve(field, &moves));

    // Part 2
    let field2 = field
        .replace('#', "##")
        .replace('O', "[]")
        .replace('.', "..")
        .replace('@', "@.");
    println!("Part2: {}", solve(&field2, &moves));
}

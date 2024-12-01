use aoc2019::read_input;
use std::collections::{HashMap, HashSet};

fn main() {
    let input = read_input!();
    let orbit_map = parse_input(&input);

    let result1 = solve_part1(&orbit_map);
    println!("Total number of direct and indirect orbits: {}", result1);

    let result2 = solve_part2(&orbit_map);
    println!("Minimum number of orbital transfers required: {}", result2);
}

fn parse_input(input: &str) -> HashMap<String, String> {
    let mut orbit_map: HashMap<String, String> = HashMap::new();
    for line in input.lines() {
        let parts: Vec<&str> = line.split(')').collect();
        if parts.len() == 2 {
            orbit_map.insert(parts[1].to_string(), parts[0].to_string());
        }
    }
    orbit_map
}

fn solve_part1(orbit_map: &HashMap<String, String>) -> usize {
    orbit_map
        .keys()
        .map(|object| count_orbits(orbit_map, object))
        .sum()
}

fn count_orbits(orbit_map: &HashMap<String, String>, object: &str) -> usize {
    let mut count = 0;
    let mut current = object;

    while let Some(parent) = orbit_map.get(current) {
        count += 1;
        current = parent;
    }

    count
}

fn solve_part2(orbit_map: &HashMap<String, String>) -> usize {
    // Get the paths from YOU and SAN to COM
    let you_path = get_path_to_com(orbit_map, "YOU");
    let san_path = get_path_to_com(orbit_map, "SAN");

    // Convert SAN path to set for finding common ancestors
    let san_set: HashSet<_> = san_path.iter().collect();

    // Find the first common ancestor
    let first_common = you_path
        .iter()
        .find(|&node| san_set.contains(node))
        .unwrap();

    // Calculate distances to the first common ancestor
    let get_distance =
        |path: &Vec<String>| path.iter().position(|node| node == first_common).unwrap();
    let you_distance = get_distance(&you_path);
    let san_distance = get_distance(&san_path);

    // Total transfers needed is the sum of distances to the common ancestor
    you_distance + san_distance
}

fn get_path_to_com(orbit_map: &HashMap<String, String>, start: &str) -> Vec<String> {
    let mut path = Vec::new();
    let mut current = start;

    while let Some(parent) = orbit_map.get(current) {
        path.push(parent.to_string());
        current = parent;
    }

    path
}

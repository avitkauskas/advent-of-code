fn count_column_heights(schematic: &str) -> Vec<usize> {
    let rows: Vec<_> = schematic.lines().collect();
    let width = rows[0].len();
    let inner_rows = &rows[1..rows.len() - 1]; // Skip first and last rows

    (0..width)
        .map(|col| {
            inner_rows
                .iter()
                .filter(|row| row.chars().nth(col).unwrap() == '#')
                .count()
        })
        .collect()
}

fn can_fit(lock: &[usize], key: &[usize]) -> bool {
    lock.iter().zip(key.iter()).all(|(&l, &k)| l + k <= 5)
}

fn main() {
    let input = aoc2024::read_input!();
    let schematics: Vec<_> = input.split("\n\n").collect();

    let mut locks = vec![];
    let mut keys = vec![];

    for schematic in schematics {
        let first_row = schematic.lines().next().unwrap();
        if first_row.chars().all(|c| c == '#') {
            locks.push(count_column_heights(schematic));
        } else {
            keys.push(count_column_heights(schematic));
        }
    }

    let fitting_count = locks
        .iter()
        .flat_map(|lock| keys.iter().filter(|key| can_fit(lock, key)))
        .count();

    println!("Part 1: {}", fitting_count);
}

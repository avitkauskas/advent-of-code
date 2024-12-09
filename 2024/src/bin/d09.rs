fn main() {
    let input = aoc2024::read_input!();
    let disk = DiskState::parse(&input);
    println!("Part 1: {}", solve_part1(&disk));
    println!("Part 2: {}", solve_part2(&disk));
}

#[derive(Clone)]
struct DiskState {
    blocks: Vec<Option<usize>>,
    file_sizes: Vec<usize>,
}

impl DiskState {
    fn parse(input: &str) -> Self {
        let numbers: Vec<_> = input
            .chars()
            .filter_map(|c| c.to_digit(10).map(|d| d as usize))
            .collect();

        let mut blocks = Vec::new();
        let mut file_sizes = Vec::new();
        let mut file_id = 0;

        for (i, &size) in numbers.iter().enumerate() {
            if i % 2 == 0 {
                file_sizes.push(size);
                blocks.extend(std::iter::repeat(Some(file_id)).take(size));
                file_id += 1;
            } else {
                blocks.extend(std::iter::repeat(None).take(size));
            }
        }

        Self { blocks, file_sizes }
    }

    fn calculate_checksum(&self) -> i64 {
        self.blocks
            .iter()
            .enumerate()
            .filter_map(|(pos, &block)| block.map(|id| pos as i64 * id as i64))
            .sum()
    }

    fn find_free_space(&self, current_pos: usize, required_size: usize) -> Option<usize> {
        let mut pos = 0;
        let mut continuous_empty = 0;

        while pos < current_pos {
            if self.blocks[pos].is_none() {
                continuous_empty += 1;
                if continuous_empty == required_size {
                    return Some(pos - required_size + 1);
                }
            } else {
                continuous_empty = 0;
            }
            pos += 1;
        }
        None
    }

    fn move_file(&mut self, from: usize, to: usize, size: usize) {
        let file_id = self.blocks[from].unwrap();
        self.blocks[from..from + size].fill(None);
        self.blocks[to..to + size].fill(Some(file_id));
    }
}

fn solve_part1(disk: &DiskState) -> i64 {
    let mut disk = disk.clone();
    compact_blocks_part1(&mut disk.blocks);
    disk.calculate_checksum()
}

fn solve_part2(disk: &DiskState) -> i64 {
    let mut disk = disk.clone();
    compact_blocks_part2(&mut disk);
    disk.calculate_checksum()
}

fn compact_blocks_part1(blocks: &mut [Option<usize>]) {
    loop {
        let first_empty = match blocks.iter().position(|b| b.is_none()) {
            Some(pos) => pos,
            None => break,
        };

        let last_file = match blocks[first_empty..].iter().rposition(|b| b.is_some()) {
            Some(pos) => first_empty + pos,
            None => break,
        };

        if last_file <= first_empty {
            break;
        }

        blocks.swap(first_empty, last_file);
    }
}

fn compact_blocks_part2(disk: &mut DiskState) {
    // Process files in reverse ID order
    for file_id in (0..disk.file_sizes.len()).rev() {
        let file_size = disk.file_sizes[file_id];

        // Find current position of the file
        if let Some(current_pos) = disk.blocks.iter().position(|&b| b == Some(file_id)) {
            // Try to find leftmost suitable position
            if let Some(new_pos) = disk.find_free_space(current_pos, file_size) {
                disk.move_file(current_pos, new_pos, file_size);
            }
        }
    }
}

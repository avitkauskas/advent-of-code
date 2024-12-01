use std::fs;
use std::path::Path;

#[macro_export]
macro_rules! read_input {
    () => {
        $crate::read_input(file!())
    };
}

pub fn read_input(bin_path: &str) -> String {
    let file_name = Path::new(bin_path)
        .file_name()
        .unwrap()
        .to_str()
        .unwrap()
        .replace(".rs", ".txt");

    fs::read_to_string(Path::new("input").join(&file_name))
        .unwrap_or_else(|_| panic!("Failed to read input file: {}", file_name))
}

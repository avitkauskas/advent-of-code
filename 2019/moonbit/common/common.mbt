///|
pub fn read_input(file_name : String) -> String raise {
  @fs.read_file_to_string("../input/\{file_name}")
}

///|
pub fn parse_ints(input : String, separator : String) -> Array[Int] {
  input
  .trim()
  .split(separator)
  .map(line => try! @string.parse_int(line))
  .to_array()
}

///|
pub fn parse_int(input : StringView) -> Int {
  try! @string.parse_int(input)
}

///|
pub fn lines(input : String) -> Array[String] {
  input.trim().split("\n").map(line => line.to_owned()).to_array()
}

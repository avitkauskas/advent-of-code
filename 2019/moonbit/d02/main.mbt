///|
fn run_memory(memory : Array[Int]) -> Int {
  let mut position = 0
  while position < memory.length() {
    match memory[position] {
      99 => break
      1 =>
        memory[memory[position + 3]] = memory[memory[position + 1]] +
          memory[memory[position + 2]]
      2 =>
        memory[memory[position + 3]] = memory[memory[position + 1]] *
          memory[memory[position + 2]]
      _ => abort("Unknown opcode encountered!")
    }
    position = position + 4
  }
  memory[0]
}

///|
fn run_intcode(program : Array[Int], noun : Int, verb : Int) -> Int {
  let memory = program.copy()
  memory[1] = noun
  memory[2] = verb
  run_memory(memory)
}

///|
fn find_noun_verb(program : Array[Int], target : Int) -> Int? {
  for noun in 0..<=99 {
    for verb in 0..<=99 {
      if run_intcode(program, noun, verb) == target {
        return Some(100 * noun + verb)
      }
    }
  }
  None
}

///|
fn main raise {
  let program = @common.parse_ints(@common.read_input("d02.txt"), ",")

  println("Part 1: Value at position 0: \{run_intcode(program, 12, 2)}")

  match find_noun_verb(program, 19690720) {
    Some(result) => println("Part 2: 100 * noun + verb = \{result}")
    None => println("Part 2: No solution found!")
  }
}

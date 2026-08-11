///|
fn fuel(mass : Int) -> Int {
  mass / 3 - 2
}

///|
fn total_fuel(mass : Int) -> Int {
  let mut total = 0
  let mut current = mass

  while current > 0 {
    let fuel = fuel(current)
    total = total + fuel.max(0)
    current = fuel
  }

  total
}

///|
fn main raise {
  let masses = @common.parse_ints(@common.read_input("d01.txt"), "\n")

  let part1 = masses.fold(init=0, (acc, mass) => acc + fuel(mass))
  let part2 = masses.fold(init=0, (acc, mass) => acc + total_fuel(mass))

  println("part1: \{part1}")
  println("part2: \{part2}")
}

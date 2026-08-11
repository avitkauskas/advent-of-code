///|
let password_start : Int = 125730

///|
let password_end : Int = 579381

///|
fn digits_of(num : Int) -> Array[Int] {
  let digits = Array::make(6, 0)
  let mut n = num
  let mut i = 5
  while n > 0 {
    digits[i] = n.mod(10)
    n = n / 10
    i = i - 1
  }
  digits
}

///|
fn never_decreases(digits : Array[Int]) -> Bool {
  for i in 0..<(digits.length() - 1) {
    if digits[i] > digits[i + 1] {
      return false
    }
  }
  true
}

///|
fn has_adjacent_digits(digits : Array[Int]) -> Bool {
  for i in 0..<(digits.length() - 1) {
    if digits[i] == digits[i + 1] {
      return true
    }
  }
  false
}

///|
fn has_exact_double(digits : Array[Int]) -> Bool {
  let mut i = 0
  while i < digits.length() {
    let digit = digits[i]
    let mut run_length = 1
    while i + 1 < digits.length() && digits[i + 1] == digit {
      run_length = run_length + 1
      i = i + 1
    }
    if run_length == 2 {
      return true
    }
    i = i + 1
  }
  false
}

///|
fn valid(password : Int, exact_double : Bool) -> Bool {
  let digits = digits_of(password)
  if !never_decreases(digits) {
    return false
  }
  if exact_double {
    has_exact_double(digits)
  } else {
    has_adjacent_digits(digits)
  }
}

///|
fn solve_part1() -> Int {
  let mut count = 0
  for num in password_start..<=password_end {
    if valid(num, false) {
      count = count + 1
    }
  }
  count
}

///|
fn solve_part2() -> Int {
  let mut count = 0
  for num in password_start..<=password_end {
    if valid(num, true) {
      count = count + 1
    }
  }
  count
}

///|
fn main {
  println("part1: \{solve_part1()}")
  println("part2: \{solve_part2()}")
}

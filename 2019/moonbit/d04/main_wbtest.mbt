///|
test "valid passwords by example semantics" {
  assert_true(valid(111111, false))
  assert_true(valid(111111, true) == false)
  assert_true(valid(223450, false) == false)
  assert_true(valid(123789, false) == false)
  assert_true(valid(112233, true))
  assert_true(valid(123444, true) == false)
  assert_true(valid(111122, true))
}

///|
test "full range part 1" {
  assert_eq(solve_part1(), 2081)
}

///|
test "full range part 2" {
  assert_eq(solve_part2(), 1411)
}

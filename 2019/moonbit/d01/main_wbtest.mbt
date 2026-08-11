///|
test "fuel for known masses" {
  assert_eq(fuel(12), 2)
  assert_eq(fuel(14), 2)
  assert_eq(fuel(1969), 654)
  assert_eq(fuel(100756), 33583)
}

///|
test "total fuel for known masses" {
  assert_eq(total_fuel(14), 2)
  assert_eq(total_fuel(1969), 966)
  assert_eq(total_fuel(100756), 50346)
}

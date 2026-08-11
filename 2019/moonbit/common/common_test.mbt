///|
test "parse_ints handles separators" {
  assert_eq(@common.parse_ints("1,2,3", ","), [1, 2, 3])
  assert_eq(@common.parse_ints("10\n20\n30\n", "\n"), [10, 20, 30])
}

///|
test "parse_int and lines" {
  assert_eq(@common.parse_int("42"), 42)
  assert_eq(@common.lines("a\nb\nc\n"), ["a", "b", "c"])
}

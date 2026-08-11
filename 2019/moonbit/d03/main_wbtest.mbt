///|
test "example from the problem statement" {
  let input =
    #|R8,U5,L5,D3
    #|U7,R6,D4,L4
    #|
  assert_eq(solve_part1(input), 6)
  assert_eq(solve_part2(input), 30)
}

///|
test "second example" {
  let input =
    #|R75,D30,R83,U83,L12,D49,R71,U7,L72
    #|U62,R66,U55,R34,D71,R55,D58,R83
    #|
  assert_eq(solve_part1(input), 159)
  assert_eq(solve_part2(input), 610)
}

///|
test "third example" {
  let input =
    #|R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
    #|U98,R91,D20,R16,D67,R40,U7,R15,U6,R7
    #|
  assert_eq(solve_part1(input), 135)
  assert_eq(solve_part2(input), 410)
}

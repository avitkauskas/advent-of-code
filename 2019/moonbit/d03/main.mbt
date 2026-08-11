///|
struct Point {
  x : Int
  y : Int
} derive(Hash, Eq)

///|
fn manhattan_distance(point : Point) -> Int {
  point.x.abs() + point.y.abs()
}

///|
fn trace_wire(
  path : String,
) -> (@hashset.HashSet[Point], @hashmap.HashMap[Point, Int]) {
  let points = @hashset.HashSet([])
  let steps = @hashmap.HashMap([])
  let mut current : Point = { x: 0, y: 0 }
  let mut total_steps = 0

  for instruction in path.split(",") {
    let direction = instruction[0]
    let distance = @common.parse_int(instruction[1:])
    for _ in 0..<distance {
      match direction {
        'R' => current = { x: current.x + 1, y: current.y }
        'L' => current = { x: current.x - 1, y: current.y }
        'U' => current = { x: current.x, y: current.y + 1 }
        'D' => current = { x: current.x, y: current.y - 1 }
        _ => abort("Invalid direction")
      }
      total_steps = total_steps + 1
      points.add(current)
      if !steps.contains(current) {
        steps[current] = total_steps
      }
    }
  }

  (points, steps)
}

///|
fn solve_part1(input : String) -> Int {
  let wires = @common.lines(input)
  let (wire1_points, _) = trace_wire(wires[0])
  let (wire2_points, _) = trace_wire(wires[1])

  let mut min_distance : Int? = None
  for point in wire1_points.intersection(wire2_points) {
    let distance = manhattan_distance(point)
    match min_distance {
      None => min_distance = Some(distance)
      Some(min) => if distance < min { min_distance = Some(distance) }
    }
  }

  match min_distance {
    Some(result) => result
    None => abort("No intersections found")
  }
}

///|
fn solve_part2(input : String) -> Int {
  let wires = @common.lines(input)
  let (wire1_points, wire1_steps) = trace_wire(wires[0])
  let (wire2_points, wire2_steps) = trace_wire(wires[1])

  let mut min_steps : Int? = None
  for point in wire1_points.intersection(wire2_points) {
    let combined = wire1_steps[point] + wire2_steps[point]
    match min_steps {
      None => min_steps = Some(combined)
      Some(min) => if combined < min { min_steps = Some(combined) }
    }
  }

  match min_steps {
    Some(result) => result
    None => abort("No intersections found")
  }
}

///|
fn main raise {
  let input = @common.read_input("d03.txt")

  println("Part1: \{solve_part1(input)}")
  println("Part2: \{solve_part2(input)}")
}

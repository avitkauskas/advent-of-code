def vertices(plan)
  x, y = 0_i64, 0_i64
  vertices = [{x, y}]
  plan.each do |(d, s)|
    case d
    when "U" then nx, ny = x, y + s
    when "D" then nx, ny = x, y - s
    when "L" then nx, ny = x - s, y
    when "R" then nx, ny = x + s, y
    else raise "Wrong direction!"
    end
    vertices << {nx, ny}
    x, y = nx, ny
  end
  raise "Not closed!" if vertices.first != vertices.last
  vertices
end

def perimeter(vertices)
  vertices.each_cons(2).sum(0) { |((x1, y1), (x2, y2))| (x2 - x1).abs + (y2 - y1).abs }
end

def area(vertices)
  area = 0_i64
  x, y = vertices[0]
  i = 1
  while i < vertices.size - 2
    x1, y1 = vertices[i]
    x2, y2 = vertices[i + 1]
    dx1, dy1 = x1 - x, y1 - y
    dx2, dy2 = x2 - x, y2 - y
    cross = dx1 * dy2 - dx2 * dy1
    area += cross
    i += 1
  end
  (area // 2).abs
end

def solve(plan)
  v = vertices(plan)
  a, p = area(v), perimeter(v)
  a + p - (p // 2 - 1)
end

input = "input/input18.txt"

plan = File.read_lines(input).map(&.split).map { |(a, b, _)| {a, b.to_i64} }
puts "Part 1: #{solve(plan)}"

plan = File.read_lines(input).map(&.split)
  .map { |(_, _, c)| { {'0' => "R", '1' => "D", '2' => "L", '3' => "U"}[c[7]], c[2..6].to_i64(16) } }
puts "Part 2: #{solve(plan)}"

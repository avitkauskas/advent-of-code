GRID_SERIAL = 7803
GRID_SIZE   =  300

GRID = Array.new(GRID_SIZE) do |x|
  x += 1
  Array.new(GRID_SIZE) do |y|
    y += 1
    ((((x + 10) * y) + GRID_SERIAL) * (x + 10)) // 100 % 10 - 5
  end
end

def square_power(x, y, size)
  power = 0
  (x...x + size).each do |i|
    (y...y + size).each do |j|
      power += GRID[i][j]
    end
  end
  power
end

def max_square(size)
  max_power = {x: 0, y: 0, power: square_power(0, 0, size)}
  n = GRID_SIZE - size
  n.times do |x|
    n.times do |y|
      power = square_power(x, y, size)
      if power > max_power[:power]
        max_power = {x: x, y: y, power: power}
      end
    end
  end
  max_power
end

part1 = max_square 3
puts "#{part1[:x] + 1}, #{part1[:y] + 1}"

part2 = {x: 0, y: 0, power: square_power(0, 0, 1)}
max_size = 1

(1..GRID_SIZE).each do |size|
  interim = max_square size
  if interim[:power] > part2[:power]
    part2 = interim
    max_size = size
  end
end

puts "#{part2[:x] + 1}, #{part2[:y] + 1}, #{max_size}"

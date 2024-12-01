const GRID_SERIAL = 7803
const GRID_SIZE   =  300

function cell_power(x, y)
  ((((x + 10) * y) + GRID_SERIAL) * (x + 10)) ÷ 100 % 10 - 5
end

const grid = [cell_power(x, y) for x = 1:GRID_SIZE, y = 1:GRID_SIZE]

function square_power(x, y, size)
  power = 0
  for i = x:(x + size - 1), j = y:(y + size - 1)
    power += grid[i, j]
  end
  power
end

function max_square(size)
  max_power = (x=1, y=1, power=square_power(1, 1, size))
  n = GRID_SIZE - size
  for x = 1:n, y = 1:n
    power = square_power(x, y, size)
    if power > max_power.power
      max_power = (x=x, y=y, power=power)
    end
  end
  max_power
end

part1 = max_square(3)
println("$(part1.x), $(part1.y)")

function max_total()
  max_interim = (x=1, y=1, power=square_power(1, 1, 1))
  max_size = 1

  for size = 1:GRID_SIZE
    interim = max_square(size)
    if interim.power > max_interim.power
      max_interim = interim
      max_size = size
    end
  end

  (x=max_interim.x, y=max_interim.y, size=max_size)
end

part2 = max_total()
println("$(part2.x), $(part2.y), $(part2.size)")
grid_serial: Static[int] = 7803
grid_size: Static[int] = 300

def hundreds_digit(num):
  return (num // 100) % 10

def cell_power(x, y):
    x += 1
    y += 1
    rack_id = x + 10
    init_power = rack_id * y
    incr_power = init_power + grid_serial
    mult_power = incr_power * rack_id
    digit = hundreds_digit(mult_power)
    final_power = digit - 5
    return final_power

def square_power(grid, x, y, size):
    power = 0
    for i in range(x, x + size):
        for j in range(y, y + size):
            power += grid[i * grid_size + j]
    return power

def max_square(grid, size):
    max_x, max_y = 0, 0
    max_power = square_power(grid, 0, 0, size)
    for x in range(grid_size - size + 1):
        for y in range(grid_size - size + 1):
            power = square_power(grid, x, y, size)
            if power > max_power:
                max_x, max_y = x, y
                max_power = power
    return (max_x, max_y, max_power)


grid = __array__[int](grid_size * grid_size)
for x in range(grid_size):
    for y in range(grid_size):
        grid[x * grid_size + y] = cell_power(x, y)

# part 1
x, y, _ = max_square(grid, 3)
print(f"{x + 1}, {y + 1}")

@tuple
class MaxPower:
    x: int
    y: int
    power: int
    size: int

    def __new__():
        return MaxPower(0, 0, square_power(grid, 0, 0, 1), 1)

    def __add__(self, other: MaxPower):
        if other.power > self.power:
            return other
        else:
            return self

max_power = MaxPower()

@par(schedule='dynamic')
for size in range(1, grid_size + 1):
    x, y, power = max_square(grid, size)
    max_power += MaxPower(x, y, power, size)

print(f"{max_power.x + 1}, {max_power.y + 1}, {max_power.size}")

grid_serial = 7803
grid_size = 300

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

def make_grid():
    grid = [row[:] for row in [[0] * grid_size] * grid_size]
    for x in range(grid_size):
        for y in range(grid_size):
            grid[x][y] = cell_power(x, y)
    return grid

def square_power(grid, x, y, size):
    power = 0
    for i in range(x, x + size):
        for j in range(y, y + size):
            power += grid[i][j]
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


grid = make_grid()

# part 1
x, y, _ = max_square(grid, 3)
print(f"{x + 1}, {y + 1}")

# part 2
max_x, max_y = 0, 0
max_power = square_power(grid, 0, 0, 1)
max_size = 1
for size in range(1, grid_size + 1):
    x, y, power = max_square(grid, size)
    # if power < 0:
    #     print("cheated: break early")
    #     break
    if power > max_power:
       max_power = power
       max_x, max_y = x, y
       max_size = size
print(f"{max_x + 1}, {max_y + 1}, {max_size}")

from memory.unsafe import Pointer

alias grid_serial: Int = 7803
alias grid_size: Int = 300

alias Simd4 = SIMD[DType.int64, 4]

fn hundreds_digit(num: Int) -> Int:
    return (num // 100) % 10


fn cell_power(x: Int, y: Int) -> Int:
    let c: Int = x + 1
    let r: Int = y + 1
    let rack_id = c + 10
    let init_power = rack_id * r
    let incr_power = init_power + grid_serial
    let mult_power = incr_power * rack_id
    let digit = hundreds_digit(mult_power)
    let final_power = digit - 5
    return final_power


fn fill_grid(inout grid: Pointer[Int]):
    for y in range(grid_size):
        for x in range(grid_size):
            grid.store(y * grid_size + x, cell_power(x, y))


fn square_power(grid: Pointer[Int], x: Int, y: Int, size: Int) -> Int:
    var power: Int = 0
    for r in range(y, y + size):
        for c in range(x, x + size):
            power += grid[r * grid_size + c]
    return power


fn max_square(grid: Pointer[Int], size: Int, inout res: Simd4):
    res[0] = 0
    res[1] = 0
    res[2] = square_power(grid, 0, 0, size)
    for x in range(grid_size - size + 1):
        for y in range(grid_size - size + 1):
            let max_power: Int64 = square_power(grid, x, y, size)
            if max_power > res[2]:
                res[0] = x
                res[1] = y
                res[2] = max_power


fn main():
    var grid = Pointer[Int].alloc(grid_size * grid_size)
    fill_grid(grid)

    # part 1
    var res1 = Simd4()
    max_square(grid, 3, res1)
    print(res1[0] + 1, res1[1] + 1)

    # part 2
    var res2 = Simd4()
    var candidate = Simd4()
    res2[2] = square_power(grid, 0, 0, 1)
    res2[3] = 1
    for size in range(1, grid_size + 1):
        # print(size)
        max_square(grid, size, candidate)
        if candidate[2] > res2[2]:
            res2 = candidate
            res2[3] = size
    print(res2[0] + 1, res2[1] + 1, res2[3])

    grid.free()
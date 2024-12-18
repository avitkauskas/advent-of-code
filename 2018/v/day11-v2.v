const grid_serial = 7803
const grid_size = 300

struct Grid {
mut:
	cells [grid_size][grid_size]int
}

fn cell_power(i int, j int) int {
	x := i + 1
	y := j + 1
	return ((((x + 10) * y) + grid_serial) * (x + 10)) / 100 % 10 - 5
}

fn make_grid() Grid {
	mut grid := Grid{}

	for i in 0 .. grid_size {
		for j in 0 .. grid_size {
			grid.cells[i][j] = cell_power(i, j)
		}
	}

	return grid
}

fn (grid &Grid) square_power(x int, y int, size int) int {
	mut power := 0

	for i in x .. x + size {
		for j in y .. y + size {
			power += grid.cells[i][j]
		}
	}

	return power
}

fn (grid &Grid) max_square(size int) (int, int, int) {
	mut max_x, mut max_y, mut max_pow := 0, 0, grid.square_power(0, 0, size)

	n := grid_size - size
	for x in 0 .. n {
		for y in 0 .. n {
			power := grid.square_power(x, y, size)
			if power > max_pow {
				max_x, max_y, max_pow = x, y, power
			}
		}
	}

	return max_x, max_y, max_pow
}

fn (grid &Grid) max_total() (int, int, int) {
	mut max_x, mut max_y, mut max_pow := 0, 0, grid.square_power(0, 0, 1)
	mut max_size := 1

	for size in 1 .. grid_size + 1 {
		x, y, pow := grid.max_square(size)
		if pow > max_pow {
			max_x, max_y, max_pow, max_size = x, y, pow, size
		}
	}

	return max_x, max_y, max_size
}

fn main() {
	grid := make_grid()

	max_x1, max_y1, _ := grid.max_square(3)
	println('Part 1: ${max_x1 + 1}, ${max_y1 + 1}')

	max_x2, max_y2, max_size2 := grid.max_total()
	println('Part 2: ${max_x2 + 1}, ${max_y2 + 1}, ${max_size2}')
}

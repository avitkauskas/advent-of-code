field = File.read_lines("input/input10.txt").map &.chars

MOVES = {
  {'|', { 1, 0}} => { 1, 0},
  {'|', {-1, 0}} => {-1, 0},
  {'-', { 0, 1}} => { 0, 1},
  {'-', { 0,-1}} => { 0,-1},
  {'L', { 1, 0}} => { 0, 1},
  {'L', { 0,-1}} => {-1, 0},
  {'J', { 1, 0}} => { 0,-1},
  {'J', { 0, 1}} => {-1, 0},
  {'7', { 0, 1}} => { 1, 0},
  {'7', {-1, 0}} => { 0,-1},
  {'F', {-1, 0}} => { 0, 1},
  {'F', { 0,-1}} => { 1, 0},
}

def get_start_coords(field)
  field.size.times do |r|
    field[0].size.times do |c|
      return {r, c} if field[r][c] == 'S'
    end
  end
  {0,0}
end

def find_loop(field)
  start = coords = get_start_coords(field)
  # HACK: this is only possible because the start cell is 'L'
  move = {-1, 0}
  path = [start]
  loop do
    coords = {coords[0] + move[0], coords[1] + move[1]}
    break path if coords == start
    path << coords
    cell = field[coords[0]][coords[1]]
    move = MOVES[{cell, move}]
  end
end

path = find_loop(field)
ans = path.size // 2

puts "Part 1: #{ans}"

EXPAND_H = {
  '|' => ['.','|','.'],
  '-' => ['-','-','-'],
  'L' => ['.','L','-'],
  'J' => ['-','J','.'],
  '7' => ['-','7','.'],
  'F' => ['.','F','-'],
  '*' => ['.','*','.'],
}

EXPAND_VU = {
  '|' => '|',
  '-' => '.',
  'L' => '|',
  'J' => '|',
  '7' => '.',
  'F' => '.',
  '*' => '.',
  '.' => '.',
}

EXPAND_VD = {
  '|' => '|',
  '-' => '.',
  'L' => '.',
  'J' => '.',
  '7' => '|',
  'F' => '|',
  '*' => '.',
  '.' => '.',
}

clean_field = Array.new(field.size) { Array.new(field[0].size, '*') }

path.each do |(r,c)|
  clean_field[r][c] = field[r][c]
end

r, c = get_start_coords(field)
# HACK: 'L' is just for my map
clean_field[r][c] = 'L'

expanded_lines = Array(Array(Char)).new 

clean_field.each do |line|
  exp_line = Array(Char).new
  line.each { |ch| exp_line.concat EXPAND_H[ch] }
  expanded_lines << exp_line
end

expanded_field = Array(Array(Char)).new 

expanded_lines.each do |line|
  expanded_field << line.map { |ch| EXPAND_VU[ch] }
  expanded_field << line
  expanded_field << line.map { |ch| EXPAND_VD[ch] }
end

expanded_field.map! do |line|
  line.map! do |ch|
    case ch
    when '*' then 'X'
    when '.' then ' '
    else '*'
    end
  end
end

def flud_field(field)
  rows = (0...field.size)
  cols = (0...field[0].size)

  start = {0,0}
  queue = Deque.new(1, start)
  seen = Set{start}

  while !queue.empty?
    row, col = queue.shift
    field[row][col] = '.'
    next_cells = [] of Tuple(Int32, Int32)
    { {0,-1},{-1,0},{0,1},{1,0} }.each do |r, c|
      nr, nc = {row + r, col + c}
      new_cell = {nr, nc}
      if nr.in?(rows) && nc.in?(cols) && field[nr][nc] != '*' && !seen.includes?(new_cell)
        next_cells << new_cell
      end
    end
    seen.concat next_cells
    queue.concat next_cells
  end
  field
end

fluded_field = flud_field(expanded_field)

ans = fluded_field.sum do |line|
  line.count { |ch| ch == 'X' }
end

puts "Part 2: #{ans}"

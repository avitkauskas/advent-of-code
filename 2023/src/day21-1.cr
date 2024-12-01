DIRS = [{-1, 0}, {0, 1}, {1, 0}, {0, -1}]

garden = File.read_lines("input/input21.txt").map &.chars
start = {0, 0}
garden.each_with_index do |row, r|
  row.each_with_index do |cell, c|
    if cell == 'S'
      start = {r, c}
      garden[r][c] = '.'
      break
    end
  end
end

rows = garden.size
cols = garden[0].size

queue = Deque.new(1, {start, 0})
seen = Set{ {start, 0} }
reached = 0

total_steps = 64

while !queue.empty?
  curr, steps = queue.shift
  seen.delete({curr, steps})
  if steps == total_steps
    reached += 1
  else
    row, col = curr
    DIRS.each do |r, c|
      nr = row + r
      nc = col + c
      pos = { {nr, nc}, steps + 1 }
      if nr.in?(0...rows) && nc.in?(0...cols) && garden[nr][nc] != '#' && !seen.includes?(pos)
        queue << pos
        seen << pos
      end
    end
  end
end

puts "Part 1: #{reached}"

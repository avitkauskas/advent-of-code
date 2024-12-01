bricks = File.read_lines("input/input22.txt")
  .map(&.split('~').map(&.split(',').map(&.to_i)))
  .map { |(a, b)| p = a.zip(b); [p.map &.min, p.map &.max] }

def indexes_of_bricks_with_lower_end_at(z, bricks)
  bricks.map_with_index { |b, i| i if b[0][2] == z }.compact
end

def top_of(bricks)
  bricks.max_of { |b| b[1][2] }
end

def empty_space_below(brick, bricks)
  bricks_below = bricks.select do |b|
    b[1][2] < brick[0][2] &&
      !(b[1][0] < brick[0][0] || b[0][0] > brick[1][0]) && 
      !(b[1][1] < brick[0][1] || b[0][1] > brick[1][1])
  end
  return brick[0][2] - 1 if bricks_below.empty?
  brick[0][2] - top_of(bricks_below) - 1
end

def bricks_supported(brick, bricks)
  bricks_right_above = bricks.map_with_index do |b, i|
    i if b[0][2] == brick[1][2] + 1 && 
      !(b[1][0] < brick[0][0] || b[0][0] > brick[1][0]) && 
      !(b[1][1] < brick[0][1] || b[0][1] > brick[1][1])
  end.compact
end

def settle_bricks(bricks, from_row = 2)
  count = 0
  while from_row <= top_of(bricks)
    idxs = indexes_of_bricks_with_lower_end_at(from_row, bricks)
    idxs.each do |idx|
      below = empty_space_below(bricks[idx], bricks)
      if below > 0
        bricks[idx][0][2] -= below
        bricks[idx][1][2] -= below
        count += 1
      end
    end
    from_row += 1
  end
  count
end

def bricks_possible_to_desintegrate(bricks)
  supported = bricks.map_with_index { |b, i| {i, bricks_supported(b, bricks)}}.to_h
  bricks.map_with_index do |b, i|
    i if supported[i].all? do |s|
      supported.any? { |k, v| s.in?(v) && k != i}
  end
  end.compact
end

settle_bricks(bricks)
ans = bricks_possible_to_desintegrate(bricks).size
puts "Part 1: #{ans}"

def bricks_to_fall(idx, bricks)
  z = bricks[idx][1][2]
  bricks = bricks.clone
  bricks.delete_at(idx)
  settle_bricks(bricks, z + 1)
end

ans = bricks.size.times.sum { |i| bricks_to_fall(i, bricks) }
puts "Part 2: #{ans}"

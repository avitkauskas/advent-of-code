lines = File.read_lines("input/input11.txt")

galaxies = [] of Tuple(Int64, Int64)
lines.each_with_index do |row, r|
  row.chars.each_with_index do |ch, c|
    galaxies << {r.to_i64, c.to_i64} if ch == '#'
  end
end

def sum_distancies(galaxies, expansion_times)
  expansion = expansion_times - 1
  galaxies = galaxies.dup

  rows = galaxies.max_of &.[0]
  cols = galaxies.max_of &.[1]

  empty_rows = Set.new(0..rows) - galaxies.map &.[0]
  empty_cols = Set.new(0..cols) - galaxies.map &.[1]

  empty_rows.each_with_index do |row, i|
    galaxies.map! do |(r, c)|
      r > row + (i * expansion) ? {r + expansion, c} : {r, c}
    end
  end

  empty_cols.each_with_index do |col, i|
    galaxies.map! do |(r, c)|
      c > col + (i * expansion) ? {r, c + expansion} : {r, c}
    end
  end
  
  galaxies.each_combination(2).sum do |((r1, c1), (r2, c2))|
    (r1 - r2).abs + (c1 - c2).abs
  end
end

ans = sum_distancies(galaxies, 2)
puts "Part 1: #{ans}"

ans = sum_distancies(galaxies, 1_000_000)
puts "Part 2: #{ans}"

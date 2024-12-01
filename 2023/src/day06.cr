lines = File.read_lines("input/input06.txt")

times, distances = lines.map(&.split.skip(1).map(&.to_i))
races = times.zip(distances)

ans = races.product do |time, dist|
  (1...time).count { |t| t * (time - t) > dist }
end

puts "Part 1: #{ans}"

time, dist = lines.map(&.split.skip(1).join.to_i64)

ans = (1_i64...time).count { |t| t * (time - t) > dist }

puts "Part 2: #{ans}"

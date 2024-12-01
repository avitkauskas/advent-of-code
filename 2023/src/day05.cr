input = File.read("input/input05.txt").split("\n\n")

seeds = input[0].scan(/\d+/).map(&.[0].to_i64)

maps = input[1..].map do |data|
  data.lines[1..].map do |line|
    dst, src, size = line.split.map(&.to_i64)
    {src: (src...(src + size)), dst: (dst...(dst + size))}
  end
end

ans = seeds.min_of do |seed|
  maps.reduce(seed) do |s, map| 
    map.find { |mp| mp[:src].includes? s }
      .try { |mp| mp[:dst].begin + (s - mp[:src].begin) } || s
  end
end

puts "Part 1: #{ans}"

seeds = input[0].scan(/\d+/).map(&.[0].to_i64).in_slices_of(2)
  .map { |(start, size)| (start...(start + size)) }

maps.reverse!

ans = (0_i64..).find do |loc|
  seed = maps.reduce(loc) do |s, map|
    map.find { |mp| mp[:dst].includes? s }
      .try { |mp| mp[:src].begin + (s - mp[:dst].begin) } || s
  end
  seeds.any? { |range| range.includes? seed }
end

puts "Part 2: #{ans}"

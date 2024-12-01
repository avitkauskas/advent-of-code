alias Cache = Hash(Tuple(Array(Char),Array(Int32), Int32), Int64)

lines = File.read_lines("input/input12.txt").map &.split

records = lines.map do |(record, counts)|
  {record.chars, counts.split(',').map(&.to_i)}
end

def arrangements(record, counts, found, cache) : Int64
  if record.empty? 
    return counts.empty? || [found] == counts ? 1_i64 : 0_i64
  end

  key = {record, counts, found}
  return cache[key] if cache.has_key? key
  
  c = 0_i64

  if record[0] == '#' && !counts.empty? && found < counts[0]
    c += arrangements(record[1..], counts, found + 1, cache)
  end

  if record[0] == '.'
    if found > 0 && found == counts[0]
      c += arrangements(record[1..], counts[1..], 0, cache)
    end
    if found == 0
      c += arrangements(record[1..], counts, 0, cache)
    end
  end

  if record[0] == '?'
    c += arrangements(['#'] + record[1..], counts, found, cache)
    c += arrangements(['.'] + record[1..], counts, found, cache)
  end

  cache[key] = c
  return c
end

ans = records.sum do |(record, counts)|
  arrangements(record, counts, 0, Cache.new)
end

puts "Part 1: #{ans}"

records = records.map do |(record, counts)|
  {([record.join] * 5).join("?").chars, counts * 5}
end

ans = records.sum do |(record, counts)|
  arrangements(record, counts, 0, Cache.new)
end

puts "Part 2: #{ans}"

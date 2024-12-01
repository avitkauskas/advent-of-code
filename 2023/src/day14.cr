input = File.read_lines("input/input14.txt").map &.chars

def empty_cell(platform, row, col)
  row -= 1
  while row >= 0 && platform[row][col] == '.'
    row -= 1
  end
  row + 1
end

def tilt(platform)
  1.to(platform.size - 1) do |row|
    0.to(platform[0].size - 1) do |col|
      if platform[row][col] == 'O'
        r = empty_cell(platform, row, col)
        if r < row
          platform[r][col] = 'O'
          platform[row][col] = '.'
        end
      end
    end
  end
  platform
end

def rotate(platform)
  (0...platform[0].size).map { |i| platform.map(&.[i]).reverse}
end

def cycle(platform)
  4.times do
    platform = tilt(platform)
    platform = rotate(platform)
  end
  platform
end

def load(platform)
  load = 0
  platform.size.times do |row|
    platform[0].size.times do |col|
      if platform[row][col] == 'O'
        load += platform.size - row
      end
    end
  end
  load
end

def show(platform)
  platform.each { |line| puts line.join }
  puts
end

platform = input.clone
ans = load(tilt(platform))

puts "Part 1: #{ans}"

cache = Hash(String, Int32).new

platform = input.clone
1.to(1_000_000_000) do |i|
  platform = cycle(platform)
  hash = platform.map(&.join).join
  if cache.has_key? hash
    remaining = (1_000_000_000 - cache[hash]) % (i - cache[hash])
    remaining.times do
      platform = cycle(platform)
    end
    break
  end
  cache[hash] = i
end

ans = load(platform)

puts "Part 2: #{ans}"

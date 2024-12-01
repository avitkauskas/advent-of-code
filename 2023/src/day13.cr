patterns = File.read("input/input13.txt").split("\n\n").map(&.lines.map(&.chars))

def diffs(pattern, r, i, mode)
  a1 = (mode == :rows) ? pattern[r - i] : pattern.map &.[r - i]
  a2 = (mode == :rows) ? pattern[r + i - 1] : pattern.map &.[r + i - 1]
  a1.zip(a2).count { |(a, b)| a != b }
end

def mirror(pattern, smudges)
  {:rows, :cols}.each do |mode|
    limit = (mode == :rows) ? pattern.size : pattern[0].size
    (1...limit).each do |r|
      symetric = true
      diff = 0
      i = 1
      while r - i >= 0 && r + i - 1 < limit
        diff += diffs(pattern, r, i, mode)
        if diff > smudges
          symetric = false
          break
        end
        i += 1
      end
      if symetric && diff == smudges
        return r * (mode == :rows ? 100: 1) 
      end
    end
  end

  return 0
end

ans = patterns.sum {|p| mirror(p, 0)}

puts "Part 1: #{ans}"

ans = patterns.sum {|p| mirror(p, 1)}

puts "Part 2: #{ans}"

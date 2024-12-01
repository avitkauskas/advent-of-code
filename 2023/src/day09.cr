histories = File.read_lines("input/input09.txt").map(&.split.map(&.to_i))

def solution(histories, dir)
  histories.sum do |history|
    history.reverse! if dir == :prev
    seq = [history]
    while !seq.last.all?(0)
      seq << seq.last.each_cons_pair.map { |a, b| b - a }.to_a
    end
    seq.map(&.last).sum
  end
end

puts "Part 1: #{solution(histories, :next)}"
puts "Part 2: #{solution(histories, :prev)}"

cards = File.read_lines("input/input04.txt")
  .map(&.split(":").last)
  .map(&.split("|"))
  .map do |arr|
    arr.map do |str|
      str.scan(/\d+/).map(&.[0].to_i).to_set
    end
  end

sum = cards.map do |card|
  matches = card[0] & card[1]
  points = 0
  points = 2 ** (matches.size - 1) if matches.size > 0
  points
end.sum

puts "Part 1: #{sum}"

copies = Array.new(cards.size, 1)

def count_copies(cards, copies)
  cards.each_with_index do |card, i|
    matches = (card[0] & card[1]).size
    copies[i + 1, matches] = copies[i + 1, matches].map &.+ copies[i]
  end
  copies
end

sum = count_copies(cards, copies).sum

puts "Part 2: #{sum}"

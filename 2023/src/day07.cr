hands = File.read_lines("input/input07.txt").map do |line|
  cards, bid = line.split
  {cards.chars, bid.to_i}
end

CARDS_NO_JOCKER   = "23456789TJQKA"
CARDS_WITH_JOCKER = "J23456789TQKA"

def hand_type_no_jocker(cards)
  cards.tally.values.sort!.reverse!
end

def hand_type_with_joker(cards)
  tally = cards.tally
  jockers = tally.delete('J') || 0
  return [jockers] if tally.empty?
  type = tally.values.sort!.reverse!
  type[0] += jockers
  type
end

ans = hands
  .sort_by! { |(cards, _)| {hand_type_no_jocker(cards), cards.map { |c| CARDS_NO_JOCKER.index!(c) }} }
  .map_with_index { |(_, bid), i| bid * (i + 1) }
  .sum

puts "Part 1: #{ans}"

ans = hands
  .sort_by! { |(cards, _)| {hand_type_with_joker(cards), cards.map { |c| CARDS_WITH_JOCKER.index!(c) }} }
  .map_with_index { |(_, bid), i| bid * (i + 1) }
  .sum

puts "Part 2: #{ans}"

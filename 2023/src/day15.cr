input = File.read("input/input15.txt").delete('\n').split(',')

def hash(str)
  str.chars.reduce(0) { |acc, c| (acc + c.ord) * 17 % 256 }
end

ans = input.sum { |s| hash(s) }

puts "Part 1: #{ans}"

boxes = Array.new(256) { |_| Hash(String, Int32).new }

boxes = input.reduce(boxes) do |boxes, step|
  label, op, focal = step.partition /[=-]/
  box = hash(label)
  case op
  when "="
    boxes[box][label] = focal.to_i
  when "-"
    boxes[box].delete label
  end
  boxes
end

ans = boxes.each_with_index.sum do |(box, i)|
  box.each_with_index.sum do |((_, focal), k)|
    (i + 1) * (k + 1) * focal
  end
end

puts "Part 2: #{ans}"

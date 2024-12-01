lines = File.read_lines("input/input01.txt")

def calibration_value(s)
  digits = s.chars.select { |c| c.ascii_number? }
  "#{digits.first}#{digits.last}".to_i
end

def calibration_sum(lines)
  lines
    .map { |s| calibration_value(s) }
    .sum
end

sum = calibration_sum(lines)

puts "Part 1: #{sum}"

def clean_line(line)
  line
    .scan(/(?=(\d|one|two|three|four|five|six|seven|eight|nine))/)
    .map(&.[1])
    .join
    .gsub(/(one|two|three|four|five|six|seven|eight|nine)/,
      {one: "1", two: "2", three: "3",
       four: "4", five: "5", six: "6",
       seven: "7", eight: "8", nine: "9"})
end

lines = lines.map { |s| clean_line(s) }
sum = calibration_sum(lines)

puts "Part 2: #{sum}"

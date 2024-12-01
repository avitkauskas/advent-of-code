rules, parts = File.read("input/input19.txt").split("\n\n")

rules = rules.lines.map do |line|
  name, rules = line.split('{')
  rules = rules.chomp('}').split(',').map do |rule|
    if rule.includes? ':'
      _, typ, op, val, nxt = rule.match! /(.)([><])(\d+):(\w+)/
      {typ, op, val.to_i, nxt}
    else
      {"z", "<", 1, rule}
    end
  end
  {name, rules}
end.to_h

parts = parts.lines.map do |line|
  cats = line[1...-1].split(',').map(&.split('='))
    .map { |(cat, val)| {cat, val.to_i } }
    .concat([{"z", 0}, {"res", 0}]).to_h
end

ops = {"<" => ->(a : Int32, b : Int32) { a < b },
       ">" => ->(a : Int32, b : Int32) { a > b }}

parts.each_with_index do |part, i|
  flow = "in"
  while flow != "A" && flow != "R"
    rules[flow].each do |typ, op, val, nxt|
      if ops[op].call(part[typ], val)
        flow = nxt
        break
      end
    end
  end
  parts[i]["res"] = flow == "A" ? 1 : 0
end

ans = parts.sum do |p| 
  p["res"] == 0 ? 0 : p["x"] + p["m"] + p["a"] + p["s"]
end

puts "Part 1: #{ans}"

range = (1..4000)

init_state = {"x" => range, "m" => range, "a" => range, "s" => range}

accepted = [] of Hash(String, Range(Int32, Int32))

queue = Deque.new(1, {init_state, "in"})

while !queue.empty?
  state, flow = queue.shift
  
  rules[flow].each do |typ, op, val, nxt|
    if typ == "z"
      if nxt == "A"
        accepted << state
      elsif nxt != "R"
        queue << {state, nxt}
      end

    else
      range_in = op == "<" ? 1..(val - 1) : (val + 1)..4000
      range_ou = op == "<" ? val..4000 : 1..val
      range_st = state[typ]
      
      next_range_in = {range_st.begin, range_in.begin}.max..{range_st.end, range_in.end}.min
      next_range_ou = {range_st.begin, range_ou.begin}.max..{range_st.end, range_ou.end}.min
      
      if next_range_in.size > 0
        next_state = state.dup
        next_state[typ] = next_range_in
        if nxt == "A"
          accepted << next_state
        elsif nxt != "R"
          queue << {next_state, nxt}
        end
      end

      if next_range_ou.size > 0
        state[typ] = next_range_ou
      end
    end
  end
end

ans = accepted.sum do |acc|
  acc.reduce(1_i64) { |acc, (_, range)| acc * range.size }
end

puts "Part 2: #{ans}"

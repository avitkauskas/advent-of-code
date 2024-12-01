g = Hash(String, Array(String)).new
c = Hash(String, Int32).new

File.read_lines("input/input25.txt").each do |line|
  k, *v = line.split /:* /
  g[k] = v
  c[k] = 1
  v.each { |k| c[k] = 1 }
end

def edge_count(g)
  s = 0
  g.each { |_, v| s += v.size }
  s
end

def cut(g, c)
  g, c = g.clone, c.clone
  e = edge_count(g)
  n = 0
  while c.size > 2
    r = rand(e)
    n1, n2 = "0", "0"
    g.each do |k, v|
      if v.size <= r
        r -= v.size
        next
      else
        n1 = k
        n2 = v[r]
        break
      end
    end
    n += 1
    nn = n.to_s
    g[nn] = g[n1].select! { |e| e != n2 }
    g.delete(n1)
    if g.has_key? n2
      g[nn].concat(g[n2].select! { |e| e != n1})
      g.delete(n2)
    end
    g.each { |k, v| v.each_with_index { |e, i| v[i] = nn if e == n1 || e == n2 } }
    g.delete(nn) if g[nn].empty?
    c[nn] = c[n1] + c[n2]
    c.delete(n1)
    c.delete(n2)
    e = edge_count(g)
  end
  s = 1
  if edge_count(g) == 3
    c.each { |k, v| s *= v }
  else
    s = 0
  end
  s
end

ans = 0
1000.times do 
  a = cut(g, c)
  if a != 0
    ans = a
    break
  end
end

puts "Part 1: #{ans}"

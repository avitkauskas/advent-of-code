require "priority-queue"

# island = File.read_lines("input/test.txt").map &.chars
island = File.read_lines("input/input23.txt").map &.chars
start = {0, island[0].index!('.')}
finish = {island.size - 1, island[-1].index!('.')}

N = {-1, 0}
S = {1, 0}
E = {0, 1}
W = {0, -1}

alias Point = Tuple(Int32, Int32)

def longest_path(island, start, finish, slippery)
  queue = Priority::Queue({Point, Set(Point), Hash(Point, Int32)}).new
  queue.push 0, {start, Set{start}, {start => 0}} 

  longest = 0
  while !queue.empty?
    q = queue.shift
    q, len = q.value, q.priority
    cur, seen, dists = q[0], q[1], q[2]
    if cur == finish
      longest = len if longest > len
      # puts "longest so far: #{longest}"
    else
      {N, S, E, W}.each do |d|
        r, c = cur[0] + d[0], cur[1] + d[1]
        nxt = {r, c}
        next if r < 0
        cell = island[r][c]
        dist = len - 1
        if !nxt.in?(seen) &&
            dist < dists.fetch(nxt, 0) && 
            cell != '#' &&
            (!slippery ||
            !(d == N && cell == 'v') &&
            !(d == S && cell == '^') &&
            !(d == E && cell == '<') &&
            !(d == W && cell == '>'))
          dists_dup = dists.dup
          dists_dup[nxt] = dist
          queue.push dist, {nxt, seen.dup << nxt, dists_dup}
        end 
      end
    end
  end
  -longest
end
Hash

ans = longest_path(island, start, finish, slippery: true)
puts "Part 1: #{ans}"

# TODO This is inefficient, and should be improved.
ans = longest_path(island, start, finish, slippery: false)
puts "Part 2: #{ans}"

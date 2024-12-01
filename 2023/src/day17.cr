require "priority-queue"

alias Cells = Array(Array(Int32))
alias Direc = Tuple(Int32, Int32)
alias Coord = Tuple(Int32, Int32)
alias QueueItem = NamedTuple(
    cell: Coord,
    dir: Direc,
    same: Int32,
  )

class City
  N = {-1, 0}
  S = { 1, 0}
  E = { 0, 1}
  W = { 0,-1}

  @cells : Cells
  @rows : Int32
  @cols : Int32

  @target : Coord

  @opts = {
    N => {N, E, W},
    S => {S, E, W},
    E => {E, N, S},
    W => {W, N, S},
  }

  @queue : Priority::Queue(QueueItem)

  def initialize(cells)
    @cells = cells
    @rows = @cells.size
    @cols = @cells[0].size
    @target = {@rows - 1, @cols - 1}
    @loss = { {cell: {0, 0}, dir: S, same: 0} => 0, {cell: {0, 0}, dir: E, same: 0} => 0 }
    @queue = Priority::Queue(QueueItem).new
    @queue.push 0, {cell: {0, 0}, dir: S, same: 0}
    @queue.push 0, {cell: {0, 0}, dir: E, same: 0}
  end

  def min_loss(type)
    while !@queue.empty?
      curr = @queue.shift.value
      row, col = curr[:cell]
      if curr[:cell] == @target
        return @loss[curr]
      else
        opts = @opts[curr[:dir]].to_a
        opts = validate_opts(curr, type, opts)
        next_cells = opts
          .map { |dir| {move(curr[:cell], dir), dir, dir == curr[:dir] ? curr[:same] + 1 : 1} }
          .select { |(r, c), _, _| 0 <= r < @rows && 0 <= c < @cols }
        next_cells.each do |(r, c), dir, same|
          new_state = {cell: {r, c}, dir: dir, same: same }
          new_loss = @loss[curr] + @cells[r][c]
          if new_loss < @loss.fetch(new_state, 1000000)
            @loss[new_state] = new_loss
            @queue.push new_loss, {
              cell: {r, c},
              dir: dir,
              same: same,
            }
          end
        end
      end
    end
    "Target not reached."
  end

  private def validate_opts(state, type, opts)
    case type
    when :normal
      opts.delete state[:dir] if state[:same] == 3
    when :ultra
      return [state[:dir]] if state[:same] < 4
      opts.delete state[:dir] if state[:same] == 10
      row, col = state[:cell]
      opts.delete N if state[:dir] != N && row < 4
      opts.delete S if state[:dir] != S && row > @rows - 5
      opts.delete W if state[:dir] != W && col < 4
      opts.delete E if state[:dir] != E && col > @cols - 5
    end
    opts
  end

  private def move(coord, dir)
    {coord[0] + dir[0], coord[1] + dir[1]}
  end
end

cells = File.read_lines("input/input17.txt").map(&.chars.map(&.to_i))

ans = City.new(cells).min_loss(:normal)
puts "Part 1: #{ans}"

ans = City.new(cells).min_loss(:ultra)
puts "Part 2: #{ans}"

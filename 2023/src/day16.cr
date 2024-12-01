alias Coord = Tuple(Int32, Int32)
alias Direc = Tuple(Int32, Int32)
alias Status = Tuple(Coord, Direc)
alias Cells = Array(Array(Char))

class Field
  N = {-1, 0}
  S = { 1, 0}
  E = { 0, 1}
  W = { 0,-1}
  
  def initialize(field : Cells, start : Status)
    @cells = field
    @beams = Set{start}
    @seen = Set{start}
    @energized = Set{ {0, 0} }
  end
  
  def move_beams
    while !@beams.empty?
      @beams = @beams.reduce(@beams.class.new) do |acc, beam|
        acc.concat move_beam(beam)
      end
    end
    self
  end

  def count_energized
    @energized.size
  end

  private def move_beam(beam)
    coord, dir = beam
    r, c = coord
    cell = @cells[r][c]
    moved = case {cell, dir}
    when {'.', _},
         {'-', E}, {'-', W},
         {'|', N}, {'|', S}
      [{move(coord, dir), dir }]
    when {'-', N}, {'-', S}
      [{move(coord, E), E},
       {move(coord, W), W}]
    when {'|', E}, {'|', W}
      [{move(coord, N), N},
       {move(coord, S), S}]
    when {'/', N}, {'\\', S}
      [{move(coord, E), E}]
    when {'/', S}, {'\\', N}
      [{move(coord, W), W}]
    when {'/', E}, {'\\', W}
      [{move(coord, N), N}]
    when {'/', W}, {'\\', E}
      [{move(coord, S), S}]
    else
      [] of Status
    end
    cleaned = clean(moved)
    @seen.concat cleaned
    @energized.concat cleaned.map &.[0]
    cleaned
  end

  private def move(coord, dir)
    {coord[0] + dir[0], coord[1] + dir[1]}
  end

  private def clean(beams)
    beams.select do |beam|
      coord, _ = beam
      r, c = coord
      0 <= r < @cells.size && 0 <= c < @cells[0].size && !beam.in? @seen
    end
  end
end

field = File.read_lines("input/input16.txt").map(&.chars)

ans = Field.new(field, { {0, 0}, Field::E }).move_beams.count_energized

puts "Part 1: #{ans}"

options = (0...field.size).map { |r| { {r, 0}, Field::E }}
  .concat (0...field.size).map { |r| { {r, field[0].size - 1}, Field::W } }
  .concat (0...field[0].size).map { |c| { {0, c}, Field::S } }
  .concat (0...field[0].size).map { |c| { {field.size - 1, c}, Field::N } }

ans = options.max_of { |op| Field.new(field, op).move_beams.count_energized }

puts "Part 2: #{ans}"

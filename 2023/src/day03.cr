class Schematic
  @field : Array(Array(Char))
  @rows : Int32
  @cols : Int32

  def initialize(file)
    lines = File.read_lines(file)
    @field = lines.map &.chars
    @rows = @field.size
    @cols = @field[0].size
  end

  def neighbors(row, col)
    rows = [row - 1, row, row + 1].select { |row| - 1 < row < @rows }
    cols = [col - 1, col, col + 1].select { |col| - 1 < col < @cols }
    rows.cartesian_product(cols).select { |coords| coords != {row, col} }
  end

  def symbol_around?(row, col)
    neighbors(row, col).each do |coords|
      cell = @field[coords[0]][coords[1]]
      if !cell.number? && cell != '.'
        return true
      end
    end
    false
  end

  def part_numbers
    parts = [] of Int32
    num = ""
    in_num = false
    is_part = false
    (0...@rows).each do |row|
      (0...@cols).each do |col|
        cell = @field[row][col]
        if cell.number?
          if col == 0
            if in_num && is_part
              parts << num.to_i
            end
            is_part = false
            num = ""
          end
          in_num = true
          is_part ||= symbol_around?(row, col)
          num += cell
        else
          if in_num && is_part
            parts << num.to_i
          end
          in_num = false
          is_part = false
          num = ""
        end
      end
    end
    parts
  end
  
  def gear_around(row, col)
    neighbors(row, col).each do |coords|
      cell = @field[coords[0]][coords[1]]
      if cell == '*'
        return coords
      end
    end
    nil
  end

  def gear_rations
    gears = Hash(Tuple(Int32, Int32), Array(Int32)).new { [] of Int32 }
    gear : Tuple(Int32, Int32) | Nil = nil
    num = ""
    in_num = false
    (0...@rows).each do |row|
      (0...@cols).each do |col|
        cell = @field[row][col]
        if cell.number?
          if col == 0
            if in_num && gear
              gears[gear] = gears[gear] << num.to_i
            end
            gear = nil
            num = ""
          end
          in_num = true
          gear ||= gear_around(row, col)
          num += cell
        else
          if in_num && gear
            gears[gear] = gears[gear] << num.to_i
          end
          in_num = false
          gear = nil
          num = ""
        end
      end
    end
    gears = gears.select { |k, v| v.size == 2 }
    gears = gears.values.map &.product
  end
end

scm = Schematic.new("input/input03.txt")

sum = scm.part_numbers.sum

puts "Part 1: #{sum}"

sum = scm.gear_rations.sum

puts "Part 2: #{sum}"

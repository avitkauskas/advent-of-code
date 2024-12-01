defmodule Day17 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    lines = input |> String.split("\n", trim: true)

    for line <- lines, reduce: MapSet.new() do
      acc -> MapSet.union(acc, parse_line(line))
    end
  end

  def parse_line(line) do
    [n1, n2, n3] =
      Regex.scan(~r/\d+/, line)
      |> Enum.map(&String.to_integer(hd(&1)))

    case String.at(line, 0) do
      "x" -> for y <- n2..n3, into: MapSet.new(), do: {n1, y}
      "y" -> for x <- n2..n3, into: MapSet.new(), do: {x, n1}
    end
  end

  def flow(:down, {x, y} = cell, path, standing, clay, max_y) do
    # IO.inspect({cell, path}, label: "down:", width: 150)

    cond do
      # reached bottom line, then return result
      y + 1 == max_y ->
        {:end, MapSet.union(standing, MapSet.new(path))}

      # can go down, then go down
      not MapSet.member?(clay, {x, y + 1}) and not MapSet.member?(standing, {x, y + 1}) ->
        flow(:down, {x, y + 1}, [cell | path], standing, clay, max_y)

      # cannot go down, then split
      true ->
        left = flow(:left, cell, [], standing, clay, max_y)
        right = flow(:right, cell, [], standing, clay, max_y)

        # IO.inspect(left, label: "left_res:", width: 150)
        # IO.inspect(right, label: "right_res:", width: 150)

        case {left, right} do
          # both sides are closed, then fill standing water,
          # and restart from pevious cell
          {{:wall, left_path}, {:wall, right_path}} ->
            flow(
              :down,
              {x, y - 1},
              path,
              standing
              |> MapSet.union(MapSet.new(left_path))
              |> MapSet.union(MapSet.new(right_path))
              |> MapSet.union(MapSet.new([cell])),
              clay,
              max_y
            )

          {{_, left_path}, {_, right_path}} ->
            {:end,
             MapSet.new([cell | path])
             |> MapSet.union(MapSet.new(left_path))
             |> MapSet.union(MapSet.new(right_path))}
        end
    end
  end

  def flow(:left, {x, y} = cell, path, standing, clay, max_y) do
    # IO.inspect({cell, path}, label: "left:", width: 150)

    cond do
      # can go down, then go down
      not MapSet.member?(clay, {x, y + 1}) and not MapSet.member?(standing, {x, y + 1}) ->
        flow(:down, {x, y + 1}, [cell | path], standing, clay, max_y)

      # can go left, then go left
      not MapSet.member?(clay, {x - 1, y}) ->
        flow(:left, {x - 1, y}, [cell | path], standing, clay, max_y)

      # reached wall, return
      true ->
        {:wall, [cell | path]}
    end
  end

  def flow(:right, {x, y} = cell, path, standing, clay, max_y) do
    # IO.inspect({cell, path}, label: "right:", width: 150)

    cond do
      # can go down, then go down
      not MapSet.member?(clay, {x, y + 1}) and not MapSet.member?(standing, {x, y + 1}) ->
        flow(:down, {x, y + 1}, [cell | path], standing, clay, max_y)

      # can go right, then go left
      not MapSet.member?(clay, {x + 1, y}) ->
        flow(:right, {x + 1, y}, [cell | path], standing, clay, max_y)

      # reached wall, return
      true ->
        {:wall, [cell | path]}
    end
  end

  def print_field(clay, min_x, max_x) do
    for y <- 0..80 do
      for x <- min_x..max_x do
        if MapSet.member?(clay, {x, y}), do: "X", else: "."
      end
      |> Enum.join()
      |> IO.puts()
    end
  end

  def part1(clay) do
    {{_, _min_y}, {_, max_y}} = Enum.min_max_by(clay, &elem(&1, 1))
    # {{min_x, _}, {max_x, _}} = Enum.min_max_by(clay, &elem(&1, 0))
    flow(:down, {500, 0}, [], MapSet.new(), clay, max_y)
    # {{min_x, max_x}, {min_y, max_y}}
    # print_field(clay, min_x, max_x)
  end

  # def part2(clay) do
  # end
end

clay = Day17.parse_input("data/input17.txt")

Day17.part1(clay) |> IO.inspect()
# Day17.part1(clay) |> IO.puts()
# Day17.part2(clay) |> IO.puts()

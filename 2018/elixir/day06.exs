defmodule Day06 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    input
    |> String.split("\n", trim: true)
    |> Enum.map(fn s ->
      Regex.scan(~r/\d+/, s)
      |> Enum.map(&String.to_integer(hd(&1)))
    end)
    |> Enum.map(&List.to_tuple/1)
  end

  def distance({x1, y1}, {x2, y2}) do
    abs(x1 - x2) + abs(y1 - y2)
  end

  def closest_index(input, point) do
    distances =
      input
      |> Enum.with_index()
      |> Enum.map(fn {p, i} -> {i, distance(p, point)} end)

    min_distance =
      distances
      |> Enum.min_by(&elem(&1, 1))
      |> elem(1)

    closest =
      distances
      |> Enum.filter(fn {_, d} -> d == min_distance end)

    case length(closest) do
      1 -> closest |> hd() |> elem(0)
      _ -> nil
    end
  end

  def border_cell?({x, y}, min_x, max_x, min_y, max_y) do
    x == min_x or x == max_x or y == min_y or y == max_y
  end

  def min_max(input) do
    {{min_x, _}, {max_x, _}} = input |> Enum.min_max_by(&elem(&1, 0))
    {{_, min_y}, {_, max_y}} = input |> Enum.min_max_by(&elem(&1, 1))

    {{min_x, max_x}, {min_y, max_y}}
  end

  def part1(input, min_max) do
    {{min_x, max_x}, {min_y, max_y}} = min_max

    grid =
      for x <- min_x..max_x,
          y <- min_y..max_y,
          i = closest_index(input, {x, y}) do
        {{x, y}, i}
      end

    infinite_zones =
      grid
      |> Enum.filter(fn {p, _} -> border_cell?(p, min_x, max_x, min_y, max_y) end)
      |> Enum.map(&elem(&1, 1))
      |> MapSet.new()

    grid
    |> Enum.map(&elem(&1, 1))
    |> Enum.reject(&MapSet.member?(infinite_zones, &1))
    |> Enum.frequencies()
    |> Map.values()
    |> Enum.max()
  end

  def total_distance(input, point) do
    input
    |> Enum.map(&distance(&1, point))
    |> Enum.sum()
  end

  def part2(input, min_max) do
    {{min_x, max_x}, {min_y, max_y}} = min_max

    for x <- min_x..max_x,
        y <- min_y..max_y,
        reduce: 0 do
      acc -> if total_distance(input, {x, y}) < 10000, do: acc + 1, else: acc
    end
  end
end

input = Day06.parse_input("data/input06.txt")
min_max = Day06.min_max(input)

Day06.part1(input, min_max) |> IO.puts()
Day06.part2(input, min_max) |> IO.puts()

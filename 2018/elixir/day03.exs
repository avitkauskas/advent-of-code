defmodule Day03 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    input
    |> String.split("\n", trim: true)
    |> Enum.map(fn s ->
      Regex.scan(~r/\d+/, s)
      |> Enum.map(&hd/1)
      |> Enum.map(&String.to_integer/1)
    end)
  end

  def cells([_, x, y, w, h]) do
    for(i <- x..(x + w - 1), j <- y..(y + h - 1), do: {i, j})
    |> MapSet.new()
  end

  def overlap(claim, %{overlap: overlap, covered: covered} = acc) do
    cells = cells(claim)

    acc
    |> Map.put(:overlap, MapSet.union(overlap, MapSet.intersection(covered, cells)))
    |> Map.put(:covered, MapSet.union(covered, cells))
  end

  def overlaping_cells(input) do
    Enum.reduce(
      input,
      %{overlap: MapSet.new(), covered: MapSet.new()},
      &overlap/2
    ).overlap
  end

  def part1(overlaping_cells) do
    MapSet.size(overlaping_cells)
  end

  def part2(input, overlaping_cells) do
    Enum.drop_while(input, fn claim ->
      MapSet.intersection(cells(claim), overlaping_cells)
      |> Enum.any?()
    end)
    |> hd()
    |> hd()
  end
end

input = Day03.parse_input("data/input03.txt")
overlaping_cells = Day03.overlaping_cells(input)

Day03.part1(overlaping_cells) |> IO.puts()
Day03.part2(input, overlaping_cells) |> IO.puts()

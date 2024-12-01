defmodule Day01 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    input
    |> String.split("\n", trim: true)
    |> Enum.map(&String.to_integer/1)
  end

  def find_repeating(change, {freq, seen}) do
    freq = freq + change

    if MapSet.member?(seen, freq) do
      {:halt, freq}
    else
      {:cont, {freq, MapSet.put(seen, freq)}}
    end
  end

  def part1(input) do
    Enum.sum(input)
  end

  def part2(input) do
    Enum.reduce_while(
      Stream.cycle(input),
      {0, MapSet.new()},
      &find_repeating/2
    )
  end
end

input = Day01.parse_input("data/input01.txt")

Day01.part1(input) |> IO.puts()
Day01.part2(input) |> IO.puts()

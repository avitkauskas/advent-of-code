defmodule Day05 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    input
    |> String.trim()
  end

  def react?(el, item) do
    item && abs(el - item) == 32
  end

  def part1(input) do
    input
    |> String.to_charlist()
    |> Enum.reduce([], fn el, acc ->
      if react?(el, List.first(acc)) do
        tl(acc)
      else
        [el | acc]
      end
    end)
    |> length()
  end

  def part2(input) do
    for r <- String.to_charlist("abcdefghijklmnopqrstuvwxyz") do
      input
      |> String.to_charlist()
      |> Enum.reduce([], fn el, acc ->
        cond do
          el == r or el == r - 32 -> acc
          react?(el, List.first(acc)) -> tl(acc)
          true -> [el | acc]
        end
      end)
      |> length()
    end
    |> Enum.min()
  end
end

input = Day05.parse_input("data/input05.txt")

Day05.part1(input) |> IO.puts()
Day05.part2(input) |> IO.puts()

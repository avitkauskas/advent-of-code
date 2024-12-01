defmodule Day02 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    input
    |> String.split("\n", trim: true)
    |> Enum.map(&String.to_charlist/1)
  end

  def count_repeats(n, sets) do
    sets |> Enum.count(&MapSet.member?(&1, n))
  end

  def difference(s1, s2) do
    Enum.zip_with(s1, s2, &(&1 != &2))
    |> Enum.count(& &1)
  end

  def all_pairs([]), do: []
  def all_pairs([h | tail]), do: for(e <- tail, do: [h, e]) ++ all_pairs(tail)

  def find_pair([s1, s2] = pair, result) do
    case difference(s1, s2) do
      1 -> {:halt, pair}
      _ -> {:cont, result}
    end
  end

  def common_chars([s1, s2]) do
    Enum.zip_with(s1, s2, &if(&1 == &2, do: &1, else: nil))
    |> Enum.reject(&is_nil(&1))
    |> List.to_string()
  end

  def part1(input) do
    frequencies =
      input
      |> Enum.map(&Enum.frequencies/1)
      |> Enum.map(&Map.values/1)
      |> Enum.map(&MapSet.new/1)

    count_2 = count_repeats(2, frequencies)
    count_3 = count_repeats(3, frequencies)

    count_2 * count_3
  end

  def part2(input) do
    all_pairs(input)
    |> Enum.reduce_while(:no_pair, &find_pair/2)
    |> common_chars()
  end
end

input = Day02.parse_input("data/input02.txt")

Day02.part1(input) |> IO.puts()
Day02.part2(input) |> IO.puts()

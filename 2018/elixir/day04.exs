defmodule Day04 do
  def parse_input(filename) do
    {:ok, input} = File.read(filename)

    input
    |> String.split("\n", trim: true)
    |> Enum.sort()
    |> Enum.map(&parse_line/1)
  end

  def parse_line(line) do
    [first, second] = String.split(line, "] ", trim: true)

    minute = String.slice(first, -2..-1) |> String.to_integer()

    status =
      case second do
        "wakes up" -> :awake
        "falls asleep" -> :asleep
        str -> Regex.run(~r/\d+/, str) |> hd() |> String.to_integer()
      end

    {minute, status}
  end

  def register_asleep(%{guard: guard, from: from, table: table} = acc, to) do
    sleep_info =
      Enum.reduce(
        Enum.to_list(from..to),
        Map.get(table, guard, %{}),
        fn minute, table ->
          {_, table} =
            Map.get_and_update(table, minute, fn m ->
              case m do
                nil -> {nil, 1}
                n -> {n, n + 1}
              end
            end)

          table
        end
      )

    table = Map.put(table, guard, sleep_info)
    Map.put(acc, :table, table)
  end

  def collect_asleep({minute, status}, acc) do
    case status do
      :asleep -> Map.put(acc, :from, minute)
      :awake -> register_asleep(acc, minute - 1)
      _ -> Map.put(acc, :guard, status)
    end
  end

  def asleep_table(input) do
    asleep = Enum.reduce(input, %{guard: nil, from: nil, table: %{}}, &collect_asleep/2)
    asleep.table
  end

  def max_sleeping_guard(table) do
    Enum.max_by(table, fn {_, m} -> Map.values(m) |> Enum.sum() end)
  end

  def part1_result_hash({guard, minutes}) do
    {max_minute, _} = Enum.max_by(minutes, &elem(&1, 1))
    guard * max_minute
  end

  def part2_result_hash({guard, {minute, _}}) do
    guard * minute
  end

  def part1(asleep_table) do
    asleep_table
    |> max_sleeping_guard()
    |> part1_result_hash()
  end

  def part2(asleep_table) do
    asleep_table
    |> Enum.map(fn {guard, minutes} -> {guard, Enum.max_by(minutes, &elem(&1, 1))} end)
    |> Enum.max_by(&(elem(&1, 1) |> elem(1)))
    |> part2_result_hash()
  end
end

input = Day04.parse_input("data/input04.txt")
asleep_table = Day04.asleep_table(input)

Day04.part1(asleep_table) |> IO.puts()
Day04.part2(asleep_table) |> IO.puts()

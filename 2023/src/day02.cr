class Game
  getter id : Int32
  getter draws : Array(Hash(String, Int32))

  def initialize(@id, @draws)
  end
end

def parse_id(game_string)
  m = game_string.match(/Game (\d+)/)
  m ? m[1].to_i : 0
end

def parse_cubes(cubes_string)
  cubes_hash = {} of String => Int32
  {"red", "blue", "green"}.each do |colour|
    m = cubes_string.match(/(\d+) #{colour}/)
    cubes_hash[colour] = m ? m[1].to_i : 0
  end
  cubes_hash
end

def parse_game(game_string)
  game_id_string, draws_string = game_string.split(": ")
  id = parse_id(game_id_string)
  draws_strings_array = draws_string.split("; ")
  draws = draws_strings_array.map { |str| parse_cubes(str) }
  Game.new(id, draws)
end

MAX_CUBES = {"red" => 12, "green" => 13, "blue" => 14}

def possible_draw(draw)
  return false if draw["red"] > MAX_CUBES["red"]
  return false if draw["blue"] > MAX_CUBES["blue"]
  return false if draw["green"] > MAX_CUBES["green"]
  true
end

games = File.read_lines("input/input02.txt").map { |line| parse_game line }

possible_games = games.select do |game|
  game.draws.all? { |draw| possible_draw draw }
end

ids = possible_games.map &.id
sum = ids.sum

puts "Part 1: #{sum}"

def minimum_cubes(game)
  game.draws.reduce do |min, draw|
    min["red"] = draw["red"] if draw["red"] > min["red"]
    min["blue"] = draw["blue"] if draw["blue"] > min["blue"]
    min["green"] = draw["green"] if draw["green"] > min["green"]
    min
  end
end

def power(cubes)
  cubes.values.product
end

sum = games.map { |game| power minimum_cubes(game) }.sum

puts "Part 2: #{sum}"

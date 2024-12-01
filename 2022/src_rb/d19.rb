KEY = %w(geode obsidian clay ore)

def run input
  blueprints = input.split("\n").map { |line|
    recipes = line.split('.').map { |s|
      costs = [0, 0, 0, 0]
      s.split('costs').last.split(' and ').each { |s|
        num, type = s.split(' ')
        costs[KEY.index(type)] = -num.to_i
      }
      costs
    }.reverse
  }

  p blueprints.first(3).map { |bp| quality bp }.inject(:*)
end

def quality blueprint
  resources = [0, 0, 0, 0]
  robots =    [0, 0, 0, 1]
  states =    [[resources, robots]]

  32.times do
    children = []
    states.each { |resources, robots|
      blueprint.each.with_index { |costs, robot_type|
        _resources = resources.zip(costs).map(&:sum)
        if _resources.none?(&:negative?)
          _resources = _resources.zip(robots).map(&:sum)
          _robots = robots.clone
          _robots[robot_type] += 1
          children.push [_resources, _robots]
        end
      }
      resources = resources.zip(robots).map(&:sum)
      children.push [resources, robots]
    }

    states = children.uniq.max_by(5000) { |resources, robots|
      resources.zip(robots).flatten
    }
  end
  states.max.first.first
end

def main
  file = File.open("input19.txt")
  input = file.read
  file.close
  run input
end

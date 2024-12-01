def send_pulse(sender, destinations, pulse, queue)
  destinations.each { |dest| queue << {sender, dest, pulse} }
end

class FlipFlop
  @name : String
  getter state = {"st" => 0}
  getter destinations : Array(String)
  @queue : Deque({String, String, Int32})

  def initialize(@name, @destinations, @queue)
  end

  def handle(pulse : Int32, sender : String, push : UInt64)
    if pulse == 0
      @state["st"] = @state["st"] == 0 ? 1 : 0
      output = @state["st"] == 0 ? 0 : 1
      send_pulse(@name, @destinations, output, @queue)
    end
    0
  end
end

class Conjuction
  @name : String
  getter state = {} of String => Int32
  getter counts = {} of String => UInt64
  getter destinations : Array(String)
  @inputs = [] of String
  @queue : Deque({String, String, Int32})

  def initialize(@name, @destinations, @queue)
  end

  def init_inputs(inputs)
    @inputs = inputs
    @inputs.each do |inp| 
      @state[inp] = 0
      @counts[inp] = 0_u64
    end
  end

  def handle(pulse : Int32, sender : String, push : UInt64)
    @state[sender] = pulse
    if @counts[sender] == 0_u64 && pulse == 1
      @counts[sender] = push 
    end
    output = @state.values.all?(1) ? 0 : 1
    send_pulse(@name, @destinations, output, @queue)
    values = @counts.values
    if @name == "jz" && values.all?(1_u64..)
      values.reduce { |lcm, c| lcm.lcm(c) }
    else
      0
    end
  end
end

class Broadcaster
  @name : String
  getter state = {"st" => 0}
  getter destinations : Array(String)
  @queue : Deque({String, String, Int32})

  def initialize(@name, @destinations, @queue)
  end

  def handle(pulse : Int32, sender : String = nil, push : UInt64 = 0)
    send_pulse(@name, @destinations, pulse, @queue)
    0
  end
end

class Button
  @name : String
  getter destinations : Array(String)
  @queue : Deque({String, String, Int32})

  def initialize(@name, @destinations, @queue)
  end

  def push
    send_pulse(@name, @destinations, 0, @queue)
  end
end

class CommSystem
  @queue = Deque({String, String, Int32}).new
  @counts = [] of Tuple(Int32, Int32)
  @modules = {} of String => FlipFlop | Conjuction | Broadcaster

  def initialize(file_name)
    lines = File.read_lines(file_name)
    lines.each do |line|
      type_name, destinations = line.split(" -> ")
      make_module(type_name, destinations)
    end
    conjuctions = @modules.select { |k, v| v.is_a? Conjuction }.keys
    inputs = conjuctions.reduce({} of String => Array(String)) do |inputs, conj|
      @modules.each do |name, mod|
        if mod.destinations.includes? conj
          inputs.put_if_absent conj, Array(String).new
          inputs[conj] << name
        end
      end
      inputs
    end
    inputs.each do |name, inputs|
      conj = @modules[name]
      conj.init_inputs inputs if conj.is_a? Conjuction
    end
    @button = Button.new("button", ["broadcaster"], @queue)
  end

  def push_button(n = 0_u64)
    count_low, count_high = 0_u64, 0_u64
    i = 1_u64
    while i <= n || n == 0
      @button.push
      while !@queue.empty?
        sender, receiver, pulse = @queue.shift
        count_low += 1 if pulse == 0
        count_high += 1 if pulse == 1
        next if !@modules.has_key? receiver
        res = @modules[receiver].handle(pulse, sender, i)
        return res if res != 0 && n == 0
      end
      i += 1
    end
    count_low * count_high
  end

  private def make_module(type_name, destinations)
    type, name = type_name[0], type_name[1..]
    case type
    when '%'
      @modules[name] = FlipFlop.new(name, destinations.split(", "), @queue)
    when '&'
      @modules[name] = Conjuction.new(name, destinations.split(", "), @queue)
    else
      @modules[type_name] = Broadcaster.new(type_name, destinations.split(", "), @queue)
    end
  end
end

ans = CommSystem.new("input/input20.txt").push_button(1000)
puts "Part 1: #{ans}"

ans = CommSystem.new("input/input20.txt").push_button
puts "Part 2: #{ans}"

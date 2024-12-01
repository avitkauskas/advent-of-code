PACKET_CHUNK  =  4
MESSAGE_CHUNK = 14

buffer = File.read "input/input6.txt"

def marker_position(buffer, chunk)
  buffer
    .chars
    .each_cons(chunk)
    .take_while { |c| c.to_set.size < chunk }
    .size + chunk
end

print "Part 1: ", marker_position(buffer, PACKET_CHUNK), "\n"
print "Part 2: ", marker_position(buffer, MESSAGE_CHUNK), "\n"

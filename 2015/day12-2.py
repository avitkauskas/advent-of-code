import re
import json
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    json_str = f.read()

def ignore_red(dict):
    if "red" in dict.values():
        return {}
    return dict

no_red = json.loads(json_str, object_hook=ignore_red)
no_red_str = json.dumps(no_red)
num_str = re.findall(r"-?\d+", no_red_str)
numbers = [int(n) for n in num_str]
total = sum(numbers)

print(total)

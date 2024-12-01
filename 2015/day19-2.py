import re
import sys

input_file = f"input/input{sys.argv[0][3:5]}.txt"
if len(sys.argv) > 1:
    input_file = f"input/{sys.argv[1]}.txt"

with open(input_file) as f:
    input = f.read()

molecule = input.split('\n\n')[-1][::-1]
reps = {m[1][::-1]: m[0][::-1] 
        for m in re.findall(r'(\w+) => (\w+)', input)}

def rep(x):
    return reps[x.group()]

count = 0
while molecule != 'e':
    molecule = re.sub('|'.join(reps.keys()), rep, molecule, 1)
    count += 1

print(count)

row = 2978
col = 3083

first_code_in_row = 1
for i in range(1, row + 1):
    first_code_in_row += i - 1

required_code_number = first_code_in_row
for i in range(2, col + 1):
    required_code_number += row + i - 1

code = 20151125
multiplier = 252533
divisor = 33554393

for _ in range(1, required_code_number):
    code = (code * multiplier) % divisor

print(code)

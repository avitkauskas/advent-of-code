import math

target = 36000000

def modified_divisors(n):
    divisors = []
    i = 1
    while i <= math.sqrt(n):
        if n % i == 0:
            if i * 50 >= n:
                divisors.append(i)
            o = n // i
            if o != i and o * 50 >= n:
                divisors.append(o)
        i += 1
    return divisors

n = 1
while sum(modified_divisors(n)) < target / 11:
    n += 1

print(n)

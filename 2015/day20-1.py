import math

target = 36000000

def divisors(n):
    divisors = []
    i = 1
    while i <= math.sqrt(n):
        if n % i == 0:
            divisors.append(i)
            if n // i != i:
                divisors.append(n // i)
        i += 1
    return divisors

n = 1
while sum(divisors(n)) < target / 10:
    n += 1

print(n)

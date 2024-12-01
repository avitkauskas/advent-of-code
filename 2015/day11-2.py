import re

pswd = "vzbxxyzz"
required_len = 8

digits = "abcdefghijklmnopqrstuvwxyz"
base = len(digits)

def to_int(string):
    string, c = string[:-1], string[-1]
    num = ord(c) - ord(digits[0])
    if not string:
        return num
    return num + base * to_int(string)

def to_str(number):
    d, m = divmod(number, base)
    if not d:
        return digits[m]
    return to_str(d) + digits[m]

def bad_pswd(pswd):
    return (re.findall(r"i|l|o", pswd) or
            not re.findall(r"(.)\1.*(.)\2", pswd) or
            not re.findall(r"abc|bcd|cde|def|efg|fgh|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz", pswd))

pswd = to_str(to_int(pswd) + 1)
while bad_pswd(pswd):
    pswd = to_str(to_int(pswd) + 1)
    if len(pswd) < required_len:
        pswd = digits[0] * (8 - len(pswd)) + pswd

print(pswd)

num = '1113222113'

for _ in range(40):
    new_num = ''
    p, n = '', 0
    for c in num:
        if p == '':
            p, n = c, 1
        elif c == p:
            n += 1
        else:
            new_num += str(n) + p
            p, n = c, 1
    new_num += str(n) + p
    num = new_num

print(len(num))

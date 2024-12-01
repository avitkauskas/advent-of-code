import hashlib as h

i = 1
while h.md5(f"yzbqklnj{i}".encode('ascii')).hexdigest()[:6] != "000000":
    i += 1

print(i)

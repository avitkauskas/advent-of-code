import hashlib as h

i = 1
while h.md5(f"yzbqklnj{i}".encode('utf-8')).hexdigest()[:5] != "00000":
    i += 1

print(i)

import numpy as np

target = 36000000

presents = np.zeros(target // 10)
for i in range(1, target // 10):    
    presents[i::i] += 10 * i

print(np.argmax(presents > target))

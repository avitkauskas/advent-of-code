(ns aoc.day17)

(def steps 376)

(def init-state [0])

; (defn rotate [state len]
;   (let [n (rem steps len)]
;     (concat (drop n state) (take n state) (list len))))

(defn rotate [state len]
  (let [n (rem steps len)]
    (-> (subvec state n)
        (into (subvec state 0 n))
        (into [len]))))

(defn part1 []
  (first (reduce rotate init-state (range 1 2018))))

(def total-rotations 50000000)

(defn part2 []
  (loop [pos 0
         len 1
         res 0]
    (if (= len total-rotations)
      res
      (let [new-pos (-> (+ pos steps) (rem len) inc)]
        (recur new-pos
               (inc len)
               (if (= new-pos 1) len res))))))

(comment
  (time (part1))
  (part2))

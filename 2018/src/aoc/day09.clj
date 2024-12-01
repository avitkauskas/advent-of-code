(ns aoc.day09)

(def players 459)
(def max-marble 71790)

(defn insert-at [i x xs]
  (let [[a b] (split-at i xs)]
    (concat a [x] b)))

(defn remove-at [i xs]
  (let [[a b] (split-at i xs)]
    (concat a (rest b))))

(defn part-1 []
  (loop [circle '(0)
         idx 0
         marble 1
         player 1
         scores {}]
    (if (> marble max-marble)
      (apply max-key second scores)
      (if (zero? (rem marble 23))
        (let [idx (if (< idx 7)
                    (-> (count circle) (+ idx) (- 7))
                    (- idx 7))
              element (nth circle idx)
              circle (remove-at idx circle)
              idx (rem idx (count circle))
              scores (assoc scores player (+ marble element (scores player 0)))]
          (recur circle
                 idx
                 (inc marble)
                 (inc (rem player players))
                 scores))
        (let [idx (rem (+ idx 2) (count circle))
              circle (insert-at idx marble circle)]
          (recur circle
                 idx
                 (inc marble)
                 (inc (rem player players))
                 scores))))))

(comment
  (part-1))

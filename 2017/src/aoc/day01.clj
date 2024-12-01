(ns aoc.day01
  (:require
   [clojure.string :as str]))

(def input
  (->> (slurp "data/input01.txt")
       str/trim-newline
       vec
       (mapv #(parse-long (str %)))))

(defn part1 []
  (->> input
       first
       (conj input)
       (partition 2 1)
       (filter (fn [[a b]] (= a b)))
       (map first)
       (reduce +)))

(defn part1-v2 []
  (->> (map #(if (= %1 %2) %1 0)
            input (drop 1 (cycle input)))
       (reduce +)))

(defn part2 []
  (let [len (count input)
        n (/ len 2)
        extended (into [] (take (+ len n) (cycle input)))
        pairs (for [i (range len)] [(extended i) (extended (+ i n))])]
    (->> pairs
         (filter (fn [[a b]] (= a b)))
         (map first)
         (reduce +))))

(defn part2-v2 []
  (let [half (/ (count input) 2)]
    (->> (map #(if (= %1 %2) %1 0)
              input (drop half (cycle input)))
         (reduce +))))

(comment
  (part1)
  (part1-v2)
  (part2)
  (part2-v2)
  (dotimes [_ 10] (time (dotimes [_ 1000] (part1)))) ; "Elapsed time: 560.216625 msecs"
  (dotimes [_ 10] (time (dotimes [_ 1000] (part1-v2)))) ; "Elapsed time: 110.554459 msecs"
  (dotimes [_ 10] (time (dotimes [_ 1000] (part2)))) ; "Elapsed time: 258.428833 msecs"
  (dotimes [_ 10] (time (dotimes [_ 1000] (part2-v2))))) ; "Elapsed time: 138.055709 msecs"

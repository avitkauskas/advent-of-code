(ns aoc.day13 
  (:require
    [clojure.string :as str]))

(def input
  (->> (slurp "data/input13.txt")
       (str/split-lines)
       (map #(str/split % #": "))
       (map #(mapv parse-long %))
       (into {})))

(def n-layers (inc (reduce max (map first input))))

(defn cought? [[t layer]]
  (when-let [scanner-range (get input layer)]
    (zero? (rem t (+ scanner-range scanner-range -2)))))

(defn severity [acc layer]
  (if (cought? [layer layer])
    (+ acc (* layer (get input layer)))
    acc))

(defn part1 []
  (reduce severity (range n-layers)))

(defn part2 []
  (loop [start-delay 0]
    (if (reduce (fn [_ state] (if (cought? state) (reduced true) false))
                false
                (map vector (iterate inc start-delay) (range n-layers)))
      (recur (inc start-delay))
      start-delay)))

(comment
  (part1)
  (part2)
  (dotimes [_ 5] (time (part2)))) ; "Elapsed time: 1116.9685 msecs"

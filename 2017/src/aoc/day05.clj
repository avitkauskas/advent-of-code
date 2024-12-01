(ns aoc.day05
  (:require [clojure.string :as str]))

(def input
  (->> (slurp "data/input05.txt")
       (str/split-lines)
       (mapv parse-long)))

(defn jump [{:keys [pos table]}]
  (let [offset (get table pos)
        new-pos (+ pos offset)
        table (assoc table pos (inc offset))]
    {:pos new-pos :table table}))

(defn check-state [jumps {:keys [pos table]}]
  (if (nil? (get table pos))
    (reduced jumps)
    (inc jumps)))

(defn part1 []
  (reduce check-state 0 (iterate jump {:pos 0 :table input})))

(defn part1-v2 []
  (->> (map vector (range) (iterate jump {:pos 0 :table input}))
       (drop-while (fn [[_ {:keys [pos table]}]] (some? (get table pos))))
       ffirst))

(defn part1-v3 []
  (loop [table input
         pos 0
         jumps 0]
    (if-let [offset (get table pos)]
      (let [new-table (assoc table pos (inc offset))
            new-pos (+ pos offset)]
        (recur new-table new-pos (inc jumps)))
      jumps)))

(defn jump-part2 [{:keys [pos table]}]
  (let [offset (get table pos)
        new-pos (+ pos offset)
        new-offset (if (> offset 2) (dec offset) (inc offset))
        table (assoc table pos new-offset)]
    {:pos new-pos :table table}))

(defn part2 []
  (reduce check-state 0 (iterate jump-part2 {:pos 0 :table input})))

(defn part2-v2 []
  (loop [table input
         pos 0
         jumps 0]
    (if-let [offset (get table pos)]
      (let [new-offset (if (> offset 2) (dec offset) (inc offset))
            new-table (assoc table pos new-offset)
            new-pos (+ pos offset)]
        (recur new-table new-pos (inc jumps)))
      jumps)))

(comment
  (part1)
  (part1-v2)
  (part1-v3)
  (dotimes [_ 10] (time (dotimes [_ 10] (part1)))) ; "Elapsed time: 480.911584 msecs"
  (dotimes [_ 10] (time (dotimes [_ 10] (part1-v2)))) ; "Elapsed time: 738.415167 msecs"
  (dotimes [_ 10] (time (dotimes [_ 10] (part1-v3)))) ; "Elapsed time: 306.228375 msecs"
  (part2)
  (part2-v2)
  (dotimes [_ 10] (time (part2))) ; "Elapsed time: 3388.156667 msecs"
  (dotimes [_ 10] (time (part2-v2)))) ; "Elapsed time: 2084.809667 msecs"

(ns aoc.day02
  (:require
   [clojure.string :as str]))

(def input
  (->> (slurp "data/input02.txt")
       (str/split-lines)
       (map #(str/split % #"\s+"))
       (mapv #(mapv parse-long %))))

(defn max-min-diff [coll]
  (- (apply max coll)
     (apply min coll)))

(defn part1 []
  (->> input
       (map max-min-diff)
       (reduce +)))

(defn part1-v2 []
  (transduce (map max-min-diff) + input))

(defn find-quot [el _ candidate]
  (if (zero? (rem el candidate))
    (reduced (/ el candidate))
    0))

(defn divisable-quot [coll]
  (let [el-candidates (for [i (range (dec (count coll)))]
                        [(nth coll i) (drop (inc i) coll)])]
    (reduce (fn [acc [el candidates]]
              (if (pos? acc) 
                (reduced acc)
                (reduce (partial find-quot el) 0 candidates)))
            0 el-candidates)))

(defn part2 []
  (->> input
       (map #(sort > %))
       (map divisable-quot)
       (reduce +)))

(defn divisable-quot-v2 [coll]
  (let [desc (sort > coll)
        asc (sort coll)]
    (for [x desc
          y asc
          :while (> x y)
          :when (zero? (rem x y))]
      (/ x y))))

(defn part2-v2 []
  (->> input
       (map divisable-quot-v2)
       (map first)
       (reduce +)))

(defn divisable-quot-v3 [coll]
  (for [x coll
        y coll
        :when (> x y)
        :when (zero? (rem x y))]
    (/ x y)))

(defn part2-v3 []
  (->> input
       (map divisable-quot-v3)
       (map first)
       (reduce +)))

(comment
  (part1)
  (part1-v2)
  (dotimes [_ 10] (time (dotimes [_ 10000] (part1)))) ; "Elapsed time: 63.145125 msecs"
  (dotimes [_ 10] (time (dotimes [_ 10000] (part1-v2)))) ; "Elapsed time: 60.456041 msecs"
  (part2)
  (part2-v2)
  (part2-v3)
  (dotimes [_ 10] (time (dotimes [_ 10000] (part2)))) ; "Elapsed time: 409.152708 msecs"
  (dotimes [_ 10] (time (dotimes [_ 10000] (part2-v2)))) ; "Elapsed time: 296.891666 msecs"
  (dotimes [_ 10] (time (dotimes [_ 10000] (part2-v3))))) ; "Elapsed time: 196.766583 msecs"

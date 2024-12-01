(ns aoc.day06 
  (:require
    [clojure.string :as str]))

(def input
  (->> (slurp "data/input06.txt")
       str/split-lines
       (map #(format "[%s]" %))
       (map read-string)))

(def min-x (apply min (map first input)))
(def min-y (apply min (map second input)))
(def max-x (apply max (map first input)))
(def max-y (apply max (map second input)))

(defn dist [p1 p2]
  (->> (map - p1 p2) (map abs) (apply +)))

(defn closest-index [p]
  (let [distances (map-indexed (fn [i c] [i (dist c p)]) input)
        min-dist (apply min (map second distances))
        closest (filter #(= min-dist (second %)) distances)
        cnt (count closest)]
    (if (= cnt 1) (ffirst closest) nil)))

(defn grid []
  (for [x (range min-x (inc max-x))
        y (range min-y (inc max-y))
        :let [i (closest-index [x y])]
        :when (some? i)]
    [[x y] i]))

(defn border-cell? [[x y]]
  (or (= x min-x) (= x max-x)
      (= y min-y) (= y max-y)))

(defn infinite-zones [grid]
  (->> (filter (fn [[p _]] (border-cell? p)) grid)
       (map second)
       set))

(defn part-1 []
  (let [grid (grid)
        infinite-zones (infinite-zones grid)]
    (->> grid
         (map second)
         (remove infinite-zones)
         frequencies
         (map second)
         (apply max))))

(defn total-distance [p]
  (->> (map (partial dist p) input)
       (apply +)))

(defn part-2 []
  (reduce (fn [acc p]
            (if (< (total-distance p) 10000)
              (inc acc) acc))
          0
          (for [x (range min-x (inc max-x))
                y (range min-y (inc max-y))]
            [x y])))

(comment
  (part-1)
  (time (part-1)) ; "Elapsed time: 2091.339333 msecs"
  (part-2)
  (time (part-2))) ; "Elapsed time: 1691.176083 msecs"

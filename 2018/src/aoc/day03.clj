(ns aoc.day03 
  (:require
    [clojure.string :as str]))

(def input
  (->> (slurp "data/input03.txt")
       str/split-lines
       (map #(re-seq #"\d+" %))
       (map #(map parse-long %))))

(defn cells [[_id x0 y0 w h]]
  (for [x (range x0 (+ x0 w))
        y (range y0 (+ y0 h))]
    [x y]))

(defn overlap [{covered :covered :as state} claim]
  (let [cells (cells claim)]
    (-> state
        (update :overlap #(apply conj % (keep covered cells)))
        (update :covered #(apply conj % cells)))))

(def overlapping-cells
  (:overlap (reduce overlap {:overlap #{} :covered #{}} input)))

(defn part-1 []
  (count overlapping-cells))

(defn part-2 []
  (ffirst (drop-while #(some overlapping-cells (cells %)) input)))

(comment
  (part-1)
  (part-2))

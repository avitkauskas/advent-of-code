(ns aoc.d20-1 
  (:require
    [clojure.string :as s]))

(defn read-input []
  (->> (slurp "data/input20.txt")
       (s/split-lines)
       (mapv #(s/split % #"-"))
       (mapv #(map parse-long %))
       (sort-by first)))

(defn overlaps? [[_ end] [start _]]
  (>= end (dec start)))

(defn combined [[a1 b1] [a2 b2]]
  [(min a1 a2) (max b1 b2)])

(defn combine-intervals [acc v]
  (let [last-interval (last acc)]
    (if (overlaps? last-interval v)
      (conj (pop acc) (combined last-interval v))
      (conj acc v))))

(defn -main []
  (->> (reduce combine-intervals [[0 0]] (read-input))
       first second inc))

(comment
  (-main))


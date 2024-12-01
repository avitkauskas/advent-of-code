(ns aoc.d06-1
  (:require
    [clojure.string :as s]))

(defn get-columns []
  (->> (slurp "data/input06.txt")
       (s/split-lines)
       (apply map vector)))

(defn -main []
  (->> (get-columns)
       (map frequencies)
       (map #(sort-by val > %))
       (map #((comp first first) %))
       (apply str)))

(comment
  (-main))
  

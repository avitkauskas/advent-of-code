(ns aoc.d06-2
  (:require
    [clojure.string :as s]))

(defn get-columns []
  (->> (slurp "data/input06.txt")
       (s/split-lines)
       (apply map vector)))

(defn -main []
  (->> (get-columns)
       (map frequencies)
       (map #(sort-by val %))
       (map ffirst)
       (apply str)))

(comment
  (-main))
  

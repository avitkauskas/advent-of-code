(ns aoc.day04
  (:require [clojure.string :as str]))

(def input
  (->> (slurp "data/input04.txt")
       (str/split-lines)
       (map #(str/split % #"\s+"))))

(defn part1 []
  (->> input
       (filter #(apply distinct? %))
       count))

(defn part2 []
  (->> input
       (map #(map sort %))
       (filter #(apply distinct? %))
       count))

(comment
  (part1)
  (part2))

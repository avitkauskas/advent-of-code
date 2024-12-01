(ns aoc.day09
  (:require [clojure.string :as str]))

(str/replace "<!!>>" #"!." "")

(def input
  (-> (slurp "data/input09.txt")
      (str/trim-newline)))

(defn- remove-garbage [s]
  (-> s
      (str/replace #"!." "")
      (str/replace #"<.*?>" "")
      (str/replace #"," "")))

(defn- count-score [[total current] sym]
  (case sym
    \{ [(+ total (inc current)) (inc current)]
    \} [total (dec current)]
    [total current]))

(defn part1 []
  (->> input
       remove-garbage
       (reduce count-score [0 0])
       first))

(defn- count-garbage [s]
  (->> (str/replace s #"!." "")
       (re-seq #"<.*?>")
       (map #(- (count %) 2))
       (reduce +)))

(defn part2 []
  (count-garbage input))

(comment
  (part1)
  (part2))

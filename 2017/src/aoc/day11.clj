(ns aoc.day11 
  (:require
    [clojure.string :as str]))

(def input
  (-> (slurp "data/input11.txt")
      (str/trim-newline)
      (str/split #",")))

(defn move [coords dir]
  (let [diff (case dir
               "n"  [1 -1 0]
               "ne" [1 0 -1]
               "se" [0 1 -1]
               "s"  [-1 1 0]
               "sw" [-1 0 1]
               "nw" [0 -1 1])]
    (mapv + coords diff)))

(defn dist-from-origin [coords]
  (->> coords
       (map abs)
       (apply max)))

(defn part1 []
  (-> (reduce move [0 0 0] input)
      (dist-from-origin)))

(defn move-with-max [{:keys [max-dist coords]} dir]
  (let [new-coords (move coords dir)
        new-max (max max-dist (dist-from-origin new-coords))]
    {:max-dist new-max :coords new-coords}))

(defn part2 []
  (-> (reduce move-with-max {:max-dist 0 :coords [0 0 0]} input)
      :max-dist))

(comment
  (part1)
  (part2))

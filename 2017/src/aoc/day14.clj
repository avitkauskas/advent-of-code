(ns aoc.day14
  (:require
   [aoc.day10 :as kh]
   [clojure.pprint :as pp]
   [clojure.set :as set]))

(pp/cl-format nil "~128,'0',B" (read-string (str "0x" (kh/knot-hash "flqrgnkx-0"))))

(def input "jzgqcdpd")

(def disc
  (->> (range 128)
       (map #(str input "-" %))
       (map kh/knot-hash)
       (map #(str "0x" %))
       (map read-string)
       (map #(pp/cl-format nil "~128,'0',B" %))))

(defn part1 []
  (->> disc
       (map #(filter #{\1} %))
       (map count)
       (reduce +)))

(def used-coords
  (for [i (range 128)
        j (range 128)
        :when (= \1 (nth (nth disc j) i))]
      [i j]))

(defn neighbours [coord]
  (set (map #(mapv + %1 %2) [[1 0] [0 1] [-1 0] [0 -1]] (repeat coord))))

(defn make-groups [groups coord]
  (let [neighs (neighbours coord)
        neigh-groups (filter #(some neighs %) groups)
        joint-group (apply set/union #{coord} neigh-groups)
        other-groups (apply disj groups neigh-groups)]
    (conj other-groups joint-group)))

(defn part2 []
  (count (reduce make-groups #{} used-coords)))

(comment
  (part1)
  (part2))

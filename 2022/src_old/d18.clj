(ns coa.d18
  (:require
   [clojure.string :as str]
   [clojure.set :as set]))

(def rock-cells
  (->> (slurp "input/test.txt")
       (str/split-lines)
       (map #(str/split % #","))
       (map #(map read-string %))
       (into #{})))

(def minx (dec (apply min (map #(nth % 0) rock-cells))))
(def miny (dec (apply min (map #(nth % 1) rock-cells))))
(def minz (dec (apply min (map #(nth % 2) rock-cells))))
(def maxx (inc (apply max (map #(nth % 0) rock-cells))))
(def maxy (inc (apply max (map #(nth % 1) rock-cells))))
(def maxz (inc (apply max (map #(nth % 2) rock-cells))))

(defn neighbour-coords [[x y z]]
  (let [all-neighbours [[(dec x) y z] [(inc x) y z]
                        [x (dec y) z] [x (inc y) z]
                        [x y (dec z)] [x y (inc z)]]]
       (filterv (fn [[x y z]] (and (<= minx x maxx) (<= miny y maxy) (<= minz z maxz))) all-neighbours)))

(defn count-neighbours [cell]
  (reduce #(if (contains? rock-cells %2) (inc %1) %1) 0 (neighbour-coords cell)))

(defn count-rock-neighbours [watter-cell]
  (inc 0))

(defn count-watter-neighbours [current-count watter-pool watter-cell]
  (let [watter-neighbours (set (neighbour-coords watter-cell))
        occupied-by-rocks (set/intersection watter-neighbours rock-cells)
        already-in-pool (set/intersection watter-neighbours watter-pool)
        new-watter-cells (set/difference watter-neighbours occupied-by-rocks already-in-pool)
        expanded-pool (set/union watter-pool new-watter-cells)]
    (if (empty? new-watter-cells)
      (+ current-count (count-rock-neighbours watter-cell))
      (+ current-count (apply + (for [cell new-watter-cells]
                                  (count-watter-neighbours current-count expanded-pool cell)))))))

(defn -main []
  (let [neighbours (for [cell rock-cells] (count-neighbours cell))]
    (- (* (count neighbours) 6) (apply + neighbours))))

(comment
  (prn (-main))
  (neighbour-coords [1 1 1])
  (prn (count-watter-neighbours 0 #{[minx miny minz]} [minx miny minz]))
  )

(ns coa.d5
  (:require
   [clojure.pprint :as pp]
   [clojure.string :as s]))

(defn map-delete-spaces [coll]
  (map #(s/replace % #" " "") coll))

(defn filter-empty [coll]
  (filter #(not= % "") coll))

(defn vect->map [[a b c d e f]]
  {(keyword a) (read-string b)
   (keyword c) (read-string d)
   (keyword e) (read-string f)})

(defn make-moves [bins {:keys [move from to]}]
  (let [from (dec from) to (dec to)
        bins (assoc bins to (concat (take move (nth bins from)) (nth bins to)))
        bins (assoc bins from (drop move (nth bins from)))]
    bins))

(defn main []
  (let [input ((comp s/split-lines slurp) "input/input5.txt")
        bins (->> input
                  (take-while #(not= "" %))
                  reverse
                  pop
                  (map #(re-seq #".{1,4}" %))
                  (map map-delete-spaces)
                  (apply map list)
                  (map filter-empty)
                  (map #(into '() %))
                  (into []))
        moves (->> input
                   (drop-while #(not (s/starts-with? % "move")))
                   (map #(s/split % #" "))
                   (map vect->map)
                   (into []))]
    (reduce #(make-moves %1 %2) bins moves)))

(comment
  (pp/pprint (main))
  (make-moves ['("a" "b") '("c" "d")] {:move 1 :from 1 :to 2}))

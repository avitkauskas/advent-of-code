(ns aoc.day21
  (:require
   [clojure.math :as math]
   [clojure.string :as str]))

(def init-state [[0 1 0] [0 0 1] [1 1 1]])

(def input
  (->> (slurp "data/input21.txt")
       (str/split-lines)
       (map #(str/replace % "." "0 "))
       (map #(str/replace % "#" "1 "))
       (map #(str/replace % "/" "]["))
       (map #(str/replace % " => " "]][["))
       (map #(format "[[[%s]]]" %))
       (mapv read-string)))

(defn flip [m]
  (reverse m))

(defn rotate [m]
  (apply map list (flip m)))

(defn all-positions [m]
  (distinct
   (concat (take 4 (iterate rotate m))
           (take 4 (iterate rotate (flip m))))))

(def rules-book
  (into {}
        (for [[from to] input
              pos (all-positions from)]
          [pos to])))

(defn enhance [m]
  (rules-book m))

(defn split [m]
  (let [size (count m)
        parts (if (zero? (rem size 2)) 2 3)]
    (->> (map #(partition parts %) m)
         (partition parts)
         (mapcat #(apply map list %)))))

(defn join [coll]
  (let [parts (int (math/sqrt (count coll)))]
    (->> (partition parts coll)
         (mapcat #(apply map list %))
         (map #(apply concat %)))))

(defn enhance-all [m]
  (->> m split (map enhance) join))

(defn count-on [iterations]
  (->> (nth (iterate enhance-all init-state) iterations)
       (map #(reduce + %))
       (reduce +)))

(defn part1 []
  (count-on 5))

(defn part2 []
  (count-on 18))

(comment
  (part1)
  (part2)
  (time (part2))) ; "Elapsed time: 2077.805208 msecs"

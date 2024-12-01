(ns aoc.day06
  (:require [clojure.string :as str]))

(set! *warn-on-reflection* true)

(def input
  (->> (str/split (slurp "data/input06.txt") #"\s+")
       (mapv parse-long)))

(def len (count input))

(defn find-max [^clojure.lang.APersistentVector coll]
  (let [max-v (apply max coll)]
    [(.indexOf coll max-v) max-v]))

(defn distribute [coll [i n]]
  (->> (take n (repeat 1))
       (concat (take (inc i) (repeat 0)))
       (partition len len (repeat 0))
       (apply mapv + (assoc coll i 0))))

(defn part1 []
  (loop [seen #{}
         coll input
         cycles 0]
    (if (contains? seen coll)
      cycles
      (recur (conj seen coll)
             (distribute coll (find-max coll))
             (inc cycles)))))

(defn find-seen []
  (loop [seen #{}
         coll input]
    (if (contains? seen coll)
      coll
      (recur (conj seen coll)
             (distribute coll (find-max coll))))))

(defn part2 []
  (let [init (find-seen)]
    (loop [coll (distribute init (find-max init))
           cycles 1]
      (if (= coll init)
        cycles
        (recur (distribute coll (find-max coll))
               (inc cycles))))))

(comment
  (find-max input)
  (dotimes [_ 10] (time (dotimes [_ 1000000] (find-max input))))
  (part1)
  (dotimes [_ 10] (time (part1))) ; "Elapsed time: 74.323583 msecs"
  (part2)
  (dotimes [_ 10] (time (part2)))) ; "Elapsed time: 117.477791 msecs"

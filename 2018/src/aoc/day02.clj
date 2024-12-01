(ns aoc.day02 
  (:require
   [clojure.string :as str]
   [clojure.set :as set]
   [clojure.math.combinatorics :as combo]))

(def input
  (-> (slurp "data/input02.txt")
      str/split-lines))

(defn count-freq [freqs n]
  (->> freqs
       (filter #(% n))
       count))

(defn part-1 []
  (let [freqs (->> input
                  (map frequencies)
                  (map set/map-invert))]
    (* (count-freq freqs 2) (count-freq freqs 3))))

(defn diff-chars [id1 id2]
  (->> (map (fn [c1 c2] (if (= c1 c2) 0 1)) id1 id2)
       (apply +)))

(defn find-one-diff [acc [id1 id2 :as ids]]
  (if (= 1 (diff-chars id1 id2))
    (reduced ids)
    acc))

(defn common-letters [id1 id2]
  (->> (map (fn [c1 c2] (when (= c1 c2) c1)) id1 id2)
       str/join))

(defn part-2 []
  (->> (combo/combinations input 2)
       (reduce find-one-diff :no-match)
       (apply common-letters)))

(comment
  (part-1)
  (part-2))

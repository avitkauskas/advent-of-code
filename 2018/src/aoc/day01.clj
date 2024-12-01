(ns aoc.day01
  (:require
   [clojure.string :as str]))

(def input
  (->> (slurp "data/input01.txt")
       str/split-lines
       (map read-string)))

(defn part-1 []
  (reduce + input))

(defn find-seen [{:keys [freq seen] :as acc} change]
  (let [freq (+ freq change)]
    (if (seen freq)
      (reduced freq)
      (assoc acc :freq freq :seen (conj seen freq)))))

(defn part-2 []
  (reduce find-seen {:freq 0 :seen #{0}} (cycle input)))

(comment
  (part-1)
  (part-2))

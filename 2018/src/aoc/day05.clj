(ns aoc.day05 
  (:require
    [clojure.string :as str]))

(def input
  (-> (slurp "data/input05.txt")
      str/trim-newline))

(def destroy-re
  (->> (for [n (range 65 91)
             :let [C (char n) c (char (+ n 32))]]
         [(str c C) (str C c)])         
       flatten
       (str/join "|")
       re-pattern))

(defn react-length [polymer]
  (count (reduce (fn [prev curr] (if (= prev curr) (reduced curr) curr))
                 (iterate #(str/replace % destroy-re "") polymer))))

(defn part-1 []
  (react-length input))

(def unit-types
  (->> (for [n (range 65 91)
             :let [C (char n) c (char (+ n 32))]]
         (str c "|" C))
       (map re-pattern)))

(defn part-2 []
  (->> unit-types
       (map #(str/replace input % ""))
       (map react-length)
       (apply min)))

(defn react? [a b]
  (and a b (not= a b) (= (str/lower-case a) (str/lower-case b))))

(defn collapsed-length [polymer]
  (count (reduce (fn [acc u] (if (react? (peek acc) u) (pop acc) (conj acc u))) [] polymer)))

(defn part-1-v2 []
  (collapsed-length input))

(defn delete-type [polymer u]
  (-> polymer
      (str/replace (str u) "")
      (str/replace (str/upper-case u) "")))

(defn part-2-v2 []
  (->> "abcdefghijklmnopqrstuvwxyz"
       (map (partial delete-type input))
       (map collapsed-length)
       (apply min)))

(comment
  (part-1)
  (time (part-1)) ; "Elapsed time: 2772.292209 msecs"
  (part-1-v2)
  (time (part-1-v2)) ; "Elapsed time: 15.05925 msecs"
  (part-2)
  (time (part-2)) ; "Elapsed time: 68555.420875 msecs"
  (part-2-v2)
  (time (part-2-v2))) ; "Elapsed time: 159.710583 msecs"

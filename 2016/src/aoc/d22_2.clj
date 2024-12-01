(ns aoc.d22-2
  (:require
    [clojure.string :as s]))

(defn parse-num [s]
  (parse-long (subs s 0 (dec (count s)))))

(defn pars-coords [s]
  (->> (s/split s #"-")
       (rest)
       (mapv #(parse-long (subs % 1)))))

(defn parse-line [s]
  (as-> (s/split s #"\s+") $
        (vector (pars-coords (nth $ 0)) (parse-num (nth $ 2)))))

(defn read-input []
  (->> (slurp "data/input22.txt")
       (s/split-lines)
       (drop 2)
       (map parse-line)))

(def input (read-input))

(defn grid-line [xs]
  (->> (map (fn [[[_ _] n]]
              (cond
                (= n 0) \X
                (> n 100) \#
                :else \.))
            xs)
       (s/join)))

(defn print-grid [grid]
  (let [width (inc (reduce max (map ffirst grid)))]
    (->> grid
         (sort-by (fn [[[_ y] _]] y))
         (partition width)
         (map grid-line)
         (run! println))))

(defn -main []
  (print-grid input))

;; solved moves manually looking at the grid
;; seamed quicker than writting the program for that

(comment
  (-main))

(ns coa.d8-2
  (:require 
   [clojure.string :as s]
   [clojure.core.matrix :as m]))

(defn- get-input [file-name]
  (->> (slurp file-name)
       s/split-lines
       (map #(s/split % #""))
       (map #(map read-string %))
       m/matrix))

(def m (get-input "input/input8.txt"))

(defn- score [coll element]
    (if (= (count coll) 0) 0
        (let [visible-count (count (take-while #(< % element) coll))]
          (if (= visible-count (count coll))
            visible-count
            (inc visible-count)))))

(defn- scenic-score [index element]
  (let [x (first index)
        y (second index)
        row (m/get-row m x)
        column (m/get-column m y)]
    (* (score (reverse (take y row)) element)
       (score (drop (inc y) row) element)
       (score (reverse (take x column)) element)
       (score (drop (inc x) column) element))
    ))

(defn -main []
    (m/emax (m/emap-indexed scenic-score m)))

(comment
  (-main))

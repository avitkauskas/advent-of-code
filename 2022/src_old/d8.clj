(ns coa.d8
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
(def rows (m/row-count m))
(def columns (m/column-count m))

(defn is-visible? [index element]
  (let [row (m/get-row m (first index))
        column (m/get-column m (second index))
        x (first index)
        y (second index)]
    (if (or (= x 0) (= x (dec rows))
            (= y 0) (= y (dec columns))
            (every? true? (map #(< % element) (take y row)))
            (every? true? (map #(< % element) (drop (inc y) row)))
            (every? true? (map #(< % element) (take x column)))
            (every? true? (map #(< % element) (drop (inc x) column))))
      1 0)))

(defn -main []
    (println (m/esum (m/emap-indexed is-visible? m))))

(comment
  (-main))

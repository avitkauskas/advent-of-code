(ns aoc.d02-1
  (:require [clojure.string :as str]))

(defn- get-input
  [file-name]
  (-> (slurp file-name)
      (str/split-lines)))

(defn- move
  [[x y] dir]
  (let [pos [x y]]
    (condp = dir
      \U (if (> y 0) [x (dec y)] pos)
      \D (if (< y 2) [x (inc y)] pos)
      \L (if (> x 0) [(dec x) y] pos)
      \R (if (< x 2) [(inc x) y] pos)
      pos)))

(defn- process-line
  [{:keys [code start]} line]
  (let [[x y] (reduce move start line)
        code (+ (* 10 code) (+ (* 3 y) x 1))]
    {:code code :start [x y]}))

(defn -main []
  (let [lines (get-input "data/input02.txt")]
    (:code (reduce process-line {:code 0 :start [1 1]} lines))))
  
(comment
  (-main))
  

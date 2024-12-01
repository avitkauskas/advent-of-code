(ns aoc.d02-2
  (:require [clojure.string :as str]))

(def keypad {2 "1"
             6 "2" 7 "3" 8 "4"
             10 "5" 11 "6" 12 "7" 13 "8" 14 "9"
             16 "A" 17 "B" 18 "C"
             22 "D"})

(defn- get-input [file-name]
  (-> (slurp file-name)
      (str/split-lines)))

(defn- move [[x y] dir]
  (let [pos [x y]]
    (condp = dir
      \U (if (or (and (or (= x 1) (= x 3)) (> y 1))
                 (and (= x 2) (> y 0)))
           [x (dec y)]
           pos)
      \D (if (or (and (or (= x 1) (= x 3)) (< y 3))
                 (and (= x 2) (< y 4)))
           [x (inc y)]
           pos)
      \L (if (or (and (or (= y 1) (= y 3)) (> x 1))
                 (and (= y 2) (> x 0)))
           [(dec x) y]
           pos)
      \R (if (or (and (or (= y 1) (= y 3)) (< x 3))
                 (and (= y 2) (< x 4)))
           [(inc x) y]
           pos)
      pos)))

(defn- get-digit [x y]
  (keypad (+ (* 5 y) x)))

(defn- process-line [{:keys [code start]} line]
  (let [[x y] (reduce move start line)
        code (str code (get-digit x y))]
    {:code code :start [x y]}))

(defn -main []
  (let [lines (get-input "data/input02.txt")]
    (:code (reduce process-line {:code "" :start [0 2]} lines))))
  
(comment
  (-main))
  

(ns aoc.d13-2
  (:require
   [clojure.data.priority-map :as pm]))

(def start [1 1])
(def max-moves 50)

(def favourite-number 1350)

(defn open-space? [[x y]]
  (->> (+ favourite-number (* x x) (* 3 x) (* 2 x y) y (* y y))
       (Integer/toBinaryString)
       (filter #{\1})
       (count)
       (even?)))

(defn next-cells [coord visited]
  (for [move [[0 1] [0 -1] [1 0] [-1 0]]
        :let [new-coord (mapv + coord move)]
        :when (every? (complement neg?) new-coord)
        :when (open-space? new-coord)
        :when (not (contains? visited new-coord))]
    new-coord))

(defn add-to [visited cells dist]
  (reduce (fn [v coord] (assoc v coord dist)) visited cells))

(defn -main []
  (loop [queue (pm/priority-map start 0)
         visited {start 0}]
    (let [[coord distance] (peek queue)]
      (if (= distance max-moves)
        (count visited)
        (let [cells (next-cells coord visited)
              new-distance (inc (visited coord))]
          (recur (add-to (pop queue) cells new-distance)
                 (add-to visited cells new-distance)))))))

(comment
  (-main))
  

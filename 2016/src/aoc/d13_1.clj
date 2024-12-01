(ns aoc.d13-1
  (:require
   [clojure.data.priority-map :as pm]))

(def start [1 1])
(def target [31 39])

(def favourite-number 1350)

(defn open-space? [[x y]]
  (->> (+ favourite-number (* x x) (* 3 x) (* 2 x y) y (* y y))
       (Integer/toBinaryString)
       (filter #{\1})
       (count)
       (even?)))

(defn distance-to-target [coord]
  (->> (map (comp abs -) target coord)
       (apply +)))

(defn next-cells [coord visited]
  (for [move [[0 1] [0 -1] [1 0] [-1 0]]
        :let [new-coord (mapv + coord move)]
        :when (every? (complement neg?) new-coord)
        :when (open-space? new-coord)
        :when (not (contains? visited new-coord))]
    new-coord))

(defn add-to-visited [visited cells dist]
  (reduce (fn [v coord] (assoc v coord dist)) visited cells))

(defn add-to-queue [queue cells dist]
  (reduce (fn [q coord] (assoc q coord (+ dist (distance-to-target coord)))) queue cells))

(defn -main []
  (loop [queue (pm/priority-map start (distance-to-target start))
         visited {start 0}]
    (let [[coord distance] (peek queue)]
      (if (= coord target)
        distance
        (let [cells (next-cells coord visited)
              new-distance (inc (visited coord))]
          (recur (add-to-queue (pop queue) cells new-distance)
                 (add-to-visited visited cells new-distance)))))))

(comment
  (-main))
  

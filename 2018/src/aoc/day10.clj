(ns aoc.day10 
  (:require
    [clojure.string :as str]))

(def input
  (-> (slurp "data/input10.txt")
      (str/replace #"position=" "")
      (str/replace #"velocity=" "")
      (str/replace #"<" "")
      (str/replace #">" "")
      (str/replace #"," "")
      (str/trim)
      (str/split #"\s+")
      (->> (map parse-long))))

(def positions
  (->> input
       (partition 2 4)
       (mapv vec)))

(def velocities
  (->> input
       (drop 2)
       (partition 2 4)
       (mapv vec)))

(defn move [positions velocities]
  (mapv (fn [p v] (mapv + p v)) positions velocities))

(defn min-max [positions]
  (let [x (map first positions)
        y (map second positions)
        min-x (apply min x)
        max-x (apply max x)
        min-y (apply min y)
        max-y (apply max y)]
    [min-x max-x min-y max-y]))

(defn bounding-area [[min-x max-x min-y max-y]]
  (* (- max-x min-x) (- max-y min-y)))

(defn minimal-area-positions [positions velocities]
  (loop [pos positions
         min-area (bounding-area (min-max pos))
         t 0]
    (let [new-pos (move pos velocities)
          new-area (bounding-area (min-max new-pos))]
      (if (> new-area min-area)
        {:pos pos :time t}
        (recur new-pos new-area (inc t))))))

(defn print-message [positions]
  (let [[min-x max-x min-y max-y] (min-max positions)
        width (inc (- max-x min-x))
        height (inc (- max-y min-y))
        screen (vec (repeat height (vec (repeat width " "))))
        screen (reduce (fn [sc [x y]] (assoc-in sc [(- y min-y) (- x min-x)] "X")) screen positions)]
    (doseq [line screen]
      (println (apply str line)))))
  

(defn part-1&2 []
  (let [res (minimal-area-positions positions velocities)]
    (print-message (:pos res))
    (println (:time res))))

(comment
  (part-1&2))
 

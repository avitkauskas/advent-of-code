(ns aoc.d24-2
  (:require
    [clojure.string :as s]
    [clojure.data.priority-map :as pm]
    [clojure.math.combinatorics :as combo]))

(defn read-lines []
  (->> (slurp "data/input24.txt")
       (s/split-lines)))

(defn make-maze [lines]
  (->> (for [[y line] (map vector (range) lines)
             [x chr] (map vector (range) line)]
         [[x y] chr])
       (into {})))

(def maze (make-maze (read-lines)))
(def start (-> (filterv #(= \0 (second %)) maze) first))
(def targets (->> (filter #(when-let [d (parse-long (str (second %)))] (pos? d)) maze)
                  (sort-by second)
                  (vec)))

(defn add-move [[coords _] move]
  (let [new-coords (mapv + coords move)]
    [new-coords (maze new-coords)]))

(defn next-cells [cell visited]
  (for [move [[0 1] [0 -1] [1 0] [-1 0]]
        :let [new-cell (add-move cell move)]
        :when (not= \# (second new-cell))
        :when (not (contains? visited new-cell))]
    new-cell))

(defn add-to-queue [queue cells dist]
  (reduce (fn [q cell] (assoc q cell dist)) queue cells))

(defn get-distances [origin targets]
  (loop [distances {}
         visited #{origin} 
         targets (into #{} targets)
         queue (pm/priority-map origin 0)]
    (if (empty? targets)
      distances
      (let [[cell dist] (peek queue)
            neighbors (next-cells cell visited)
            target (some #{cell} targets)]
        (if target
          (recur (assoc distances [(second origin) (second target)] dist)
                 (apply conj visited neighbors)
                 (disj targets target)
                 (add-to-queue (pop queue) neighbors (inc dist)))
          (recur distances
                 (apply conj visited neighbors)
                 targets
                 (add-to-queue (pop queue) neighbors (inc dist))))))))

(defn make-distances [points]
  (loop [distances {}
         origin (first points)
         targets (rest points)]
    (if (empty? targets)
      distances
      (recur (into {} (concat distances (get-distances origin targets)))
             (first targets)
             (rest targets)))))

(def distances (make-distances (cons start targets)))

(def target-points (mapv char (range (int \1) (+ (int \1) (count targets)))))

(def all-paths (combo/permutations target-points))

(defn total-dist [path]
  (let [pairs (partition 2 1 (concat [\0] path [\0]))]
    (reduce + (map #(distances (sort %)) pairs))))

(defn -main []
  (reduce min (map total-dist all-paths)))

(comment
  (-main))

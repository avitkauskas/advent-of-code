(ns coa.d12
  (:require
   [clojure.string :as s]))

(defn- get-lines [file-name]
  (->> (slurp file-name)
       s/split-lines))

(def lines (get-lines "input/input12.txt"))

(def rows (count lines))
(def cols (count (first lines)))

(def input (->> (apply str lines)
                (map #(- (int %) 97))
                vec))

(def start (.indexOf input -14))
(def target (.indexOf input -28))

(def cells (assoc input start 0 target 25))

(def graph
  (into {} (for [i (range (count cells))]
             (let [row (quot i cols)
                   col (rem i cols)
                   current (nth cells i)
                   L (dec i) R (inc i) T (- i cols) B (+ i cols)
                   LR-neigh (if (= col 0) [[R (nth cells R)]]
                                (if (= col (dec cols)) [[L (nth cells L)]]
                                    [[L (nth cells L)] [R (nth cells R)]]))
                   TB-neigh (if (= row 0) [[B (nth cells B)]]
                                (if (= row (dec rows)) [[T (nth cells T)]]
                                    [[T (nth cells T)] [B (nth cells B)]]))
                   neigh (concat LR-neigh TB-neigh)]
               [i (into {} (map (fn [n] {(first n) 1}) (filter (fn [n] (< (- (second n) current) 2)) neigh)))]))))

(def ^:private inf (Long/MAX_VALUE))

(defn neighbors
  "Returns n's neighbors, optionally filtered if unvisited"
  ([g n] (get g n {}))
  ([g n uv] (select-keys (neighbors g n) uv)))

(defn update-costs
  "Returns costs updated with any shorter paths found to curr's unvisisted
  neighbors by using curr's shortest path"
  [g costs curr unvisited]
  (let [curr-cost (costs curr)]
    (reduce
     (fn [c [nbr nbr-cost]] (update-in c [nbr] (partial min (+ curr-cost nbr-cost))))
     costs
     (neighbors g curr unvisited))))

(defn dijkstra
  "Returns a mapping of nodes to minimum cost from src using Dijkstra algorithm.
  Graph is a mapping of nodes to map of neighboring nodes and associated cost.
  Optionally, specify :target node to return only the min price for target"
  [g src & {:keys [target]}]
  (loop [costs (assoc (zipmap (keys g) (repeat inf)) src 0)
         curr src
         unvisited (disj (apply hash-set (keys g)) src)]
    (if (or (empty? unvisited) (= inf (costs curr)))
      costs
      (let [costs' (update-costs g costs curr unvisited)
            curr' (first (sort-by costs' unvisited))]
        (if (= target curr)
          (costs' target)
          (recur costs'
                 curr'
                 (disj unvisited curr')))))))

(defn -main []
  (print (dijkstra graph start {:target target})))

(comment
  (-main))

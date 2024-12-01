(ns aoc.day22 
  (:require
    [clojure.string :as str]))

(defn read-lines []
  (->> (slurp "data/input22.txt")
       (str/split-lines)))

;; cell-state: 0 - clean, 1 - weakened, 2 - infected, 3 - flagged
(def init-grid
  (->> (for [[y line] (map-indexed vector (read-lines))
             [x ch] (map-indexed vector line)
             :when (= \# ch)]
         [[x y] 2])
       (into {})))

;; directions: up, right, down, left
(def dirs [[0 -1] [1 0] [0 1] [-1 0]])

(def init-state
  {:xy [12 12]
   :di 0
   :grid init-grid
   :infections 0})

;; cell-state: 0 - clean, 1 - weakened, 2 - infected, 3 - flagged
(defn burst [{:keys [xy di grid infections] :as state}]
  (let [infected? (= 2 (grid xy 0))
        grid (assoc grid xy (if infected? 0 2))
        infections (if infected? infections (inc infections))
        di (mod (if infected? (inc di) (dec di)) 4)
        xy (map + xy (dirs di))]
    (assoc state :xy xy :di di :grid grid :infections infections)))

(defn part1 []
  (:infections (nth (iterate burst init-state) 10000)))

;; cell-state: 0 - clean, 1 - weakened, 2 - infected, 3 - flagged
(defn burst-p2 [{:keys [xy di grid infections] :as state}]
  (let [cell-state (grid xy 0)
        infections (if (= 1 cell-state) (inc infections) infections)
        grid (assoc grid xy (mod (inc cell-state) 4))
        di (-> (case cell-state 0 (dec di) 1 di 2 (inc di) 3 (+ 2 di)) (mod 4))
        xy (map + xy (dirs di))]
    (assoc state :xy xy :di di :grid grid :infections infections)))

(defn part2 []
  (:infections (nth (iterate burst-p2 init-state) 10000000)))

(comment
  (part1)
  (part2)
  (time (part2))) ; "Elapsed time: 6525.383833 msecs"

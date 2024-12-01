(ns aoc.day03)

(def input 325489)

(def state {:n 1 :xy [0 0] :dir [1 0] :maxx 0 :maxy 0 :minx 0 :miny 0})

(defn change-dir [{:keys [xy dir maxx maxy minx miny] :as state}]
  (let [[x y] xy]
    (cond
      (and (= dir [1 0]) (> x maxx))
      (assoc state :maxx x :dir [0 1])
      (and (= dir [0 1]) (> y maxy))
      (assoc state :maxy y :dir [-1 0])
      (and (= dir [-1 0]) (< x minx))
      (assoc state :minx x :dir [0 -1])
      (and (= dir [0 -1]) (< y miny))
      (assoc state :miny y :dir [1 0])
      :else state)))

(defn next-state [{:keys [n xy dir] :as state}]
  (-> state
      (assoc :n (inc n))
      (assoc :xy (mapv + xy dir))
      (change-dir)))

(defn part1 []
  (->> (nth (iterate next-state state) (dec input))
       :xy
       (map abs)
       (apply +)))

(defn sum-neighbours [cells {:keys [xy] :as _state}]
  (->> (map (fn [dir xy] (get cells (map + xy dir) 0))
            [[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]]
            (repeat xy))
       (reduce +)))

(defn bigger-than-input [cells {:keys [xy] :as state}]
  (let [n (sum-neighbours cells state)]
    (if (> n input)
      (reduced n)
      (assoc cells xy n))))

(defn part2 []
  (reduce bigger-than-input {[0 0] 1} (iterate next-state (next-state state))))

(comment
  (part1)
  (part2))

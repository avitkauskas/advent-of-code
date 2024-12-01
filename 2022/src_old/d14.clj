(ns coa.d14
  (:require
   [clojure.string :as s]))

(def input
  (->> (slurp "input/input14.txt")
       (s/split-lines)
       (map #(s/split % #" -> "))
       (map #(map (fn [p] (s/split p #",")) %))
       (map #(map (fn [n] (map read-string n)) %))))

(defn- add-line [w [[x1 y1] [x2 y2]]]
  (if (= x1 x2)
    (conj w (reduce #(conj %1 {[x1 %2] \w}) {} (range (min y1 y2) (max (inc y1) (inc y2)))))
    (conj w (reduce #(conj %1 {[%2 y1] \w}) {} (range (min x1 x2) (max (inc x1) (inc x2)))))))

(defn- add-wall [w points]
  (let [pairs (partition 2 1 points)]
    (conj w (reduce add-line {} pairs))))

(def sand-start [500 0])
(def bottom-y (+ 2 (apply max (map second (apply concat input)))))

(defn- move [{:keys [_ field] :as info} [x y]]
  (if (and (contains? field [499 1])
           (contains? field [500 1])
           (contains? field [501 1]))
    (assoc info :res "done")
    (if (or (= y (dec bottom-y))
            (and (contains? field [(dec x) (inc y)])
                 (contains? field [x (inc y)])
                 (contains? field [(inc x) (inc y)])))
      (assoc-in info [:field [x y]] \s)
      (if (not (contains? field [x (inc y)]))
        (move info [x (inc y)])
        (if (not (contains? field [(dec x) (inc y)]))
          (move info [(dec x) (inc y)])
          (move info [(inc x) (inc y)]))))))

(defn -main []
  (let [field (reduce add-wall {} input)
        init-info {:res "" :field field}]
    (loop [info (move init-info sand-start)]
      (if (= (:res info) "done")
        (inc (count (filter #(= (second %) \s) (:field info))))
        (recur (move info sand-start))))))

(comment
  (prn (-main)))

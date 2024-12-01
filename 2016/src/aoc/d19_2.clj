(ns aoc.d19-2
  (:require
   [data.deque :as dq]))

(def n 3012210)

(def split-point (quot (inc n) 2))

(def start {:left (apply dq/deque (range split-point 0 -1))
            :right (apply dq/deque (range n split-point -1))})

(defn remove-across [{:keys [left right] :as q}]
  (if (> (count left) (count right))
    (->> left
         (dq/remove-last)
         (assoc q :left))
    (->> right
         (dq/remove-first)
         (assoc q :right))))

(defn rotate [{:keys [left right] :as q}]
  (if (empty? right) q
    (let [first-left (dq/peek-first left)
          first-right (dq/peek-first right)]
      (assoc q
             :left (-> left (dq/remove-first) (dq/add-last first-right))
             :right (-> right (dq/remove-first) (dq/add-last first-left))))))

(defn -main []
  (->> (iterate #(-> % remove-across rotate) start)
       (take-while #(seq (:right %)))
       (last)
       (:left)
       (dq/peek-first)))

(comment
  (time (prn (-main))))

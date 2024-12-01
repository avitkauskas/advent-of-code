(ns aoc.d11-2
  (:require
   [clojure.math.combinatorics :as combo]))

(def queue (clojure.lang.PersistentQueue/EMPTY))

; microchips - positive numbers
; generators - corresponding negative numbers
; :e - elevator floor, :m - moves so far

(def init-state
  {:e 1
   1 #{1 -1 6 -6 7 -7}
   2 #{-2 -3 -4 -5}
   3 #{2 3 4 5}
   4 #{}
   :m 0})

(def goal #{1 2 3 4 5 6 7 -1 -2 -3 -4 -5 -6 -7})

(defn normalised-state [state]
  (->> state
       (map #(condp = (first %)
               :e %
               :m nil
               [(first %) [(count (filter pos? (second %)))
                           (count (filter neg? (second %)))]]))
       (into {})))

(defn goal-reached? [state]
  (when (= goal (state 4)) state))

(defn not-in [seen-states states]
  (filter #(not-any? #{(normalised-state %)} seen-states) states))

(defn safe-combination? [floor]
  (or (empty? floor) 
      (every? pos? floor)
      (every? neg? floor)
      (every? (into #{} (filter neg? floor)) (map - (filter pos? floor)))))

(defn new-states [state seen-states]
  (let [current-floor (:e state)
        current-floor-elems (state current-floor)
        all-combinations (concat (combo/combinations (seq current-floor-elems) 1)
                                 (combo/combinations (seq current-floor-elems) 2))]
    (->> (for [direction [1 -1]
               :let [new-floor (+ current-floor direction)]
               :when (<= 1 new-floor 4)
               :let [new-floor-elems (state new-floor)]
               elems-to-move all-combinations
               :let [modified-current-floor-elems (apply disj current-floor-elems elems-to-move)
                     modified-new-floor-elems (apply conj new-floor-elems elems-to-move)]
               :when (and (safe-combination? modified-current-floor-elems)
                          (safe-combination? modified-new-floor-elems))]
           (-> state
               (assoc current-floor modified-current-floor-elems)
               (assoc new-floor modified-new-floor-elems)
               (assoc :e new-floor)
               (assoc :m (inc (:m state)))))
         (not-in seen-states))))

(defn -main []
  (loop [state-queue (conj queue init-state)
         seen-states (conj #{} (normalised-state init-state))]
    (if (empty? state-queue)
      "Goal is not reachable."
      (let [current-state (peek state-queue)]
        (if (goal-reached? current-state)
          (:m current-state)
          (let [next-states (new-states current-state seen-states)]
            (recur (apply conj (pop state-queue) next-states)
                   (apply conj seen-states (map normalised-state next-states)))))))))

(comment
  (-main))

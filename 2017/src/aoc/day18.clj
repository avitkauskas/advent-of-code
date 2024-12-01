(ns aoc.day18
  (:require [clojure.string :as str]))

(def program
  (->> (slurp "data/input18.txt")
       (str/split-lines)
       (map #(str "[" % "]"))
       (mapv read-string)))

(def init-state '{:ptr 0 :snd nil :rcv nil})

(defn get-value [state param]
  (if (symbol? param) (state param 0) param))

(defmulti execute (fn [_state [cmd & _]] cmd))

(defmethod execute 'snd [state [_ v]]
  (-> state
      (assoc :snd (get-value state v))
      (update :ptr inc)))

(defmethod execute 'set [state [_ r v]]
  (-> state
      (assoc r (get-value state v))
      (update :ptr inc)))

(defmethod execute 'add [state [_ r v]]
  (-> state
      (assoc r (+ (get-value state r) (get-value state v)))
      (update :ptr inc)))

(defmethod execute 'mul [state [_ r v]]
  (-> state
      (assoc r (* (get-value state r) (get-value state v)))
      (update :ptr inc)))

(defmethod execute 'mod [state [_ r v]]
  (-> state
      (assoc r (rem (get-value state r) (get-value state v)))
      (update :ptr inc)))

(defmethod execute 'rcv [state [_ v]]
  (cond-> state
      (not (zero? (get-value state v))) (assoc :rcv (state :snd))
      :always (update :ptr inc)))

(defmethod execute 'jgz [state [_ r v]]
  (if (pos? (get-value state r))
    (update state :ptr #(+ % (get-value state v)))
    (update state :ptr inc)))

(defn part1 []
  (loop [state init-state]
    (if-let [[cmd :as instruction] (get program (:ptr state))]
      (if (= cmd 'rcv)
        (:rcv (execute state instruction))
        (recur (execute state instruction)))
      "Exited without 'rcv call.")))

(comment
  (part1))

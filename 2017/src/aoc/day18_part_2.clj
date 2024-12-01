(ns aoc.day18-part-2
  (:require [clojure.string :as str]))

(def queue (clojure.lang.PersistentQueue/EMPTY))

(def q-0 (atom queue))
(def q-1 (atom queue))

(def program
  (->> (slurp "data/input18.txt")
       (str/split-lines)
       (map #(str "[" % "]"))
       (mapv read-string)))

(defn get-value [state param]
  (if (symbol? param) (state param 0) param))

(defmulti execute (fn [_state [cmd & _]] cmd))

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

(defmethod execute 'jgz [state [_ r v]]
  (if (pos? (get-value state r))
    (update state :ptr #(+ % (get-value state v)))
    (update state :ptr inc)))

(defmethod execute 'snd [state [_ v]]
  (if (= 0 (:id state))
    (swap! q-1 conj (get-value state v))
    (swap! q-0 conj (get-value state v)))
  (update state :ptr inc))

(defmethod execute 'rcv [state [_ r]]
  (if-let [v (if (= 0 (:id state)) (peek @q-0) (peek @q-1))]
    (do
      (if (= 0 (:id state))
        (swap! q-0 pop)
        (swap! q-1 pop))
      (-> state
          (assoc :stop false)
          (assoc r v)
          (update :ptr inc)))
    (assoc state :stop true)))

(def init-st-0 '{:id 0 :ptr 0 :stop false p 0})
(def init-st-1 '{:id 1 :ptr 0 :stop false p 1})

(defn part2 []
  (loop [st-0 init-st-0
         st-1 init-st-1
         snds 0]
    (if (and (:stop st-0) (:stop st-1))
      snds
      (let [instr-0 (get program (:ptr st-0))
            st-0 (if-not instr-0
                   (assoc st-0 :stop true)
                   (execute st-0 instr-0))
            instr-1 (get program (:ptr st-1))
            st-1 (if-not instr-1
                   (assoc st-1 :stop true)
                   (execute st-1 instr-1))
            snds (if (and instr-1 (= 'snd (first instr-1)))
                   (inc snds) snds)]
        (recur st-0 st-1 snds)))))

(comment
  (part2))

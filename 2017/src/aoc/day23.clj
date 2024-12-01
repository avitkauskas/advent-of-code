(ns aoc.day23
  (:require
   [clojure.string :as str]
   [clojure.math :as math]))

(def program
  (->> (slurp "data/input23.txt")
       (str/split-lines)
       (map #(str "[" % "]"))
       (mapv read-string)))

(def init-state '{:ptr 0 :mul 0})

(defn get-value [state param]
  (if (symbol? param) (state param 0) param))

(defmulti execute (fn [_state [cmd & _]] cmd))

(defmethod execute 'set [state [_ r v]]
  (-> state
      (assoc r (get-value state v))
      (update :ptr inc)))

(defmethod execute 'sub [state [_ r v]]
  (-> state
      (assoc r (- (get-value state r) (get-value state v)))
      (update :ptr inc)))

(defmethod execute 'mul [state [_ r v]]
  (-> state
      (assoc r (* (get-value state r) (get-value state v)))
      (update :ptr inc)))

(defmethod execute 'jnz [state [_ r v]]
  (if (zero? (get-value state r))
    (update state :ptr inc)
    (update state :ptr #(+ % (get-value state v)))))

(defn part1 []
  (loop [state init-state]
    (if-let [[cmd :as instruction] (get program (:ptr state))]
      (if (= cmd 'mul)
        (recur (execute (update state :mul inc) instruction))
        (recur (execute state instruction)))
      (:mul state))))

;; In part 2, register h is incremented only when
;; a number between 106500 and 123500 (inclusive)
;; going in increments of 17 is non-prime.
;; So we have to find the number of these non-primes.

(defn non-prime? [n]
  (let [root (int (math/sqrt n))]
    (loop [i 2]
      (if (zero? (rem n i))
        true
        (if (= i root)
          false
          (recur (inc i)))))))

(defn part2 []
  (->> (range 106500 (inc 123500) 17)
       (filter non-prime?)
       count))

(comment
  (part1)
  (part2))

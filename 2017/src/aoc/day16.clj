(ns aoc.day16 
  (:require
    [clojure.string :as str]))

(set! *warn-on-reflection* true)

(defn- parse-move [s]
  (let [[cmd & params] (vec s)]
    (case cmd
      \s [\s (parse-long (apply str params))]
      \p (let [[c1 _ c2] params]
           [\p c1 c2])
      \x (let [[p1 p2] (split-with #(not= \/ %) params)
               n1 (parse-long (apply str p1))
               n2 (parse-long (apply str (rest p2)))]
           [\x n1 n2]))))

(def input
  (as-> (slurp "data/input16.txt") $
      (str/trim-newline $)
      (str/split $ #",")
      (mapv #(parse-move %) $)))

(def init-state (vec "abcdefghijklmnop"))

(defn choose-move [_state [move & _params]] move)

(defmulti move choose-move)

(defmethod move \s
  ; "move n elements from the end to the front"
  [state [_ n]]
  (into [] (concat (take-last n state) (drop-last n state))))

(defmethod move \x
  ; swap positions n1 and n2
  [state [_ n1 n2]]
  (assoc state n1 (state n2) n2 (state n1)))

(defmethod move \p
  ; swap elements p1 and p2
  [^clojure.lang.APersistentVector state [_ p1 p2]]
  (let [n1 (.indexOf state p1)
        n2 (.indexOf state p2)]
    (assoc state n1 p2 n2 p1)))
  
(defn dance [state]
  (reduce move state input))

(defn part1 []
  (apply str (dance init-state)))
  
(def total-dances 1000000000)

(defn part2 []
  (loop [i 1
         state (dance init-state)]
    (if (= i total-dances)
      (apply str state)
      (if (= state init-state)
        (recur (inc (* i ^long (quot total-dances i))) (dance state))
        (recur (inc i) (dance state))))))

(comment
  (part1)
  (part2))

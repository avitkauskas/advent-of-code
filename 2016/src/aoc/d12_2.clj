(ns aoc.d12-2
  (:require
    [clojure.string :as s]))

(defn parse-input []
  (->> (slurp "data/input12.txt")
       (s/split-lines)
       (map #(str "[" % "]"))
       (map read-string)
       (vec)))

(defn parse-param [state param]
  (if (symbol? param) (state param) param))

(defmulti execute (fn [_state [cmd & _args]] cmd))

(defmethod execute 'inc [state [_cmd reg]]
  (-> state
      (update reg inc)
      (update :ptr inc)))

(defmethod execute 'dec [state [_cmd reg]]
  (-> state
      (update reg dec)
      (update :ptr inc)))

(defmethod execute 'cpy [state [_cmd param reg]]
  (let [arg (parse-param state param)]
    (-> state
        (assoc reg arg)
        (update :ptr inc))))

(defmethod execute 'jnz [state [_cmd param step]]
  (let [arg (parse-param state param)]
    (update state :ptr + (if-not (zero? arg) step 1))))

(def program (parse-input))

(def init-state '{:ptr 0 a 0 b 0 c 1 d 0})

(defn -main []
  (loop [state init-state]
    (let [cmd (get program (:ptr state))]
      (if-not cmd
        (get state 'a)
        (recur (execute state cmd))))))

(comment
  (-main))

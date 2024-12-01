(ns aoc.d25-1
  (:require
    [clojure.string :as s]))

(defn parse-input []
  (->> (slurp "data/input25.txt")
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

(defmethod execute 'out [state [_cmd param]]
  (let [arg (parse-param state param)]
    (-> state
        (assoc :res (conj (:res state) arg))
        (update :ptr inc))))

(def program (parse-input))

(def init-state '{:ptr 0 a 0 b 0 c 0 d 0 :res []})

(defn try-value [_acc n]
  (loop [state (assoc init-state 'a n)]
    (let [res (:res state)]
      (if (= 12 (count res))
        (when (= [0 1 0 1 0 1 0 1 0 1 0 1] res)
          (reduced {n res}))
        (let [cmd (get program (:ptr state))]
          (recur (execute state cmd)))))))

(defn -main []
  (reduce try-value (range)))

(comment
  (-main))


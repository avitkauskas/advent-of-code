(ns aoc.d23-1
  (:require
    [clojure.string :as s]))

(defn parse-input []
  (->> (slurp "data/input23.txt")
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
  (if-not (symbol? reg)
    (update state :ptr inc)
    (let [arg (parse-param state param)]
      (-> state
          (assoc reg arg)
          (update :ptr inc)))))

(defmethod execute 'jnz [state [_cmd par1 par2]]
  (let [arg (parse-param state par1)
        step (parse-param state par2)]
    (update state :ptr + (if-not (zero? arg) step 1))))

(defn toggle [state ptr]
  (let [prg (:prg state)]
    (if-let [[cmd & args] (get prg ptr)]
      (let [alt (case (count args)
                  1 (if (= 'inc cmd)
                      (vec (cons 'dec args))
                      (vec (cons 'inc args)))
                  2 (if (= 'jnz cmd)
                      (vec (cons 'cpy args))
                      (vec (cons 'jnz args))))]
        (->> (assoc prg ptr alt)
             (assoc state :prg)))
      state)))

(defmethod execute 'tgl [state [_cmd param]]
  (let [arg (parse-param state param)
        ptr (+ (:ptr state) arg)]
    (-> state
        (toggle ptr)
        (update :ptr inc))))

(def init-state (-> '{:ptr 0 a 7 b 0 c 0 d 0}
                    (assoc :prg (parse-input))))

(defn -main []
  (loop [state init-state]
    (if-let [cmd (get (:prg state) (:ptr state))]
      (recur (execute state cmd))
      (get state 'a))))

(comment
  (time (-main)))

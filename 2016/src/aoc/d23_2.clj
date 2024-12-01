(ns aoc.d23-2
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

(defmethod execute 'mul [state [_cmd]]
  (-> state
      (update 'a + (* (get state 'c) (get state 'd)))
      (assoc 'c 0)
      (assoc 'd 0)
      (update :ptr + 5)))

(def init-state (-> '{:ptr 0 a 12 b 0 c 0 d 0}
                    (assoc :prg (parse-input))))

(def multiply-prg [['inc 'a] ['dec 'c] ['jnz 'c -2] ['dec 'd] ['jnz 'd -5]])

(defn -main []
  (loop [state init-state]
    (if-let [cmd (get (:prg state) (:ptr state))]
      (if (= multiply-prg (->> (:prg state) (drop (:ptr state)) (take 5)))
        (recur (execute state ['mul]))
        (recur (execute state cmd)))
      (get state 'a))))

(comment
  (-main))

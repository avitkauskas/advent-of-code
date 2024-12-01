(ns coa.d9
  (:require
   [clojure.string :as s]))

(defn- get-input [file-name]
  (->> (slurp file-name)
       s/split-lines
       (map #(s/split % #" "))
       (map (fn [e] [(first e) (read-string (second e))]))
       ))

(def commands (get-input "input/input9.txt"))

(defn- move [direction [x y]]
  (case direction
    "R" [(inc x) y]
    "L" [(dec x) y]
    "U" [x (inc y)]
    "D" [x (dec y)]))

(defn- is-too-far? [[Hx Hy] [Tx Ty]]
  (or (> (abs (- Hx Tx)) 1)
      (> (abs (- Hy Ty)) 1)))

(defn- go [state command]
  (let [[direction times] command
        state (if (> times 1) (go state [direction (dec times)]) state)
        {:keys [H T V]} state
        H-new (move direction H)
        T-new (if (is-too-far? H-new T) H T)
        V-new (conj V T-new)]
    {:H H-new :T T-new :V V-new}))

(defn -main []
  (println (count (:V (reduce go {:H [0 0] :T [0 0] :V #{}} commands)))))

(comment
  (-main))

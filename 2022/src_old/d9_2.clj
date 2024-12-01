(ns coa.d9-2
  (:require
   [clojure.string :as s]
   [clojure.core.matrix :as m]))

(defn- get-input [file-name]
  (->> (slurp file-name)
       s/split-lines
       (map #(s/split % #" "))
       (map (fn [e] [(first e) (read-string (second e))]))))

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

(defn- follow [[Hx Hy] [Tx Ty]]
  (if (is-too-far? [Hx Hy] [Tx Ty])
    (m/add [Tx Ty] (m/clamp [(- Hx Tx) (- Hy Ty)] -1 1))
    [Tx Ty]))

(defn- go [state command]
  (let [[direction times] command
        state (if (> times 1) (go state [direction (dec times)]) state)
        {:keys [H N1 N2 N3 N4 N5 N6 N7 N8 N9 V]} state
        H-new (move direction H)
        N1-new (follow H-new N1)
        N2-new (follow N1-new N2)
        N3-new (follow N2-new N3)
        N4-new (follow N3-new N4)
        N5-new (follow N4-new N5)
        N6-new (follow N5-new N6)
        N7-new (follow N6-new N7)
        N8-new (follow N7-new N8)
        N9-new (follow N8-new N9)
        V-new (conj V N9-new)]
    {:H H-new :N1 N1-new :N2 N2-new :N3 N3-new :N4 N4-new :N5 N5-new
     :N6 N6-new :N7 N7-new :N8 N8-new :N9 N9-new :V V-new}))

(defn -main []
  (count
   (:V (reduce go
               {:H [0 0] :N1 [0 0] :N2 [0 0] :N3 [0 0] :N4 [0 0] :N5 [0 0]
                :N6 [0 0] :N7 [0 0] :N8 [0 0] :N9 [0 0] :V #{}}
               commands))))

(comment
  (-main))

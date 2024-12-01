(ns coa.d15-2
  (:require
   [clojure.string :as str]))

(def input
  (->> (slurp "input/input15.txt")
       (str/split-lines)
       (map #(str/replace % #"[^-:,\d]" ""))
       (map #(str/split % #":"))
       (map #(map (fn [p] (str/split p #",")) %))
       (map #(map (fn [n] (map read-string n)) %))
       (map (fn [[[sx sy] [bx by]]] [[sx sy] [bx by] (+ (abs (- sx bx)) (abs (- sy by)))]))))

(defn sensor-edges []
  (mapcat identity
          (for [[[sx sy] [_ _] d] input]
            (lazy-cat
             (map (fn [x y] [x y]) (range sx (+ sx d 2)) (range (- sy d 1) (+ sy 1)))
             (map (fn [x y] [x y]) (range (+ sx d) sx -1) (range (+ sy 1) (+ sy d 1)))
             (map (fn [x y] [x y]) (range sx (- sx d 1) -1) (range (+ sy d 1) sy -1))
             (map (fn [x y] [x y]) (range (- sx d 1) sx) (range sy (- sy d 1) -1))))))

(defn is-covered [[x y]]
  (or (< x 0) (> x 4000000) (< y 0) (> y 4000000) 
      (some true? (map (fn [[[sx sy] [_ _] d]] (<= (+ (abs (- sx x)) (abs (- sy y))) d)) input))))

; (defn -main []
;   (let [beacon (some #(if (not (is-covered %)) %) (sensor-edges))]
;     (->> beacon first (* 4000000) (+ (second beacon)))))

(defn -main []
  (loop [edge (first (sensor-edges))
         other (next (sensor-edges))]
    (if (not (is-covered edge))
      (->> edge first (* 4000000) (+ (second edge)))
      (recur (first other) (next other)))))

(comment
  (prn (-main))
  (count (sensor-edges))
  )

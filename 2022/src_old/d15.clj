(ns coa.d15
  (:require
   [clojure.string :as str]
   [clojure.set :as set]))

(def input
  (->> (slurp "input/input15.txt")
       (str/split-lines)
       (map #(str/replace % #"[^-:,\d]" ""))
       (map #(str/split % #":"))
       (map #(map (fn [p] (str/split p #",")) %))
       (map #(map (fn [n] (map read-string n)) %))
       (map (fn [[[sx sy] [bx by]]] [[sx sy] [bx by] (+ (abs (- sx bx)) (abs (- sy by)))]))))

(def line 2000002)

(defn sensor-area [[bx by] d]
  (concat
   (for [y (range (- by d) (+ by 1))
         x (range (- bx (- d (- by y))) (+ bx (- d (- by y)) 1))]
     [x y])
   (for [y (range (+ by 1) (+ by d 1))
         x (range (- bx (- (+ by d) y)) (+ bx (- (+ by d) y) 1))]
     [x y])))

(defn intersection [[x y] d ly]
  (let [dist (abs (- ly y))]
    (if (> dist d) #{}
        (set (range (- x (- d dist)) (+ x (- d dist) 1))))))

(defn collect-intersections [s [[x y] [_ _] d]]
  (set/union s (intersection [x y] d line)))

(defn object-x [s [[sx sy] [bx by] _]]
  (set/union s 
             ; (if (= sy line)
             ;     #{sx} 
                 (if (= by line)
                         #{bx} #{})
               ; )
             ))

(defn objects-x-on-line [input]
  (reduce object-x #{} input))

(defn -main []
  ; (count (set/difference (reduce collect-intersections #{} input)
  ;                        ; (objects-x-on-line input))
  (count (set/difference (set (range 0 4000001))
          (reduce collect-intersections #{} input)
          )))
  ; (count (reduce collect-intersections #{} input)))

(comment
  (prn (-main))
  (prn input)
  (prn (objects-x-on-line input))
  (prn (sensor-area [15 20] 1))
  )

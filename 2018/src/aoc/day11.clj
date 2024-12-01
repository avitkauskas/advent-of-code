(ns aoc.day11)

(set! *warn-on-reflection* true)

(def grid-serial 7803)
(def grid-size 300)

(defn cell-power [r c]
  (-> (+ (inc c) 10)
      (* (inc r))
      (+ grid-serial)
      (* (+ (inc c) 10))
      (quot 100)
      (rem 10)
      (- 5)))

(def grid (make-array Long/TYPE grid-size grid-size))

(defn fill-grid []
  (dotimes [r grid-size]
    (dotimes [c grid-size]
      (aset ^longs (aget grid r) c (cell-power r c)))))

(defn square-power [row col size]
  (loop [r row
         c col
         power 0]
    (if (= r (+ row size))
      power
      (if (= c (+ col size))
        (recur (inc r) col power)
        (recur r (inc c) (+ power (aget ^longs (aget grid r) c)))))))

(defn max-power [size]
  (loop [r 0
         c 0
         res {:r 0 :c 0 :pow (square-power 0 0 size)}]
    (if (> r (- grid-size size))
      [(inc (:c res)) (inc (:r res)) (:pow res)]
      (if (> c (- grid-size size))
        (recur (inc r) 0 res)
        (let [power (square-power r c size)]
          (if (> power (:pow res))
            (recur r (inc c) {:r r :c c :pow power})
            (recur r (inc c) res)))))))

(defn part-1 []
  (max-power 3))

(defn part-2 []
  (loop [s 1
         max-row 1
         max-col 1
         max-size 1
         max-pow (square-power 0 0 1)]
    (if (> s grid-size)
      [max-col max-row max-size max-pow]
      (let [[c r power] (max-power s)]
        (println "size:" s "pow:" power)
        (if (< power 0)
          [max-col max-row max-size max-pow]
          (if (> power max-pow)
            (recur (inc s) r c s power)
            (recur (inc s) max-row max-col max-size max-pow)))))))

(defn -main []
  (fill-grid)
  (part-1)
  (part-2))

(comment
  (fill-grid)
  (part-1)
  (part-2))

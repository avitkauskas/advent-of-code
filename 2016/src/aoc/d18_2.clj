(ns aoc.d18-2)

(def input ".^.^..^......^^^^^...^^^...^...^....^^.^...^.^^^^....^...^^.^^^...^^^^.^^.^.^^..^.^^^..^^^^^^.^^^..^")
(def total-rows 400000)

;; 1 - safe tile (.), 0 - trapped tile (^)
(def first-row (mapv {\. 1 \^ 0} input))

(defn new-tile-type [tripplet]
  (if (#{[0 0 1] [1 0 0] [0 1 1] [1 1 0]} tripplet) 0 1))

(defn next-row [row]
  (->> (concat [1] row [1])
       (partition 3 1)
       (mapv new-tile-type)))

(defn -main []
  (loop [row first-row
         safe-tiles 0
         i 0]
    (if (= i total-rows)
      safe-tiles
      (recur (next-row row)
             (+ safe-tiles (apply + row))
             (inc i)))))

(comment
  (time (-main)))
  

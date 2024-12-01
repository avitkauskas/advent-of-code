(ns aoc.d18-1)

(def input ".^.^..^......^^^^^...^^^...^...^....^^.^...^.^^^^....^...^^.^^^...^^^^.^^.^.^^..^.^^^..^^^^^^.^^^..^")
(def total-rows 40)

;; 1 - safe tile, 0 - trapped tile
(def first-row (mapv {\. 1 \^ 0} input))

(defn new-tile-type [tripplet]
  (if (some #{tripplet} #{[0 0 1] [1 0 0] [0 1 1] [1 1 0]}) 0 1))

(defn add-next-row [rows i]
  (->> (concat [1] (rows i) [1])
       (partition 3 1)
       (mapv new-tile-type)
       (conj rows)))

(defn -main []
  (loop [rows [first-row]
         i 0]
    (if (= i (dec total-rows))
      (->> rows
           (map #(apply + %))
           (apply +))
      (recur (add-next-row rows i)
             (inc i)))))

(comment
  (-main))
  

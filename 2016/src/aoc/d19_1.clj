(ns aoc.d19-1)

(def n 3012210)
(def start (range 1 (inc n)))

(defn play-round [r]
  (if (zero? (rem (count r) 2))
    (take-nth 2 r)
    (rest (take-nth 2 r))))

(defn -main []
  (->> (iterate play-round start)
       (take-while seq)
       last
       first))

(comment
  (time (-main)))

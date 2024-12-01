(ns aoc.d15-1)

(def discs [7 13 3 5 17 19])
(def positions [0 0 2 2 0 7])
(def timed-pos (map + positions (map inc (range))))

(defn find-time [_ n]
  (when (every?
         zero?
         (as-> timed-pos $
           (map (partial + n) $)
           (map rem $ discs)))
    (reduced n)))

(defn -main []
  (reduce find-time nil (range)))

(comment
  (-main))

(ns coa.d13
  (:require
   [clojure.string :as s]))

(def input
  (->> ((comp #(s/split % #"\n+") slurp) "input/input13.txt")
       (map read-string)
       (concat [[[2]]] [[[6]]])))

(defn- cmpv [vx vy]
  (let [vx (or (and (vector? vy) (int? vx) [vx]) vx)
        vy (or (and (vector? vx) (int? vy) [vy]) vy)]
    (if (int? vx)
      (compare vx vy)
      (or (first (drop-while zero? (map cmpv vx vy)))
          (compare (count vx) (count vy))))))

(defn -main []
  (->> input
       (sort-by identity cmpv)
       (keep-indexed #(if (or (= [[2]] %2) (= [[6]] %2)) (inc %1) nil))
       (apply *)))

(comment
  (-main))

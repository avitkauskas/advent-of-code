(ns aoc.d01-1
  (:require [clojure.string :as str]))

(defn- move [{:keys [coeffs xy]} cmd]
  (let [turn (first cmd)
        steps (parse-long (subs cmd 1))
        coeffs (condp = turn
                 \R (concat (rest coeffs) [(first coeffs)])
                 \L (concat [(last coeffs)] (butlast coeffs))
                 coeffs)
        coeff (first coeffs)
        xy (mapv + xy [(* steps (first coeff)) (* steps (second coeff))])]
      {:coeffs coeffs :xy xy}))

(defn -main []
  (let [commands (-> (slurp "data/input01.txt")
                     (str/trim-newline)
                     (str/split #", "))
        coeffs [[0 1] [1 0] [0 -1] [-1 0]]
        destination (reduce
                     move
                     {:coeffs coeffs :xy [0 0]}
                     commands)]
    (print (apply + (map abs (:xy destination))))))

(comment
  (-main))

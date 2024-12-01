(ns aoc.d01-2
  (:require [clojure.string :as str]))

(defn- move [{:keys [coeffs xy visited]} cmd]
  (let [turn (first cmd)
        steps (parse-long (subs cmd 1))
        coeffs (condp = turn
                 \R (concat (rest coeffs) [(first coeffs)])
                 \L (concat [(last coeffs)] (butlast coeffs))
                 coeffs)
        coeff (first coeffs)
        xy (mapv + xy [(* steps (first coeff)) (* steps (second coeff))])]
    (if (some #{xy} visited)
      (reduced {:coeffs coeffs :xy xy :visited visited})
      {:coeffs coeffs :xy xy :visited (conj visited xy)})))

(defn- expand-commands [cmd]
  (let [turn (str (subs cmd 0 1) "1")
        steps (repeat (dec (parse-long (subs cmd 1))) "F1")]
    (conj steps turn)))

(defn -main []
  (let [commands (-> (slurp "data/input01.txt")
                     (str/trim-newline)
                     (str/split #", "))
        commands (flatten (map expand-commands commands))
        coeffs [[0 1] [1 0] [0 -1] [-1 0]]
        destination (reduce
                     move
                     {:coeffs coeffs :xy [0 0] :visited #{[0 0]}}
                     commands)]
    (print (apply + (map abs (:xy destination))))))

(comment
  (-main))

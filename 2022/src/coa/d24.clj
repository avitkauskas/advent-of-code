(ns coa.d24
  (:require
   [clojure.string :as s]))

(def lines
  (->> (slurp "input/test.txt")
       (s/split-lines)
       (map #(s/split % #""))
       vec))

(defn read-wind [lines wind]
    (for [x (range (count lines))
          y (range (count (first lines)))
          :when (= (get-in lines [y x]) wind)]
      [x y]))

(def L (read-wind lines "<"))
(def R (read-wind lines ">"))
(def U (read-wind lines "^"))
(def D (read-wind lines "v"))

(defn -main []
  lines
  )

(comment
  (prn (-main))
  )


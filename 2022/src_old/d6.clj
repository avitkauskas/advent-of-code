(ns coa.d6)

(def length-of-sequence 14)

(defn -main []
  (->> (slurp "input/input6.txt")
       (partition-all length-of-sequence 1)
       (take-while #(not (apply distinct? %)))
       count
       (+ length-of-sequence)
       println))

(comment
  (-main))

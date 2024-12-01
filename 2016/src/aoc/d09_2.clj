(ns aoc.d09-2
  (:require [clojure.string :as s]))

(defn get-string []
  (-> (slurp "data/input09.txt")
      (s/replace #"\s+" "")))

(defn expanded-length [input]
  (let [[head marker tail] (s/split input #"[\(\)]" 3)]
    (if (nil? marker) (count head)
        (let [[len mult] (map parse-long (s/split marker #"x"))
              region (subs tail 0 len)
              remaining (subs tail len)]
          (+ (count head) (* mult (expanded-length region)) (expanded-length remaining))))))
 
(defn -main []
  (expanded-length (get-string)))

(comment
  (-main))
  

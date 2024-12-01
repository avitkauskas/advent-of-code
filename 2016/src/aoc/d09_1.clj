(ns aoc.d09-1
  (:require [clojure.string :as s]))

(defn get-string []
  (-> (slurp "data/input09.txt")
      (s/replace #"\s+" "")))

(defn -main []
  (let [input (get-string)]
    (loop [remaining input
           out-len 0]
      (if (= "" remaining) out-len
          (let [[pre marker _] (s/split remaining #"[\(\)]" 3)
                marker-len (if (nil? marker) 0 (+ 2 (count marker)))
                [len mult] (if (nil? marker) [0 0]
                               (map parse-long (s/split marker #"x")))]
            (recur (subs remaining (+ (count pre) marker-len len))
                   (+ out-len (count pre) (* len mult))))))))

(comment
  (-main))
  

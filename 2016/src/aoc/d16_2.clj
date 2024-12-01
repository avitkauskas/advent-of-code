(ns aoc.d16-2
  (:require
   [clojure.string :as s]))

(def input "10001110011110000")
(def size 35651584)

(defn semi-random [input size]
  (loop [s input]
    (let [res (->> (s/reverse s)
                   (map {\1 \0 \0 \1})
                   (s/join)
                   (str s "0"))]
      (if (>= (count res) size)
        (subs res 0 size)
        (recur res)))))

(defn checksum [input]
  (loop [s input]
    (let [res (->> (partition 2 s)
                   (mapv (fn [[a b]] (if (= a b) \1 \0))))]
      (if (pos? (rem (count res) 2))
        (s/join res)
        (recur res)))))

(defn -main []
  (-> input
      (semi-random size)
      (checksum)))

(comment
  (time (-main)))

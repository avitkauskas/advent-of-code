(ns aoc.d16-1
  (:require
   [clojure.string :as s]))

(def input "10001110011110000")
(def size 272)

(defn semi-random [input size]
  (loop [s input]
    (let [res (->> (s/reverse s)
                   (map #(if (= \1 %) \0 \1))
                   (s/join)
                   (str s "0"))]
      (if (>= (count res) size)
        (subs res 0 size)
        (recur res)))))

(defn checksum [input]
  (loop [s input]
    (let [res (->> (partition 2 s)
                   (map #(if (= (first %) (second %)) \1 \0))
                   (s/join))]
      (if (pos? (rem (count res) 2))
        res
        (recur res)))))

(defn -main []
  (-> input
      (semi-random size)
      (checksum)))

(comment
  (-main))

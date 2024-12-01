(ns aoc.day15
  (:require
   [clojure.string :as str]))

(def init-values
  (->> (slurp "data/input15.txt")
       (str/split-lines)
       (map #(str/split % #"\s+"))
       (mapv #(parse-long (last %)))))

(def n-pairs-part1 40000000)
(def n-pairs-part2  5000000)

(def g1-factor 16807)
(def g2-factor 48271)
(def g1-multiplier 4)
(def g2-multiplier 8)
(def divisor 2147483647)

(defn make-values [pair]
  (->> (map * [g1-factor g2-factor] pair)
       (map #(rem % divisor))))

(defn count-equal [cnt pair]
  (if (apply = (map #(bit-and % (unchecked-dec (bit-shift-left 1 16))) pair))
    (inc cnt)
    cnt))

(defn part1 []
  (reduce count-equal 0 (take n-pairs-part1 (iterate make-values (make-values init-values)))))

(defn generator-v1 [factor number]
  (-> number (* factor) (rem divisor)))

(defn last16bits [number]
  (bit-and number (unchecked-dec (bit-shift-left 1 16))))

(defn part1-v2 []
  (let [g1 (partial generator-v1 g1-factor)
        g2 (partial generator-v1 g2-factor)]
    (loop [i 0
           cnt 0
           v1 (g1 (first init-values))
           v2 (g2 (second init-values))]
      (if (= i n-pairs-part1)
        cnt
        (recur (inc i)
               (if (= (last16bits v1) (last16bits v2)) (inc cnt) cnt)
               (g1 v1)
               (g2 v2))))))

(defn generator-v2 [factor multiplier number]
  (loop [n number]
    (let [nn (-> n (* factor) (rem divisor))]
      (if (zero? (rem nn multiplier)) nn (recur nn)))))

(defn part2 []
  (let [g1 (partial generator-v2 g1-factor g1-multiplier)
        g2 (partial generator-v2 g2-factor g2-multiplier)]
    (loop [i 0
           cnt 0
           v1 (g1 (first init-values))
           v2 (g2 (second init-values))]
      (if (= i n-pairs-part2)
        cnt
        (recur (inc i)
               (if (= (last16bits v1) (last16bits v2)) (inc cnt) cnt)
               (g1 v1)
               (g2 v2))))))

(comment
  (part1)
  (time (part1)) ; "Elapsed time: 15906.90225 msecs"
  (part1-v2)
  (dotimes [_ 10] (time (part1-v2))) ; "Elapsed time: 938.702291 msecs"
  (part2)
  (dotimes [_ 10] (time (part2)))) ; "Elapsed time: 828.500667 msecs"

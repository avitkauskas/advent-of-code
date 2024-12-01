(ns aoc.day20 
  (:require
    [clojure.string :as str]))

(def input
  (->> (slurp "data/input20.txt")
       (str/split-lines)
       (map #(str/replace % "<" "["))
       (map #(str/replace % ">" "]"))
       (map #(str/replace % #"(.)=" ":$1 "))
       (map #(str "{" % "}"))
       (map read-string)))

(defn acceleration [particle]
  (->> (:a particle)
       (map abs)
       (apply +)))

(defn min-acceleration []
  (->> (map acceleration input)
       (reduce min)))

(defn min-acceleration-particle []
  (-> (filter #(= (min-acceleration) (acceleration %)) input)
      (first)))

(defn part1 []
  (.indexOf input (min-acceleration-particle)))

(defn move [particle]
  (let [a (:a particle)
        v (mapv + (:v particle) a)
        p (mapv + (:p particle) v)]
    {:p p :v v :a a}))

(defn eliminate-colliding [particles]
  (into []
        (comp
         (remove (fn [[_ group]] (> (count group) 1)))
         (map (comp first second)))
        (group-by :p particles)))

(defn part2 []
  (loop [particles input
         n (count particles)
         no-change 0]
    (if (= no-change 100)
      n
      (let [particles (-> (map move particles) eliminate-colliding)
            new-n (count particles)
            no-change (if (= n new-n) (inc no-change) 0)]
        (recur particles new-n no-change)))))

(comment
  (part1)
  (part2))

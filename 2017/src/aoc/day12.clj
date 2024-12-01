(ns aoc.day12 
  (:require
    [clojure.string :as str]
    [clojure.set :as set]))

(def input
  (->> (slurp "data/input12.txt")
       (str/split-lines)
       (map #(str/split % #" <-> "))
       (map (fn [[pipe connections]]
              [(parse-long pipe)
               (into #{} (mapv parse-long (str/split connections #", ")))]))
       (into {})))

(def queue (clojure.lang.PersistentQueue/EMPTY))

(defn- group-of [program]
  (loop [queue (conj queue program)
           group #{program}]
      (if (empty? queue)
        group
        (let [program (peek queue)
              connections (input program)
              not-seen (set/difference connections group)]
          (recur (apply conj (pop queue) not-seen)
                 (set/union group not-seen))))))

(defn part1 []
  (count (group-of 0)))

(def all-programs (into #{} (map first input)))

(defn part2 []
  (loop [programs all-programs
         groups 0]
    (if (empty? programs)
      groups
      (let [program (first programs)
            group (group-of program)]
        (recur (set/difference programs group)
               (inc groups))))))

(comment
  (part1)
  (part2))

(ns aoc.day07
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input
  (->> (slurp "data/input07.txt")
       (str/split-lines)
       (map #(re-find #"(\S+) \((\d+)\)( -> (.*))*" %))
       (map (fn [[_ s n _ c]] [s (parse-long n) (when (some? c) (str/split c #", "))]))))

(defn- find-root []
  (let [with-children (filter #(seq (nth % 2)) input)
        parent-nodes (into #{} (map first with-children))
        all-children (into #{} (mapcat #(nth % 2) with-children))]
    (first (set/difference parent-nodes all-children))))

(defn part1 []
  (find-root))

(def input-map
  (into {} (map (fn [[s & c]] [s (vec c)]) input)))

(defn weight-with-children [node]
  (let [node-info (input-map node)
        node-weight (first node-info)
        children (second node-info)
        children-discs (map first (map input-map children))
        children-weights (map weight-with-children children)]
    (if (or (nil? children)
            (apply = children-weights))
      (apply + node-weight children-weights)
      (throw (ex-info "Unequal weights"
                      {:weights children-weights :discs children-discs})))))

(defn normal-weight [weights] 
  (->> weights frequencies (sort-by second) last first))

(defn part2 []
  (try (weight-with-children (find-root))
       (catch Exception e
         (let [data (ex-data e)
               weights (:weights data)
               discs (:discs data)
               normal-weight (normal-weight weights)
               wrong-weight (first (filter #(not= normal-weight %) weights))
               diff (- normal-weight wrong-weight)
               idx (.indexOf weights wrong-weight)
               wrong-disc (nth discs idx)]
           (+ wrong-disc diff)))))

(comment
  (part1)
  (part2))

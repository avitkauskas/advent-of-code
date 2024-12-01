(ns aoc.day04 
  (:require
    [clojure.string :as str]))

(def input
  (->> (slurp "data/input04.txt")
       str/split-lines
       sort
       (map #(re-seq #":(\d+)\] (.*)" %))
       (map #(map rest %))
       (map first)
       (map (fn [[a b]] [a (case b "falls asleep" :asleep "wakes up" :awake (re-find #"\d+" b))]))
       (map #(map (fn [s] (if (string? s) (parse-long s) s)) %))))

(defn register-asleep [{:keys [guard from table] :as acc} to]
  (let [sleep-info (table guard {})
        sleep-info (reduce (fn [info minute] (assoc info minute (inc (info minute 0))))
                           sleep-info
                           (range from to))
        table (assoc table guard sleep-info)]
    (assoc acc :table table)))

(defn collect-asleep [acc [minute status]]
  (case status
    :asleep (assoc acc :from minute)
    :awake (register-asleep acc minute)
    (assoc acc :guard status)))
  
(defn max-sleeping-quard-info [table]
  (apply max-key (fn [[_ sleep-minutes]] (apply + (map second sleep-minutes))) (vec table)))

(defn max-minute [minutes]
  (apply max-key second (vec minutes)))

(defn part-1-result-hash [[guard minutes]]
  (let [max-minute (first (max-minute minutes))]
    (* guard max-minute)))

(def asleep-table
  (:table (reduce collect-asleep {:guard nil :from nil :table {}} input)))

(defn part-1 []
  (->> asleep-table
       max-sleeping-quard-info
       part-1-result-hash))

(defn part-2-result-hash [[guard [minute _]]]
  (* guard minute))

(defn part-2 []
  (->> asleep-table
       (map (fn [[guard minutes]] [guard (max-minute minutes)]))
       (apply max-key #(second (second %)))
       part-2-result-hash))

(comment
  (part-1)
  (part-2))

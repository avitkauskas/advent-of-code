(ns aoc.d22-1 
  (:require
    [clojure.string :as s]))

(defn parse-num [s]
  (parse-long (subs s 0 (dec (count s)))))

(defn read-input []
  (->> (slurp "data/input22.txt")
       (s/split-lines)
       (drop 2)
       (map #(s/split % #"\s+"))
       (map #(drop 2 %))
       (map butlast)
       (map #(map parse-num %))))

(def grid (read-input))

(defn count-self-fitting [grid]
  (-> (filter #(>= (second %) (first %)) grid)
      (count)))

(def count-viable-nodes
  (memoize
   (fn [used]
     (-> (filter #(>= (second %) used) grid)
         (count)))))

(defn -main []
  (let [non-zero (filter #(pos? (first %)) grid)
        self-count (count-self-fitting non-zero)
        fits-count (->> (map first non-zero)
                        (map count-viable-nodes)
                        (reduce +))]
    (- fits-count self-count)))

(comment
  (-main))

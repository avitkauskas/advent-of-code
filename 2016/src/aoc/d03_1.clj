(ns aoc.d03-1
  (:require [clojure.string :as str]))

(defn- get-data [file-name]
  (->> (slurp file-name)
       (str/split-lines)
       (map str/trim)
       (map #(map parse-long (str/split % #"\s+")))))

(defn- triangle? [triad]
  (let [max-val (apply max triad)
        other (let [[a b] (split-with (partial not= max-val) triad)]
                (concat a (rest b)))]
    (< max-val (apply + other))))

(defn -main []
  (->> (get-data "data/input03.txt")
       (filter triangle?)
       (count)))

(comment
  (-main))

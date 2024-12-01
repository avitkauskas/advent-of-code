(ns aoc.day19 
  (:require
    [clojure.string :as str]))

(defn read-input []
  (->> (slurp "data/input19.txt")
       (str/split-lines)
       (map vec)))

(def maze
  (->> (for [[y row] (map-indexed vector (read-input))
             [x chr] (map-indexed vector row)
             :when (not= chr \space)]
         [[x y] (if (contains? #{\| \- \+} chr) \* chr)]) 
       (into {})))

(def dirs [[0 1] [0 -1] [1 0] [-1 0]])

(defn- find-start []
  (ffirst (filter (fn [[[_x y] _]] (= 0 y)) maze)))

(defn- next-dir [pos dir]
  (if (maze (mapv + pos dir))
    dir
    (-> (for [d dirs
              :when (maze (mapv + pos d))
              :when (not= d (mapv #(* -1 %) dir))]
          d)
        first)))

(defn part1&2 []
  (loop [pos (find-start)
         dir [0 1]
         letters []
         steps 1]
    (let [letters (if-not (= \* (maze pos)) (conj letters (maze pos)) letters)]
      (if-let [dir (next-dir pos dir)]
        (recur (mapv + pos dir) dir letters (inc steps))
        [(apply str letters) steps]))))

(comment
  (part1&2))

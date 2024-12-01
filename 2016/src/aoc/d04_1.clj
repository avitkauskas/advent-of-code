(ns aoc.d04-1
  (:require
   [clojure.string :as str]))

(defn- get-lines []
  (-> (slurp "data/input04.txt")
      (str/split-lines)))

(defn- parse-room [[room-name sector checksum]]
  [(str/replace room-name #"-" "") (parse-long sector) checksum])

(defn- parse-line [line]
  (->> line
       (re-matches #"([a-z-]+)([0-9]+)\[([a-z]+)\]")
       (rest)))

(defn- five-most-common [room-name]
  (->> (frequencies room-name)
       (sort-by key)
       (sort-by val >)
       (take 5)
       (map first)
       (apply str)))

(defn- real? [[room-name _ checksum]]
  (= (five-most-common room-name) checksum))

(defn -main []
  (->> (get-lines)
       (map parse-line)
       (map parse-room)
       (filter real?)
       (map second)
       (reduce +)))

(comment
  (-main))
  

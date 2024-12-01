(ns aoc.d04-2
  (:require
   [clojure.string :as str]))

(defn- get-lines []
  (-> (slurp "data/input04.txt")
      (str/split-lines)))

(defn- parse-room [[room-name sector checksum]]
  [room-name (parse-long sector) checksum])

(defn- parse-line [line]
  (->> line
       (re-matches #"([a-z-]+)([0-9]+)\[([a-z]+)\]")
       (rest)))

(defn- five-most-common [room-name]
  (->> (str/replace room-name #"-" "") 
       (frequencies) 
       (sort-by key)
       (sort-by val >)
       (take 5)
       (map first)
       (apply str)))

(defn- rotate [n ch]
  (if (= \- ch) " "
      (-> ch int (- 97) (+ n) (rem 26) (+ 97) char)))

(defn- decrypt-name [room-name sector]
  (->> room-name 
       (map #(rotate sector %))
       (apply str)
       (str/trim)))

(defn- decrypt [[room-name sector _]]
  [(decrypt-name room-name sector) sector])

(defn- real? [[room-name _ checksum]]
  (= (five-most-common room-name) checksum))

(defn -main []
  (->> (get-lines)
       (map parse-line)
       (map parse-room)
       (filter real?)
       (map decrypt)
       (filter #(boolean (re-find #"pole" (first %))))))

(comment
  (-main))
  

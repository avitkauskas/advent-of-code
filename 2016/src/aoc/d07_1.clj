(ns aoc.d07-1
  (:require
   [clojure.string :as s]))

(defn get-ips []
  (->> (slurp "data/input07.txt")
       (s/split-lines)
       (map #(s/split % #"[\[\]]"))))

(defn is-abba? [[a b c d :as quartet]]
  {:pre [(= 4 (count quartet))]}
  (and (not= a b) (= a d) (= b c)))

(defn has-abba? [ip-part]
  (->> (partition 4 1 ip-part)
       (some is-abba?)))

(defn supports-tls? [ip]
  (let [supernets (take-nth 2 ip)
        hypernets (take-nth 2 (rest ip))]
    (and (some has-abba? supernets)
         (not-any? has-abba? hypernets))))

(defn -main []
  (->> (get-ips)
       (filter supports-tls?)
       (count))) 

(comment
  (-main))
  

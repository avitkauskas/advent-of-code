(ns aoc.d07-2
  (:require
   [clojure.string :as s]))

(defn get-ips []
  (->> (slurp "data/input07.txt")
       (s/split-lines)
       (map #(s/split % #"[\[\]]"))))

(defn is-aba? [[a b c :as trio]]
  {:pre [(= 3 (count trio))]}
  (and (not= a b) (= a c)))

(defn aba->bab [[a b _ :as trio]]
  {:pre [(= 3 (count trio))]}
  (list b a b))

(defn get-aba [ip-part]
  (->> (partition 3 1 ip-part)
       (filter is-aba?)))

(defn get-all-aba [supernets]
  (->> supernets
       (map get-aba)
       (apply concat)))

(defn supports-ssl? [ip]
  (let [supernets (take-nth 2 ip)
        hypernets (take-nth 2 (rest ip))
        all-aba (get-all-aba supernets)
        all-bab (into #{} (map aba->bab all-aba))]
    (and (not-empty all-aba)
         (some all-bab (get-all-aba hypernets)))))

(defn -main []
  (->> (get-ips)
       (filter supports-ssl?)
       (count))) 

(comment
  (-main))
  

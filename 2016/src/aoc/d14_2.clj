(ns aoc.d14-2
  (:require
   [clj-commons.digest :as d]))

(def salt "ngcjuoqr")
(def idx-needed 64)

(defn stretch-hash-algo [idx]
  (let [initial (d/md5 (str salt idx))]
    (reduce (fn [h _] (d/md5 h)) initial (range 2016))))
  
(def stretch-hash (memoize stretch-hash-algo))

(defn contains-3 [s]
  (second (re-find #"(.)(\1)(\1)" s)))

(defn contains-5 [symb _acc idx]
  (let [s (stretch-hash idx)]
    (if (re-find (re-pattern (str symb "{5}")) s)
      (reduced true)
      false)))

(defn next-1000-contains-5 [symb idx]
  (let [next-idx (inc idx)]
    (reduce (partial contains-5 symb)
            false
            (range next-idx (+ 1000 next-idx)))))

(defn key-index? [idx]
  (let [symb (contains-3 (stretch-hash idx))]
    (and symb (next-1000-contains-5 symb idx))))

(defn acc-key-indexes [acc idx]
  (if (= idx-needed (count acc))
    (reduced acc)
    (if (key-index? idx) (conj acc idx) acc)))

(defn gen-key-indexes []
  (reduce acc-key-indexes [] (range)))

(defn -main []
  (-> (gen-key-indexes)
      (last)))

(comment
  (-main))

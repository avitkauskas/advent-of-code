(ns aoc.d14-1
  (:require
   [clj-commons.digest :as d]))

(def salt "ngcjuoqr")
(def idx-needed 64)

(defn contains-3 [s]
  (second (re-find #"(.)(\1)(\1)" s)))

(defn contains-5 [symb _acc idx]
  (let [s (d/md5 (str salt idx))]
    (if (re-find (re-pattern (str symb "{5}")) s)
      (reduced true)
      false)))

(defn next-1000-contains-5 [symb idx]
  (let [next-idx (inc idx)]
    (reduce (partial contains-5 symb)
            false
            (range next-idx (+ 1000 next-idx)))))

(defn key-index? [idx]
  (let [symb (contains-3 (d/md5 (str salt idx)))]
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

(ns coa.d21
  (:require
   [clojure.string :as str]))

(def input
  (->> (slurp "input/input21.txt")
       (str/split-lines)
       (map #(str/split % #": "))))

(defn- parse-op [[m op]]
  (let [k (keyword m)
        v (if (int? (read-string op))
            (read-string op)
            (let [[a o' b] (str/split op #" ")
                  o (if (= k :root) "=" o')]
              (read-string (str "(" o " " (keyword a) " " (keyword b) ")"))))]
    {k v}))

(defn- parse-input [input]
  (into {} (map parse-op input)))

(defn rr [k ops]
 (let [res (k ops)]
   (if (int? res) res ((resolve (first res)) (rr (nth res 1) ops) (rr (nth res 2) ops)))))

(defn -main []
  (let [inp (parse-input input)
        target (rr :czdp inp)]
    (loop [lo 0 hi 10000000000000]
      (let [mid (quot (+ hi lo) 2)
            ops (assoc inp :humn mid)
            res (rr :hppd ops)
            dif (- target res)]
        (if (= dif 0) mid
          (if (> dif 0)
            (recur lo mid)
            (recur mid hi)))))))

(comment
  (prn (-main)))

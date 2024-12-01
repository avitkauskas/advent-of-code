(ns coa.d16
  (:require
   [clojure.string :as str]))

(def input
  (->> (slurp "input/test.txt")
       (str/split-lines)
       (map #(re-matches #"Valve (\w+) .*=(\d+); .* valves? (.*)" %))
       (map rest)
       ))

(def V (map first input))
(def F (filterv #(not= % 0) (map (comp read-string second) input)))

(def D (let [d (into {} (map (fn [e] (into {} (map (fn [t] {[(first e) t] 1})
                                                   (str/split (nth e 2) #", ")))) input))]
         (reduce (fn [m [k i j]] 
                   (let [dist (min (get d [i j] 1000) (+ (get d [i k] 1000) (get d [k j] 1000)))]
                     (if (< dist 1000) (assoc m [i j] dist) m)))
                 {}
                 (for [k V i V j V] [k i j]))))

(defn -main []
  )

(comment
  (prn (-main))
  (prn D)
  )

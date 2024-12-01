(ns aoc.day24 
  (:require
    [clojure.string :as str]
    [clojure.set :as set]))

(def input
  (->> (slurp "data/input24.txt")
       (str/split-lines)
       (map #(str/replace % "/" " "))
       (map #(format "[%s]" %))
       (map read-string)
       (into #{})))

(defn get-end [[a b :as port] connected-to]
  (if (nil? port)
      -1
      (if (= a connected-to) b a)))

(def init-state
  (let [zero-ports (filter (fn [[a b]] (or (zero? a) (zero? b))) input)]
    (for [port zero-ports
          :let [end (get-end port 0)]]
      {:bridge [port] :end end})))

(defn fitting-ports [bridge end]
  (let [bridge (set bridge)
        not-used-ports (set/difference input bridge)]
    (filter (fn [[a b]] (or (= a end) (= b end))) not-used-ports)))

(defn extend-bridges [bridges]
  (for [br bridges
        :let [{:keys [bridge end]} br]
        :let [ports (fitting-ports bridge end)]
        port (if (seq ports) ports [nil])
        :let [end (get-end port end)]]
    {:bridge (if (nil? port) bridge (conj bridge port)) :end end}))

(defn all-bridges []
  (loop [bridges init-state]
    (let [extended-bridges (extend-bridges bridges)]
      (if (= extended-bridges bridges)
        bridges
        (recur extended-bridges)))))

(defn part1 [& _args]
  (->> (all-bridges)
       (map :bridge)
       (map (fn [ports] (map #(apply + %) ports)))
       (map #(apply + %))
       (reduce max)
       println))

(defn part2 [& _args]
  (let [bridges (map :bridge (all-bridges))
        max-len (reduce max (map count bridges))]
    (->> (filter #(= max-len (count %)) bridges)
         (map (fn [ports] (map #(apply + %) ports)))
         (map #(apply + %))
         (reduce max)
         println)))

(comment
  (part1)
  (part2))

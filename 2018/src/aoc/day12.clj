(ns aoc.day12
  (:require
    [clojure.string :as str]))

(def input
  (-> (slurp "data/input12.txt")
      (str/replace #"initial state: " "")
      (str/split-lines)))

(def init-state
  (vec (first input)))

(def rules
  (->> (drop 2 input)
       (map #(str/split % #" => "))
       (map (fn [[rule ch]] [rule (char (first ch))]))
       (into {})))

(defn next-generation [state]
  (->> (concat [\. \. \. \.] state [\. \. \. \.])
       (partition 5 1)
       (map #(rules (apply str %)))))

(defn nth-generation [n]
  (->> (iterate next-generation init-state)
       (take (inc n))
       (last)))

(defn sum-indexes [start-idx state]
  (->> (map (fn [idx item] (if (= item \#) idx 0))
            (range start-idx (+ start-idx (count state)))
            state)
       (reduce +)))

(defn part-1 [n]
  (let [start-idx (* n -2)]
    (->> (nth-generation n)
         (sum-indexes start-idx))))

(defn trim-state [state]
  (as-> state s
    (apply str s)
    (str/replace s #"(^\.+|\.+$)" "")))

(defn count-empty-start [state]
  (->> state (take-while #(= % \.)) count))

(defn find-cycle [state]
  (loop [generation 0
         state state]
    (let [new-state (next-generation state)
          trimmed-new-state (trim-state new-state)
          trimmed-state (trim-state state)]
      (if (= trimmed-new-state trimmed-state)
        (let [shift (- (count-empty-start new-state)
                       (count-empty-start state) 2)]
          [generation shift state])
        (recur (inc generation) new-state)))))

(defn part-2 [n]
  (let [[generation shift state] (find-cycle init-state)]
    (sum-indexes (+ (* generation -2)
                    (* (- n generation) shift))
                 state)))

(comment
  (part-1 20)
  (part-2 50000000000))

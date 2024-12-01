(ns aoc.day07 
  (:require
    [clojure.string :as str]))

(def input
  (->> (slurp "data/input07.txt")
       str/split-lines
       (map #(rest (re-seq #"[A-Z]" %)))))

(def graph
  (reduce
   (fn [g [a b]]
     (-> g (assoc-in [a :right] (conj (get-in g [a :right] []) b))
           (assoc-in [b :left] (conj (get-in g [b :left] []) a))))
   {} input))

(defn sorted-starting-nodes []
  (->> (filter #(nil? (:left (second %))) graph)
       (map first)
       (apply sorted-set)))

(defn unblocked? [completed node]
  (let [dependencies (get-in graph [node :left])]
    (every? (set completed) dependencies)))

(defn part-1 []
  (loop [completed []
         available (sorted-starting-nodes)]
    (if (empty? available)
      (str/join completed)
      (let [node (first available)
            completed (conj completed node)
            available (disj available node)
            new-available (filter (partial unblocked? completed)
                                  (get-in graph [node :right]))]
        (recur completed
               (apply conj available new-available))))))

(defn duration [node]
  (- (int (first node)) 4))

(defn min-time [in-progress]
  (if-let [durations (vals in-progress)]
    (apply min durations)
    0))

(defn completed-nodes [in-progress current-time]
  (->> (filter (fn [[_ v]] (= v current-time)) in-progress)
       (map first)))

(defn available-nodes [completed nodes]
  (let [candidates (flatten (map #(get-in graph [% :right] []) nodes))]
    (filter (partial unblocked? completed) candidates)))

(defn add-started-nodes [in-progress nodes elapsed-time]
  (if (empty? nodes)
    in-progress
    (let [started-with-finish-time
          (flatten
           (for [node nodes]
             [node (+ elapsed-time (duration node))]))]
      (apply assoc in-progress started-with-finish-time))))

(defn part-2 []
  (loop [agents 5
         completed []
         available (sorted-starting-nodes)
         in-progress {}]
    (let [elapsed-time (min-time in-progress)
          new-completed (completed-nodes in-progress elapsed-time)
          in-progress (apply dissoc in-progress new-completed)
          completed (apply conj completed new-completed)
          new-available (available-nodes completed new-completed)
          available (apply conj available new-available)
          agents (+ agents (count new-completed))
          started (take agents available)
          agents (- agents (count started))
          available (apply disj available started)
          in-progress (add-started-nodes in-progress started elapsed-time)]
      (if (empty? in-progress)
        elapsed-time
        (recur agents
               completed
               available
               in-progress)))))

(comment
  (part-1)
  (part-2))

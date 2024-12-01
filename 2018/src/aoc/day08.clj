(ns aoc.day08)

(def input
  (->> (slurp "data/input08.txt")
       (format "[%s]")
       read-string))

(defn sum-meta [sum stack data]
  (if (empty? data)
    sum
    (let [[childs metas] (peek stack)]
      (if (pos? childs)
        (let [[ch mt & data] data
              stack (conj stack [ch mt])]
          (recur sum stack data))
        (let [sum (+ sum (apply + (take metas data)))
              data (drop metas data)
              stack (pop stack)
              stack (if (empty? stack)
                      stack
                      (let [[ch mt] (peek stack)]
                        (conj (pop stack) [(dec ch) mt])))]
          (recur sum stack data))))))

(defn part-1 []
  (let [sum 0
        stack [[(first input) (second input)]]
        data (drop 2 input)]
    (sum-meta sum stack data)))

(defn node-value
  "Recursively return a vector of node value and remaining data [value data]"
  [data]
  (let [[childs metas & data] data]
    (if (zero? childs)
      (let [value (apply + (take metas data))
            data (drop metas data)]
        [value data])
      (loop [i 1
             ch-vals {}
             data data]
        (if (> i childs)
          (let [refs (->> (take metas data) (filter #(< 0 % (inc childs))))
                value (apply + (map #(ch-vals %) refs))
                data (drop metas data)]
            [value data])
          (let [[value data] (node-value data)]
            (recur (inc i)
                   (assoc ch-vals i value)
                   data)))))))

(defn part-2 []
  (first (node-value input)))

(comment
  (part-1)
  (part-2))

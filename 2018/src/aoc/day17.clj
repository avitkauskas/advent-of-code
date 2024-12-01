(ns aoc.day17
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (let [lines (str/split-lines input)]
    (reduce (fn [acc line]
              (let [[_ coord ranges] (re-find #"(x|y)=(\d+(?:\.\.\d+)?)" line)
                    [start end] (map #(Integer/parseInt %) (str/split ranges #"\.\."))]
                (if (= coord "x")
                  (into acc (for [y (range start (inc (or end start)))] [start y]))
                  (into acc (for [x (range start (inc (or end start)))] [x start])))))
            #{}
            lines)))

(defn bounds [clay]
  (let [xs (map first clay)
        ys (map second clay)]
    [(apply min xs) (apply max xs) (apply min ys) (apply max ys)]))

(defn flow [clay [x y] [min-x max-x min-y max-y]]
  (loop [water #{}
         flowing #{[x y]}
         settled #{}]
    (if (empty? flowing)
      [water settled]
      (let [[cx cy] (first flowing)
            below [cx (inc cy)]
            left [(dec cx) cy]
            right [(inc cx) cy]]
        (cond
          (> cy max-y)
          (recur water (disj flowing [cx cy]) settled)

          (not (or (clay below) (settled below)))
          (recur (conj water below) (conj (disj flowing [cx cy]) below) settled)

          :else
          (let [[left-flow left-bound] (loop [x (dec cx) flow #{}]
                                         (if (clay [x cy])
                                           [flow true]
                                           (if (not (or (clay [x (inc cy)]) (settled [x (inc cy)])))
                                             [flow false]
                                             (recur (dec x) (conj flow [x cy])))))
                [right-flow right-bound] (loop [x (inc cx) flow #{}]
                                           (if (clay [x cy])
                                             [flow true]
                                             (if (not (or (clay [x (inc cy)]) (settled [x (inc cy)])))
                                               [flow false]
                                               (recur (inc x) (conj flow [x cy])))))]
            (if (and left-bound right-bound)
              (recur water
                     (disj flowing [cx cy])
                     (apply conj settled [cx cy] left-flow right-flow))
              (recur (apply conj water [cx cy] left-flow right-flow)
                     (apply conj (disj flowing [cx cy])
                            (when-not left-bound (first left-flow))
                            (when-not right-bound (first right-flow)))
                     settled))))))))

(defn count-water-tiles [input]
  (let [clay (parse-input input)
        [min-x max-x min-y max-y] (bounds clay)
        [water settled] (flow clay [500 0] [min-x max-x min-y max-y])]
    (count (filter #(<= min-y (second %) max-y) (into water settled)))))

(def input (slurp "data/input17.txt"))
(println (count-water-tiles input))

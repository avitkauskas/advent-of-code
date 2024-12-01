(ns aoc.day08
  (:require [clojure.string :as str]))

(def input
  (->> (slurp "data/input08.txt")
       (str/split-lines)
       (mapv #(str/split % #"\s+"))))

(defn eval-instruction [regs [r1 op n1 _ r2 cmp n2]]
  (let [n1 (parse-long n1)
        n2 (parse-long n2)
        op (case op
             "inc" +
             "dec" -)
        cmp (case cmp
              "==" =
              "!=" not=
              (resolve (symbol cmp)))]
    (if (cmp (get regs r2 0) n2)
      (assoc regs r1 (op (get regs r1 0) n1))
      regs)))

(defn part1 []
  (->> (reduce eval-instruction {} input)
       (map second)
       (reduce max)))

(defn find-max [{:keys [max-v regs]} instruction]
  (let [regs (eval-instruction regs instruction)
        max-v (reduce max max-v (map second regs))]
    {:max-v max-v :regs regs}))

(defn part2 []
  (:max-v (reduce find-max {:max-v 0 :regs {}} input)))

(comment
  (part1)
  (part2))

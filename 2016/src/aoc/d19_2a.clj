(ns aoc.d19-2a)

(def n 3012210)

(->> (iterate #(* 3 %) 1)
     (take-while #(< % n))
     (last)
     (- n))

(ns aoc.day14)

(def input 652601)

(defn digits [n]
  (if (< n 10)
    [n]
    [1 (- n 10)]))

(defn make-recipes [required]
  (loop [recipes [3 7]
         i1 0
         i2 1
         made 2]
    (if (>= made required)
      {:total required :recipes recipes}
      (let [r1 (recipes i1)
            r2 (recipes i2)
            new-recipes (digits (+ r1 r2))
            new-total (+ made (count new-recipes))]
        (recur (apply conj recipes new-recipes)
               (rem (+ i1 r1 1) new-total)
               (rem (+ i2 r2 1) new-total)
               new-total)))))

(defn part-1 []
  (let [required (+ input 10)
        made (make-recipes required)]
    (->> (if (> (:total made) required)
           (->> (take-last 11 (:recipes made)) (take 10))
           (take-last 10 (:recipes made)))
         (apply str))))

(defn n->vec [n]
  (->> n str vec (map str) (mapv parse-long)))

(defn find-seq [look-up-seq]
  (let [len (count look-up-seq)]
    (loop [recipes [3 7]
           i1 0
           i2 1
           made 2
           last-n [3 7]]
      (cond
        (= look-up-seq (take-last len last-n))
        (- made len)
        (= look-up-seq (take len last-n))
        (- made len 1)
        :else
        (let [r1 (recipes i1)
              r2 (recipes i2)
              new-recipes (digits (+ r1 r2))
              new-total (+ made (count new-recipes))]
          (recur (apply conj recipes new-recipes)
                 (rem (+ i1 r1 1) new-total)
                 (rem (+ i2 r2 1) new-total)
                 new-total
                 (into [] (take-last (inc len) (apply conj last-n new-recipes)))))))))

(defn part-2 []
  (find-seq (n->vec input)))

(comment
  (part-1)
  (part-2))

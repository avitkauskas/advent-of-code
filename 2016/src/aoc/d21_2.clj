(ns aoc.d21-2 
  (:require
    [clojure.string :as s]))

(defn read-input []
  (->> (slurp "data/input21.txt")
       (s/split-lines)
       (map #(s/split % #" "))
       (reverse)))

(def pswd "fbgdceah")

(defmulti operation
  (fn [_ [op1 op2 & _]]
    [op1 op2]))

;; reverse operation is the same
(defmethod operation ["swap" "position"]
  [s [_ _ x _ _ y]]
  (let [x (parse-long x)
        y (parse-long y)]
    (-> (vec s)
        (assoc x (get s y))
        (assoc y (get s x))
        (s/join))))

;; reverse operation is the same
(defmethod operation ["swap" "letter"]
  [s [_ _ a _ _ b]]
  (let [x (s/index-of s a)
        y (s/index-of s b)
        a (get a 0)
        b (get b 0)]
    (-> (vec s)
        (assoc x b)
        (assoc y a)
        (s/join))))
        
;; changed to actually rotate left
(defmethod operation ["rotate" "right"]
  [s [_ _ n _]]
  (let [n (rem (parse-long n) (count s))]
    (str (subs s n) (subs s 0 n))))

;; changed to actually rotate right
(defmethod operation ["rotate" "left"]
  [s [_ _ n _]]
  (let [c (count s)
        i (rem (parse-long n) c)
        n (- c i)]
    (operation s ["rotate" "right" (str n) "steps"])))

(defmethod operation ["rotate" "based"]
  [s [_ _ _ _ _ _ a]]
  (let [i (s/index-of s a)
        n ({0 9, 1 1, 2 6, 3 2, 4 7, 5 3, 6 8, 7 4} i)]
    ;; using right actually rotates left
    (operation s ["rotate" "right" (str n) "steps"])))

;; reverse operation is the same
(defmethod operation ["reverse" "positions"]
  [s [_ _ x _ y]]
  (let [x (parse-long x)
        y (inc (parse-long y))]
    (str (subs s 0 x) (s/reverse (subs s x y)) (subs s y))))

;; changed positions of indexes in parameters
(defmethod operation ["move" "position"]
  [s [_ _ y _ _ x]]
  (let [x (parse-long x)
        y (parse-long y)
        c (get s x)
        r (str (subs s 0 x) (subs s (inc x)))]
    (str (subs r 0 y) c (subs r y))))

(defn -main []
  (reduce operation pswd (read-input)))

(comment
  (-main))

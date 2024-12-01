(ns aoc.d08-2
  (:require
    [clojure.string :as s]
    [clojure.core.matrix :as m]))

(def screen (m/mutable (m/matrix (repeat 6 (repeat 50 " ")))))

(defn rect [cols rows matrix]
  (doseq [r (range rows)
          c (range cols)]
    (m/mset! matrix r c "*")))

(defn rotate-row [r shift matrix]
  (m/set-row! matrix r (-> matrix (m/get-row r) (m/rotate 0 (- shift)))))
  
(defn rotate-column [c shift matrix]
  (m/set-column! matrix c (-> matrix (m/get-column c) (m/rotate 0 (- shift)))))

(defn get-lines []
  (-> (slurp "data/input08.txt")
      (s/split-lines)))

(defn line->sexp [line]
  (let [parts (s/split line #"\s+")
        command (if (= "rect" (first parts))
                  (first parts)
                  (str (first parts) "-" (second parts)))
        params (condp = command
                 "rect"          (s/join " " (s/split (second parts) #"x"))
                 "rotate-row"    (str (last (s/split (nth parts 2) #"=")) " " (last parts))
                 "rotate-column" (str (last (s/split (nth parts 2) #"=")) " " (last parts)))]
    (str "(partial " command " " params ")")))

(defn -main []
  (let [commands (map line->sexp (get-lines))]
    (doseq [cmd commands]
      (eval (read-string (str "(" cmd " screen)")))))
  (m/pm screen))

(comment
  (-main))

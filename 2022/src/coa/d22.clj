(ns coa.d22
  (:require
   [clojure.string :as s]
   [clojure.core.matrix :as m]))

(defn- get-input []
  (-> (slurp "input/input22.txt")
      (s/split #"\n\n")))

(defn- get-lines [input]
  (->> (first input)
       (s/split-lines)))

(defn- parse-move [move]
  (let [n (read-string move)]
    (if (int? n) n move)))

(def moves
  (->> (second (get-input))
       (re-seq #"(\d+)(\w{0,1})")
       (map rest)
       (flatten)
       (butlast)
       (map parse-move)))

(def max-r (dec (count (get-lines (get-input)))))
(def max-c (dec (apply max (map count (get-lines (get-input))))))

(defn- parse-maze-cell [cell]
  (case cell
    " " 2
    "#" 1
    "." 0))

(def maze
  (->> (get-lines (get-input))
       (map #(str % (apply str (repeat (- (inc max-c) (count %)) " "))))
       (map #(s/split % #""))
       (map #(map parse-maze-cell %))
       (m/matrix)))

(defn- init-state [maze]
  (let [c (->> (m/get-row maze 0)
               (take-while #(not= % 0))
               (count))]
    {:r 0 :c c :d 0}))

(defn- turn [state d]
  (case d
    "L" (assoc state :d (mod (dec (:d state)) 4))
    "R" (assoc state :d (mod (inc (:d state)) 4))))

(defn- oposit-cell [{:keys [r c d]}]
  (case d
    0 (let [nc (count (take-while #(= % 2) (m/get-row maze r)))]
        {:r r :c nc :s (m/mget maze r nc)})
    1 (let [nr (count (take-while #(= % 2) (m/get-column maze c)))]
        {:r nr :c c :s (m/mget maze nr c)})
    2 (let [nc (- max-c (count (take-while #(= % 2) (reverse (m/get-row maze r)))))]
        {:r r :c nc :s (m/mget maze r nc)})
    3 (let [nr (- max-r (count (take-while #(= % 2) (reverse (m/get-column maze c)))))]
        {:r nr :c c :s (m/mget maze nr c)})))

(defn- next-cell [{:keys [r c d] :as state}]
  (case d
    0 (if (= c max-c) (oposit-cell state)
          (let [nxt {:r r :c (inc c) :s (m/mget maze r (inc c))}]
            (if (= (:s nxt) 2) (oposit-cell state) nxt)))
    1 (if (= r max-r) (oposit-cell state)
          (let [nxt {:r (inc r) :c c :s (m/mget maze (inc r) c)}]
            (if (= (:s nxt) 2) (oposit-cell state) nxt)))
    2 (if (= c 0) (oposit-cell state)
          (let [nxt {:r r :c (dec c) :s (m/mget maze r (dec c))}]
            (if (= (:s nxt) 2) (oposit-cell state) nxt)))
    3 (if (= r 0) (oposit-cell state)
          (let [nxt {:r (dec r) :c c :s (m/mget maze (dec r) c)}]
            (if (= (:s nxt) 2) (oposit-cell state) nxt)))))

(defn- move [state _]
  (let [{:keys [r c s]} (next-cell state)]
    (if (= s 0) (assoc state :r r :c c) state)))

(defn- act [state command]
  (if (int? command)
    (reduce move state (range command))
    (turn state command)))

(defn -main []
  (let [final-state (reduce act (init-state maze) moves)]
    (+ (* 1000 (inc (:r final-state))) (* 4 (inc (:c final-state))) (:d final-state))))

(comment
  (prn (-main))
  (prn (get-lines (get-input)))
  moves
  (prn maze)
  (m/shape maze)
  (init-state maze)
  (turn (init-state maze) "R")
  (m/mget maze 0 0)
  )

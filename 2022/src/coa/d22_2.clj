(ns coa.d22-2
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

(defn- opposit-cell [{:keys [r c d]}]
  (case d
    0 (cond
        (< r  50) (let [nr (- 149 r) nc 99] {:r nr :c nc :d 2 :s (m/mget maze nr nc)})
        (< r 100) (let [nr 49 nc (+ 50 r)] {:r nr :c nc :d 3 :s (m/mget maze nr nc)})
        (< r 150) (let [nr (- 149 r) nc 149] {:r nr :c nc :d 2 :s (m/mget maze nr nc)})
        (< r 200) (let [nr 149 nc (- r 100)] {:r nr :c nc :d 3 :s (m/mget maze nr nc)}))
    1 (cond
        (< c  50) (let [nr 0 nc (+ 100 c)] {:r nr :c nc :d 1 :s (m/mget maze nr nc)})
        (< c 100) (let [nr (+ 100 c) nc 49] {:r nr :c nc :d 2 :s (m/mget maze nr nc)})
        (< c 150) (let [nr (- c 50) nc 99] {:r nr :c nc :d 2 :s (m/mget maze nr nc)}))
    2 (cond
        (< r  50) (let [nr (- 149 r) nc 0] {:r nr :c nc :d 0 :s (m/mget maze nr nc)})
        (< r 100) (let [nr 100 nc (- r 50)] {:r nr :c nc :d 1 :s (m/mget maze nr nc)})
        (< r 150) (let [nr (- 149 r) nc 50] {:r nr :c nc :d 0 :s (m/mget maze nr nc)})
        (< r 200) (let [nr 0 nc (- r 100)] {:r nr :c nc :d 1 :s (m/mget maze nr nc)}))
    3 (cond
        (< c  50) (let [nr (+ 50 c) nc 50] {:r nr :c nc :d 0 :s (m/mget maze nr nc)})
        (< c 100) (let [nr (+ 100 c) nc 0] {:r nr :c nc :d 0 :s (m/mget maze nr nc)})
        (< c 150) (let [nr 199 nc (- c 100)] {:r nr :c nc :d 3 :s (m/mget maze nr nc)}))))

(defn- next-cell [{:keys [r c d] :as state}]
  (case d
    0 (if (= c max-c) (opposit-cell state)
          (let [nxt {:r r :c (inc c) :d d :s (m/mget maze r (inc c))}]
            (if (= (:s nxt) 2) (opposit-cell state) nxt)))
    1 (if (= r max-r) (opposit-cell state)
          (let [nxt {:r (inc r) :c c :d d :s (m/mget maze (inc r) c)}]
            (if (= (:s nxt) 2) (opposit-cell state) nxt)))
    2 (if (= c 0) (opposit-cell state)
          (let [nxt {:r r :c (dec c) :d d :s (m/mget maze r (dec c))}]
            (if (= (:s nxt) 2) (opposit-cell state) nxt)))
    3 (if (= r 0) (opposit-cell state)
          (let [nxt {:r (dec r) :c c :d d :s (m/mget maze (dec r) c)}]
            (if (= (:s nxt) 2) (opposit-cell state) nxt)))))

(defn- move [state _]
  (let [{:keys [r c d s]} (next-cell state)]
    (if (= s 0) (assoc state :r r :c c :d d) state)))

(defn- act [state command]
  (if (int? command)
    (reduce move state (range command))
    (turn state command)))

(defn -main []
  (let [final-state (reduce act (init-state maze) moves)]
    (+ (* 1000 (inc (:r final-state))) (* 4 (inc (:c final-state))) (:d final-state))))

(comment
  (prn (-main)))

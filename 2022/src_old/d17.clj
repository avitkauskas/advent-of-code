(ns coa.d17
  (:require 
   [clojure.string :as str]))

(defn get-input [file]
  (map #(if (= % "<") -1 1)
       (-> (slurp file)
           (str/split #""))))

(def p (atom 0))
(def b (atom 0))

(def COLS 7)
(def blows (cycle (get-input "input/input17-2.txt")))
(def pieces (cycle [[[2 3] [3 3] [4 3] [5 3]]
                    [[3 5] [2 4] [3 4] [4 4] [3 3]]
                    [[4 5] [4 4] [2 3] [3 3] [4 3]]
                    [[2 6] [2 5] [2 4] [2 3]]
                    [[2 4] [3 4] [2 3] [3 3]]]))

(defn initial-piece-position [top-line piece]
  (map (fn [[x y]] [x (+ y top-line 1)]) piece))

(defn pos-to-xy [pos]
  (let [x (rem pos COLS)
        y (quot (- pos x) COLS)]
    [x y]))

(defn xy-to-pos [[x y]]
  (+ x (* y COLS)))

(defn collides?
  ([_ x y pos]
   (let [[posx posy] (pos-to-xy pos)]
     (and (> x -1)
          (< x COLS)
          (> y -1)
          (not (and (= posx x)
                    (= posy y))))))
  ([board piece pos]
   (every?  #{true}
            (for [[x y] piece]
              (collides? board x y pos))))
  ([board piece]
   (if (empty? board)
     (not (collides? board piece -1))
     (not (reduce #(and %1 (collides? board piece %2)) true board)))))

(defn move-side [board offset piece]
  (let [moved (map (fn [[x y]] [(+ x offset) y]) piece)]
    (if (collides? board moved) piece moved)))

(defn move-piece [board blows piece]
  (loop [piece' piece
         blows' blows]
    (let [after-blow (move-side board (first blows') piece')
          moved-down (map (fn [[x y]] [x (- y 1)]) after-blow)]
      (if (collides? board moved-down)
        (do
          (swap! p inc) (swap! b inc)
          (when (= (rem @p 1000) 0) (println "--- " @p) (flush))
          (when (and (= (rem @p 5) 0) (= (rem @b 10091) 0)) (println (str "+++ " @p " " @b)) (flush))
          ; (prn (str "---p: " (swap! p inc)))
          ; (prn (str "   b: " (swap! b inc)))
          {:piece after-blow :blows (rest blows')})
        ; (do
          ; (prn (str "   b: " (swap! b inc)))
          (recur moved-down (rest blows')
                 ; )
          )))))

(defn add-piece [board piece]
  (reduce (fn [board coords] (conj board (xy-to-pos coords))) board piece))

(defn make-move [{:keys [board top-line blows]} iter]
  (let [after-move (->> (nth pieces iter)
                   (initial-piece-position top-line)
                   (move-piece board blows))
        new-board (add-piece board (:piece after-move))
        new-top-line (max top-line (apply max (map second (:piece after-move))))]
    {:board new-board :top-line new-top-line :blows (:blows after-move)}))

(defn -main []
  (inc (:top-line (reduce make-move {:board '() :top-line -1 :blows blows} (range 50000))))
  ; (:board (reduce make-move {:board '() :top-line -1 :blows blows} (range 7)))
  )

(comment
  (prn (-main))
  (prn (get-input "input/test.txt"))
  (prn (initial-piece-position -1 (nth pieces 0)))
  (prn (move-side '(22) -1 (nth pieces 0)))
  (collides? '() [[-2 3] [-1 3] [0 3] [1 3]] -1)
  (add-piece '() (nth pieces 0))
  (second (pos-to-xy (apply max '(26 25 24 23))))
  (nth blows 2021)
  (add-piece '() '([2 0] [3 0] [4 0] [5 0]))
  )

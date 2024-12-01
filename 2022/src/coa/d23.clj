(ns coa.d23
  (:require
   [clojure.string :as s]))

(defn- read-lines []
  (->> (slurp "input/input23.txt")
       (s/split-lines)
       (map #(s/split % #""))))

(defn- parse-line [y line]
  (letfn [(elf [x symb] (if (= symb "#") [x y] nil))]
    (filter #(not (nil? %)) (map-indexed elf line))))

(defn- make-elves []
  (mapcat identity (map-indexed parse-line (read-lines))))

(defn- can-move [elves-set elf dir]
  (let [[x y] elf]
    (case dir
      :N (let [ny (dec y)] (if (some #{[(dec x) ny] [x ny] [(inc x) ny]} elves-set) nil [x ny]))
      :S (let [ny (inc y)] (if (some #{[(dec x) ny] [x ny] [(inc x) ny]} elves-set) nil [x ny]))
      :W (let [nx (dec x)] (if (some #{[nx (dec y)] [nx y] [nx (inc y)]} elves-set) nil [nx y]))
      :E (let [nx (inc x)] (if (some #{[nx (dec y)] [nx y] [nx (inc y)]} elves-set) nil [nx y])))))

(defn- empty-arround? [elves-set [x y]]
  (not (some #{[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
               [(dec x) y] [(inc x) y]
               [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]}
             elves-set)))

(defn- propose-moves [state]
  (let [elves (:elves state)
        elves-set (set elves)
        dirs (:dirs state)]
    (for [elf elves]
      (if (empty-arround? elves-set elf) elf
          (loop [i 0]
            (if (= i 4) elf
                (let [move (can-move elves-set elf (dirs i))]
                  (if move move
                      (recur (inc i))))))))))

(defn- unique-moves [moves]
  (->> (frequencies moves)
       (filter #(= (second %) 1))
       (map first)
       set))

(defn- play-round [state _]
  (let [dirs (:dirs state)
        moves (vec (propose-moves state))
        moves-set (unique-moves moves)
        old-elves (:elves state)
        elves (map-indexed #(let [move (moves %1)]
                              (if (= %2 move) %2
                                  (if (some #{move} moves-set) move %2)))
                           old-elves)]
    {:elves elves :dirs (vec (concat (rest dirs) [(first dirs)]))}))

(defn -main []
  (let [elves (:elves (reduce play-round {:elves (make-elves) :dirs [:N :S :W :E]} (range 10)))
        xx (map first elves)
        yy (map second elves)
        min-x (apply min xx)
        max-x (apply max xx)
        min-y (apply min yy)
        max-y (apply max yy)
        empty-cells (- (* (inc (- max-x min-x)) (inc (- max-y min-y))) (count elves))]
    empty-cells))

(comment
  (prn (-main)))

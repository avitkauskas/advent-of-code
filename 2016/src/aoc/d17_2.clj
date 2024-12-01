(ns aoc.d17-2
  (:require
   [clj-commons.digest :as d]))

(def passcode "awrkjxxr")
(def init-state {:cell [0 0] :path ""})
(def move-coord {"U" [0 -1] "D" [0 1] "L" [-1 0] "R" [1 0]})

(def queue (clojure.lang.PersistentQueue/EMPTY))

(defn no-walls [{:keys [cell] :as _state}]
  (let [[x y] cell]
    (-> [true true true true]
        (assoc 0 (pos? y))
        (assoc 1 (< y 3))
        (assoc 2 (pos? x))
        (assoc 3 (< x 3)))))

(defn- open-doors [{:keys [path] :as _state}]
  (->> (str passcode path)
       (d/md5)
       (take 4)
       (map #(pos? (compare % \a)))))

(defn- possible-moves [state]
  (->> (map #(and %1 %2 %3) (open-doors state) (no-walls state) ["U" "D" "L" "R"])
         (remove false?)))

(defn get-new-states [{:keys [cell path] :as state}]
  (for [move (possible-moves state)]
    {:cell (map + cell (move-coord move))
     :path (str path move)}))

(defn -main []
  (loop [q (conj queue init-state)
         path-lengths #{}]
    (if (empty? q)
      (apply max 0 path-lengths)
      (let [{:keys [cell path] :as state} (peek q)]
        (if (= [3 3] cell)
          (recur 
           (pop q)
           (conj path-lengths (count path)))
          (recur
           (apply conj (pop q) (get-new-states state))
           path-lengths))))))

(comment
  (-main))

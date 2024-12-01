(ns aoc.day15-1
  (:require
   [flatland.ordered.map :as om]
   [clojure.string :as str]))

(def init-hit-points 200)
(def hit-power 3)

(def movements [[-1 0] [0 -1] [0 1] [1 0]])

(def init-field
  (let [lines (->> (slurp "data/input15.txt")
                   (str/split-lines))]
    (into {} (for [[r row] (map vector (range) lines)
                   [c chr] (map vector (range) row)]
               (if (#{\G \E} chr)
                 [[r c] {:typ chr :hit init-hit-points}]
                 [[r c] {:typ chr}])))))

(defn get-units [field]
  (-> (filter (fn [[_rc cell]] (#{\G \E} (:typ cell))) field)
      (sort)))

(defn get-enemies [field unit-typ]
  (let [enemy (if (= \G unit-typ) \E \G)]
    (filter (fn [[_rc cell]] (= enemy (:typ cell))) field)))

(defn open-adjacent-cells [field position]
  (for [pos (map #(mapv + position %) movements)
        :when (= \. (:typ (field pos)))]
    pos))

(defn target-cells [field enemies]
  (->> (map first enemies)
       (reduce (fn [acc pos] (into acc (open-adjacent-cells field pos))) #{})))

(defn add-to [m-coll cells path dist]
  (reduce (fn [m cell] (assoc m cell {:dist dist :path (conj path cell)})) m-coll cells))

(defn find-all-paths [field origin]
  (loop [queue (om/ordered-map origin {:dist 0 :path []})
         paths {origin {:dist 0 :path []}}]
    (if (empty? queue)
      paths
      (let [[rc {:keys [dist path]}] (first queue)
            next-cells (for [cell (open-adjacent-cells field rc)
                             :when (not (contains? paths cell))]
                         cell)]
        (recur (add-to (dissoc queue rc) next-cells path (inc dist))
               (add-to paths next-cells path (inc dist)))))))

(defn next-to-enemy? [field [rc cell :as _unit]]
  (let [enemy (if (= \E (:typ cell)) \G \E)
        neighbours (->> (map #(mapv + rc %) movements)
                        (map field)
                        (map :typ))]
    (some #{enemy} neighbours)))

(defn move [field [rc cell :as unit] enemies]
  (if (next-to-enemy? field unit)
    {:field field :unit unit}
    (let [target-cells-set (target-cells field enemies)
          all-paths (find-all-paths field rc)
          paths-to-targets (filter (fn [[rc _path]] (target-cells-set rc)) all-paths)]
      (if (empty? paths-to-targets)
        {:field field :unit unit}
        (let [min-dist (-> (apply min-key #(:dist (second %)) paths-to-targets) second :dist)
              min-dist-paths (filter #(= min-dist (:dist (second %))) paths-to-targets)
              move-to-cell (->> (sort min-dist-paths)
                                (first) (second) :path (first))
              field (-> field
                        (update rc (fn [_] (field move-to-cell)))
                        (update move-to-cell (fn [_] cell)))]
          {:field field :unit [move-to-cell cell]})))))

(defn choose-target [field [rc cell :as _unit]]
  (let [enemy (if (= \E (:typ cell)) \G \E)
        adjacent-cells (into #{} (map #(mapv + rc %) movements))
        targets (->> (filter #(and (adjacent-cells (first %))
                                   (= (:typ (second %)) enemy))
                             field))] 
    (-> (sort-by (juxt (comp :hit second) first) targets)
        (first))))

(defn fight [field unit kills]
  (if-not (next-to-enemy? field unit)
    {:field field :kills kills}
    (let [[rc {:keys [_ hit]}] (choose-target field unit)
          new-hit (- hit hit-power)]
      (if (pos? new-hit)
        {:field (update field rc (fn [cell] (update cell :hit (fn [_] new-hit)))) :kills kills}
        {:field (update field rc (fn [_] {:typ \.})) :kills (conj kills rc)}))))

(defn take-turn [{:keys [field enemies? kills] :as state} [rc cell :as unit]]
  (if (kills rc) ; unit was killed already
    state ; do nothing
    (let [enemies (get-enemies field (:typ cell))]
      (if (empty? enemies)
        (reduced (update state :enemies? (fn [_] false)))
        (let [{:keys [field unit]} (move field unit enemies)
              {:keys [field kills]} (fight field unit kills)]
          {:field field :enemies? enemies? :kills kills})))))

(defn play-round [{:keys [field _ _] :as state}]
  (update (reduce take-turn state (get-units field))
          :kills (fn [_] #{})))

(defn field-points [field]
  (apply + (map (fn [[_rc cell]] (:hit cell 0)) field)))

(defn part-1 [& _args]
  (let [rounds (take-while :enemies? (iterate play-round {:field init-field :enemies? true :kills #{}}))
        full-rounds (dec (count rounds))
        last-round (play-round {:field (-> rounds last :field) :enemies? true :kills #{}})
        points (field-points (:field last-round))]
    (println (* full-rounds points))))

(comment
  (part-1))

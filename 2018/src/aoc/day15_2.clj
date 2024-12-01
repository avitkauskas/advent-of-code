(ns aoc.day15-2
  (:require
   [flatland.ordered.map :as om]
   [clojure.string :as str]))

(def init-hit-points 200)

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

(defn fight [field [_ cell :as unit] kills hit-powers]
  (if-not (next-to-enemy? field unit)
    {:field field :kills kills :elf-killed? false}
    (let [[rc {:keys [typ hit]}] (choose-target field unit)
          new-hit (- hit (hit-powers (:typ cell)))]
      (if (pos? new-hit)
        {:field (update field rc (fn [cell] (update cell :hit (fn [_] new-hit)))) :kills kills :elf-killed? false}
        (if (= \E typ)
          {:field (update field rc (fn [_] {:typ \.})) :kills (conj kills rc) :elf-killed? true}
          {:field (update field rc (fn [_] {:typ \.})) :kills (conj kills rc) :elf-killed? false})))))

(defn take-turn [{:keys [field enemies? kills hit-powers] :as state} [rc cell :as unit]]
  (if (kills rc) ; unit was killed already
    state ; do nothing
    (let [enemies (get-enemies field (:typ cell))]
      (if (empty? enemies)
        (reduced (update state :enemies? (fn [_] false)))
        (let [{:keys [field unit]} (move field unit enemies)
              {:keys [field kills elf-killed?]} (fight field unit kills hit-powers)]
          (if elf-killed?
            (reduced {:field field :enemies? false :kills kills :elf-killed? true :hit-powers hit-powers})
            {:field field :enemies? enemies? :kills kills :elf-killed? elf-killed? :hit-powers hit-powers}))))))

(defn play-round [{:keys [field _ _ _ _] :as state}]
  (reduce take-turn (assoc state :enemies? true :kills #{} :elf-killed? false) (get-units field)))

(defn field-points [field]
  (apply + (map (fn [[_rc cell]] (:hit cell 0)) field)))

(defn part-2 []
  (loop [hit-powers {\G 3 \E 4}]
    (prn "loop:" hit-powers)
    (let [rounds
          (take-while :enemies? 
                      (iterate play-round 
                               {:field init-field :enemies? true :kills #{} 
                                :elf-killed? false :hit-powers hit-powers}))
          last-round
          (play-round {:field (-> rounds last :field) :enemies? true :kills #{}
                       :elf-killed? false :hit-powers hit-powers})]
      (if (not (:elf-killed? last-round))
        (let [full-rounds (dec (count rounds))
              points (field-points (:field last-round))]
          (* full-rounds points))
        (recur (update hit-powers \E inc))))))

(comment
  (part-2))

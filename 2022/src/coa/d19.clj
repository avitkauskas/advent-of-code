(ns coa.d19
  (:require
    [clojure.string :as str]))

(def pattern
  (re-pattern
   (str "Blueprint (\\d+): "
        "Each ore robot costs (\\d+) ore. "
        "Each clay robot costs (\\d+) ore. "
        "Each obsidian robot costs (\\d+) ore and (\\d+) clay. "
        "Each geode robot costs (\\d+) ore and (\\d+) obsidian.")))

(defn- read-resources
  [[b rr rc ro co rg og]]
  {:blueprint b
   :resources [[(- rr) 0 0 0] [(- rc) 0 0 0] [(- ro) (- co) 0 0] [(- rg) 0 (- og) 0]]
   :robots [[1 0 0 0] [0 1 0 0] [0 0 1 0] [0 0 0 1]]})

(def blueprints
  (->> (slurp "input/test.txt")
       (str/split-lines)
       (map #(re-find pattern %))
       (map rest)
       (map #(map read-string %))
       (map read-resources)))

(def total-time 24)

(def initial-status {:resources [0 0 0 0] :robots [1 0 0 0]})

(defn- find-strategies [{:keys [resources robots]} blueprint]
  (let [first-strategy {:resources (map + resources robots) :robots robots}]
    (concat [first-strategy]
            (for [option (range 4)
                  :let [option-resources (nth (:resources blueprint) option)
                        option-robots (nth (:robots blueprint) option)]
                  :when (not (some #(< % 0) (map + resources option-resources)))]
              {:resources (map + (:resources first-strategy) option-resources)
               :robots (map + (:robots first-strategy) option-robots)}))))

(defn- max-geodes [tt status blueprint]
  (if (= tt (dec total-time))
      (nth (:resources status) 3)
    (let [strategies (find-strategies status blueprint)]
        (apply max (map #(max-geodes (inc tt) % blueprint) strategies)))))

(defn -main []
  ; (let [quality-levels (map #(max-geodes 0 initial-status %) blueprints)]
  ;   quality-levels)
  (let [quality-levels (max-geodes 0 initial-status (second blueprints))]
    (println quality-levels))
  )

(comment
  (prn (-main))
  (prn (find-strategies {:resources [4 0 0 0] :robots [1 0 0 0]} (first blueprints)))
  )

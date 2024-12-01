(ns aoc.day25)

(def instructions
  {\A {0 {:w 1 :m inc :s \B} 1 {:w 0 :m dec :s \B}}
   \B {0 {:w 0 :m inc :s \C} 1 {:w 1 :m dec :s \B}}
   \C {0 {:w 1 :m inc :s \D} 1 {:w 0 :m dec :s \A}}
   \D {0 {:w 1 :m dec :s \E} 1 {:w 1 :m dec :s \F}}
   \E {0 {:w 1 :m dec :s \A} 1 {:w 0 :m dec :s \D}}
   \F {0 {:w 1 :m inc :s \A} 1 {:w 1 :m dec :s \E}}})

(def init-info {:pos 0 :state \A :tape {0 0}})

(def steps 12629077)

(defn make-step [{:keys [pos state tape] :as info}]
  (let [instr (get instructions state)
        value (get tape pos 0)
        actions (get instr value)]
    (-> info
        (update :pos (:m actions))
        (assoc :state (:s actions))
        (update :tape #(assoc % pos (:w actions))))))

(defn part1 []
  (-> (iterate make-step init-info)
      (nth steps)
      :tape
      vals
      (->> (reduce +))))

(comment
  (part1))

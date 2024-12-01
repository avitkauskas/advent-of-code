(ns aoc.d10-1
  (:require [clojure.string :as s]))

(def state (atom {:bot {} :output {}}))
(def instructions (atom {:value {} :bot {}}))

(defn get-lines []
  (->> (slurp "data/input10.txt")
       (s/split-lines)
       (map #(s/split % #"\s+"))))

(defn get-instructions [lines]
  (doseq [line lines]
    (condp = (first line)
      "value" (swap! instructions 
                     assoc-in [:value (parse-long (second line))]
                     (parse-long (last line)))
      "bot" (swap! instructions 
                   assoc-in [:bot (parse-long (second line))]
                   {:low [(keyword (nth line 5)) (parse-long (nth line 6))]
                    :high [(keyword (nth line 10)) (parse-long (nth line 11))]}))))
           
(defn has-two-chips [[_bot chips]]
  (= 2 (count chips)))

(defn active-bots []
  (filter has-two-chips (:bot @state)))

(defn bot-with-chips [a b]
  (some (fn [[k v]] (when (= v #{a b}) k)) (:bot @state)))

(defn get-form-input-bin [[chip bot]]
  (swap! state assoc-in [:bot bot] (conj (get-in @state [:bot bot] #{}) chip)))

(defn initialize-state []
  (doseq [chip-info (:value @instructions)]
    (get-form-input-bin chip-info)))

(defn give-chip [owner chip to id]
  (swap! state assoc-in [to id] (conj (get-in @state [to id] #{}) chip))
  (swap! state assoc-in [:bot owner] (disj (get-in @state [:bot owner]) chip)))

(defn pass-chips [[owner chips]]
  (doseq [chip chips]
    (let [rank (if (= chip (apply min chips)) :low :high)
          recipient (get-in @instructions [:bot owner rank])]
      (give-chip owner chip (first recipient) (second recipient)))))

(defn -main []
  (get-instructions (get-lines))
  (initialize-state)
  (while (nil? (bot-with-chips 61 17))
    (doseq [bot-info (active-bots)]
      (pass-chips bot-info)))
  (bot-with-chips 61 17))

(comment
  (-main))

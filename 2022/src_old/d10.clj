(ns coa.d10
  (:require
   [clojure.string :as s]))

(defn- get-input [file-name]
  (->> (slurp file-name)
       s/split-lines
       (map #(s/split % #" "))
       (map (fn [e] (if (< (count e) 2) e [(first e) (read-string (second e))])))))

(defn- get-signal [{:keys [signals last-signal] :as acc} [command value]]
  (let [cycle-n (first last-signal)
        signal (second last-signal)]
    (if (= command "addx")
      (assoc acc
             :signals (concat signals [[(+ 1 cycle-n) signal] [(+ 2 cycle-n) signal]])
             :last-signal [(+ 2 cycle-n) (+ value signal)])
      (assoc acc
             :signals (concat signals [[(+ 1 cycle-n) signal]])
             :last-signal [(+ 1 cycle-n) signal]))))

(defn- get-symbols [symb [cycle-n signal]]
  (str symb (if (<= (dec signal) (rem (dec cycle-n) 40) (inc signal)) "#" " ")))

(defn -main []
  (let [commands (get-input "input/input10.txt")
        signals (:signals (reduce get-signal {:signals [] :last-signal [0 1]} commands))
        symbols (reduce get-symbols "" signals)]
    (doseq [line (map s/join (partition 40 symbols))]
      (println line))))

(comment
  (-main))

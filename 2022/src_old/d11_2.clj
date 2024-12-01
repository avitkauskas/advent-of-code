(ns coa.d11-2)

(def items
  [[92, 73, 86, 83, 65, 51, 55, 93]
   [99, 67, 62, 61, 59, 98]
   [81, 89, 56, 61, 99]
   [97, 74, 68]
   [78, 73]
   [50]
   [95, 88, 53, 75]
   [50, 77, 98, 85, 94, 56, 89]])

(def lcm (* 11 2 5 17 19 7 3 13))

(def monkeys
  [{:op (fn [it] (rem (* it 5) lcm)) :div 11 :t 3 :f 4}
   {:op (fn [it] (rem (* it it) lcm)) :div 2 :t 6 :f 7}
   {:op (fn [it] (rem (* it 7) lcm)) :div 5 :t 1 :f 5}
   {:op (fn [it] (rem (+ it 1) lcm)) :div 17 :t 2 :f 5}
   {:op (fn [it] (rem (+ it 3) lcm)) :div 19 :t 2 :f 3}
   {:op (fn [it] (rem (+ it 5) lcm)) :div 7 :t 1 :f 6}
   {:op (fn [it] (rem (+ it 8) lcm)) :div 3 :t 0 :f 7}
   {:op (fn [it] (rem (+ it 2) lcm)) :div 13 :t 4 :f 0}])

(def monkey-inspections (atom (vec (repeat (count monkeys) 0))))

(defn- register-inspection [inspections monkey-idx]
  (assoc inspections monkey-idx (inc (nth inspections monkey-idx))))

(defn- get-monkey-action [monkey-idx]
  (let [{:keys [op div t f]} (nth monkeys monkey-idx)]
    (fn [items item]
      (let [new-item (op item)
            next-monkey-idx (if (= (rem new-item div) 0) t f)
            next-monkey-items (conj (nth items next-monkey-idx) new-item)
            monkey-items (vec (rest (nth items monkey-idx)))]
        (swap! monkey-inspections register-inspection monkey-idx)
        (assoc items monkey-idx monkey-items next-monkey-idx next-monkey-items)))))

(defn- monkey-turn [items monkey-idx]
  (let [monkey-items (nth items monkey-idx)
        monkey-action (get-monkey-action monkey-idx)]
    (reduce monkey-action items monkey-items)))

(defn- play-round [items _]
  (reduce monkey-turn items (range (count monkeys))))

(defn -main []
  (reduce play-round items (range 10000))
  (println (apply * (take 2 (reverse (sort @monkey-inspections))))))

(comment
  (-main))

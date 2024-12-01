(ns aoc.day13
  (:require
    [clojure.string :as str]))

(defn read-input []
  (->> (slurp "data/input13.txt")
       (str/split-lines)
       (map vec)))

(defn cmp-rc [a b]
  (compare (:rc a) (:rc b)))

(def cart-set #{\^ \v \< \>})

(def init-carts
  (into (sorted-set-by cmp-rc)
    (for [[r row] (map-indexed vector (read-input))
          [c cell] (map-indexed vector row)
          :when (cart-set cell)]
      {:rc [r c] :dir cell :state 0}))) 

(defn cart->dir [cell]
  (if (cart-set cell)
    ({\^ \| \v \| \< \- \> \-} cell)
    cell))

(def tracks
  (mapv (fn [row] (mapv (fn [cell] (cart->dir cell)) row))
        (read-input)))

(def offset-map {\^ [-1 0] \v [1 0] \< [0 -1] \> [0 1]})

(defn next-rc [rc dir]
  (mapv + rc (offset-map dir)))

(def curve-set #{\\ \/})
(def curve-map {\/ {\^ \>, \v \<, \> \^, \< \v}
                \\ {\^ \<, \v \>, \> \v, \< \^}})

(def inter-set #{\+})
(def inter-map {\^ [\< \^ \>]
                \v [\> \v \<]
                \> [\^ \> \v]
                \< [\v \< \^]})

(defn turn [cell dir state]
  (cond
    (curve-set cell) [(get-in curve-map [cell dir]) state]
    (inter-set cell) [(get-in inter-map [dir state]) (rem (inc state) 3)]
    :else [dir state]))

(defn move [{:keys [rc dir state] :as _cart}]
  (let [rc (next-rc rc dir)
        cell (get-in tracks rc)
        [dir state] (turn cell dir state)]
    {:rc rc :dir dir :state state}))

(defn move-reducer-p1 [{:keys [moved remaining]} cart]
  (let [moved-cart (move cart)
        rc (:rc moved-cart)
        remaining (disj remaining cart)
        remaining-rc (into #{} (map :rc remaining))
        moved-rc (into #{} (map :rc moved))]
    (if (or (moved-rc rc)
            (remaining-rc rc))
      (reduced {:moved #{moved-cart} :remaining #{}})
      {:moved (conj moved moved-cart) :remaining remaining})))

(defn move-reducer-p2 [{:keys [moved remaining] :as accumulator} cart]
  (if (remaining cart)
    (let [moved-cart (move cart)
          rc (:rc moved-cart)
          remaining (disj remaining cart)
          remaining-rc (into #{} (map :rc remaining))
          moved-rc (into #{} (map :rc moved))]
      (if (moved-rc rc)
        {:moved (disj moved moved-cart) :remaining remaining}
        (if (remaining-rc rc)
          {:moved moved :remaining (disj remaining moved-cart)}
          {:moved (conj moved moved-cart) :remaining remaining})))
    accumulator))

(defn tick [carts reducer]
  (-> (reduce reducer {:moved (sorted-set-by cmp-rc) :remaining carts} carts)
      :moved))

(defn format-output [[r c]]
  (str c "," r))

(defn part-1 []
  (loop [carts init-carts]
    (if (= 1 (count carts))
      (format-output (:rc (first carts)))
      (recur (tick carts move-reducer-p1)))))

(defn part-2 []
  (loop [carts init-carts]
    (if (= 1 (count carts))
      (format-output (:rc (first carts)))
      (recur (tick carts move-reducer-p2)))))

(comment
  (part-1)
  (part-2))

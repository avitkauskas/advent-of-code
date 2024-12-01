(ns aoc.d05-2
  (:require [clj-commons.digest :as d]
            [clojure.string :as s]))

(def door-id "cxdnnyjw")

(defn- interesting-hash [pswd index]
  (let [md5-hash (d/md5 (str door-id index))
        pos (- (int (nth md5-hash 5)) (int \0))]
    (if (and (s/starts-with? md5-hash "00000")
             (< pos 8)
             (= \_ (pswd pos)))
      (let [new-pswd (assoc pswd pos (nth md5-hash 6))]
        (println (apply str new-pswd))
        (if (not-any? #{\_} new-pswd)
          (reduced new-pswd)
          new-pswd))
      pswd)))

(defn -main []
  (let [empty-pswd "________"]
    (println empty-pswd)
    (->> (reduce interesting-hash (vec empty-pswd) (range))
         (reduce str))))

(comment
  (-main))

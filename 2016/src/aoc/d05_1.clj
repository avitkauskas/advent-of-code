(ns aoc.d05-1
  (:require [clj-commons.digest :as d]
            [clojure.string :as s]))

(def door-id "cxdnnyjw")

(defn- interesting-hash [pswd index]
  (let [md5-hash (d/md5 (str door-id index))]
    (if (s/starts-with? md5-hash "00000")
      (let [new-pswd (str pswd (nth md5-hash 5))]
        (if (= 8 (count new-pswd))
          (reduced new-pswd)
          new-pswd))
      pswd)))

(defn -main []
  (reduce interesting-hash "" (range)))

(comment
  (-main))

(ns coa.d7
  (:require
   [clojure.string :as s]))

(defn- parse-line [line]
  (if (s/starts-with? line "$")
    (subs line 5)
    (read-string line)))

(defn- read-input [fs line]
  (if (string? line)
    (if (= line "..")
      (assoc fs :pwd (pop (:pwd fs)))
      (let [fs (assoc fs :pwd (conj (:pwd fs) line))]
        (assoc-in fs (conj (:pwd fs) :size) 0)))
    (let [size-path (conj (:pwd fs) :size)]
      (assoc-in fs size-path (+ line (get-in fs size-path 0))))))

(defn- get-fs-info [file-name]
  (dissoc
   (->> ((comp s/split-lines slurp) file-name)
        (remove #(or (= "$ ls" %) (s/starts-with? % "dir")))
        (map parse-line)
        (reduce read-input {:pwd []}))
   :pwd))

(defn- sum-inner-sizes [inner-sizes curr-dir]
  (let [inner-sizes (pop inner-sizes)
        curr-dir-len (count curr-dir)
        filtered (filter #(and (= (take curr-dir-len (first %)) curr-dir)
                               (= (count (first %)) (inc curr-dir-len)))
                         inner-sizes)]
    (apply + (map second filtered))))

(defn- calc-sizes [sizes dirs]
  (let [dir-name (first dirs)
        pwd (conj (peek sizes) dir-name)
        dir-info-map (second dirs)
        dir-size (:size dir-info-map)
        inner-dirs-map (dissoc dir-info-map :size)
        inner-dirs-vec (into [] inner-dirs-map)]
    (if (empty? inner-dirs-map)
      (into [] (concat [[pwd dir-size]] sizes))
      (let [inner-sizes (reduce calc-sizes [pwd] inner-dirs-vec)]
        (into [] (concat [[pwd (+ (sum-inner-sizes inner-sizes pwd) dir-size)]]
                         (pop inner-sizes)
                         sizes))))))

(defn -main []
  (let [fs (get-fs-info "input/input7.txt")
        sizes (into [] (sort-by second (pop (reduce calc-sizes [[]] (into [] fs)))))
        space-needed (- 30000000 (- 70000000 (second (peek sizes))))]
    ; (apply + (map second (filter #(<= (second %) 100000) sizes)))
    (println (second (first (filter #(> (second %) space-needed) sizes))))))

(comment
  (-main))

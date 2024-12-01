(ns aoc.day16 
  (:require
    [clojure.string :as str]
    [clojure.set :as set]))

(defn read-input []
  (-> (slurp "data/input16.txt")
      (str/split #"\n\n\n\n")))

(defn parse-sample [[s1 s2 s3]]
  (let [in (read-string s1)
        out (read-string s3)
        [op a b c] (->> (str/split s2 #" ") (map parse-long))]
    {:op op :a a :b b :c c :in in :out out}))

(def samples
  (-> (read-input)
      (first)
      (str/split #"\n\n")
      (->> (map #(str/replace % #"Before: " ""))
           (map #(str/replace % #"After:  " ""))
           (map #(str/split-lines %))
           (mapv parse-sample))))

(defn addr [{:keys [in a b c]}]
  (update in c (fn [_] (+ (in a) (in b)))))

(defn addi [{:keys [in a b c]}]
  (update in c (fn [_] (+ (in a) b))))

(defn mulr [{:keys [in a b c]}]
  (update in c (fn [_] (* (in a) (in b)))))

(defn muli [{:keys [in a b c]}]
  (update in c (fn [_] (* (in a) b))))

(defn banr [{:keys [in a b c]}]
  (update in c (fn [_] (bit-and (in a) (in b)))))

(defn bani [{:keys [in a b c]}]
  (update in c (fn [_] (bit-and (in a) b))))

(defn borr [{:keys [in a b c]}]
  (update in c (fn [_] (bit-or (in a) (in b)))))

(defn bori [{:keys [in a b c]}]
  (update in c (fn [_] (bit-or (in a) b))))

(defn setr [{:keys [in a c]}]
  (update in c (fn [_] (in a))))

(defn seti [{:keys [in a c]}]
  (update in c (fn [_] a)))

(defn gtir [{:keys [in a b c]}]
  (update in c (fn [_] (if (> a (in b)) 1 0))))

(defn gtri [{:keys [in a b c]}]
  (update in c (fn [_] (if (> (in a) b) 1 0))))

(defn gtrr [{:keys [in a b c]}]
  (update in c (fn [_] (if (> (in a) (in b)) 1 0))))

(defn eqir [{:keys [in a b c]}]
  (update in c (fn [_] (if (= a (in b)) 1 0))))

(defn eqri [{:keys [in a b c]}]
  (update in c (fn [_] (if (= (in a) b) 1 0))))

(defn eqrr [{:keys [in a b c]}]
  (update in c (fn [_] (if (= (in a) (in b)) 1 0))))

(def functions #{addr addi mulr muli banr bani borr bori setr seti gtir gtri gtrr eqir eqri eqrr})

(defn count-variants [sample]
  (->> (map #(if (= (:out sample) (% sample)) 1 0) functions)
       (apply +)))

(defn part-1 []
  (->> (map count-variants samples)
       (filter #(>= % 3))
       (count)))

(def init-opcodes
  (into {} (for [i (range (count functions))] [i functions])))

(defn check-sample [opcodes sample]
  (let [valid-opcodes (into #{} (for [fun functions :when (= (:out sample) (fun sample))] fun))]
    (update opcodes (:op sample) #(set/intersection % valid-opcodes))))

(defn find-opcodes-candidates []
  (->> (reduce check-sample init-opcodes samples)))

(defn deduce-opcodes [candidates]
  (loop [deduced {}
         candidates candidates]
    (if (empty? candidates)
      (into {} (map (fn [[i f]] [i (first f)]) deduced))
      (let [counts (map (fn [[i fs]] [i (count fs)]) candidates)
            uniques (->> (filter #(= (second %) 1) counts) (map first))
            found (map (fn [i] [i (candidates i)]) uniques)
            deduced (into deduced found)
            candidates (reduce (fn [m i]
                                 (into {} 
                                       (map (fn [[j fs]]
                                              [j (set/difference fs (deduced i))])
                                            m)))
                               candidates
                               uniques)
            candidates (reduce #(dissoc %1 %2) candidates uniques)]
        (recur deduced candidates)))))

(def program
  (->> (read-input)
       (second)
       (str/split-lines)
       (map #(read-string (str "[" % "]")))))

(defn part-2 []
  (let [ops (-> (find-opcodes-candidates) (deduce-opcodes))
        regs (reduce (fn [r [op a b c]] ((ops op) {:in r :a a :b b :c c})) [0 0 0 0] program)]
    (regs 0)))

(comment
  (part-1)
  (part-2))

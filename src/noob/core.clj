;; This is practice from braveclojure website
(ns noob.core
  (:gen-class))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  ([part]
   matching-part part "right")
  ([part & other-part-names]
   (reduce
    (fn [acc other-part]
      (conj acc
            {:name (clojure.string/replace (:name part)
                                           #"^left-"
                                           (str other-part "-"))
             :size (:size part)}))
    [part]
    other-part-names)))

;; reinvent reduce
;; (defn my-reduce
;;   [f initial coll]
;;   (loop [acc initial
;;          items coll]
;;     (if (empty? items)
;;       acc
;;       (let [[head & remaining] items]
;;         (recur (f acc head) remaining)))))

;; how to use recur
(defn symmetrize-body-parts
  ([parts]
   symmetrize-body-parts parts "right")
  ([parts & other-part-names]
   (reduce (fn [acc part]
             (into acc (set (apply matching-part part other-part-names))))
           []
           parts)))

;; exercise 6 from https://www.braveclojure.com/do-things/
(defn symmetrize-body-parts-with-amount
  [parts amount]
  (let [other-part-names
        (cond (= 2 amount) ["right"]
              (= 5 amount) ["right" "top" "left-bottom" "right-bottom"]
              :else ["undefined"])]
    (apply symmetrize-body-parts parts other-part-names)))

(defn hit
  [asym-body-parts]
  (let [sym-parts (symmetrize-body-parts asym-body-parts)
        body-parts-size-sum (reduce + (map :size sym-parts))
        target (rand body-parts-size-sum)]
    (loop [accumulated-size 0
           [part & remaining] sym-parts]
      (if (> accumulated-size target)
        part
        (recur (+ accumulated-size (:size part)) remaining)))))

(defn hello
  ([]
   "Hello, is it me you're looking for?")
  ([name]
   (str "May I help you, " name))
  ([greeting, name]
   (str greeting name)))

;; create function that return function
;; exercise 3 from https://www.braveclojure.com/do-things/
(defn dec-maker [x]
  (fn [a] (- a x)))

(def dec9 (dec-maker 9))

;; mapset
;; exercise 4 from https://www.braveclojure.com/do-things/
(defn mapset [f coll]
  (set (map f coll)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Where should i hit a hobbit?")
  (print (str (hit asym-hobbit-body-parts))))

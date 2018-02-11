(ns noob.core
  (:gen-class))

(defn even-numbers
  "generate infinite even-numbers"
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

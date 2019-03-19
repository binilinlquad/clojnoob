(ns noob.core.future-atom
  (:refer-clojure)
  (:require [clojure.string :as str]))

(defn split-words
  "split sentence into list of words"
  [quote]
  (str/split quote #" "))

(defn recount-words
  "calculate word occurrence in list
  and return it as pair word and counter"
  [pairs-word-counter words]
  (loop [word (first words)
         rst (rest words)]
    (if (nil? word)
      pairs-word-counter
      (do
        (swap! pairs-word-counter update word (fnil inc 0))
        (recur (first rst) (rest rst))))))

(defn request-and-count-words
  "create future for convert random quote and calculate each word occurrence"
  []
  (future
    (->>
     (slurp "https://www.braveclojure.com/random-quote")
     split-words
     (recount-words (atom {})))))

(defn join-quotes
  "join each future of atom into a map"
  [futures]
  (reduce
   (fn
     [acc item]
     (merge-with + acc @@item))
   {}
   futures))

(defn request-quote-multiple-times
  "convert quote and calculate word occurrence n times"
  [n]
  (->
   (repeatedly n #(request-and-count-words))
   join-quotes))

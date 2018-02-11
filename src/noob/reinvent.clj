(ns noob.core
  (:gen-class))

(defn my-reduce
  "reinvent reduce"
  [f initial coll]
  (loop [acc initial
         items coll]
    (if (empty? items)
      acc
      (let [[head & remaining] items]
        (recur (f acc head) remaining)))))

(defn map-by-reducing [f coll]
  (lazy-seq
   (reduce (fn [acc e]
             (conj acc (f e)))
           []
           coll)))

(defn map-by-reducing-eager [f coll]
  (seq
   (reduce (fn [acc e]
             (conj acc (f e)))
           []
           coll)))

(defn filter-by-reducing [f coll]
  (seq
   (reduce (fn [acc e]
             (if (f e)
               (conj acc e)
               acc))
           []
           coll)))

(defn some-by-reducing [f coll]
  ;; use reduced to terminate reduce operation
  ;; use (f e) as reduced value so it fulfill some contract
  (reduce (fn [ret e]
            (if (f e)
              (reduced (f e))
              false))
          false
          coll))

(defn my-partial
  "create partial-fn by hand"
  [partial-fn & args]
  (fn [& moar-args]
    (apply partial-fn (into args moar-args))))

;; Chapter 5
(defn my-comp
  "create comp by hand"
  ([f]
   (fn [x]
     (f x)))
  ([f g]
   (fn [x]
     (f (g x))))
  ([f g & fs]
   (fn [x]
     (f (g ((apply my-comp fs) x))))))

(defn my-assoc-in
  "implement association in by hand.
  It doesn't handle stack overflow"
  [m [k & ks] v]
  (if (empty? ks)
    (assoc m k v)
    (assoc m k (my-assoc-in (get m k) ks v))))

(defn my-better-assoc-in
  "implement association in by hand and should not
  result stack overflow problem"
  [m [k & ks] v]
  (loop [func assoc
         map m
         key k
         remaining ks
         value v]
    (if (empty? remaining)
      (func map key value)
      (recur (fn [tm tk tv]
               (assoc map key (assoc tm tk tv)))
             (get map key)
             (first remaining)
             (rest remaining)
             value))))

(defn my-update-in
  "implement update-in by hand.
  It doesn't handle stack overflow"
  [m [k & ks] f & args]
  (if (empty? ks)
    (assoc m k (apply f (cons (get m k) args)))
    (assoc m k (my-update-in (get m k)
                             ks
                             f
                             args))))


(defn my-better-update-in
  "implement update-in with recur and should not
  result stack overflow"
  [m [k & ks] f & args]
  (loop [func assoc
         map m
         key k
         remaining ks]
    (if (empty? remaining)
      (func map key (apply f (cons (get map key) args)))
      (recur (fn [tm tk tv]
               (assoc map key (assoc tm tk tv)))
             (get map key)
             (first remaining)
             (rest remaining)))))

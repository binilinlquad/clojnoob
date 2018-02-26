(ns noob.core.macro
  (:refer-clojure))

(defmacro ignore-last-operand
  "Never use the last operand"
  [function-call]
  (butlast function-call))

(defn second-rest
  "Return except two first elements"
  [lst]
  (rest (rest lst)))

(defn last-element
  [lst]
  (= 1 (count lst)))

(defmacro infix
  "Evaluate infix expression with calling (infix (op operation op)).
  Statement will be evaluated from left to right whatever operation"
  [expr]
  (list (second expr)
        (first expr)
        (if (last-element (second-rest expr))
          (last expr)
          `(infix ~(second-rest expr)))))

(defn print-favorite-movie
  []
  (read-string (str (list 'println "cc" "Interstellar"))))

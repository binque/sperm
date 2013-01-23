;; coding: utf-8
;; Copyright (C) dirlt

(defn fib [n]
  (cond (= n 0) 1
        (= n 1) 1
        true (+ (fib (- n 1)) (fib (- n 2)))))
(def x (time (fib 10)))
(println x)

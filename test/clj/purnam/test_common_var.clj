(ns purnam.test-common-var
  (:use midje.sweet)
  (:require [purnam.common.var :as j]
            [purnam.checks :refer [matches]]))

(fact "make-var"
  (j/make-var {})
  => (matches
      '(let [%1 (js-obj)] %1))

  (j/make-var [])
  => '(array)

  (j/make-var {:a {:b {:c 1}}})
  => (matches
      '(let [%1 (js-obj)]
         (aset %1 "a" (let [%2 (js-obj)]
                        (aset %2 "b" (let [%3 (js-obj)]
                                       (aset %3 "c" 1) %3)) %2)) %1))

  (j/make-var {:a 'self})
  => (matches '(let [%1 (js-obj)] (aset %1 "a" %1) %1))

  (j/make-var {:a 'this})
  => (matches '(let [%1 (js-obj)] (aset %1 "a" this) %1)))

(ns purnam.test-common-accessors
  (:require [purnam.test])
  (:use-macros [purnam.common :only [aset-in aset-in-obj aget-in set-safe-aget]]
               [purnam.test.clojure :only [deftest is]]))

(set-safe-aget true)

(def o (js* "{a:{b:1}}"))

(deftest "testing aget"
   (is (aget-in o ["a" "b"]) 1)
   (is (aget-in o ["a" "c"]) nil)
   (is (aget-in o ["b" "c"]) nil)
   (is (aget-in o ["a" "b" "c"]) nil))

(deftest "testing aset"
  (is (aset-in-obj (js* "{a:1}") ["b"] 2) (js* "{a:1,b:2}"))
  (is (aset-in-obj (js* "{a:{b:1}}") ["a" "b"] 2) (js* "{a:{b:2}}"))
  (is (aset-in-obj (js* "{a:{b:1}}") ["a" "b" "c"] 1) (js* "{a:{b:1}}"))
  (is (aset-in-obj (js* "{a:{b:1}}") ["a" "c"] 2) (js* "{a:{b:1,c:2}}"))
  (is (aset-in-obj (js* "{}") ["a" "b" "c"] 1) (js* "{a:{b:{c:1}}}"))
  (is (aset-in-obj (js* "{a:{b:{c:1}}}") ["a" "b" "c"] 2) (js* "{a:{b:{c:2}}}")))
  
(deftest "testing that aset works for "
  (def t0 (js* "{a:1}"))
  (aset-in t0 ["a"] 2)
  (is t0 (js* "{a:2}"))
  
  (def t1 (js* "{a:{b:{c:1}}}"))
  (aset-in t1 ["a" "b" "c"] 2)
  (is t1 (js* "{a:{b:{c:2}}}")))
  

(set-safe-aget false)
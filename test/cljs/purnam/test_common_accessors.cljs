(ns purnam.test-common-accessors
  (:require [purnam.test.helpers])
  (:use-macros [purnam.core :only [aset-in aget-in]]
               [purnam.test :only [deftest is init]]))
  
(init)

(deftest "testing aget"
   (is (aget-in (js* "{a:{b:1}}") ["a" "b"]) 1)
   (is (aget-in (js* "{a:{b:1}}") ["a" "c"]) nil)
   (is (aget-in (js* "{a:{b:1}}") ["b" "c"]) nil)
   (is (aget-in (js* "{a:{b:1}}") ["a" "b" "c"]) nil))

(deftest "testing aset"
  (is (aset-in (js* "{a:1}") ["a"] 2) (js* "{a:2}"))
  (is (aset-in (js* "{a:{b:1}}") ["a" "b"] 2) (js* "{a:{b:2}}"))
  (is (aset-in (js* "{a:{b:1}}") ["a" "b" "c"] 1) (js* "{a:{b:1}}"))
  (is (aset-in (js* "{a:{b:1}}") ["a" "c"] 2) (js* "{a:{b:1,c:2}}"))
  (is (aset-in (js* "{}") ["a" "b" "c"] 1) (js* "{a:{b:{c:1}}}"))
  (is (aset-in (js* "{a:{b:{c:1}}}") ["a" "b" "c"] 2) (js* "{a:{b:{c:2}}}")))
  
(deftest "testing that aset works for "
  (def t0 (js* "{a:1}"))
  (aset-in t0 ["a"] 2)
  (is t0 (js* "{a:2}"))
  
  (def t1 (js* "{a:{b:{c:1}}}"))
  (aset-in t1 ["a" "b" "c"] 2)
  (is t1 (js* "{a:{b:{c:2}}}")))
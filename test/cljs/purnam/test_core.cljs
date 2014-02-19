(ns purnam.test-core
  (:require [purnam.test.common])
  (:use-macros [purnam.core :only [aset-in aget-in obj arr]]
               [purnam.test :only [deftest is init]]))
               
(deftest "obj creates a js object"
  (is (obj :a 1 :b 2) (js* "{a:1,b:2}"))
  (is (obj :a 1 :b [1 2 3]) (js* "{a:1,b:[1,2,3]}"))
  (is (obj :a 1 :b [1 2 {:c 3}]) (js* "{a:1,b:[1,2,{c:3}]}")))

(deftest "arr creates a js array"
  (is (arr {:a 1} {:b [2 3 4]}) (js* "[{a:1}, {b:[2,3,4]}]")))
  
(def ka "a")
(def kb "b")  

(deftest "aget-in with obj"
  (is (aget-in (obj :a {:b 1}) ["a" "b"]) 1)
  (is (aget-in (obj :a {:b 1}) [ka kb]) 1)
  (is (aget-in (obj :a {:b 1}) [:a :b]) 1)
  (is (aget-in (obj :a {:b 1}) [:a]) (obj :b 1)))
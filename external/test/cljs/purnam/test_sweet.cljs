(ns purnam.test-sweet
  (:use-macros [purnam.test :only [fact]]))

(fact [[{:doc "One Plus One"
         :globals [o1 (js* "{a:1}")]}]]

  (+ 1 1) => 2
  
  (str 1 2 3) => "123"
  
  o1 => (js* "{a:1}")
  
  (js* "{a:1,b:3}") 
  => (js* "{a:1,b:3}"))
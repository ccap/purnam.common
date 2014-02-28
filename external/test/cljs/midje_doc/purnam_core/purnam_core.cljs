(ns midje-doc.api.purnam-core
  (:require [purnam.test]
            [purnam.test.common :refer [js-equals]])
  (:use-macros [purnam.core :only [? ?> ! !> f.n def.n do.n
                                 obj arr def* do*n def*n]]
               [purnam.test :only [fact facts]]))
               
(facts [[{:doc "! - setter"}]]

  "The `!` form provides setting using dot notation:"
  (let [o (obj)]
    (! o.a 6)  
    (? o.a)) => 6)
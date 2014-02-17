(ns purnam.test-common-raw
  (:use midje.sweet)
  (:require [purnam.common.raw :as j] :reload))


(fact "walk-raw"
  (j/walk-raw 1)
  => 1

  (j/walk-raw {:a {:b {:c 1}}})
  => '(purnam.core/obj :a (purnam.core/obj :b (purnam.core/obj :c 1)))

  (j/walk-raw #{:a {:b {:c 1}}})
  => '(clojure.core/set (purnam.core/obj :b (purnam.core/obj :c 1)) :a)


  (j/walk-raw '(fn [] {:a 1}))
  => '(fn [] (purnam.core/obj :a 1)))

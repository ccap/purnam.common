(ns purnam.test-walk-expand
  (:use midje.sweet)
  (:require [purnam.core.expand :as j]
            [purnam.core.common :refer :all]
            [purnam.checks :refer :all]))

(swap! purnam.core.expand/*exclusions* conj 'go.n)

(fact "expand-sym"
  (j/expand-sym 'this)
  => '(js* "this")

  (j/expand-sym 'a)
  => 'a

  (j/expand-sym 'j/a)
  => 'j/a

  (j/expand-sym 'j/.a)
  => 'j/.a

  (j/expand-sym 'j/.a.b)
  => '(purnam.core.accessors/aget-in j/.a ["b"])

  (j/expand-sym 'a.b)
  => '(purnam.core.accessors/aget-in a ["b"])

  (j/expand-sym 'j/a.b)
  => '(purnam.core.accessors/aget-in j/a ["b"])

  (j/expand-sym 'a.b.c)
  => '(purnam.core.accessors/aget-in a ["b" "c"])

  (j/expand-sym 'a.b.c/d)
  => 'a.b.c/d)

(def test.sym nil)

(fact "expand"
  (j/expand '(test.sym 1))
  => '(test.sym 1)

  (j/expand '(purnam.test-walk-expand/test.sym 1))
  => '(purnam.test-walk-expand/test.sym 1)

  (j/expand '(a.b 1))
  => '(let [obj# (purnam.core.accessors/aget-in a [])
            fn#  (aget obj# "b")]
        (.call fn# obj# 1)))

(fact "expand with-exclusions"
  (j/expand '(go.n 1))
  => '(go.n 1)

  (j/expand '(do.n 1))
  => '(let [obj# (purnam.core.accessors/aget-in do [])
            fn# (aget obj# "n")]
        (.call fn# obj# 1))

  (j/expand 'go.n)
  => 'go.n)
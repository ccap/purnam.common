(ns purnam.test-common-expand
  (:use midje.sweet)
  (:require [purnam.common.expand :as j]
            [purnam.common :refer :all]
            [purnam.checks :refer :all]))

(swap! purnam.common.expand/*exclusions* conj 'go.n)

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
  => '(purnam.core/aget-in j/.a ["b"])

  (j/expand-sym 'a.b)
  => '(purnam.core/aget-in a ["b"])

  (j/expand-sym 'j/a.b)
  => '(purnam.core/aget-in j/a ["b"])

  (j/expand-sym 'a.b.c)
  => '(purnam.core/aget-in a ["b" "c"])

  (j/expand-sym 'a.b.c/d)
  => 'a.b.c/d)

(defmacro test.sym [] nil)

(fact "expand"
  (j/expand '(test.sym 1))
  => '(test.sym 1)

  (j/expand '(purnam.test-common-expand/test.sym 1))
  => '(purnam.test-common-expand/test.sym 1)

  (j/expand '(a.b 1))
  => '(let [obj# (purnam.core/aget-in a [])
            fn#  (aget obj# "b")]
        (.call fn# obj# 1)))

(fact "expand with-exclusions"
  (j/expand '(go.n 1))
  => '(go.n 1)

  (j/expand '(do.n 1))
  => '(let [obj# (purnam.core/aget-in do [])
            fn# (aget obj# "n")]
        (.call fn# obj# 1))

  (j/expand 'go.n)
  => 'go.n)

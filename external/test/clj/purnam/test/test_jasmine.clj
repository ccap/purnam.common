(ns purnam.test.test-jasmine
  (:use midje.sweet)
  (:require [purnam.test.jasmine :as j]
            [purnam.core :refer [! ?]]
            [purnam.test :refer [describe it is]]))

(fact "it-fn"
  (j/it-fn "<DESC>" '[<BODY>])
  => '(js/it "<DESC>" (clojure.core/fn [] <BODY>)))

(fact "describe-bind-vars"
  (j/describe-bind-vars '<SPEC> ['a 1])
  => '((aset <SPEC> "a" 1)))

(fact "describe-roots-map"
  (j/describe-roots-map '<SPEC> ['a 1])
  => '{a <SPEC>.a})

(fact "describe-fn"
  (j/describe-fn {:doc "hello"} '[<BODY>])
  => '(let [spec (js-obj)]
        (js/describe "hello"
                     (clojure.core/fn [] <BODY> nil))))

(fact "describe-fn"
  (j/describe-fn {:doc "hello"}
                 '[(it "has stuff"
                       (is (let [o (js-obj)]
                             (! o.a 4)
                             (? o.a))
                           4))])
 ;;=> (let [spec (js-obj)] (js/describe "hello" (clojure.core/fn [] (it "has stuff" (purnam.test/is (let [o (js-obj)] (! o.a 4) (? o.a)) 4 "'(let [o (js-obj)] (! o.a 4) (? o.a))'" "'4'")) nil)))
  )

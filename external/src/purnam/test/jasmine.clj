(ns purnam.test.jasmine
  (:require [purnam.common :refer :all]
            [purnam.common.expand :refer [expand]]
            [purnam.common.scope :refer [change-roots-map]]))

(def l list)

(def describe-default-options
  {:doc  ""
   :spec 'spec
   :vars []
   :globals []})

(defn describe-bind-vars
  [spec vars]
  (let [bindings (partition 2 vars)]
    (apply list
           (map (fn [[v b]]
                  (list 'aset spec (str v) b))
                bindings))))

(defn describe-roots-map
  [spec vars]
  (let [bindings (partition 2 vars)]
    (into {}
          (map (fn [[v _]]
                 [v (symbol (str spec "." v))])
                bindings))))

(defn describe-fn [options body]
  (let [[options body]
        (if (hash-map? options)
          [(merge describe-default-options options) body]
          [describe-default-options (cons options body)])
        {:keys [doc spec globals vars]} options]
    (expand
     (concat (l 'let (apply vector spec '(js-obj) globals))
             (describe-bind-vars spec vars)
             (l (l 'js/describe doc
                   `(fn [] ~@(change-roots-map
                             body
                             (describe-roots-map spec vars))
                      nil)))))))

(defn it-preprocess [desc body]
  (if (string? desc)
    [desc body]
    ["" (cons desc body)]))

(defn it-fn [desc body]
  (list 'js/it desc
        `(fn [] ~@body)))

(defn is-fn [actual expected]
  (list '.toSatisfy (list 'js/expect (expand actual)) (expand expected) (str actual) (str expected)))

(defn is-not-fn [actual expected]
  (list '.toSatisfy
     (list '.-not
       (list 'js/expect (expand actual)))
             (expand expected)
             (str actual)
             (str expected)))

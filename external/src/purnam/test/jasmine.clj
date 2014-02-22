(ns purnam.test.jasmine
  (:require [purnam.common :refer :all]
            [purnam.common.expand :refer [expand]]
            [purnam.common.scope :refer [change-roots-map]]))

(add-symbols purnam.common/*exclude-expansion*
           '[purnam.test describe])

(add-symbols purnam.common/*exclude-scoping*
            '[purnam.test describe])
            
(def ^:dynamic *current-roots* nil)

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

(defn describe-expand-is [body]
  (clojure.walk/postwalk
    (fn [form]
       (if (and (list? form)
                (or (= 'is (first form))
                    (= 'purnam.test/is (first form)))
                (= 3 (count form)))
        (let [[actual expected] (rest form)]
           (list 'purnam.test/is actual expected (str (str actual)) (str expected)))
        form))
    body))

(defn describe-fn [options body]
  (let [[options body]
        (if (hash-map? options)
          [(merge describe-default-options options) body]
          [describe-default-options (cons options body)])
        {:keys [doc spec globals vars]} options]
    (binding [*current-roots* (describe-roots-map spec vars)]
      (expand
       (concat (l 'let (apply vector spec '(js-obj) globals))
               (describe-bind-vars spec vars)
               (l (l 'js/describe doc
                     `(fn [] ~@(change-roots-map
                                 (describe-expand-is body)
                               *current-roots*)
                        nil))))))))

(defn it-preprocess [desc body]
  (if (string? desc)
    [desc body]
    ["" (cons desc body)]))

(defn it-fn [desc body]
  (list 'js/it desc
        `(fn [] ~@body)))

(defn is-fn 
  ([actual expected]
    (is-fn actual expected 
      (str actual) 
      (str expected)))
  ([actual expected tactual texpected]
    (list '.toSatisfy 
      (list 'js/expect actual)
      expected
      tactual texpected)))

(defn is-not-fn 
  ([actual expected]
    (is-not-fn actual expected 
      (str actual) 
      (str expected)))
  ([actual expected tactual texpected]    
    (list '.toSatisfy
       (list '.-not
         (list 'js/expect actual))
       expected
       tactual
       texpected)))

(ns purnam.common
  (:require [purnam.common.accessors :refer [aget-in-form aset-in-form]]))

(def ^:dynamic *exclude-expansion* (atom #{}))

(def ^:dynamic *exclude-scoping* (atom #{}))

(def ^:dynamic *binding-forms* 
  (atom '#{let loop for doseq if-let when-let}))

(defmacro add-exclude-expansion [& args]
  (swap! *exclude-expansion* into args)
  `(deref *exclude-expansion*))

(defmacro remove-exclude-expansion [& args]
  (swap! *exclude-expansion* #(apply disj % args))
  `(deref *exclude-expansion*))

(defmacro add-binding-forms [& args]
  (swap! *binding-forms* into args)
  `(deref *binding-forms*))

(defmacro remove-binding-forms [& args]
  (swap! *binding-forms* #(apply disj % args))
  `(deref *binding-forms*))

(defmacro aget-in [var arr]
  (aget-in-form var (map #(if (symbol? %) % (name %)) arr)))

(defmacro aset-in [var arr val]
  (list 'let ['o# var]
    (aset-in-form 'o# (map #(if (symbol? %) % (name %)) arr) val)))
  
(defn hash-set? [obj]
  (instance? clojure.lang.APersistentSet obj))

(defn hash-map? [obj]
  (instance? clojure.lang.APersistentMap obj))

(defn lazy-seq?
  "Returns `true` if `x` is of type `clojure.lang.LazySeq`."
  [x] (instance? clojure.lang.LazySeq x))

(defmacro suppress
  "Suppresses any errors thrown.

    (suppress (error \"Error\")) ;=> nil

    (suppress (error \"Error\") :error) ;=> :error
  "
  ([body]
     `(try ~body (catch Throwable ~'t)))
  ([body catch-val]
     `(try ~body (catch Throwable ~'t
                   (cond (fn? ~catch-val)
                         (~catch-val ~'t)
                         :else ~catch-val)))))

(defmacro case-let [[var bound] & body]
  `(let [~var ~bound]
     (case ~var ~@body)))
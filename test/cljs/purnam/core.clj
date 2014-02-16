(ns purnam.core
  (:require [purnam.common :refer :all]
            [purnam.common.accessors :refer [aget-in-form aset-in-form]]
            [purnam.common.parse :refer [split-syms parse-var parse-sub-exp]]
            [purnam.common.expand :refer [expand expand-fn expand-sym]]
            [purnam.common.scope :refer [change-roots-map]]
            [purnam.common.var :refer [make-var make-js-array]]
            [purnam.common.raw :refer [walk-raw]]))
            
(defmacro add-exclusions [& args]
  (swap! purnam.common.expand/*exclusions* into args)
  `(deref purnam.common.expand/*exclusions*))

(defmacro remove-exclusions [& args]
  (swap! purnam.common.expand/*exclusions* #(apply disj % args))
  `(deref purnam.common.expand/*exclusions*))

(defmacro add-binding-forms [& args]
  (swap! purnam.common.raw/*binding-forms* into args)
  `(deref purnam.common.raw/*binding-forms*))

(defmacro remove-binding-forms [& args]
  (swap! purnam.common.raw/*binding-forms* #(apply disj % args))
  `(deref purnam.common.raw/*binding-forms*))

(defmacro aget-in [var arr]
  (aget-in-form var (map name arr)))

(defmacro aset-in [var arr val]
  (list 'let ['o# var]
    (aset-in-form 'o# (map name arr) val)))
    
(defmacro ? [sym]
  (expand-sym sym))

(defmacro ?> [& args]
  (apply list (map expand args)))

(defmacro ! [sym & [val]]
  (let [[var & ks] (split-syms sym)]
    (list 'purnam.common.accessors/aset-in (parse-var var)
          (vec (map parse-sub-exp ks))
          (expand val))))

(defmacro !> [sym & args]
  (expand-fn sym args))

(defmacro f.n [args & body]
  `(fn ~args ~@(expand body)))

(defmacro def.n [sym args & body]
  `(defn ~sym ~args
     ~@(expand body)))

(defmacro do.n [& body]
  `(do ~@(expand body)))

(defmacro obj [& args]
  (let [m (apply hash-map args)]
    (expand (make-var m))))

(defmacro arr [& args]
  (expand (make-js-array args)))

(defmacro def* [name form]
  `(def ~name
        ~(expand (walk-raw form))))

(defmacro def*n [name args & body]
  `(defn ~name ~args
         ~@(expand (walk-raw body))))

(defmacro f*n [args & body]
  `(fn ~args ~@(expand (walk-raw body))))

(defmacro do*n [& body]
  `(do ~@(expand (walk-raw body))))
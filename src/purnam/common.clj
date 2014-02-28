(ns purnam.common
  (:require [purnam.common.accessors :refer [aget-in-form aset-in-form]]))

;;----------------------------------------------------------

(def ^:dynamic *exclude-expansion* (atom #{}))

(def ^:dynamic *exclude-scoping* (atom #{}))

(def ^:dynamic *binding-forms* (atom '#{}))

(defn create-symbols [args]
  (mapcat (fn [arg]
            (if (vector? arg)
              (map #(symbol (str (first arg) "/" %))
                   (rest arg))
              (list arg)))
          args))

(defn add-symbols [atm & args]
  (apply swap! atm conj (create-symbols args)))

(defn remove-symbols [atm & args]
  (apply swap! atm disj (create-symbols args)))

(add-symbols *binding-forms* '[clojure.core let loop for doseq if-let when-let]
                             'let 'loop 'for 'doseq 'if-let 'when-let)


;;----------------------------------------------------------


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

(defn var-sym [var]
  (let [mta (meta var)
        ns (.getName (:ns mta))
        name (:name mta)]
    (symbol (str ns "/" name))))

(defn resolved-sym [sym]
  (if-let [var (suppress (resolve @#'*ns* sym))]
    (var-sym var)))

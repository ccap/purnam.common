(ns purnam.common)

(def ^:dynamic *exclusions* (atom #{}))

(def ^:dynamic *binding-forms* 
  (atom '#{let loop for doseq if-let when-let}))

(defmacro add-exclusions [& args]
  (swap! *exclusions* into args)
  `(deref *exclusions*))

(defmacro remove-exclusions [& args]
  (swap! *exclusions* #(apply disj % args))
  `(deref *exclusions*))

(defmacro add-binding-forms [& args]
  (swap! *binding-forms* into args)
  `(deref *binding-forms*))

(defmacro remove-binding-forms [& args]
  (swap! *binding-forms* #(apply disj % args))
  `(deref *binding-forms*))
  
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
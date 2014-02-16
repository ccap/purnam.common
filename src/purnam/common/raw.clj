(ns purnam.common.raw
  (:require [purnam.common.common :refer :all]
            [purnam.common.scope :refer [change-roots-map]]))
  
(def ^:dynamic *binding-forms* 
  (atom '#{let loop for doseq if-let when-let}))

(declare walk-raw)

(defn walk-binding-form [[f bindings & body]]
  (let [b (partition 2 bindings)
        res (-> (mapcat (fn [[k v]] [k (walk-raw v)]) b)
                vec)]
    (apply list f res (walk-raw body))))

(defn walk-lambda-form [[f bindings & body]]
  (apply list f bindings (walk-raw body)))

(defn walk-raw [form]
  (cond (vector? form)
        (apply list 'array (map walk-raw form))

        (hash-map? form)
        (apply list 'purnam.common/obj
               (mapcat (fn [[k x]] [k (walk-raw x)]) form))

        (seq? form)
        (cond (get @*binding-forms* (first form))
              (walk-binding-form form)

              (= 'fn (first form))
              (walk-lambda-form form)

              :else
              (apply list (map walk-raw form)))
        :else form))
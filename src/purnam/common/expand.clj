(ns purnam.common.expand
  (:require [purnam.common :refer :all]
            [purnam.common.parse :refer [exp? split-syms parse-var parse-exp parse-sub-exp]]))

(defn expand-sym [obj]
  (cond (exp? obj)
        (parse-exp obj)

        (= 'this obj)
        '(js* "this")

        :else obj))

(declare expand)

(defn expand-fn [sym args]
  (let [[var & ks] (split-syms sym)
        sel  (vec (butlast ks))
        fnc  (last ks)]
    (list 'let ['obj# (list 'purnam.common/aget-in (parse-var var)
                            (vec (map parse-sub-exp sel)))
                'fn#  (list 'aget 'obj# (parse-sub-exp fnc))]
          (apply list '.call 'fn# 'obj#
                 (expand args false)))))

(defn expand
  ([form] (expand form true))
  ([form pfn]
     (expand form pfn @*exclude-expansion*))
  ([form pfn ex]
     (cond (set? form) (set (map expand form))

           (hash-map? form)
           (into {}
                 (map (fn [en] (mapv expand en)) form))

           (vector? form) (mapv expand form)

           (seq? form)
           (cond (get ex (resolved-sym (first form)))
                 form

                 (and pfn (exp? (first form)))
                 (expand-fn (first form) (next form))

                 :else
                 (apply list (map expand form)))

           (get ex (resolved-sym form)) form

           :else (expand-sym form))))

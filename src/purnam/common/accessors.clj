(ns purnam.common.accessors)

(defn aget-in-form
  [var arr]
  (if (empty? arr) var
      (let [bs (gensym)]
        (list 'if-let [bs (list 'aget var (first arr))]
              (aget-in-form bs (next arr))))))

(defn nested-val-form [[k & ks] val]
  (if (nil? k) val
      (let [bs (gensym)]
        (list 'let [bs (list 'js-obj)]
              (list 'aset bs k (nested-val-form ks val))
              bs))))

(defn aset-in-form* [var [k & ks] val]
  (cond (nil? k) nil
        (empty? ks)
        (if val
          (list 'aset var k val)
          (list 'js-delete var k))
        :else
        (let [bs (gensym)]
          (list 'if-let [bs (list 'aget var k)]
                (aset-in-form* bs ks val)
                (if val
                  (list 'aset var k (nested-val-form ks val)))))))

(defn aset-in-form [var ks val]
  (list 'do (aset-in-form* var ks val)
        var))
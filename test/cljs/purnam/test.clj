(ns purnam.test)

(defmacro init []
  '(js/beforeEach
     (fn []
       (.addMatchers (js* "this")
         (let [o (js-obj)]
           (aset o "toSatisfy"
             (fn [expected tactual texpected]
               (let [actual (.-actual (js* "this"))
                     actualText (str actual)
                     actualText (if (= actualText "[object Object]")
                                   (let [ks (js/goog.object.getKeys actual)
                                         vs (js/goog.object.getValues actual)]
                                     (into {} (map (fn [x y] [x y]) 
                                                 ks vs)))
                                   actualText)
                     notText (if (.-isNot (js* "this")) "Not " "")]
                 (aset (js* "this") "message"
                       (fn []
                         (str "Expression: " tactual
                              "\n  Expected: " notText texpected
                              "\n  Actual: " actualText)))
                 (cond (= (js/goog.typeOf expected) "array")
                       (purnam.test.helpers/js-equals expected actual)

                       (fn? expected)
                       (expected actual)

                       :else
                       (or (= expected actual)
                           (purnam.test.helpers/js-equals expected actual))))))
            o)))))

(defmacro statement [v expected]
  (list '.toSatisfy (list 'js/expect v) expected (str v) (str expected)))

(defmacro is [info v expected]
  (list 'js/describe info 
     (list 'fn []
       (list 'js/it ""
         `(fn [] 
             (statement ~v ~expected))))))                          
                                   
(defmacro deftest [info & args]
  `(do
    ~@(map (fn [[f & rst :as form]] 
             (if (= f 'is)
               (concat [f info] rst)
               form)) 
           args)))

#_(defmacro is [v expected]
  (list '.toSatisfy (list 'js/expect v) expected (str v) (str expected)))

#_(defmacro is-not [v expected]
  (list '.toSatisfy (list '.-not (list 'js/expect v)) expected (str v) (str expected)))
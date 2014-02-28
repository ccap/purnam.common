(defproject im.chit/purnam.common "0.4.1"
  :description "Common utility functions for purnam"  
  :url "http://www.github.com/purnam/purnam.common"
  :license {:name "The MIT License"
            :url "http://opensource.org/licencses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :test-paths ["test/clj" "external/src" "external/test/clj"]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2138"]
                                  [midje "1.6.0"]]
                   :plugins [[lein-cljsbuild "1.0.0"]
                             [lein-midje "3.1.3"]]}}
  :cljsbuild {:builds [{:source-paths ["src"  "external/src" "test/cljs" "external/test/cljs"]
                       :compiler {:output-to "test/purnam.common.js"
                                  :optimizations :whitespace
                                  :pretty-print true}}]})

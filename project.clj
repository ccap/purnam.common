(defproject im.chit/purnam.walk "0.4.0"
  :description "Code walking transforms for purnam"  
  :url "http://www.github.com/purnam/purnam.walk"
  :license {:name "The MIT License"
            :url "http://opensource.org/licencses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :test-paths ["test/clj"]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2138"]
                                  [im.chit/gyr "0.4.0"]
                                  [midje "1.6.0"]
                                  [lein-midje "3.1.3"]]
                   :plugins [[lein-cljsbuild "1.0.0"]]}}
  :cljsbuild {:builds [{:source-paths ["src", "test/cljs"]
                       :compiler {:output-to "test/purnam.walk.js"
                                  :optimizations :whitespace
                                  :pretty-print true}}]})

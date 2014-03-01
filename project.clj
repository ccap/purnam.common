(defproject im.chit/purnam.common "0.4.3"
  :description "Common utility functions for purnam"  
  :url "http://www.github.com/purnam/purnam.common"
  :license {:name "The MIT License"
            :url "http://opensource.org/licencses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :test-paths ["test/clj" "extern/core/src" "extern/test/src" "extern/core/test/clj" "extern/test/test/clj"]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2138"]
                                  [midje "1.6.0"]]
                   :plugins [[lein-cljsbuild "1.0.0"]
                             [lein-midje "3.1.3"]]}}
  :cljsbuild {:builds [{:source-paths ["src" "extern/core/src" "example/purnam/game/src"]
                        :compiler {:output-to "example/purnam/game/resource/public/crafty-demo.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:source-paths ["src" "extern/core/src" "extern/test/src"
                                       "test/cljs" "extern/core/test/cljs"
                                       "extern/test/test/cljs" "example/purnam/test/src"
                                       "example/purnam/test/test"]
                        :compiler {:output-to "target/purnam.common.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})

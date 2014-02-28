(ns midje-doc.purnam-native-guide)

[[:chapter {:title "Introduction"}]]

"
`purnam.test` was written to allow clojurescript tests to work with the karma test runner. There are various advantages to using the [karma](http://karma-runner.github.io/) test runner over existing clojurescript solutions:
  - Supports a variety of browsers including Chrome, Firefox, Safari, Phantomjs and [Node Webkit](https://github.com/intelligentgolf/karma-nodewebkit-launcher)
  - Uses [nodejs](http://nodejs.org) and so has a very quick startup time
  - It is supported by [Google](http://google.com) and the [Angularjs](http://angularjs.org) team
  - Very active community with lots of users and development activity

Furthermore, the javascript `dot.notational.style` to be used for better native integration. Since version `0.4`, there is much better support for error reporting and the library can now be used on its own, outside of all other purnam libraries. Currently two flavors are supported describing clojurescript tests:
  - [Jasmine style](#describe)  - `describe`, `it` and `is`. It gives a more fully featured interface and has [async](#async) support (`runs`, `waits-for`)
  - [Midje style](#fact)  - `fact` and *=>*. It can be combined with [MidjeDoc](https://www.github.com/zcaudate/lein-midje-doc) to generate documentation from unit tests (the current article being an auto-generated document using this technique).
"

[[:chapter {:title "Installation"}]]

"To use just the test functionality, add to `project.clj` dependencies:

  `[im.chit/purnam.test` \"`{{PROJECT.version}}`\"`]` 

The library is also included when `[im.chit/purnam` \"`{{PROJECT.version}}`\"`]` is used."

[[:chapter {:title "Usage"}]]

"To run tests, the [karma](http://karma-runner.github.io/) test library is required. It can be installed using [npm](https://www.npmjs.org). There are two files that needs to be updated:
   - `/project.clj`
   - `/karma.conf.js`

In `project.clj`, add your clojurescript builds. Usually the test code is built to a seperate file than the application code. In the following case, the tests are compiled to `target/example-test.js`."

[[{:title "project.clj - cljsbuild"}]]
(comment
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "target/example.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:source-paths ["src", "test/cljs"]
                        :compiler {:output-to "target/example-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})

"A `karma.conf.js` file is required at the root level of your project. It is created by running `karma init` in the root project directory and following a bunch of instructions:

![karma-setup](https://raw.github.com/purnam/purnam.test/master/karma-setup.png)

Make sure that `autoWatch` is set to `true` and that the the compiled output from cljsbuild is included as one of the files."

[[{:lang "js" :title "karma.conf.js" :tag "c-py-1"}]]
[[:code 
  "....
  
  files: [
    'target/example-test.js'
  ],
  
  autoWatch: true,
  
  ...."]]

"
Run in one window: `lein cljsbuild auto`.

Run in another window: `karma start`.

Note that you will have to restart karma if the *.js file was not found before the test runner starts. The following video shows the entire process in detail:

[![karma-testing](https://raw.github.com/purnam/example.purnam.test/master/karma-testing.png)](http://www.youtube.com/watch?v=9mryE5vggR0&feature=youtu.be)
"

[[:chapter {:title "Generating Documentation"}]]

"If the midje flavor testing is used, [MidjeDoc](https://www.github.com/zcaudate/lein-midje-doc) can be used to auto generate documentation from test cases. The following should be added to `project.clj`:"

[[{:title "project.clj - documentation"}]]
(comment
  :profiles {:dev {:plugins [...
                             [lein-midje-doc "0.0.18"]
                             ... ]}}
                             
  :documentation {:files {"index"
                          {:input "test/cljs/midje_doc/example_guide.cljs"
                           :title "example"
                           :sub-title "this is an example"
                           :author "Your Name"
                           :email  "example@email.com"
                           :tracking "UA-31320512-2"}}})

"
Running in a third window: `lein midje-doc` will watch files for changes and generate a pretty viewable output to `index.html` on any change. A demonstration of how this works can be seen here:

[![Demo](https://raw.github.com/zcaudate/lein-midje-doc/master/documentation_tool.png)](http://youtu.be/8FjvhDPIUWE)
"


[[:chapter {:title "API" :tag "purnam-test"}]]

[[:file {:src "test/cljs/midje_doc/api_purnam_test_jasmine.cljs"}]]
    
[[:file {:src "test/cljs/midje_doc/api_purnam_test_sweet.cljs"}]]

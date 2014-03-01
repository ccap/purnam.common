# purnam.common

[![Build Status](https://travis-ci.org/purnam/purnam.common.png?branch=master)](https://travis-ci.org/purnam/purnam.common)

Common utility and code walking functions for the purnam suite.

## Quickstart

Stable Version: 

```clojure
[im.chit/purnam.common "0.4.3"] 
```

#### expand

The `purnam.common.expand` namespace has the `expand` function, which walks through a form and manipulates 

```clojure
(:require [purnam.common.expand :refer [expand]])
```

What it does it essentially is walk through your code and turn every instance of dotted notation into another call to the `aget-in` and `aset-in` macro:

```clojure
(expand '(let [a o.x]
             a.|b|.c))
=> '(let [a (purnam.common/aget-in o ["x"])] 
       (purnam.common/aget-in a [b "c"]))
```

If a symbol is already defined, it will leave the symbol alone:

```clojure
(defmacro test.sym [x] nil)
(expand '(purnam.test-common-expand/test.sym 1))
=> '(test.sym 1)
```

If the symbol is not defined and is the first in the list, it will perform a function call:

```clojure
(expand '(a.b 1))
=> '(let [obj# (purnam.common/aget-in a [])
          fn#  (aget obj# "b")]
      (.call fn# obj# 1))
```

#### aget-in and aset-in

Since version `0.4`, `aget-in` and `aset-in` have been implemented as macros so that it is faster than ever. The macroexpansion of `aget-in` is shown as follows:
 
```clojure
(macroexpand-1 '(aget-in dog ["leg" "count"]))
=> '(if-let [o1# (aget dog "leg")]
       (if-let [o2# (aget o1# "count")]
         o2#))
```

The macroexpansion of `aset-in` can also be seen:

```clojure
(macroexpand-1 '(aset-in dog ["a" "b"] "hello"))
=>  '(do (if-let [o1# (aget dog "a")]
           (aset o1# "b" "hello")
           (aset dog "a"
                 (let [o2# (js-obj)]
                   (aset o2# "b" "hello")
                   o2#)))
         dog)
```

#### Write your own `javascript.dot.notation` macro:

The `purnam.common` has been designed so that anyone can write their own library implementation. The `do.n` macro from [purnam.core](https://github.com/purnam/purnam.core) is implemented like this:

```clojure
(add-symbols purnam.common/*exclude-expansion*
         '[purnam.core do.n] 'do.n)

(defmacro do.n [& body]
  `(do ~@(expand body)))    
```

The `*exclude-expansion*` atom contains a list of symbols that will be ignored when expand is called. Typically, if `do.n` is included inside another form that calls expand on its body, there may be a clash. The call `add-symbol` adds both `purnam.core/do.n` and `do.n` ot the exclusion list. When `expand` comes across the `do.n` symbol, it will stop expanding.

#### safe-aget mode

There is a `safe-mode` for `aget-in` which means that evaluation of arbitrary chained dotted forms will always return `nil`:

```clojure
(let [o (obj)]
  o.arbitrary.nested.code)
=> nil
```

It is possible to turn off safe-aget to get better performance or if it interferes with native code. However, if there is chained dotted forms, it will throw an error.

```clojure
(purnam.common/set-safe-aget false)

(let [o (obj)]
  o.arbitrary.nested.code)
=> (throws Error "'undefined' is not an object")
```
For an exampl of where this was used, see the [crafty.js](https://github.com/purnam/example.purnam.game/blob/master/src/purnam_crafty_game/core.cljs) game example.


## Code Examples

See implementations for current purnam codewalking macros
  - [purnam.core](https://github.com/purnam/purnam.core/blob/master/src/purnam/core.clj)
  - [purnam.test](https://github.com/purnam/purnam.test/blob/master/src/purnam/test.clj)

## License

Copyright © 2014 Chris Zheng

Distributed under the The MIT License.

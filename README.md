<img src="http://upload.wikimedia.org/wikipedia/en/c/cb/FuturamaHermesConrad.png"  alt="Hermes" title="Hermes" align="right" height=/>


# Hermes
>That's a calculator. I ate it to gain its power.
>-[Hermes Conrad](https://www.youtube.com/watch?v=AkA0fYfT-vI)

A Clojure library designed to make it easy to work with embedded
[Titan](http://thinkaurelius.github.com/titan/) graphs. Hermes is very
much a work in progress. Titan is a young project and has yet to even
hit 0.2. Hermes will probably always be a work in progress until Titan
hits 1.0. So, use this in production at your own peril.

Check [clojars](https://clojars.org/hermes) for the latest jar. The
best thing to do is set up lein checkouts and clone the library
directly for now. Reading the tests and source code should bring you
up to speed with the library. We are still writing the docs and
example projects.


## Example
``` clojure
(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]
            [hermes.type   :as t]))

(g/open {:storage {:backend "cassanda"
                   :hostname "127.0.0.1"}})


(g/transact! (t/create-vertex-key-once :name String {:unique true
                                                     :indexed true}))

(def Zack (g/transact! (v/create {:name "Zack"
                                  :age 21
                                  :gender "Male"
                                  :occupation "INTERN"})))

(def Brooke (g/transact! (v/create {:name "Brooke"
                                    :age 19
                                    :gender "Female"
                                    :occupation "Student"})))

(g/transact! (connect! Zack Brook "siblings" {:since 1991}))

;;Oops mistake, should probably fix that
(g/transact! (upconnect! Zack Brook "siblings" {:since 1993}))

;;And well, I am really more than just an intern
(g/transact! (upsert! :name {:name "Zack"
                             :occupation "Software specialist and all around nice guy"}))

(defn -main [& args]
  ["Hello World!" Zack])
```

## License

Copyright © 2012 Game Closure

Distributed under the MIT license. 

Yell at [Zack](http://www.twitter.com/ZackMaril) if it breaks. 


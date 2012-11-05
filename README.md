<img src="http://upload.wikimedia.org/wikipedia/en/c/cb/FuturamaHermesConrad.png"  alt="Hermes" title="Hermes" align="right" height=/>

# Hermes

A Clojure library designed to make it easy to work with embedded [Titan](http://thinkaurelius.github.com/titan/) graphs. 

Check [clojars](https://clojars.org/hermes) for the latest jar.
## Usage

Open up a graph in memory.
``` clojure 
user> (hermes.core/open)
#<TitanInMemoryBlueprintsGraph titaninmemoryblueprintsgraph[null]>
```

Open up a graph backed by hbase running locally.
``` clojure 
user> (hermes.core/open {:storage {:backend "hbase" :hostname "127.0.0.1"}})
#<StandardTitanGraph titangraph[hbase:127.0.0.1]>
```

Open up a graph backed by cassandra running ... someplace else.
``` clojure 
user> (hermes.core/open {:storage {:backend "cassandra" :hostname "24.6.0.1"}})
#<StandardTitanGraph titangraph[hbase:127.0.0.1]>
```

Create and work with vertices!
``` clojure
user> (hermes.core/open)
#<TitanInMemoryBlueprintsGraph titaninmemoryblueprintsgraph[null]>
user> (hermes.vertex/create)
#<PersistStandardTitanVertex v[4]>
user> (def v (hermes.vertex/get 4))
#'user/v
user> v
#<PersistStandardTitanVertex v[4]>
user> (hermes.vertex/prop-map v)
{:id 4}
user> (hermes.vertex/set-property! v :name "Hermes Conrad") 
nil
user> (hermes.vertex/get-property v :name)
"Hermes Conrad"
user> (hermes.vertex/prop-map v)
{:id 4, :name "Hermes Conrad"}
user> (hermes.vertex/remove-property! v :name)
"Hermes Conrad"
user> (hermes.vertex/prop-map v)
{:id 4}
```

And all the thrill of working with edges!
``` clojure
user> (def husband (hermes.vertex/create {:name "Hermes Conrad"}))
#'user/husband
user> (def wife (hermes.vertex/create {:name "LaBarbara Conrad"}))
#'user/wife
user> (def edge (hermes.edge/create husband wife "married")) 
#'user/edge
user> edge
#<PersistLabeledTitanEdge e[35:36:118][36-married->44]>
user> (def child (hermes.vertex/create {:name "Dwight Conrad"}))
#'user/child
user> (hermes.edge/create husband child "parent" {:role "father"}) 
#<PersistLabeledTitanEdge e[49:36:174][36-parent->72]>
user> (hermes.edge/create wife child "parent" {:role "mother"}) 
#<PersistLabeledTitanEdge e[59:44:174][44-parent->72]>
```

Transactions are for lovers. 
``` clojure
user> (hermes.core/transact
 (let [v (hermes.vertex/create)
       u (hermes.vertex/create)
       e (hermes.edge/create v u "lovers")]
   [v u e]))
[#<PersistStandardTitanVertex v[2400004]> #<PersistStandardTitanVertex v[2400008]> #<PersistLabeledTitanEdge e[12000013:2400004:36028797018965582][2400004-lovers->2400008]>]
```

Indexes are for nerds. 
``` clojure
user> (hermes.vertex/index-on :name)
nil
user> (def Zack (v/create {:name "Zack"}))
#'user/Zack
user> (v/find :name "Zack")
#{#<PersistStandardTitanVertex v[20]>}
```

Queries killed the cat.
``` clojure
(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e])
  (:use hermes.query))

(g/open)
(v/index-on "name")

(def Zack (v/create {:name "Zack"
                     :age "21"
                     :gender "Male"
                     :occupation "INTERN"}))

(def Brooke (v/create {:name "Brooke"
                       :age "19"
                       :gender "Female"
                       :occupation "Student"}))

(def Cindy (v/create {:name "Cindy"
                      :occupation "Saleswoman"}))

(def Steve (v/create {:name "Steve"
                      :occupation "Salesmen"}))

(e/create Zack Brooke "siblings")

(e/create Steve Cindy  "married")

(e/create Zack Cindy  "child")
(e/create Zack Steve  "child")

(e/create Brooke Cindy  "child")
(e/create Brooke Steve  "child")

(defquery siblings-with
  (--- "siblings"))

(defquery child-of
  (--> "child"))

(defquery find-parents-of-siblings
  siblings-with
  child-of
  properties!)

(println (find-parents-of-siblings Zack))
;;({:id 108, :occupation "Saleswoman", :name "Cindy"} {:id 120, :occupation "Salesmen", :name "Steve"})
```

The best bet right now is to read the source code. 
TODO: Still working on understanding types as they pertain to indexes and RelationIdentifiers. 
## License

Copyright © 2012 Game Closure

Distributed under the Eclipse Public License, the same as Clojure.


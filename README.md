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
user> (hermes.vertex/to-map v)
{:id 4}
user> (hermes.vertex/set-property v "name" "Hermes Conrad") 
nil
user> (hermes.vertex/get-property v "name")
"Hermes Conrad"
user> (hermes.vertex/to-map v)
{:id 4, :name "Hermes Conrad"}
user> (hermes.vertex/remove-property v "name")
"Hermes Conrad"
user> (hermes.vertex/to-map v)
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
TODO: Still working on understanding types as they pertain to indexes and RelationIdentifiers. 
## License

Copyright © 2012 Game Closure

Distributed under the Eclipse Public License, the same as Clojure.


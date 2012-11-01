# Hermes

A Clojure library designed to make it easy to work with embedded [Titan](http://thinkaurelius.github.com/titan/) graphs. 

## Usage

Messing around with vertices.
``` clojure
user> (require 'hermes.core 'hermes.edge 'hermes.vertex)
;;nil
user> (hermes.core/open)
;;#<TitanInMemoryBlueprintsGraph titaninmemoryblueprintsgraph[null]>
user> (hermes.vertex/create)
#<PersistStandardTitanVertex v[4]>
user> (def v (hermes.vertex/get 4))
#'user/v
user> v
#<PersistStandardTitanVertex v[4]>
user> (hermes.element/to-map v)
{:id 4}
user> (hermes.element/set-property v "name" "Hermes Conrad") 
nil
user> (hermes.element/get-property v "name")
"Hermes Conrad"
user> (hermes.element/to-map v)
{:id 4, :name "Hermes Conrad"}
user> (hermes.element/remove-property v "name")
"Hermes Conrad"
user> (hermes.element/to-map v)
{:id 4}
```

Messing around with edges. 
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

TODO: Still working on understanding types as they pertain to indexes and RelationIdentifiers. 
## License

Copyright © 2012 Game Closure

Distributed under the Eclipse Public License, the same as Clojure.

# Hermes

A Clojure library designed to make it easy to work with embedded [Titan](http://thinkaurelius.github.com/titan/) graphs. 

## Usage
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
```
FIXME

## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.


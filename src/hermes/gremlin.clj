(ns hermes.gremlin
  (:import (com.tinkerpop.gremlin.java GremlinPipeline)))

(defn gremlin-eval [ob]
  (if (instance? GremlinPipeline ob)
    (seq (.toList ob))
    ob))

(defmacro query [vertex & body]  
  `(gremlin-eval (.. (GremlinPipeline. ~vertex)                     
                          ~@body)))


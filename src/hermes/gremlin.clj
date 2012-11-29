(ns hermes.gremlin
  (:import (com.tinkerpop.gremlin.java GremlinPipeline)))

(defn make-ogre [vertex]
  (let [Ogre (proxy [GremlinPipeline] [vertex])]
    Ogre))

(defn gremlin-eval [ob]
  (if (instance? GremlinPipeline ob)
    (seq (.toList ob))
    ob))

(defmacro query [vertex & body]  
 `(gremlin-eval (.. (make-ogre ~vertex)
                          ~@body)))


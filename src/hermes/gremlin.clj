(ns hermes.gremlin
  (:import (com.tinkerpop.gremlin.java GremlinPipeline)))

(defn make-ogre [vertex]
  (let [Ogre (proxy [GremlinPipeline] [vertex])]
    Ogre)

  )

(defmacro query [vertex & body]
  `(seq (.. (make-ogre ~vertex)
            ~@body
            toList
            )))


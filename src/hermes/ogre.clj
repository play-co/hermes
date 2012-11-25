(ns hermes.ogre
  (:gen-class
   :name hermes.ogre.Ogre
   :extends com.tinkerpop.gremlin.java.GremlinPipeline
   :constructors {[Object] [Object]}
   :methods [[out [] void]
             [out [String] void]]
   :exposes-methods {out parent_out}))

(defn -out [this]
  (.parent_out this (into-array String [])))

(defn -out [this label]
  (.parent_out this (into-array String [label])))
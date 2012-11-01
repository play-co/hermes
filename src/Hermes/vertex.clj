(ns hermes.vertex
  (:import (com.tinkerpop.blueprints Vertex))
  (:use [Hermes.core :only (*graph*)]
        Hermes.element))

(defn create
  ([] (create {}))
  ([data] (let [vertex (.addVertex *graph*)
                _      (doseq [[k v] data] (set-property vertex (name k) v))]
            vertex)))

(defn get [& ids]
  (for [id ids] (.getVertex *graph* id)))

(defn all []
  (.getVertices *graph*))

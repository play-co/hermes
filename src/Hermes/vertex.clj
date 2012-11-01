(ns hermes.vertex
  (:import (com.tinkerpop.blueprints Vertex))
  (:use [hermes.core :only (*graph*)]
        hermes.element))

(defn create
  ([] (create {}))
  ([data] (let [vertex (.addVertex *graph*)
                _      (doseq [[k v] data] (set-property vertex (name k) v))]
            vertex)))

(defn get [& ids]
  (if (= 1 (count ids))
    (.getVertex *graph* (first ids))
    (seq (for [id ids] (.getVertex *graph* id)))))

(defn all []
  (.getVertices *graph*))

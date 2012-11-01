(ns hermes.edge
  (:import (com.tinkerpop.blueprints Edge))  
  (:use [hermes.core :only (*graph*)]
        [hermes.util :only (immigrate)]))

(immigrate 'hermes.element)

(defn create
  ([u v label] (create u v label {}))
  ([u v label data] (let [edge (.addEdge *graph* u v label)
                          _     (doseq [[k v] data] (set-property edge (name k) v))]
                      edge)))

(defn get [& ids]
  (for [id ids] (.getEdge *graph* id)))

(defn all []
  (.getVertices *graph*))

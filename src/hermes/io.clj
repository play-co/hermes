(ns hermes.io
  (:require [clojure.java.io :as io]
            [hermes.core :as g])
  (:import [com.tinkerpop.blueprints.util.io.graphml GraphMLWriter GraphMLReader]
           [com.tinkerpop.blueprints.util.io.gml GMLWriter GMLReader]))

(defn- load-graph-with-reader
  [reader string-or-file]
  (let [in-stream (io/input-stream string-or-file)]
    (reader g/*graph* in-stream)))

(defn- write-graph-with-writer
  [writer string-or-file]
  (if (not (g/get-feature "supportsVertexIteration"))
    (throw (Exception. "Cannot write a graph that does not support vertex iteration.")))
  (let [out-stream (io/output-stream string-or-file)]
    (writer g/*graph* out-stream)))

;; GML
(def load-graph-gml (partial load-graph-with-reader #(GMLReader/inputGraph %1 %2)))
(def write-graph-gml (partial write-graph-with-writer #(GMLWriter/outputGraph %1 %2)))

;; GraphML
(def load-graph-graphml (partial load-graph-with-reader #(GraphMLReader/inputGraph %1 %2)))
(def write-graph-graphml (partial write-graph-with-writer #(GraphMLWriter/outputGraph %1 %2)))


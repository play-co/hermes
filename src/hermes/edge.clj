(ns hermes.edge
  (:import (com.tinkerpop.blueprints Edge Direction))
  (:require [hermes.vertex :as v]
            [hermes.type   :as t])
  (:use [hermes.core :only (*graph* transact!)]
        [hermes.util :only (immigrate)]))

(immigrate 'hermes.element)

(defn endpoints [this]
  [(.getVertex this Direction/OUT)
   (.getVertex this Direction/IN)])

(defn refresh [edge]
  (.getEdge edge))

(defn connect!
  ([u v label] (connect! u v label {}))
  ([u v label data]
     (let [edge (.addEdge *graph* (v/refresh u) (v/refresh v) label)]
       (set-properties! edge data)
       edge)))

(defn edges-between
  ([u v] (edges-between u v nil))
  ([u v label]
    (if-let [edges (seq (.. u
                           query
                           (labels (into-array String (if label [label] [])))
                           (has "id" (.getId v))
                           titanEdges))]
       edges
       nil)))

(defn connected?
  ([u v] (connected? u v nil))
  ([u v label] (boolean (edges-between u v label))))

(defn upconnect!
  ([u v label] (upconnect! u v label {}))
  ([u v label data]
    (transact!
      (let [fresh-u (v/refresh u)
            fresh-v (v/refresh v)]
        (if-let [edges (edges-between fresh-u fresh-v label)]
          edges
          #{(connect! u v label data)})))))

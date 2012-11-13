(ns hermes.edge
  (:import (com.tinkerpop.blueprints Edge Direction))
  (:require [hermes.vertex :as v]
            [hermes.type   :as t]
            [clj-gremlin.core   :as gremlin])
  (:use [hermes.core :only (*graph* transact!)]
        [hermes.util :only (immigrate)]))

(immigrate 'hermes.element)

(defn endpoints [this]
  "Returns the endpoints of the edge in array with the order [starting-node,ending-node]."
  [(.getVertex this Direction/OUT)
   (.getVertex this Direction/IN)])

(defn refresh [edge]
  "Goes and grabs the edge from the graph again. Useful for \"refreshing\" stale edges."
  (.getEdge edge))

(defn connect!
  "Connects two vertices with the given label, and, optionally, with the given properties."
  ([u v label] (connect! u v label {}))
  ([u v label data]
     (let [edge (.addEdge *graph* (v/refresh u) (v/refresh v) label)]
       (set-properties! edge data)
       edge)))

(defn edges-between
  "Returns a set of the edges between two vertices."
  ([u v] (edges-between u v nil))
  ([u v label]
    (letfn [(label-filter [g] (if label (gremlin/outE g label) (gremlin/outE g)))]
      (if-let [edges
                ; This awesome query was provided by Marko himselt at
                ; See https://groups.google.com/forum/?fromgroups=#!topic/gremlin-users/R2RJxJc1BHI
                (seq (-> *graph*
                  (gremlin/v (.getId u))
                  (label-filter)
                  (gremlin/inV)
                  (gremlin/has "id" (.getId v))
                  (gremlin/back 2)))]
         edges
         nil))))

(defn connected?
  "Returns whether or not two vertices are connected. Optional third
   arguement specifying the label of the edge."
  ([u v] (connected? u v nil))
  ([u v label] (boolean (edges-between u v label))))


(defn upconnect!
  "Upconnect takes all the edges between the given vertices with the
   given label and, if the data is provided, merges the data with the
   current properties of the edge. If no such edge exists, then an
   edge is created with the given data."
  ([u v label] (upconnect! u v label {}))
  ([u v label data]
    (transact!
      (let [fresh-u (v/refresh u)
            fresh-v (v/refresh v)]
        (if-let [edges (edges-between fresh-u fresh-v label)]
          (do
            (doseq [edge edges] (set-properties! edge data))
            edges)
          #{(connect! u v label data)})))))

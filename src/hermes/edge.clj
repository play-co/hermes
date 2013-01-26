(ns hermes.edge
  (:import (com.tinkerpop.blueprints Edge Direction)
           (com.tinkerpop.gremlin.java GremlinPipeline))
  (:require [hermes.vertex :as v]
            [hermes.type   :as t])
  (:use [hermes.core :only (*graph* transact! ensure-graph-is-transaction-safe)]
        [hermes.util :only (immigrate)]
        [hermes.gremlin :only (query)]))

(immigrate 'hermes.element)

;;
;;Information getters
;;
(defn find-by-id [& ids]
  "Retrieves nodes by id from the graph."
  (ensure-graph-is-transaction-safe)
  (if (= 1 (count ids))
    (.getEdge *graph* (first ids))
    (seq (for [id ids] (.getEdge *graph* id)))))


(defn get-label [edge]
  (.. edge getTitanLabel getName))

(defn prop-map [edge]
  (into {:__id__ (get-id edge)
         :__label__ (get-label edge)}
        (map #(vector (keyword %) (get-property edge %)) (get-keys edge))))

(defn endpoints [this]
  "Returns the endpoints of the edge in array with the order [starting-node,ending-node]."
  (ensure-graph-is-transaction-safe)
  [(.getVertex this Direction/OUT)
   (.getVertex this Direction/IN)])

(defn edges-between
  "Returns a set of the edges between two vertices."
  ([v1 v2] (edges-between v1 v2 nil))
  ([v1 v2 label]
     (ensure-graph-is-transaction-safe)
     (when-let [edges
              ;; Source for this edge query:
              ;; https://groups.google.com/forum/?fromgroups=#!topic/gremlin-users/R2RJxJc1BHI
                (query v1
                     (outE (into-array String (if label [label] [])))
                     inV
                     (has "id" (.getId v2))
                     (back 2))]
       edges)))

(defn connected?
  "Returns whether or not two vertices are connected. Optional third
   arguement specifying the label of the edge."
  ([v1 v2] (connected? v1 v2 nil))  
  ([v1 v2 label]     
     (ensure-graph-is-transaction-safe)
     (boolean (edges-between v1 v2 label))))
;;
;;Transaction management
;;

(defn refresh [edge]
  "Goes and grabs the edge from the graph again. Useful for \"refreshing\" stale edges."
  (ensure-graph-is-transaction-safe)
  (.getEdge *graph* (.getId edge)))

;;
;;Creation methods
;;

(defn connect!
  "Connects two vertices with the given label, and, optionally, with the given properties."
  ([v1 v2 label] (connect! v1 v2 label {}))
  ([v1 v2 label data]
     (ensure-graph-is-transaction-safe)
     (let [edge (.addEdge *graph* v1 v2 label)]
       (set-properties! edge data)
       edge)))

(defn upconnect!
  "Upconnect takes all the edges between the given vertices with the
   given label and, if the data is provided, merges the data with the
   current properties of the edge. If no such edge exists, then an
   edge is created with the given data."
  ([v1 v2 label] (upconnect! v1 v2 label {}))
  ([v1 v2 label data]
     (ensure-graph-is-transaction-safe)
     (if-let [edges (edges-between v1 v2 label)]
       (do
         (doseq [edge edges] (set-properties! edge data))
         edges)
       #{(connect! v1 v2 label data)})))

;;
;;Deletion methods
;;

(defn delete!
  "Delete an edge."
  [edge]
  (.removeEdge *graph* edge ))

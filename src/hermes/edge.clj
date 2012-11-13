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

;;Try to put the lesser degree connected node first, otherwise
;;supernodes will bust the ids and if statement.
(defn connected?
  ([u v] (connected? u v nil))  
  ([u v label]
     (if-let [edges (seq (.. u
                             query
                             (labels (into-array String (if label [label] [])))
                             titanEdges))]
       edges
       nil)))
       ;; (if ((set (map v/get-id ids))
       ;;      (v/get-id v))
       ;;   true
       ;;   false)
       ;; fales)))

(defn upconnect!
  ([u v label] (upconnect! u v label {}))
  ([u v label data]
   (transact!
     (let [fresh-u (v/refresh u)
           fresh-v (v/refresh v)]
       (if-let [edges (connected? fresh-u fresh-v label)]
         edges
         #{(connect! u v label data)})))))

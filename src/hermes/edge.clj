(ns hermes.edge
  (:import (com.tinkerpop.blueprints Edge))
  (:require [hermes.vertex :as v]
            [hermes.type   :as t])
  (:use [hermes.core :only (*graph*)]
        [hermes.util :only (immigrate)]))

(immigrate 'hermes.element)

(defn endpoints [this]
  [(.getVertex this)
   (.getOtherVertex this)])

(defn connect!
  ([u v label] (connect u v label {}))
  ([u v label data]
     (println u v label data)
     (let [edge (.addEdge *graph* (v/refresh u) (v/refresh v) label)]
       (doseq [[k v] data] (set-property! edge (name k) v))
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
       (map endpoints edges)
       nil)))
       ;; (if ((set (map v/get-id ids))
       ;;      (v/get-id v))
       ;;   true
       ;;   false)
       ;; fales)))

(defn upconnect!
  ([u v label] (upconnect! u v label {}))
  ([u v label data]
     (if (connected? u v label)
       true
       false
       )
     ))

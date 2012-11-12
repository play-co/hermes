(ns hermes.query
  (:import (com.tinkerpop.blueprints Direction))
  (:require [hermes.vertex :as v])
  (:use [hermes.core :only (*graph*)]))

(defmacro defquery [name & body]
  `(defn ~name [v#]
     (let [q# (if (= com.thinkaurelius.titan.graphdb.query.SimpleTitanQuery
                         (type v#))
                    v#
                    (.query v#))
           results# (-> q# ~@body)]
       results#)))

(def out-flag Direction/OUT)
(def in-flag Direction/IN)
(def both-flag Direction/BOTH)

(defmacro def-directed-query [name direction]
  `(defn ~name
     ([q#]          (-> q#
                        (.labels (into-array String []))
                        (.direction ~direction)))
     ([q# & labels#] (-> q#
                         (.labels (into-array String labels#))
                         (.direction ~direction)))))

(def-directed-query --> out-flag)
(def-directed-query <-- in-flag)
(def-directed-query --- both-flag)

(defn has-key [q & keys]
  (.keys q (into-array String keys)))

(defn V! [q]
  (seq (.vertexIds q)))

(defn properties! [q]
  (map v/prop-map (V! q)))




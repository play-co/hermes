(ns hermes.vertex
  (:import (com.tinkerpop.blueprints Vertex))
  (:use [hermes.core :only (*graph* transact!)]
        [hermes.util :only (immigrate)]))

(immigrate 'hermes.element)

(defn create
  ([] (create {}))
  ([data]  (let [vertex (.addVertex *graph*)
                 _      (doseq [[k v] data]
                          (set-property! vertex
                                         (name k) v))]
             vertex)))

(defn get-by-id [& ids]
  (if (= 1 (count ids))
    (.getVertex *graph* (first ids))
    (seq (for [id ids] (.getVertex *graph* id)))))

(defn find-by-kv [k v]
  (set (.getVertices *graph* (name k) v)))

(defn all []
  (.getVertices *graph*))

(defn refresh [vertex]
  (.getVertex *graph* vertex))

(defn upsert! [k m]
  (if-let [vertex (transact! (first (find-by-kv k (k m))))]
    (transact! (set-properties! vertex m))
    (transact! (create m))))
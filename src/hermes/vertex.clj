(ns hermes.vertex
  (:import (com.tinkerpop.blueprints Vertex))
  (:use [hermes.core :only (*graph* transact!)]
        [hermes.util :only (immigrate)]))

(immigrate 'hermes.element)

(defn create!
  "Create a vertex, optionally with the given property map."
  ([] (create! {}))
  ([data]
     (set-properties! (.addVertex *graph*) data)))

(defn get-by-id [& ids]
  "Retrieves nodes by id from the graph."
  (if (= 1 (count ids))
    (.getVertex *graph* (first ids))
    (seq (for [id ids] (.getVertex *graph* id)))))

(defn find-by-kv [k v]
  "Given a key and a value, returns the set of all vertices that
   sastify the pair."
  (set (.getVertices *graph* (name k) v)))

(defn refresh [vertex]
  "Gets a vertex back from the database and refreshes it to be usable again."
  (.getVertex *graph* vertex))

(defn upsert! [k m]
  "Given a key and a property map, upsert! either creates a new node
   with that property map or updates all nodes with the given key
   value pair to have the new properties specifiied by the map. Always
   returns the set of vertices that were just update or created."
  (let [vertices (transact! (find-by-kv (name k) (k m)))]
    (println vertices)
    (if (empty? vertices)
      (transact! (set [(create! m)]))
      (transact! (let [r-vertices (map refresh vertices)]
                   (doseq [vertex r-vertices] (set-properties! vertex m))
                   r-vertices)))))

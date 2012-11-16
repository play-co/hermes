(ns hermes.memory.io-test
  (:use clojure.test)
  (:require [hermes.core :as g]
            [hermes.io :as io]
            [hermes.vertex :as v]
            [hermes.edge :as e]
            [clojure.java.io :as clj-io]))

(defn has-n-vertices [n]
  (is (= n (count (seq (.getVertices g/*graph*))))))

(defn has-n-edges [n]
  (is (= n (count (seq (.getEdges g/*graph*))))))

(deftest test-loading-and-saving-graphs-graphml
  (g/open)
  (let [filename "my-test-graph.graphml"
        file (clj-io/file filename)]
    (letfn [(delete-graph-file [] (clj-io/delete-file file true))] ;; Delete file *silently* (no failure if it don't exist).
      (delete-graph-file)
      (let [vertex-1 (v/create!)
            vertex-2 (v/create!)
            edge (e/upconnect! vertex-1 vertex-2 "edge")]
        (io/write-graph-graphml filename))

      ;; Open new graph and read it
      (g/open)
      (io/load-graph-graphml filename)

      (has-n-vertices 2)
      (has-n-edges 1)

      (delete-graph-file))))

(deftest test-loading-and-saving-graphs-gml
  (g/open)
  (let [filename "my-test-graph.gml"
        file (clj-io/file filename)]
    (letfn [(delete-graph-file [] (clj-io/delete-file file true))] ;; Delete file *silently* (no failure if it don't exist).
      (delete-graph-file)
      (let [vertex-1 (v/create!)
            vertex-2 (v/create!)
            edge (e/upconnect! vertex-1 vertex-2 "edge")]
        (io/write-graph-gml filename))

      ;; Open new graph and read it
      (g/open)
      (io/load-graph-gml filename)

      (has-n-vertices 2)
      (has-n-edges 1)

      (delete-graph-file))))

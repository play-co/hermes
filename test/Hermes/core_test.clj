(ns hermes.core-test
  (:use clojure.test)
  (:require [hermes.core :as g])
  (:import  (com.thinkaurelius.titan.graphdb.blueprints TitanInMemoryBlueprintsGraph)
            (com.thinkaurelius.titan.graphdb.database   StandardTitanGraph)
            (com.thinkaurelius.titan.graphdb.vertices   PersistStandardTitanVertex)))

(deftest test-opening-a-graph-in-memory
  (testing "Graph in memory"
    (g/open)
    (is (= (type g/*graph*)
           TitanInMemoryBlueprintsGraph))))

(deftest test-opening-a-graph-with-hbase
  (testing "Stored graph"
    (println "Make sure hbase is up and running locally")
    (g/open {:storage {:backend "hbase"
                   :hostname "127.0.0.1"}})
    (is (= (type g/*graph*)
           StandardTitanGraph))))

(deftest test-simple-transaction
  (testing "Stored graph"
    (g/open {:storage {:backend "hbase"
                       :hostname "127.0.0.1"}})
    (let [vertex (g/transact! (.addVertex g/*graph*))]      
      (is (= PersistStandardTitanVertex (type vertex))))))



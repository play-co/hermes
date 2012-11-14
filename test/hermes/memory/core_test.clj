(ns hermes.memory.core-test
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

(deftest test-with-graph
  (testing "with-graph macro"
    (println "TODO: write with-graph macro test")
    (= 0 0)))
(ns hermes.persistent.core-test
  (:use clojure.test)
  (:require [hermes.core :as g])
  (:import  (com.thinkaurelius.titan.graphdb.blueprints TitanInMemoryBlueprintsGraph)
            (com.thinkaurelius.titan.graphdb.database   StandardTitanGraph)
            (com.thinkaurelius.titan.graphdb.vertices   PersistStandardTitanVertex)))

(deftest test-opening-a-graph-with-hbase
  (testing "Stored graph"
    (println "Make sure hbase is up and running locally.")
    (println "Be careful with types! They don't get removed or rewritten ever. ")
    (println "When you are doing the backed-by-hbase tests, always be on the look out.")
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


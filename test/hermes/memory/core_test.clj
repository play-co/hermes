(ns hermes.memory.core-test
  (:use clojure.test)
  (:require [hermes.core :as g]
            [hermes.type :as t]
            [hermes.vertex :as v])
  (:import  (com.thinkaurelius.titan.graphdb.blueprints TitanInMemoryBlueprintsGraph)
            (com.thinkaurelius.titan.graphdb.vertices   PersistStandardTitanVertex)
            (com.thinkaurelius.titan.core               TitanFactory)))

(deftest test-opening-a-graph-in-memory
  (testing "Graph in memory"
    (g/open)
    (is (= (type g/*graph*)
           TitanInMemoryBlueprintsGraph))))

(deftest test-with-graph
  (testing "with-graph macro"
    ; Open the usual *graph*
    (g/open)
    ; Open a real graph the hard wary
    (let [graph (TitanFactory/openInMemoryGraph)]
      (g/with-graph graph
        (.addVertex graph))
      (is (= 1 (count (seq (.getVertices graph)))) "graph has the new vertex")
      (is (= 0 (count (seq (.getVertices g/*graph*)))) "the usual *graph* is still empty"))))

(deftest test-retry-transact!
  (testing "with backoff function"
    (g/open)
    (let [sum (partial reduce +)
          clock (atom [])
          punch-clock (fn [] (swap! clock concat [(System/currentTimeMillis)]))]
      (is (thrown? Exception (g/retry-transact! 3 (fn [n] (* n 100))
                                                (punch-clock)
                                                (/ 1 0))))
      (let [[a,b,c] (map (fn [a b] (- a b)) (rest @clock) @clock)]
        (is (>= a 100))
        (is (>= b 200))
        (is (>= c 300)))))

  (testing "with transaction that returns nil"
    (g/open)
    (g/transact!
      (t/create-vertex-key-once :vertex-id Long {:indexed true
                                                 :unique true}))
    (g/retry-transact! 3 10
      (v/upsert! :vertex-id {:vertex-id 100})
      nil)

    (is (= 1 (count (seq (.getVertices g/*graph*)))) "graph has the new vertex")))



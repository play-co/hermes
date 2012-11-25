(ns hermes.memory.gremlin
  (:use [clojure.test]
        [hermes.gremlin :only (query)])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]))

(deftest test-idempotence
  (g/open)
  (let [v1 (v/create!)
        v1-a (first (query v1))]
    (is (= (.getId v1) (.getId v1-a)))))

(deftest test-idempotence
  (g/open)
  (let [v1 (v/create!)
        v2 (v/create!)
        e1 (e/connect! v1 v2 "testlabel")
        results (query v1
                       out
                       )
        ]
    (println results)
    
    ))
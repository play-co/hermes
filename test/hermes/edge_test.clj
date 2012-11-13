(ns hermes.edge-test
  (:use [clojure.test]
        [hermes.util :only [immigrate]])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]))

(immigrate 'hermes.element)

(deftest test-endpoints
  (g/open)
  (let [v1 (v/create {:name "v1"})
        v2 (v/create {:name "v2"})
        edge (first (e/upconnect! v1 v2 "connexion"))]
    (is (= ["v1" "v2"] (map #(get-property % :name) (e/endpoints edge))))))

(deftest test-upconnect!
  (testing "Upconnecting once"
    (g/open)
    (let [v1 (v/create {:name "v1"})
          v2 (v/create {:name "v2"})
          edge (first (e/upconnect! v1 v2 "connexion" {:name "the edge"}))]
      (is (e/connected? v1 v2))
      (is (e/connected? v1 v2 "connexion"))
      (is (not (e/connected? v2 v1)))
      (is (= "the edge" (get-property edge :name)))
      (is (= 1 (count (seq (.getEdges g/*graph*)))))))

  (testing "Upconnecting multiple times"
    (g/open)
    (let [v1 (v/create {:name "v1"})
          v2 (v/create {:name "v2"})
          edge (first (e/upconnect! v1 v2 "connexion" {:name "the edge"}))
          edge (first (e/upconnect! v1 v2 "connexion" {:name "the edge"}))
          edge (first (e/upconnect! v1 v2 "connexion"))]
      (is (e/connected? v1 v2))
      (is (e/connected? v1 v2 "connexion"))
      (is (not (e/connected? v2 v1)))
      (is (= "the edge" (get-property edge :name)))
      (is (= 1 (count (seq (.getEdges g/*graph*))))))))



(ns hermes.edge-test
  (:use [clojure.test]
        [hermes.util :only [immigrate]])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]))

(deftest test-simple-property-mutation
  (g/open)
  (let [v1 (v/create {:name "v1"})
        v2 (v/create {:name "v2"})
        edge (e/connect! v1 v2 "test" {:a 1})]
    (e/set-property! edge :b 2)
    (e/remove-property! edge :a)
    (is (= 2   (e/get-property edge :b)))
    (is (= nil (e/get-property edge :a)))))

(deftest test-multiple-property-mutation
  (g/open)
  (let [v1 (v/create {:name "v1"})
        v2 (v/create {:name "v2"})
        edge (e/connect! v1 v2 "test" {:a 0})]
    (e/set-properties! edge {:a 1 :b 2 :c 3})
    (is (= 1 (e/get-property edge :a)))
    (is (= 2 (e/get-property edge :b)))
    (is (= 3 (e/get-property edge :c)))))

(deftest test-property-map
  (g/open)
  (let [v1 (v/create {:name "v1"})
        v2 (v/create {:name "v2"})
        edge (e/connect! v1 v2 "test" {:a 1 :b 2 :c 3})
        prop-map (e/prop-map edge)]
    (is (= 1 (prop-map :a)))
    (is (= 2 (prop-map :b)))
    (is (= 3 (prop-map :c)))))

(deftest test-endpoints
  (g/open)
  (let [v1 (v/create {:name "v1"})
        v2 (v/create {:name "v2"})
        edge (first (e/upconnect! v1 v2 "connexion"))]
    (is (= ["v1" "v2"] (map #(e/get-property % :name) (e/endpoints edge))))))

(deftest test-upconnect!
  (testing "Upconnecting once"
    (g/open)
    (let [v1 (v/create {:name "v1"})
          v2 (v/create {:name "v2"})
          edge (first (e/upconnect! v1 v2 "connexion" {:name "the edge"}))]
      (is (e/connected? v1 v2))
      (is (e/connected? v1 v2 "connexion"))
      (is (not (e/connected? v2 v1)))
      (is (= "the edge" (e/get-property edge :name)))
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
      (is (= "the edge" (e/get-property edge :name)))
      (is (= 1 (count (seq (.getEdges g/*graph*))))))))



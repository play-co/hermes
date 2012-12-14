(ns hermes.memory.edge-test
  (:use [clojure.test])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]))

(deftest test-delete
  (g/open)
  (let [u (v/create!)
        w (v/create!)
        a (e/connect! u w "test")
        a-id (e/get-id a)]
    (e/delete! a)
    (is (=  nil (e/find-by-id a-id)))))

(deftest test-simple-property-mutation
  (g/open)
  (let [v1 (v/create! {:name "v1"})
        v2 (v/create! {:name "v2"})
        edge (e/connect! v1 v2 "test" {:a 1})]
    (e/set-property! edge :b 2)
    (e/remove-property! edge :a)
    (is (= 2   (e/get-property edge :b)))
    (is (= nil (e/get-property edge :a)))))

(deftest test-multiple-property-mutation
  (g/open)
  (let [v1 (v/create! {:name "v1"})
        v2 (v/create! {:name "v2"})
        edge (e/connect! v1 v2 "test" {:a 0})]
    (e/set-properties! edge {:a 1 :b 2 :c 3})
    (is (= 1 (e/get-property edge :a)))
    (is (= 2 (e/get-property edge :b)))
    (is (= 3 (e/get-property edge :c)))))

(deftest test-property-map
  (g/open)
  (let [v1 (v/create! {:name "v1"})
        v2 (v/create! {:name "v2"})
        edge (e/connect! v1 v2 "test" {:a 1 :b 2 :c 3})
        prop-map (e/prop-map edge)]
    (is (= {:a 1 :b 2 :c 3} (dissoc prop-map :__id__ :__label__)))))

(deftest test-endpoints
  (g/open)
  (let [v1 (v/create! {:name "v1"})
        v2 (v/create! {:name "v2"})
        edge (first (e/upconnect! v1 v2 "connexion"))]
    (is (= ["v1" "v2"] (map #(e/get-property % :name) (e/endpoints edge))))))

(deftest test-refresh
  (g/open)
  (let [v1 (v/create! {:name "v1"})
        v2 (v/create! {:name "v2"})
        edge (first (e/upconnect! v1 v2 "connexion"))
        fresh-edge (e/refresh edge)]

    (is fresh-edge)
    (is (= (.getId edge) (.getId fresh-edge)))
    (is (= (e/prop-map edge) (e/prop-map fresh-edge)))))

(deftest test-upconnect!
  (testing "Upconnecting once"
    (g/open)
    (let [v1 (v/create! {:name "v1"})
          v2 (v/create! {:name "v2"})
          edge (first (e/upconnect! v1 v2 "connexion" {:name "the edge"}))]
      (is (e/connected? v1 v2))
      (is (e/connected? v1 v2 "connexion"))
      (is (not (e/connected? v2 v1)))
      (is (= "the edge" (e/get-property edge :name)))
      (is (= 1 (count (seq (.getEdges g/*graph*)))))))

  (testing "Upconnecting multiple times"
    (g/open)
    (let [v1 (v/create! {:name "v1"})
          v2 (v/create! {:name "v2"})
          edge (first (e/upconnect! v1 v2 "connexion" {:name "the edge"}))
          edge (first (e/upconnect! v1 v2 "connexion" {:a 1 :b 2}))
          edge (first (e/upconnect! v1 v2 "connexion" {:b 0}))]
      (is (e/connected? v1 v2))
      (is (e/connected? v1 v2 "connexion"))
      (is (not (e/connected? v2 v1)))
      (is (= "the edge" (e/get-property edge :name)))
      (is (= 1 (e/get-property edge :a)))
      (is (= 0 (e/get-property edge :b)))
      (is (= 1 (count (seq (.getEdges g/*graph*))))))))



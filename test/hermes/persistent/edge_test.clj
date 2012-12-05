(ns hermes.persistent.edge-test
  (:use [clojure.test])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]))

(deftest test-refresh
  (g/open)
  (let [v1 (g/transact! (v/create! {:name "v1"}))
        v2 (g/transact! (v/create! {:name "v2"}))
        edge (g/transact! (first (e/upconnect! v1 v2 "connexion")))
        fresh-edge (g/transact! (e/refresh edge))]

    (is fresh-edge)
    (is (= (.getId edge) (.getId fresh-edge)))
    (is (= (e/prop-map edge) (e/prop-map fresh-edge)))))

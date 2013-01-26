(ns hermes.persistent.edge-test
  (:use [clojure.test]
        [hermes.persistent.conf :only (conf)])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]))

(deftest test-refresh
  (g/open conf)
  (let [v1 (g/transact! (v/create! {:name "v1"}))
        v2 (g/transact! (v/create! {:name "v2"}))
        edge (g/transact! (first (e/upconnect! (v/refresh v1) (v/refresh v2) "connexion")))
        fresh-edge (g/transact! (e/refresh edge))]

    (is fresh-edge)
    (is (g/transact! (= (.getId (e/refresh edge)) (.getId (e/refresh fresh-edge)))))
    (is (g/transact! (= (e/prop-map (e/refresh edge)) (e/prop-map (e/refresh fresh-edge)))))))

(deftest test-edges-between
  (g/open conf)
  (let [v1 (g/transact! (v/create! {:name "v1"}))
        v2 (g/transact! (v/create! {:name "v2"}))
        edge (g/transact! (e/upconnect! (v/refresh v1) (v/refresh v2) "connexion"))
        found-edges (g/transact! (e/edges-between (v/refresh v1) (v/refresh v2)))]

    (is edge)
    (is (g/transact! (= (e/prop-map (e/refresh (first edge)))
                        (e/prop-map (e/refresh (first found-edges))))))))

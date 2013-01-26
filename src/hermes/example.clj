(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]
            [hermes.type   :as t]))
(g/open 
    {:storage {:backend "cassandra"
               :hostname "localhost"}})

(def shilei (g/transact! (v/create! {:name "shilei"})))
(def friend (g/transact! (v/create! {:name "friend"})))
(g/transact! (e/upconnect! (v/refresh shilei) (v/refresh friend) "has-phone"))


(g/transact! (e/edges-between (v/refresh shilei) (v/refresh friend)))



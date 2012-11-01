(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]))

(g/open)
(v/index-on "name")
(def Zack (v/create {:name "Zack"
                     :age "21"
                     :occupation "ARWOO"}))
(println  (v/find "name" "Zack"))

(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]
            [hermes.type   :as t])
  (:use hermes.query))

(g/open)

(t/create-vertex-key-once :name String {:unique true
                                        :indexed true})

(def Zack (v/create {:name "Zack"
                     :age "21"
                     :gender "Male"
                     :occupation "INTERN"}))

(def Brooke (v/create {:name "Brooke"
                       :age "19"
                       :gender "Female"
                       :occupation "Student"}))

(def Cindy (v/create {:name "Cindy"
                      :occupation "Saleswoman"}))

(def Steve (v/create {:name "Steve"
                      :occupation "Salesmen"}))

;;Direction? 
(e/create Zack Brooke "siblings")

(e/create Steve Cindy  "married")

(e/create Zack Cindy  "child")
(e/create Zack Steve  "child")

(e/create Brooke Cindy  "child")
(e/create Brooke Steve  "child")

(defquery siblings-with []
  (--- "siblings"))

(defquery child-of []
  (--> "child"))

(defquery find-parents-of-siblings []
  siblings-with
  child-of
  properties!)

;;(println (find-parents-of-siblings Zack))


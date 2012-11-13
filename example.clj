(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]
            [hermes.type   :as t]))

(g/open {:storage {:backend "hbase"
                   :hostname "127.0.0.1"}})


(g/transact! (t/create-vertex-key-once :name String {:unique true
                                                     :indexed true}))

(t/get-type "friend")

(def Zack (g/transact! (v/create {:name "Zack"
                                  :age "21"
                                  :gender "Male"
                                  :occupation "INTERN"})))

(def Brooke (g/transact! (v/create {:name "Brooke"
                                    :age "19"
                                    :gender "Female"
                                    :occupation "Student"})))

(def Mack (g/transact! (v/create {:name "Mack"
                                  :age "21"
                                  :gender "Male"
                                  :occupation "Stoner"})))

;; (def edge0 (g/transact! (e/connect! Zack Brooke "starter")))
;; (println "edge0" edge0)
;; (def edge1 (g/transact! (e/connect! Zack Brooke "sibling")))
;; (println "edge1" edge1)
(def edge2 (g/transact! (e/connect! Mack Brooke "friend")))
(println "edge2" edge2)
;; (def edge3 (g/transact! (e/connect! Zack Mack "bros")))
;; (println "edge3" edge3)
;;(def edge4 (g/transact! (e/connect! Zack Mack friend)))
;;(println "edge4" (try edge4 (catch Exception e e)))

;; (def Cindy (v/create {:name "Cindy"
;;                       :occupation "Saleswoman"}))

;; (def Steve (v/create {:name "Steve"
;;                       :occupation "Salesmen"}))

;; ;;Direction? 
;; (e/create Zack Brooke "siblings")

;; (e/create Steve Cindy  "married")

;; (e/create Zack Cindy  "child")
;; (e/create Zack Steve  "child")

;; (e/create Brooke Cindy  "child")
;; (e/create Brooke Steve  "child")

;; (defquery siblings-with 
;;    (--- "siblings"))

;; (defquery child-of 
;;    (--> "child"))

;; (defquery find-parents-of-siblings
;;   siblings-with
;;   child-of
;;   properties!)

;; (println (find-parents-of-siblings Zack))

(defn -main [& args]
  ["Hello World!" Zack])

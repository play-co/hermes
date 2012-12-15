(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]
            [hermes.type   :as t]))
(g/open 
    {:storage {:backend "cassandra"
               :hostname "localhost"}})

(def fail (g/transact! (v/create! {:fail [1]})))

(g/transact! (t/create-vertex-key-once :name-a String {:unique true
                                                       :indexed true}))

(def Zack (g/transact! (first (v/upsert! :name-a
                                         {:name-a "Zack"
                                          :age 21
                                          :gender "Male"
                                          :occupation "INTERN"}))))

(def Brooke (g/transact! (first (v/upsert! :name-a
                                           {:name-a "Brooke"
                                            :age 19
                                            :gender "Female"
                                            :occupation "Student"}))))

(g/transact! (println "connect" (e/connect! (v/refresh Zack)
                                  (v/refresh Brooke)
                                  "siblings" {:since 1991})))

;;Oops mistake, should probably fix that
(g/transact! (println "upconnect!" (e/upconnect! (first (v/find-by-kv :name-a "Zack"))
                                    (v/refresh Brooke)
                                    "siblings" {:since 1993})))

;;And well, I am really more than just an intern
(g/transact! (v/upsert! :name-a {:name-a "Zack"
                               :occupation "Software specialist and all around nice guy"}))

(defn -main [& args]
  ["Hello World!"
   (g/transact! (doall (for [vertex [Zack Brooke]]
                         (-> vertex
                             v/refresh
                             v/prop-map) )))])

(ns hermes.example
  (:require [hermes.core   :as g]
            [hermes.vertex :as v]
            [hermes.edge   :as e]
            [hermes.type   :as t]))

(g/open {:storage {:backend "cassanda"
                   :hostname "127.0.0.1"}})


(g/transact! (t/create-vertex-key-once :name String {:unique true
                                                     :indexed true}))

(def Zack (g/transact! (v/create {:name "Zack"
                                  :age 21
                                  :gender "Male"
                                  :occupation "INTERN"})))

(def Brooke (g/transact! (v/create {:name "Brooke"
                                    :age 19
                                    :gender "Female"
                                    :occupation "Student"})))

(g/transact! (connect! Zack Brook "siblings" {:since 1991}))

;;Oops mistake, should probably fix that
(g/transact! (upconnect! Zack Brook "siblings" {:since 1993}))

;;And well, I am really more than just an intern
(g/transact! (upsert! :name {:name "Zack"
                             :occupation "Software specialist and all around nice guy"}))

(defn -main [& args]
  ["Hello World!" Zack])

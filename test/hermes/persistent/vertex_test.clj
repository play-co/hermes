(ns hermes.persistent.vertex-test
  (:use [clojure.test])
  (:require [hermes.core :as g]
            [hermes.vertex :as v]
            [hermes.type :as t]))

(def conf {:storage {:backend "hbase"
                     :hostname "127.0.0.1"}})

(deftest test-find-by-kv-backed-by-hbase
  (g/open conf)
  (g/transact! (t/create-vertex-key-once :age Long {:indexed true}))
  (g/transact!
    (let [v1 (v/create! {:age  1
                         :name "A"})
          v2 (v/create! {:age 2
                         :name "B"})
          v3 (v/create! {:age 2
                         :name "C"})]
      (is (= #{"A"}
             (set (map #(v/get-property % :name) (v/find-by-kv :age 1)))))
      (is (= #{"B" "C"}
             (set (map #(v/get-property % :name) (v/find-by-kv :age 2))))))))

(deftest test-upsert!-backed-by-hbase
  (g/open conf)
  (g/transact! (t/create-vertex-key-once :first-name String {:indexed true})
               (t/create-vertex-key-once :last-name  String {:indexed true}))  
  (let [v1-a (v/upsert! :first-name
                        {:first-name "Zack"
                         :last-name "Maril"
                         :test 0})
        v1-b (v/upsert! :first-name
                        {:first-name "Zack"
                         :last-name "Maril"
                         :test 1})
        v2   (v/upsert! :first-name
                        {:first-name "Brooke"
                         :last-name "Maril"})]
    (is (= 1
           (v/get-property (v/refresh (first v1-a)) :test)
           (v/get-property (v/refresh (first v1-b)) :test)))

    (v/upsert! :last-name {:last-name "Maril"
                           :heritage "Some German Folks"})
    (is (= "Some German Folks"
           (v/get-property (v/refresh (first v1-a)) :heritage)
           (v/get-property (v/refresh (first v1-b)) :heritage)
           (v/get-property (v/refresh (first v2)) :heritage)))))

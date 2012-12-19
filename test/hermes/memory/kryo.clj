(ns hermes.memory.kryo
  (:use [clojure.test])
  (:require [hermes.core :as g]
            [hermes.edge :as e]
            [hermes.vertex :as v]
            [hermes.kryo :as k]))

(deftest test-vector-and-back-again
  (let [a [1 2 3]
        b ["a" "b" "c"]
        c [1 "b" "c"]]
    (is (= a (k/revert (k/prepare a))))
    (is (= b (k/revert (k/prepare b))))
    (is (= c (k/revert (k/prepare c))))))

(deftest test-list-and-vector-again
  (let [a '(1 2 3)
        b '("a" "b" "c")
        c '(1 "b" "2")]
    (is (= clojure.lang.PersistentVector (type (k/revert (k/prepare a)))))
    (is (= a (k/revert (k/prepare a))))
    (is (= b (k/revert (k/prepare b))))
    (is (= c (k/revert (k/prepare c))))))

(deftest test-map-and-back-again 
  (let [a {:a 1 :b 2 :c 3}
        b {:a "a" :b "b" :c "c"}
        c {:a-a "a" :b 2 :c "c"}]
    (is (= a (k/revert (k/prepare a))))
    (is (= b (k/revert (k/prepare b))))
    (is (= c (k/revert (k/prepare c))))))

(deftest test-mixed-data-structures
  (let [a {:a [1 "a" 3] :b #{1 "a" 3}}
        b #{[1 "a" 3] {:a 1 :b 2}}
        c [#{1 "a 3"} {:a 1 :b 2}]
        d #{{
             :unholy [1 2 3 #{"a"}]
             :unwieldy "random"
             }
            {
             :why "oh why"
             :a  "test"}}]
    (is (= a (k/revert (k/prepare a))))
    (is (= b (k/revert (k/prepare b))))
    (is (= c (k/revert (k/prepare c))))
    (is (= d (k/revert (k/prepare d))))))

(deftest test-no-persisting-of-keywords
  (g/open)
  (is (thrown? Throwable #"keyword"  (v/create! {:a :a}))))

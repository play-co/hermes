(ns hermes.memory.type-test
  (:use [clojure.test]
        [hermes.core :only (open *graph*)])
  (:require [hermes.type :as t]))

(deftest test-create-groups
  (open)
  (let [my-group-name "My-Group-Name"
        my-group (t/create-group 100 my-group-name)
        default-group-id (.getID t/default-group)]
    (is (= my-group-name (.getName my-group)) "the group has the correct name")
    (is (= 100 (.getID my-group)) "the group has the correct ID")
    (is (not (= default-group-id (.getID my-group))) "my-group has a different ID from the default group")))

(deftest test-create-vertex-key
  (testing "With no parameters"
    (open)
    (t/create-vertex-key :my-key Integer)
    ;; Get the key from the graph
    (let [k (t/get-type :my-key)]
      (is (.isPropertyKey k) "the key is a property key")
      (is (not (.isEdgeLabel k)) "the key is not an edge label")
      (is (= "my-key" (.getName k)) "the key has the correct name")
      (is (not (.isFunctional k)) "the key is not functional")
      (is (not (.hasIndex k)) "the key is not indexed")
      (is (not (.isUnique k)) "the key is not unique")))

  (testing "With parameters"
    (open)
    (t/create-vertex-key :my-key Integer
              {:functional true
               :indexed true
               :unique true})
    ;; Get the key from the graph
    (let [k (t/get-type :my-key)]
      (is (.isPropertyKey k) "the key is a property key")
      (is (not (.isEdgeLabel k)) "the key is not an edge label")
      (is (= "my-key" (.getName k)) "the key has the correct name")
      (is (.isFunctional k) "the key is functional")
      (is (.hasIndex k) "the key is indexed")
      (is (.isUnique k) "the key is unique"))))

(deftest test-create-edge-label
  (testing "With no parameters"
    (open)
    (t/create-edge-label :my-label)
    ;; Get the label from the graph
    (let [lab (t/get-type :my-label)]
      (is (.isEdgeLabel lab) "the label is an edge label")
      (is (not (.isPropertyKey lab)) "the label is not a property key")
      (is (= "my-label" (.getName lab)) "the label has the correct name")
      (is (not (.isFunctional lab)) "the label is not functional")
      (is (not (.isSimple lab)) "the label is not simple")
      (is (not (.isUndirected lab)) "the label is not undirected")
      (is (not (.isUnidirected lab)) "the label is not unidirected")
      (is (= t/default-group (.getGroup lab)) "the label has the default group")))

  (testing "With parameters"
    (open)
    (t/create-edge-label :my-label
                       {:functional true
                        :simple true
                        :direction "undirected"})
    ;; Get the label from the graph
    (let [lab (t/get-type :my-label)]
      (is (.isEdgeLabel lab) "the label is an edge label")
      (is (not (.isPropertyKey lab)) "the label is not a property key")
      (is (= "my-label" (.getName lab)) "the label has the correct name")
      (is (.isFunctional lab) "the label is functional")
      (is (.isSimple lab) "the label is simple")
      (is (.isUndirected lab) "the label is undirected")
      (is (= t/default-group (.getGroup lab)) "the label has the default group")))

  (testing "With a group"
    (open)
    (let [a-group (t/create-group 101 "a-group")
          the-label (t/create-edge-label :lab {:group a-group})]
      (is (.isEdgeLabel the-label) "the label is an edge label")
      (is (= a-group (.getGroup the-label)) "the label has the correct group"))))

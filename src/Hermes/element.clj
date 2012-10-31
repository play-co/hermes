(ns Hermes.element
  (:import (com.tinkerpop.blueprints Element)))

(defprotocol HermesElement
  "Tinkerpop element"
  (get-keys [this])
  (get-id [this])
  (get-property [this key])
  (to-map [this])
  (remove-property [this key])
  (set-property [this key value]))


(extend-type Element
  HermesElement

  (get-keys [this]
    (.getPropertyKeys this))
  
  (get-id [this]
    (.getId this))

  (set-property [this key value]
    (.setProperty this key value))
  
  (get-property [this key]
    (.getProperty this key))

  (remove-property [this key]
    (.removeProperty this key))
  
  (to-map [this]
    (into {:id (get-id this)} (map
              #(vector (keyword %1) (get-property this %1))
              (get-keys this)))))

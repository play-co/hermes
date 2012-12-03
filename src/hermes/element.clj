(ns hermes.element
  (:import (com.tinkerpop.blueprints Element)))

(defprotocol HermesElement
  "Tinkerpop element"
  (get-keys [this])
  (get-id [this])
  (get-property [this key])
  (prop-map [this])
  (remove-property! [this key])
  (set-property! [this key value])
  (set-properties! [this data]))

(extend-type Element
  HermesElement

  (get-keys [this]
    (set (map keyword (.getPropertyKeys this))))
  
  (get-id [this]
    (.getId this))

  (set-property! [this key value]
    ;;Avoids changing keys that shouldn't be changed.
    ;;Important when using types. You aren't ever going to change a
    ;;user's id for example. 
    (when (not= value (get-property this (name key)))
      (.removeProperty this (name key)) ;;Hacky work around! Yuck!
      (.setProperty this (name key) value)))
  
  (set-properties!  [this data]
    (doseq [[k v] data] (set-property! this (name k) v))
    this)

  (get-property [this key]
    (.getProperty this (name key)))

  (remove-property! [this key]
    (.removeProperty this (name key)))
  
  (prop-map [this]
    (into {:id (get-id this)}
          (map
           #(vector (keyword %1) (get-property this %1))              
           (get-keys this)))))

;; There is a way of doing this that involves reify or proxy that
;; would make (:name (v/create {:name "Zack"})) work. 
;; (extend Element
;;   clojure.lang.ILookup
;;   {:valAt (fn
;;             ([this k not-found] nil)
;;             ([this k] (.getProperty this (name k))))})  

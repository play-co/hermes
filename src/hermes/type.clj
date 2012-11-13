(ns hermes.type
  (:import (com.thinkaurelius.titan.core TypeGroup
                                         TitanType))
  (:use [hermes.core :only (*graph*)]))

(defn get-type [tname]
  (.getType *graph* (name tname)))

;; "A TitanGroup is defined with a name and an id, however, two groups with the
;; same id are considered equivalent. The name is only used for recognition
;; [and] is not persisted in the database. Group ids must be positive (>0) and
;; the maximum group id allowed is configurable."
;; http://thinkaurelius.github.com/titan/javadoc/current/com/thinkaurelius/titan/core/TypeGroup.html
(defn create-group [group-id group-name]
  (TypeGroup/of group-id group-name))

(defn create-type-maker [tname {:keys [functional f-locked group]
                                :or   {functional  false
                                       f-locked    false
                                       group       TypeGroup/DEFAULT_GROUP}}]
  (let [type-maker   (.. *graph*
                         makeType
                         (name (name tname))
                         (group group))
        _     (when functional (.functional type-maker f-locked))]
    type-maker))


(defn create-edge-label
  ([name] (create-edge-label name {}))
  ([name {:keys [simple direction primary-key signature]
          :as m
          :or {simple false
               direction "directed"
               primary-key nil
               signature   nil}}]
     (let [type-maker (create-type-maker name m)
           _ (when simple (.simple type-maker))
           _ (case direction
               "directed"    (.directed type-maker)
               "unidirected" (.unidirected type-maker)
               "undirected"  (.undirected type-maker))
           _ (when signature (.signature type-maker signature))
           _ (when primary-key (.primaryKey type-maker (into-array TitanType primary-key)))]
       (.makeEdgeLabel type-maker))))

(defn create-vertex-key
  ([name data-type] (create-vertex-key name data-type {}))
  ([name data-type {:keys [unique indexed]
                    :as m
                    :or {unique false
                         indexed false}}]
     (let [type-maker (.. (create-type-maker name m)
                          (dataType data-type))
           _ (when unique (.unique type-maker))
           _ (when indexed (.indexed type-maker))]
       (.makePropertyKey type-maker))))

(defn create-edge-label-once [name & args]
  (if-let [named-type (get-type name)]
    named-type
    (apply create-edge-label (cons name args))))

(defn create-vertex-key-once [name & args]
  (if-let [named-type (get-type name)]
    named-type
    (apply create-vertex-key (cons name args))))

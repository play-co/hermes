(ns Hermes.reflect
  (:require [clojure.reflect :as r])
  (:use [clojure.pprint :only (pprint)]))

(defn members-of-object [object]
  (-> object r/reflect :members))

(defmacro find-member [object prop]
  `(filter #(= '~prop (:name %)) (members-of-object ~object)))

(defn names-of-members [object]
  (sort (distinct (map :name (members-of-object object)))))
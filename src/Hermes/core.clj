(ns Hermes.core
  (:import (com.thinkaurelius.titan.core TitanFactory)
           (com.tinkerpop.blueprints Element TransactionalGraph TransactionalGraph$Conclusion)
           (org.apache.commons.configuration BaseConfiguration))
  (:use Hermes.reflect))


(def ^{:dynamic true} *graph*)

(def success-flag TransactionalGraph$Conclusion/SUCCESS)
(def failure-flag TransactionalGraph$Conclusion/FAILURE)

(defn create-config [m]
  "string"

  )
(defn open
  ([ ] (alter-var-root (var *graph*) (fn [_] (TitanFactory/openInMemoryGraph))))
  ([m] (alter-var-root (var *graph*) (fn [_]
                                       (if (string? m)
                                         (TitanFactory/open m)
                                         (TitanFactory/open (create-config m)))))))

(defmacro transact [& forms]
  `(try (let [tx#      (.startTransaction *graph*)
              results# (binding [*graph* tx#] ~@forms)
              _#       (.commit tx#)
              end#     (.stopTransaction *graph* success-flag)]
          results#)
        (catch Exception e#
          (.stopTransaction *graph* failure-flag)
          (throw e#))))

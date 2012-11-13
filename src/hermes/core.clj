(ns hermes.core
  (:import (com.thinkaurelius.titan.core TitanFactory)
           (com.tinkerpop.blueprints Element TransactionalGraph TransactionalGraph$Conclusion)
           (org.apache.commons.configuration BaseConfiguration)))

(def ^{:dynamic true} *graph*)

(def success-flag TransactionalGraph$Conclusion/SUCCESS)
(def failure-flag TransactionalGraph$Conclusion/FAILURE)

(defn convert-config-map [m]
  (let [conf (BaseConfiguration.)]
    (doseq [[k1 v1] m]
            (if (string? v1)
              (.setProperty conf (name k1) v1)
              (doseq [[k2 v2] v1]
                (.setProperty conf (str (name k1) "." (name k2)) v2))))
    conf))

(defn open
  ([ ] (alter-var-root (var *graph*) (fn [_] (TitanFactory/openInMemoryGraph))))
  ([m] (alter-var-root (var *graph*) (fn [_]
                                       (if (string? m)
                                         (TitanFactory/open m)
                                         (TitanFactory/open (convert-config-map m)))))))
(defmacro transact! [& forms]
  `(try
     (let [tx#      (.startTransaction *graph*)
           results# (binding [*graph* tx#] ~@forms)]
       (.commit tx#)
       (.stopTransaction *graph* success-flag)
       results#)
     (catch Exception e#
       (.stopTransaction *graph* failure-flag)
       (throw e#))))

(defmacro with-graph
  [g & forms]
  `(binding [*graph* ~g]
     ~@forms))

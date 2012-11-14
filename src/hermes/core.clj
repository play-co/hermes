(ns hermes.core
  (:import (com.thinkaurelius.titan.core TitanFactory)
           (com.tinkerpop.blueprints Element TransactionalGraph TransactionalGraph$Conclusion)
           (com.thinkaurelius.titan.graphdb.database StandardTitanGraph)
           (com.thinkaurelius.titan.graphdb.blueprints TitanInMemoryBlueprintsGraph)
           (com.thinkaurelius.titan.graphdb.transaction StandardPersistTitanTx)
           (org.apache.commons.configuration BaseConfiguration)))

(def ^{:dynamic true} *graph*)

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

(defn- stop-transaction
  [success?]
  (let [success-flag TransactionalGraph$Conclusion/SUCCESS
        failure-flag TransactionalGraph$Conclusion/FAILURE]
        (.stopTransaction *graph* (if success? success-flag failure-flag))))

(defn- supports-transactions?
  []
  (-> *graph*
      .getFeatures
      .toMap
      (.get "supportsTransactions")))

(defn- transact!* [f]
  (if (supports-transactions?)
    (try
      (let [tx      (.startTransaction *graph*)
            results (binding [*graph* tx] (f))]
        (.commit tx)
        (stop-transaction true)
        results)
      (catch Exception e
        (stop-transaction false)
        (throw e)))
    ; Transactions not supported.
    (f)))

(defmacro transact! [& forms]
  `(~transact!* (fn [] ~@forms)))

(defmacro with-graph
  [g & forms]
  `(binding [*graph* ~g]
     ~@forms))

(defn ensure-graph-is-transaction-safe []
  (when (not (#{StandardPersistTitanTx TitanInMemoryBlueprintsGraph}
              (type *graph*)))
    ;;TODO: Not a great error message. Could be better.
    (throw (Throwable. "All actions on a persistent graph must be wrapped in transact! "))))

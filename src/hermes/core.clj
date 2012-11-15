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
  "Open a graph.  If no configuration is supplied an in-memory graph is opened."
  ([ ] (alter-var-root (var *graph*) (fn [_] (TitanFactory/openInMemoryGraph))))
  ([m] (alter-var-root (var *graph*) (fn [_]
                                       (if (string? m)
                                         (TitanFactory/open m)
                                         (TitanFactory/open (convert-config-map m)))))))

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
        (.stopTransaction *graph* TransactionalGraph$Conclusion/SUCCESS)
        results)
      (catch Exception e
        (.stopTransaction *graph* TransactionalGraph$Conclusion/FAILURE)
        (throw e)))
    ; Transactions not supported.
    (f)))

(defmacro transact! [& forms]
  "Perform graph operations inside a transaction."
  `(~transact!* (fn [] ~@forms)))

(defn- retry-transact!* [max-retries wait-time try-count f]
  (let [res (try {:value (transact!* f)}
              (catch Exception e
                {:exception e}))]
     (if (:value res)
       (:value res)
       (if (> try-count max-retries)
         (throw (:exception res)
         (do
           (Thread/sleep wait-time)
           (recur max-retries wait-time (inc try-count) f)))))))

(defmacro retry-transact! [max-retries wait-time & forms]
  "Perform graph operations inside a transaction.  The transaction will retry up to `max-retries` times, and will wait
  `wait-time` milliseconds before each retry."
  `(~retry-transact!* ~max-retries ~wait-time 1 (fn [] ~@forms)))

(defmacro with-graph
  "Perform graph operations against a specific graph."
  [g & forms]
  `(binding [*graph* ~g]
     ~@forms))

(defn ensure-graph-is-transaction-safe []
  "Ensure that we are either in a transaction or using an in-memory graph."
  (when (not (#{StandardPersistTitanTx TitanInMemoryBlueprintsGraph}
              (type *graph*)))
    ;;TODO: Not a great error message. Could be better.
    (throw (Throwable. "All actions on a persistent graph must be wrapped in transact! "))))

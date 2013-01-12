(ns hermes.core
  (:import (com.thinkaurelius.titan.core TitanFactory)
           (com.tinkerpop.blueprints Element TransactionalGraph TransactionalGraph$Conclusion)
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

(defn get-features
  "Get a map of features for a graph.
  (http://tinkerpop.com/docs/javadocs/blueprints/2.1.0/com/tinkerpop/blueprints/Features.html)"
  []
  (-> *graph* .getFeatures .toMap))

(defn get-feature
  "Gets the value of the feature for a graph."
  [f]
  (.get (get-features) f))

(defn- transact!* [f]
  (if (get-feature "supportsTransactions")
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

(defn- retry-transact!* [max-retries wait-time-fn try-count f]
  (let [res (try {:value (transact!* f)}
              (catch Exception e
                {:exception e}))]
     (if (not (:exception res))
       (:value res)
       (if (> try-count max-retries)
         (throw (:exception res))
         (let [wait-time (wait-time-fn try-count)]
           (Thread/sleep wait-time)
           (recur max-retries wait-time-fn (inc try-count) f))))))

(defmacro retry-transact! [max-retries wait-time & forms]
  "Perform graph operations inside a transaction.  The transaction will retry up
  to `max-retries` times.  `wait-time` can be an integer corresponding to the
  number of milliseconds to wait before each try, or it can be a function that
  takes the retry number (starting with 1) and returns the number of
  milliseconds to wait before that retry."
  `(let [wait-time-fn# (if (ifn? ~wait-time)
                           ~wait-time
                           (constantly ~wait-time))]
    (~retry-transact!* ~max-retries wait-time-fn# 1 (fn [] ~@forms))))

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

(defproject hermes "0.1.4-SNAPSHOT"
  :description "Embedded Titan Graph"
  :url "https://github.com/gameclosure/hermes"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["typesafe" "http://repo.typesafe.com/typesafe/snapshots/"]
                 ["apache" "http://repository.apache.org/content/repositories/releases/"]] 
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.tinkerpop.blueprints/blueprints-core "2.1.0"]
                 [com.thinkaurelius.titan/titan "0.1.0"]])

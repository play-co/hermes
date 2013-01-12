(defproject hermes "0.2.6-SNAPSHOT"
  :description "Embedded Titan Graph"
  :url "https://github.com/gameclosure/hermes"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["typesafe" "http://repo.typesafe.com/typesafe/snapshots/"]
                 ["apache" "http://repository.apache.org/content/repositories/releases/"]
                 ["sonatype" "https://oss.sonatype.org/content/repositories/snapshots/"]
                 ["oracle" "http://download.oracle.com/maven/"]] 
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.tinkerpop.blueprints/blueprints-core "2.2.0"]
                 [com.tinkerpop.gremlin/gremlin-java "2.2.0"]
                 [com.thinkaurelius.titan/titan "0.2.0-SNAPSHOT"]]
  :profiles {:dev {:plugins [[lein-kibit "0.0.7"]]}})


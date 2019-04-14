(defproject timelord "0.0.1" 
  :description "A web app to track notes and time for cases and provides a game and incentives for tracking time"
  :url "http://timelord/"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring "1.7.1"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.6.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler timelord_web.routes/timelord}
  :profiles  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring/ring-mock "0.3.2"]]}}
  :main ^:skip-aot timelord.core)

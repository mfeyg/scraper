(ns scraper.core
  (:use scraper.api)
  (:use [ring.adapter.jetty :only [run-jetty]]))

(defn -main [port]
  (run-jetty scraper-api {:port (Integer/parseInt port)}))

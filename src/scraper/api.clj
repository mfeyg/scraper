(ns scraper.api
  (:use scraper.engine)
  (:use [ring.util.request :only [body-string]])
  (:use [ring.util.response :only [response]])
  (:require [clojure.data.json :as json]))

(defn scraper-api [req]
  (-> req body-string json/read-str execute response))

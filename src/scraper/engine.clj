(ns scraper.engine
  (:require [clj-http.client :as client])
  (:use net.cgrand.enlive-html))

(defn- http [method url & [options]]
  (->
    (({"get" client/get
       "put" client/put 
       "post" client/post 
       "delete" client/delete} method) url (clojure.walk/keywordize-keys options))
    (get :body)))

(defn- walk-to [doc [step & steps]]
  (if step
    (recur (if (integer? step)
             (nth doc step)
             (get doc (keyword step)))
           steps)
    doc))

(declare ^:dynamic ^:private *env*)

(defn- evaluate [form]
  (if (vector? form)
    (case (first form)
      "var" (get *env* (second form))
      ("get" "put" "post" "delete") (apply http form)
      "select" (let [[_ doc selector loc] form]
                 (-> doc evaluate java.io.StringReader. html-resource
                     (select (clojure.walk/postwalk #(if (string? %) (keyword %) %) selector))
                     (walk-to loc))))
    form))

(defn- exec [env [cmd & cmds]]
  (try
    (if (= (first cmd) "let")
      (let [[_ name & forms] cmd]
        (exec (assoc env name (exec env forms)) cmds))
      (let [res (binding [*env* env]
                  (evaluate cmd))]
        (if (empty? cmds)
          res
          (exec env cmds))))
    (catch Exception e (.getMessage e))))

(defn execute [cmds]
  (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
    (exec {} cmds)))

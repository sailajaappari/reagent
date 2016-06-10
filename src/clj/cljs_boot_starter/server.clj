(ns cljs-boot-starter.server
  (:use compojure.core)
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resources]
            [ring.util.response :as response]
            [noir.response :refer [edn]]
            [clojure.pprint :refer [pprint]])
  (:gen-class))

(defn save-document [doc]
   (pprint doc)
   (:status "ok"))

(defroutes service-routes
  (POST "/save" {:keys [body-params]
       (edn (save-document body-params))}))

(defn handler [request]
  (response/redirect "/index.html"))

(def app
  (-> handler
      (resources/wrap-resource "public")))

(defn -main [& args]
  (jetty/run-jetty app {:port 3000}))

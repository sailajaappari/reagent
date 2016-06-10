(ns cljs-boot-starter.client
  (:require [reagent.core :as ra :refer [atom render]]))

(enable-console-print!)

(def state (atom {:doc {} :saved? false}))

(defn row [label body]
  [:div 
   [:div [:span label]]
   [:div body]])

(defn set-value [id value]
  (swap! state assoc :saved? false)
  (swap! state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @state [:doc id]))

(defn text-input [id label]
  [row label [:input {:type "text"
                      :class "form.control"
                      :value (get-value id)
                      :on-change #(set-value id (-> % .-target .-value))}]])

(defn hello []
  [:div
   [:div 
    [:h1 "Reagent Form"]]
   [text-input :first-name "First Name"]
   [text-input :last-name "Last Name"]
   [:button {:type "submit"
             :on-click #(js/console.log (clj->js @state))} "Submit"]])

(defn init []
  (render [hello] (.getElementById js/document "my-app-area")))

(init)

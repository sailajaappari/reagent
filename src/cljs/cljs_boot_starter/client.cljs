(ns cljs-boot-starter.client
  (:require [reagent.core :as ra :refer [atom render]]
            [ajax.core :refer [POST]]))

(enable-console-print!)

(def state (atom {:doc {} :saved? false}))

(defn row [label body]
  [:div 
   [:div [:span label]]
   [:div body]])

(defn set-value! [id value]
  (swap! state assoc :saved? false)
  (swap! state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @state [:doc id]))

(defn text-input [id label]
  [row label [:input {:type "text"
                      :class "form.control"
                      :value (get-value id)
                      :on-change #(set-value! id (-> % .-target .-value))}]])

(defn list-item [id k v selections]
  (letfn [(handle-click! []
            (swap! selections update-in [k] not)
            (set-value! id (->> @selections (filter second) (map first))))]
     [:li {:class (str "List Group Item"
                     (if (k @selections) " acitve"))
           :on-click handle-click!}
       v]))

(defn selection-list [id label & items]
  (let [selections (->> items (map (fn [[k]] [k false])) (into {}) atom)]
     (fn []
       [:div 
        [:div [:span label]]
        [:div
         [:div
          (for [[k v] items]
             [list-item id k v selections])]]])))

(defn save-doc []
  (POST (str js/context "/save")
        {:params (:doc @state)
         :handler (fn [_]
                     (swap! state assoc :saved? true))}))

(defn hello []
  [:div
   [:div 
    [:h1 "Reagent Form"]]
   [text-input :first-name "First Name"]
   [text-input :last-name "Last Name"]
   [selection-list :favorite-drinks "Favorite Drinks" [:coffee "Coffee"]
                                                      [:juice "Juice"]
                                                      [:milk "Milk"]]
   (if (:saved? @state)
     [:p "Saved"]
     [:button {:type "submit"
              :class "btn btn-default"
              :on-click save-doc}
     "Submit"])])

(defn init []
  (render [hello] (.getElementById js/document "my-app-area")))

(init)

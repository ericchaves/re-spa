(ns respa.views
  
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [respa.components :as c]))

(defn home-page []
  (let [user (subscribe [:current-user])]
    [:div  [:h1 (str "Home - " (:username @user))]
     [:a {:href "/about"} "goto about"] [:br]
     [:button {:on-click #(comp (dispatch [:set-current-user nil])
                                (dispatch [:navigate-to :home]))} "Log me out"] 
     [c/calling-component]]))


(defn login-page []
  [:div [:h1 "Login"]
   [:button {:on-click #(comp (dispatch [:set-current-user {:username "eric chaves"}])
                              (dispatch [:navigate-to :home]))} "Log me in"]])

(defn about-page []
  [:div  [:h1 "About"]
   [:a {:href "/home"} "back to home"]
   [c/calling-component]])



(defmulti pages identity)
(defmethod pages :home [] [home-page])
(defmethod pages :login [] [login-page])
(defmethod pages :about [] [about-page])
(defmethod pages :default [] [:div "No page defined"])

(defn main [] 
  (let [page (subscribe [:current-page])]
    (pages @page)))


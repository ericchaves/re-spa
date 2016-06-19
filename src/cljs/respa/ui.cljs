(ns respa.ui
  "reusable UI elements
   - each element is a pure functions returning hiccup
   - all logic/behaviour MUST be left on *components* ns"
  (:require [cljsjs.material]))


(when ^boolean goog.DEBUG 
  (enable-console-print!))

;; Navigation ui

(defn navigation-links [links]
  [:nav.mdl-navigation.mdl-layout--large-screen-only
   (for [link links] 
     ^{:key link} [:a.mdl-navigation__link (dissoc link :title) (:title link)])])

(defn navigation-drawer [props]
  (let [{:keys [title links]} props]
    [:div.mdl-layout__drawer
     [:span.mdl-layout-title title]
     [navigation-links links]]))

(defn navbar [props]
  (let [{:keys [title links drawer content]} props]
    [:div.mdl-layout.mdl-js-layout.mdl-layout--fixed-header
     [:header.mdl-layout__header
      [:div.mdl-layout__header-row
       [:span.mdl-layout-title title]
       [:div.mdl-layout-spacer]
       [navigation-links links]]]
     (when drawer
       [navigation-drawer drawer])
     content
     ]))

;; Inputs

;; Alerts

(defn toast [props]
  (let [notification (.querySelector js/document "#toast")]
    (when (not (nil? notification))
      (js/notification.MaterialSnackbar.showSnackbar (clj->js props)))))

(defn snackbar []
  [:div.mdl-snackbar.mdl-js-snackbar {:id "toast"
                                      :aria-live "assertive"
                                      :aria-atomic "true"
                                      :aria-relevant "text"}
   [:div.mdl-snackbar__text]
   [:button.mdl-snackbar__action {:type "button"}]])

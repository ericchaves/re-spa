(ns respa.components
  "first class components
   - MAY contain some logic/behavior
   - re-frame subscriptions or dispatchs SHOULD occur here most of the time
   - for temporary local state (like handling form inputs) use a temporary ratom"
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [respa.ui :as ui]
            [cljsjs.material]
            [clojure.string :refer [split blank?]]))

(defn home-page []
  (let [current-user (subscribe [:current-user])
        user @current-user]
    [ui/navbar {:title "Home"
                :links [{:title (str (:username user) " (log me out!)")
                         :href "#"
                         :on-click #(comp (dispatch [:set-current-user nil])
                                          (dispatch [:navigate-to :home]))}]
                :drawer {:title "Drawer"
                         :links [{:title "About"
                                  :href "/about"
                                  :on-click #(dispatch [:navigate-to :about])}]}
                :content [:h4 (str "Hello " (:username user) "!")]}]))

(defn about-page []
  (let [current-user (subscribe [:current-user])
        user @current-user
        name (first (split (:username user) #" "))]
    [ui/navbar {:title "About"
                :links [{:title (str (:username user) " (log me out!)")
                         :href "#"
                         :on-click #(comp (dispatch [:set-current-user nil])
                                          (dispatch [:navigate-to :home]))}]
                :drawer {:title "Drawer"
                         :links [{:title "Home"
                                  :href "/home"
                                  :on-click #(dispatch [:navigate-to :about])}]}
                :content [:div.align-center
                          [:h4 (str "Hey " name ", curious about this stack? Here what we are using.")] 
                          [:ul
                           [:li [:a {:href "https://clojure.org/"} "Clojure"]]
                           [:li [:a {:href "https://github.com/clojure/clojurescript"} "Clojurescript"]]
                           [:li [:a {:href "https://reagent-project.github.io/" :target "_blank"} "Reagent"]]
                           [:li [:a {:href "https://github.com/Day8/re-frame" :target "_blank"} "re-frame"]]
                           [:li [:a {:href "http://getmdl.io" :target "_blank"} "Material Design"]]
                           [:li [:a {:href "http://boot-clj.com/"} "Boot for clojure"]]]]
                }]))

(defn login-page []
  (let [state (r/atom {:username ""})
        username-valid? #(not (blank? (:username %)))
        on-change #(swap! state assoc :username (-> % .-target .-value))
        on-click  #(let [user @state]
                     (if (username-valid? user)
                       (comp 
                        (dispatch [:set-current-user user])
                        (dispatch [:navigate-to :home]))
                       (ui/toast {:message "Please inform your name"
                                  :timeout 1750})))]
    [:div.mdl-grid.align-center
     [:div.demo-card-wide.mdl-card.mdl-shadow--2p
      [:div.mdl-card__title
       [:h2.mdl-card__title-text "Welcome"]]
      [:div.mdl-card__supporting-text "Please inform your name to log into our sample app."
       [:form {:action "#"}
        [:div.mdl-textfield.mdl-js-textfield.mdl-textfield--floating-label
         [:input.mdl-textfield__input {:type "text"
                                       :id "username"
                                       :on-change on-change}] 
         [:span.mdl-textfield__error "Please inform your name"]]]]
      [:div.mdl-card__actions.mdl-card--border
       [:a.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect {:on-click on-click} "Log me in"]]]
     [ui/snackbar]]))


(defmulti pages identity)
(defmethod pages :home [] [home-page])
(defmethod pages :login [] [login-page])
(defmethod pages :about [] [about-page])
(defmethod pages :default [] [:div "No page defined"])

(def upgrade-dom (.. js/componentHandler -upgradeDom))

(defn main []
  (let [page (subscribe [:current-page])]
    (r/create-class
     {:display-name         "mdl-wrapper"
      :component-did-mount  (fn [] (upgrade-dom))
      :component-did-update (fn [] (upgrade-dom))
      :reagent-render       (fn [] (pages @page))})))


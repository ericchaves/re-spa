(ns respa.db
  "Application state
  - use a global db for all application state (same concept of a redux store).
  - schema is used to ensure that app-db is always consistent"
  (:require [cljs.reader]
            [schema.core :as s :include-macros true]))



(def username-valid? (s/pred
                      #(re-matches #"^[a-z]([-']?[a-z]+)*( [a-z]([-']?[a-z]+)*)+$" %)
                      'at-least-two-words))
(def USER {:username username-valid?})
(def schema {(s/optional-key :user) USER
             :page (s/enum :home
                           :login
                           :about)})


(def default-db {:page :login})

(def lsk "respa-db")

(defn load-db!
  "Load db state from localstorage"
  []
  (some->> (.getItem js/localStorage lsk)
           (cljs.reader/read-string)))

(defn save-db
  "Save db state into localstorage"
  [state]
  (.setItem js/localStorage lsk (str state)))

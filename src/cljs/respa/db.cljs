(ns respa.db
  (:require [cljs.reader]
            [schema.core :as s :include-macros true]))


(def USER {:username s/Str})
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

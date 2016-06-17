(ns respa.handlers
  (:require [re-frame.core :refer [register-handler path trim-v after debug]]
            [schema.core :as s]
            [respa.db :refer [load-db! save-db default-db schema]]))

(defn check-and-report
  "log error in console if db doesn't match the schema."
  [a-schema db]
  (if-let [problems  (s/check a-schema db)]
    (comp 
     (.error js/console "schema check failed:" db)
     (.error js/console problems))))

(def debug? (when ^boolean js/goog.DEBUG debug))

(def check-schema (after (partial check-and-report schema)))

(def save (after (partial save-db)))

(def default-middleware [check-schema
                         save
                         debug?
                         trim-v])

(register-handler
 :initialize-db
 debug?
 (fn [_ _]
   (merge default-db (load-db!))))

(register-handler
 :navigate-to
 default-middleware
 (fn [db [active-page]]
   (println "db:" (:user db))
   (if (nil? (:user db))
     (assoc db :page :login) 
     (assoc db :page active-page))))

(register-handler
 :set-current-user
 default-middleware
 (fn [db [user]]
   (if (nil? user)
     (dissoc db :user)
     (assoc db :user user))))

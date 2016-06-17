(ns respa.subs
  ;;make-reation used instead of reaction due to tracer
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
 :current-user
 (fn [db _]
   (reaction (:user @db)))

 (register-sub
  :current-page
  (fn [db _]
    (reaction (:page @db)))))

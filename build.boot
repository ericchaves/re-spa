(set-env!
 :source-paths    #{"src/cljs" "less"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "1.7.228-1"  :scope "test"]
                 [adzerk/boot-cljs-repl     "0.3.0"      :scope "test"]
                 [adzerk/boot-reload        "0.4.8"      :scope "test"]
                 [pandeiro/boot-http        "0.7.3"      :scope "test"]
                 [com.cemerick/piggieback   "0.2.1"      :scope "test"]
                 [org.clojure/tools.namespace "0.2.11"   :scope "test"]
                 [org.clojure/tools.nrepl   "0.2.12"     :scope "test"]
                 [weasel                    "0.7.0"      :scope "test"]
                 [metosin/reagent-dev-tools "0.1.0"      :scope "test"]
                 [deraen/boot-less          "0.5.0"      :scope "test"]
                 [binaryage/devtools        "0.7.0"      :scope "test"]
                 [cljsjs/material "1.1.3-1"]
                 [org.clojure/clojurescript "1.9.36"]
                 [reagent "0.6.0-alpha2"]
                 [re-frame "0.7.0"]
                 [prismatic/schema "1.1.2"]
                 [metosin/schema-tools "0.9.0"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.7"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[deraen.boot-less      :refer [less]]
 '[clojure.tools.namespace.repl :refer [refresh]])

(deftask build []
  (comp (cljs)        
        (less)
        (sift   :move {#"less.css" "css/less.css" #"less.main.css.map" "css/less.main.css.map"})))

(deftask run []
  (comp (serve)
        (watch)        
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced
                       :source-map true
                       :closure-defines {"goog.DEBUG" false}}
                 less {:compression true})
  identity)

(deftask development []
  (task-options! cljs   {:optimizations :none
                         :source-map true}
                 reload {:on-jsload 'respa.core/render}
                 less   {:source-map  true})
  identity)

(deftask package []
  (comp (production)
        (build)
        (target :dir #{"dist"})))

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

(deftask prod
  "Simple alias to run application in production mode"
  []
  (comp (production)
        (run)))

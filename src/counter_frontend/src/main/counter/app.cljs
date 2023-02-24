(ns counter.app
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [reagent.core :as r]
            [reagent.dom  :as dom])
  (:require ["hash.js" :as hashjs] 
            [counter.actor :refer [backend]]
            ))

(println "code reloaded!")


(defn JS-Library-Demo []
  [:div [:h3 "JS Library Demo"]
   "Importing and calling a JS library from node_modules/ in ClojureScript:" [:br]
   [:p "sha256('abc') => " (-> (hashjs/sha256) (.update "abc") (.digest "hex"))]])


(defonce current-count (r/atom 0))
(defn Counter-Frontend []
  [:div [:h3 "Counter-Frontend"]
   "This is a counter that stores state in a global atom in the frontend" [:br]
   "Current Count: " @current-count [:br]
   [:button {:onClick (fn [] (swap! current-count inc))} "inc"]
   [:button {:onClick (fn [] (swap! current-count dec))} "dec"]])


;; using js promises
(defn WhoAmI-Checker-Vanilla []
  (let [whoami (r/atom "")
        fetch-whoami 
        (fn []
          (reset! whoami "loading...")
          (-> (-> (js/Promise.resolve (.whoami backend)) (.then #(.toString %)))
              (.then #(reset! whoami %))
              (.catch #(reset! whoami "error"))
              (.finally #(js/console.log "whoami call finished"))))]
    (fn [] 
      [:div 
       "js.promise whoami => " @whoami [:br]
       [:button {:onClick fetch-whoami} "fetch"]
       [:button {:onClick #(reset! whoami "")} "reset"]])))



;; using core.async (preferred)
(defn WhoAmI-Checker-Async []
  (let [whoami (r/atom "")
        fetch-whoami 
        (fn [] (go (reset! whoami "loading...")
                   (reset! whoami (str (<p! (.whoami backend))))))]
    (fn []
      [:div 
       "core.async whoami => " @whoami [:br]
       [:button {:onClick fetch-whoami       } "fetch"] 
       [:button {:onClick #(reset! whoami "")} "reset"]])))




(defn Counter-Backend []
  [:div [:h3 "Counter-Backend"] 
   "Calling the canister" [:br] [:br]
   [WhoAmI-Checker-Vanilla] [:br]
   [WhoAmI-Checker-Async] [:br]
   "Current Count: " "Not Yet Implemented"])


(defn Application []
  [:div
   [:h1 "A simple counter app!"] [:hr]
   [JS-Library-Demo] [:hr]
   [Counter-Frontend] [:hr]
   [Counter-Backend] [:hr] 
   ])


(dom/render [Application] (js/document.getElementById "app"))

(defn init []
  (println "The app has started"))

(comment

  ;; this only shows up in the js console
  (go (let [val (<p! (.whoami backend))]
        (prn val)))
  
)


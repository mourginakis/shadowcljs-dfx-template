(ns counter.app
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [reagent.core :as r]
            [reagent.dom  :as dom])
  (:require ["hash.js" :as hashjs]
            ["/js/actor.js" :refer [backend]]))

(println "code reloaded!")


(defn JS-Library-Demo []
  [:div [:h3 "JS Library Demo"]
   "Importing and calling a JS library from node_modules/ in ClojureScript:" [:br]
   [:p "sha256('abc') => " (-> (hashjs/sha256) (.update "abc") (.digest "hex"))]])

(defonce current-count (r/atom 0))
(defn Counter-Frontend []
  [:div [:h3 "Counter-Frontend"]
   "This is a counter that stores state in an atom in the frontend" [:br]
   "Current Count: " @current-count [:br]
   [:button {:onClick (fn [] (swap! current-count inc))} "inc"]
   [:button {:onClick (fn [] (swap! current-count dec))} "dec"]])


(defonce whoami-value (r/atom ""))

(defn WhoAmI-Checker-Vanilla 
  "Uses vanilla JS Promises"
  []
  [:div 
   "js.promise whoami => " @whoami-value [:br]
   [:button {:onClick
             (fn [] 
               (reset! whoami-value "loading...")
               (-> (-> (js/Promise.resolve (.whoami backend)) (.then #(.toString %)))
                   (.then #(reset! whoami-value %))
                   (.catch #(reset! whoami-value "error"))
                   (.finally #(js/console.log "whoami call finished"))))} 
    "fetch"]
   
   [:button {:onClick (fn [] (reset! whoami-value ""))} "reset"]
   ])


(defn WhoAmI-Checker-Async
  "Uses core.async"
  [] 
  [:div
   "core.async whoami => " @whoami-value [:br]
   [:button {:onClick
             (fn [] (go (reset! whoami-value "loading...")
                        (reset! whoami-value (str (<p! (.whoami backend))))))} 
    "fetch"]
   
   [:button {:onClick (fn [] (reset! whoami-value "clear"))} "reset"]
   ])


(defn Counter-Backend []
  [:div [:h3 "Counter-Backend"] 
   "Calling the canister" [:br] [:br]
   (WhoAmI-Checker-Vanilla) [:br]
   (WhoAmI-Checker-Async) [:br]
   "Current Count: " "Not Yet Implemented"])


(defn Application []
  [:div
   [:h1 "A simple counter app!"] [:hr]
   (JS-Library-Demo) [:hr]
   (Counter-Frontend) [:hr]
   (Counter-Backend) [:hr]
   
   ])


(dom/render [Application] (js/document.getElementById "app"))

(defn init []
  (println "The app has started"))

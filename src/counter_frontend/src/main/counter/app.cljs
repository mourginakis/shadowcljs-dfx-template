(ns counter.app
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [reagent.core :as r]
            [reagent.dom  :as dom]
            [reagent.dom.client :as rdomc])
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



(defn Counter-App []
  ;; ideally each button would have its own object to make code cleaner
  (let [count (r/atom "?") 
        getCount-disabled? (r/atom false)
        getCount (fn [] (go (reset! getCount-disabled? true)
                            (reset! count (str (<p! (.getCount backend))))
                            (reset! getCount-disabled? false)))
        incrementCount-disabled? (r/atom false)
        incrementCount (fn [] (go (reset! incrementCount-disabled? true) 
                                  (reset! count (str (<p! (.incrementCount backend))))
                                  (reset! incrementCount-disabled? false))) 
        resetCount-disabled? (r/atom false)
        resetCount (fn [] (go (reset! resetCount-disabled? true)
                              (reset! count (str (<p! (.resetCount backend))))
                              (reset! resetCount-disabled? false)))]
     
    (fn []
      [:div 
       "Current count [backend] => " @count [:br]
       [:button {:onClick getCount :disabled @getCount-disabled? } "getCount"] [:br]
       [:button {:onClick incrementCount :disabled @incrementCount-disabled?} "incrementCount"] [:br]
       [:button {:onClick resetCount :disabled @resetCount-disabled?} "resetCount"]]))) 



(defn Counter-Backend []
  [:div [:h3 "Counter-Backend"] 
   "Calling the canister" [:br] [:br]
   [WhoAmI-Checker-Vanilla] [:br]
   [WhoAmI-Checker-Async] [:br]
   [Counter-App]])


(defn Application []
  [:div
   [:h1 "A simple counter app!"] [:hr]
   [JS-Library-Demo] [:hr]
   [Counter-Frontend] [:hr]
   [Counter-Backend] [:hr] 
   ])


(defonce root 
  (rdomc/create-root (js/document.getElementById "app")))
(rdomc/render root [Application])


(defn init []
  (println "The app has started"))

(comment

  ;; this only shows up in the js console
  (go (let [val (<p! (.whoami backend))]
        (prn val)))
  
)


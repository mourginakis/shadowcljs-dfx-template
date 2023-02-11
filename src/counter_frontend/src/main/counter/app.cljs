(ns counter.app
  (:require [reagent.core :as r]
            [reagent.dom  :as dom])
  (:require ["hash.js" :as hashjs]))

(println "code reloaded!")

(defonce current-count (r/atom 0))

(defn Application []
  [:div
   [:h1 "A simple counter app!"]
   [:h3 "Calling a JS library from ClojureScript:"]
   [:p "sha256('abc') =>" (-> (hashjs/sha256) (.update "abc") (.digest "hex"))]
   
   [:h3 "Current count:"]
   [:p @current-count]
   
   [:button
    {:onClick (fn [] (swap! current-count inc))}
    "+1"]
   [:button
    {:onClick (fn [] (swap! current-count dec))}
    "-1"]])


(dom/render [Application] (js/document.getElementById "app"))

(defn init []
  (println "The app has started"))

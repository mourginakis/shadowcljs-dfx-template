(ns counter.app
  (:require [reagent.core :as r])
  (:require [reagent.dom  :as dom]))

(println "code reloaded!")

(defonce current-count (r/atom 0))

(defn Application []
  [:div @current-count
   " "
   [:button
    {:onClick (fn [] (swap! current-count inc))}
    "+1"]
   [:button
    {:onClick (fn [] (swap! current-count dec))}
    "-1"]])


(dom/render [Application] (js/document.getElementById "app"))

(defn init []
  (println "The app has started"))

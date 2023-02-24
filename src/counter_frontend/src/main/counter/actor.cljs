(ns counter.actor
  (:require ["@dfinity/agent" :refer [Actor, HttpAgent]]
            ["counter-backend-did" :refer [idlFactory]]))

;; create custom actor depending on local or mainnet environment

;; set default
(goog-define dfx-network "local")

(def opts 
  (case dfx-network
    "local" {:host "http://127.0.0.1:4943"
             :canisterId "rrkah-fqaaa-aaaaa-aaaaq-cai"
             :isDevelopment true}
    
    "ic"    {:host "https://ic0.app"
             :canisterId "g2mhx-fqaaa-aaaag-qb2xq-cai"
             :isDevelopment false}))

(def localAgent (HttpAgent. #js {:host (:host opts)}))

(when (:isDevelopment opts)
  (.fetchRootKey localAgent))

(def backend 
  (.createActor Actor
                idlFactory
                (clj->js {:agent localAgent
                          :canisterId (:canisterId opts)})))




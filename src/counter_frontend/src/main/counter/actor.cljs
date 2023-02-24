(ns counter.actor
  (:require ["@dfinity/agent" :refer [Actor, HttpAgent]]
            ["counter-backend-did" :refer [idlFactory]]
            ["/js/actor.js" :refer [createActor]]))

;; would one day like to delete main/js/actor.js
;; and completely replace it with this file.
;; But I currently don't know enough cljs interop.

(goog-define dfx-network "local")

(def opts 
  (case dfx-network
    "local" {:host "http://127.0.0.1:4943"
             :canisterId "rrkah-fqaaa-aaaaa-aaaaq-cai"
             :isDevelopment true}
    
    "ic"    {:host "https://ic0.app"
             :canisterId "g2mhx-fqaaa-aaaag-qb2xq-cai"
             :isDevelopment false}))


(def backend 
  (createActor
   (:isDevelopment opts)
   (:canisterId opts)
   (clj->js {:agentOptions {:host (:host opts)}})))




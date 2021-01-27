(ns arthagri.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [arthagri.events :as events]
   [arthagri.views :as views]
   [arthagri.config :as config]
   ["aws-amplify" :default Amplify :as amp]
   ["aws-amplify-react" :refer (withAuthenticator)]
   
   ))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(def root-view
  (reagent/adapt-react-class
   (withAuthenticator
    (reagent/reactify-component views/main-panel) true)))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (.configure Amplify (clj->js {
        :aws_project_region "us-east-2"
        :aws_cognito_identity_pool_id "us-east-2:086cecc3-0f58-468f-a380-be64e7d6f6da"
        :aws_cognito_region "us-east-2"
        :aws_user_pools_id "us-east-2_ShI50MSgu"
        :aws_user_pools_web_client_id "6jldtesda3gag5baosc1ctchkl",
        :oauth {}}))
  (re-frame/dispatch-sync [::events/initialize-db])
  (reagent.dom/render [root-view]
                  (.getElementById js/document "app")))

(defn init []
  (dev-setup)
  (mount-root))

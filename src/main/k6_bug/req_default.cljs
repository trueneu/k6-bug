(ns k6-bug.req-default
  (:require ["k6/http$default" :as http]))

(defn run []
  (js/console.log (str "http keys: " (js/Object.keys http)))
  (http/get "http://example.org"))

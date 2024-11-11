(ns k6-bug.req-simple
  (:require ["k6/http" :as http]))

(defn run []
  (js/console.log (str "http keys: " (js/Object.keys http)))
  (js/console.log (str "http/default keys: " (js/Object.keys http/default)))
  (http/get "http://example.org"))

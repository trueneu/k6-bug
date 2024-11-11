(ns k6-bug.core
  (:require [k6-bug.req-default :as r1]
            [k6-bug.req-simple :as r2]))

(def ^:export options
  #js {:vus 1
       :iterations "1"})

(defn ^:export setup [])

(defn ^:export default [setup-data]
  (js/console.log "Running req-default")
  (r1/run)
  (js/console.log "Running req-simple")
  (r2/run))

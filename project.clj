(defproject trueneu/k6-bug "0.0.1"
  :dependencies
  [[org.clojure/clojure "1.11.1"]
   [org.clojure/clojurescript "1.11.132"]
   [com.google.javascript/closure-compiler-unshaded "v20240317"]
   [thheller/shadow-cljs "2.28.19"]]

  :profiles
  {:default
   {:source-paths ["src/main"]}})


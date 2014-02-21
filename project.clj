(defproject org.clojars.ray1729/lein-immutant "1.2.1-SNAPSHOT"
  :description "Leiningen plugin for managing an Immutant project."
  :url "https://github.com/ray1729/lein-immutant"
  :mailing-list {:name "Immutant users list"
                 :post "immutant-users@immutant.org"
                 :subscribe "immutant-users-subscribe@immutant.org"
                 :unsubscribe "immutant-users-unsubscribe@immutant.org"}
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :eval-in-leiningen true
  :exclusions [org.clojure/clojure dynapath]
  :dependencies [[org.immutant/overlay                        "1.5.2"]
                 [org.clojars.ray1729/deploy-tools            "0.14.1-SNAPSHOT"]
                 [org.immutant/immutant-dependency-exclusions "0.1.0"]
                 [org.clojure/data.json                       "0.2.1"]
                 ;; [org.clojure/tools.cli                       "0.3.1"]
                 [org.immutant/fntest                         "0.5.2"]]
  :profiles {:dev
             {:dependencies [[lein-midje "2.0.3"]
                             [leinjacker "0.4.2"]
                             [midje      "1.3.1" :exclusions [org.clojure/core.unify]]]
              :resource-paths ["test-resources"]}}
  :pedantic :warn
  :signing {:gpg-key "B009089F"}
  :lein-release {:deploy-via :clojars})

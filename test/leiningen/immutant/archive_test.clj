(ns leiningen.immutant.archive-test
  (:use midje.sweet
        leiningen.immutant.test-helper)
  (:require [clojure.java.io :as io]))

(setup-test-env)

(def base-contents #{"project.clj" "src/test_project/core.clj"})

(for-all-generations
  (println "\n==> Testing archive with lein generation" *generation*)

  
  (facts "archive"
    (facts "in a project"
      (fact (str "with no args should work for lein " *generation*)
        (with-tmp-dir
          (let [project-dir (copy-resource-to-tmp "test-project")
                archive (io/file project-dir "test-project.ima")]
            (run-lein *generation* "immutant" "archive"
                      :dir project-dir
                      :env base-lein-env)    => 0
            (.exists archive)                => true
            (verify-archive archive
                            base-contents)   => true)))
      
      (fact (str "with path arg should print a warning for lein " *generation*)
        (with-tmp-dir
          (let [project-dir (copy-resource-to-tmp "test-project")
                archive (io/file project-dir "test-project.ima")
                result 
                (run-lein *generation* "immutant" "archive" "yarg"
                          :dir project-dir
                          :env base-lein-env
                          :return-result? true)]
            (re-find #"specified a root path of 'yarg'" (:err result)) =not=> nil
            (:exit result)                   => 0
            (.exists archive)                => true
            (verify-archive archive
                            base-contents)   => true)))
      
      (fact (str "with a --name arg should work for lein " *generation*)
        (with-tmp-dir
          (let [project-dir (copy-resource-to-tmp "test-project")
                archive (io/file project-dir "blarg.ima")]
            (run-lein *generation* "immutant" "archive" "--name" "blarg"
                      :dir project-dir
                      :env base-lein-env)  => 0
            (.exists archive)              => true
            (verify-archive archive
                            base-contents) => true)))


      (fact (str "with a --include-dependencies arg should work for lein " *generation*)
        (with-tmp-dir
          (let [project-dir (copy-resource-to-tmp "test-project")
                archive (io/file project-dir "test-project.ima")]
            (run-lein *generation* "immutant" "archive" "--include-dependencies"
                      :dir project-dir
                      :env base-lein-env)    => 0
            (.exists archive)                => true
            (verify-archive
             archive
             (conj base-contents
                   "lib/clojure-1.4.0.jar")) => true))))

    
    (facts "not in a project"
      (fact (str "with a path arg should work for lein " *generation*)
        (with-tmp-dir
          (let [project-dir (copy-resource-to-tmp "test-project")
                archive (io/file *tmp-dir* "test-project.ima")]
            (run-lein *generation* "immutant" "archive" "test-project"
                      :dir *tmp-dir*
                      :env base-lein-env)    => 0
            (.exists archive)                => true
            (verify-archive archive
                            base-contents)   => true)))
      
      (fact (str "with a non-existent path arg should work for lein " *generation*)
        (let [{:keys [err exit]}
              (run-lein *generation* "immutant" "archive" "/tmp/hAmBisCuit"
                        :dir "/tmp"
                        :env base-lein-env
                        :return-result? true)]
          exit                                                     => 1
          (re-find #"Error: '/tmp/hAmBisCuit' does not exist" err) =not=> nil))
        
      (fact (str "with a --name arg and a path arg should work for lein " *generation*)
        (with-tmp-dir
          (copy-resource-to-tmp "test-project")
          (let [archive (io/file *tmp-dir* "blarg.ima")]
            (run-lein *generation* "immutant" "archive" "--name" "blarg" "test-project"
                      :dir *tmp-dir*
                      :env base-lein-env)  => 0
            (.exists archive)              => true
            (verify-archive archive
                            base-contents) => true)))

      (fact (str "with a --name arg, --include-dependencies, and a path arg should work for lein " *generation*)
        (with-tmp-dir
          (copy-resource-to-tmp "test-project")
          (let [archive (io/file *tmp-dir* "blarg.ima")]
            (run-lein *generation* "immutant" "archive" "--name" "blarg" "--include-dependencies" "test-project"
                      :dir *tmp-dir*
                      :env base-lein-env)  => 0
            (.exists archive)              => true
            (verify-archive
             archive
             (conj base-contents
                   "lib/clojure-1.4.0.jar")) => true))))))







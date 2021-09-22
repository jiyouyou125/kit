(ns kit.api
  (:require
    [kit.generator.modules.generator :as generator]
    [kit.generator.modules :as modules]
    [kit.generator.io :as io]))

(defn read-ctx
  ([] (read-ctx nil))
  ([path]
   (-> (or path "kit.edn")
       (slurp)
       (io/str->edn))))

(defn clone-modules []
  (modules/clone-modules (read-ctx)))

(defn list-modules []
  (let [ctx (modules/load-modules (read-ctx))]
    (modules/list-modules ctx)))

(defn install-module [module-key & [feature-flag]]
  (let [ctx (modules/load-modules (read-ctx))]
    (generator/generate ctx module-key (or feature-flag :default))))

(defn list-installed-modules []
  (defn list-installed-modules []
    (doseq [[id status] (-> (read-ctx)
                            :modules
                            :root
                            (generator/read-modules-log))]
      (println id (if (= status :success)
                    "installed successfully"
                    "failed to install")))))

(comment
  (read-ctx "test/resources/kit.edn")


  )
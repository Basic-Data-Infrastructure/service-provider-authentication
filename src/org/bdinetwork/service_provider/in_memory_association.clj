(ns org.bdinetwork.service-provider.in-memory-association
  (:require [org.bdinetwork.service-provider.ishare-validator :refer [parse-yaml validate]]
            [org.bdinetwork.service-provider.association :refer [Association]]))

(defrecord InMemoryAssociation [source]
  Association
  ;; Right now we track the OpenAPI schema closely in these interfaces,
  ;; both for return shapes and parameters. Should we keep this up, or
  ;; implement internal interfaces and data model differently?
  ;;
  ;; If we want to use a different model, is there an existing information
  ;; model we could use? Preferably with a standardized translation?
  ;;
  ;; Related: use keywords instead of strings in internal representation?
  ;; namespaced keys? Use time objects instead of strings?
  (party [_ party-id]
    (let [{:strs [parties]} source]
      (some #(when (= party-id (get % "party_id"))
               %)
            parties)))
  (trusted-list [_]
    (get source "trusted_list")))

(defn in-memory-association
  "Create a new in-memory Assocation from source data"
  [source]
  {:pre [(map? source)]}
  (when-let [issues (validate (get source "parties")
                              ["components" "schemas" "PartiesInfo" "properties" "data"])]
    (throw (ex-info "Invalid party in data source" {:issues issues})))
  (->InMemoryAssociation source))

(defn read-source
  "Read source data from yaml file at `path`"
  [path]
  (parse-yaml path))

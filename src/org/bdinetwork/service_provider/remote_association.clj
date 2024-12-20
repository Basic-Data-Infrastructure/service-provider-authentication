(ns org.bdinetwork.service-provider.remote-association
  "Implement org.bdinetwork.service-provider.association.Assocation
  protocol by querying a BDI Assocation Register."
  (:require [org.bdinetwork.service-provider.association :refer [Association]]
            [org.bdinetwork.ishare.client :as client]
            [clojure.walk :as walk]))

(defn ensure-ok
  [{:keys [status] :as response}]
  (when-not (= 200 status)
    (throw (ex-info (str "Unexpected status code '" status "' from association register")
                    (update response :request client/redact-request))))
  response)

(defrecord RemoteAssociation [client-data]
  Association
  (party [_ party-id]
    (-> client-data
        (assoc :ishare/message-type :party, :ishare/party-id party-id)
        (client/exec)
        ensure-ok
        :ishare/result
        (walk/stringify-keys)
        (get "party_info")))
  (trusted-list [_]
    (-> client-data
        (assoc :ishare/message-type :trusted-list)
        (client/exec)
        ensure-ok
        :ishare/result
        (walk/stringify-keys)
        (get "trusted_list"))))

(defn remote-association
  [{:ishare/keys [client-id x5c private-key satellite-id satellite-base-url]
    :as client-data}]
  {:pre [client-id x5c private-key satellite-id satellite-base-url]}
  (->RemoteAssociation client-data))

(ns etl.apis
  (:require [clj-http.client :as client])
  (:gen-class))
; url (str "https://api.openweathermap.org/data/2.5/weather?q=" city "&appid=" api-key "&units=metric")
(defn get-json-from-api [url]
  (let [response (client/get url {:as :json})]
    (println response)
    (:body response)))

(defn get-text-from-api [url]
  (let [response (client/get url)]
    (:body response)))

(defn -main
  [& args]
  (let [host "http://localhost"
        port "3000"
        url (str host ":" port)
        retries 1]
    ;; (get-text-from-api url)))
    (get-json-from-api url)))

(-main)
;; {:cached nil, :request-time 121, :repeatable? false, :protocol-version {:name HTTP, :major 1, :minor 1}, :streaming? true, :http-client #object[org.apache.http.impl.client.InternalHttpClient 0x5fc987fe org.apache.http.impl.client.InternalHttpClient@5fc987fe], :chunked? false, :reason-phrase OK, :headers {Date Thu, 29 Aug 2024 05:43:22 GMT, Connection close, Content-Type application/json, Server Jetty(11.0.21)}, :orig-content-encoding nil, :status 200, :length -1, :body {:text hello, me}, :trace-redirects []}

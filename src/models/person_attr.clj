(ns models.person_attr
  (:require [clojure.string :as str])
  (:gen-class))

(defrecord PersonAttribute [person-id attr-id])

(defn parse-person-attr [line]
  "Parses a line of text and returns an PersonAttribute record."
  (let [[person-id attr-id] (str/split line #",")]
    (->PersonAttribute person-id attr-id)))

(parse-person-attr "0,0")

(defn read-person-attr-from-file [file-path]
  "Reads a file and converts its contents to a list of PersonAttribute records."
  (with-open [reader (clojure.java.io/reader file-path)]
    (doall (map parse-person-attr (rest (line-seq reader))))))

(read-person-attr-from-file "resources/people-attr.txt")
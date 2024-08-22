(ns models.attr
  (:require [clojure.string :as str]))

(defrecord Attr [id name])

(defn valid-attr-name? [name]
  (and (string? name) (not (empty? name))))

(defn parse-attr [line]
  "Parses a line of text and returns a Attr record."
  (let [[id name] (str/split line #",")]
    (->Attr id name)))

(parse-attr "0,woodworking")

(defn read-attr-from-file [file-path]
  "Reads a file and converts its contents to a list of Hobby records."
  (with-open [reader (clojure.java.io/reader file-path)]
    (doall (map parse-attr (line-seq reader)))))

(read-attr-from-file "resources/attributes.txt")


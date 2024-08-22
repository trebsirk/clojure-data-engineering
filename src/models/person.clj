(ns models.person
  (:require [clojure.string :as str]))

;; (str/split "2,Alice,30" #",")

(defrecord Person [id name age])

(defn valid-person-name? [name]
  (and (string? name) (not (empty? name))))

(defn valid-age? [age]
  (and (integer? age) (pos? age)))

(defn parse-int [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException e
      nil)))

(defn parse-person [line]
  "Parses a line of text and returns a Person record."
  (let [[id name age] (str/split line #",")]
    (->Person id name (parse-int age))))

(defn read-persons-from-file [file-path]
  "Reads a file and converts its contents to a list of Person records."
  (with-open [reader (clojure.java.io/reader file-path)]
    (doall (map parse-person (line-seq reader)))))

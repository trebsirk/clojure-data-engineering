(ns models.person
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;; (str/split "2,Alice," #"," -1)

(defrecord Person [id ^String name age])

;; (->Person 1 "ty" nil)

(defn valid-name? [name]
  (and (string? name) (not (empty? name))))

(defn valid-age? [age]
  (and (integer? age) (pos? age)))

(defn parse-int [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException e
      nil)))

(defn parse-name-valid-or-nil [name]
  (cond
    (not (valid-name? name)) nil
    :else name))

(defn parse-age-valid-or-nil [age]
  (cond
    (not (valid-age? age)) nil
    :else age))

(defn create-person [id name age]
  (cond
    (not (valid-name? name)) (throw (IllegalArgumentException. "Invalid name"))
    (not (valid-age? age)) (throw (IllegalArgumentException. "Invalid age"))
    :else (->Person id name (Integer. age))))

(defn create-person-with-nils [id name age]
  (->Person id (parse-name-valid-or-nil name) (parse-age-valid-or-nil age)))

(defn parse-person [line]
  "Parses a line of text and returns a Person record."
  ;; (println line)
  (let [[id name age] (str/split line #"," -1)]
    (create-person-with-nils id name (parse-int age))))

(defn read-persons-from-file [file-path]
  "Reads a file and converts its contents to a list of Person records."
  (with-open [reader (io/reader file-path)]
    (doall (map parse-person (line-seq reader)))))

(read-persons-from-file "resources/people.txt")

;; parallel implementation
(defn parse-line [line]
  (let [[id name-str age-str] (str/split line #"," -1)
        name (cond (not (valid-name? name-str)) nil :else name-str)
        age (try (Integer/parseInt age-str) (catch Exception _ nil))]
    (->Person id name age)))

(defn parse-lines [lines]
  (map parse-line lines))

(defn read-persons-from-file-parallel [filename chunk-size]
  (let [lines (line-seq (io/reader filename))
        chunks (partition-all chunk-size lines)]
    (->> chunks
         (pmap parse-lines)
         (apply concat))))

(read-persons-from-file-parallel "resources/people.txt" 4)

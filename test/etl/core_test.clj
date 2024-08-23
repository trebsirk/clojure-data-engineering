(ns etl.core-test
  (:require [clojure.test :refer :all]
            [etl.core :refer :all]
            [models.person :refer [->Person read-persons-from-file
                                   read-persons-from-file-parallel
                                   write-persons-to-csv]]))
(deftest read-person-test
  (testing "test person csv file reader."
    (let [file-path "resources/people-min.txt"  ;; Replace with the path to your file
          persons (read-persons-from-file file-path)]
      (is (= 7 (count persons)))))
  (testing "test data in file."
    (let [file-path "resources/people-min.txt"  ;; Replace with the path to your file
          persons (read-persons-from-file file-path)]
      (is (= (:age (last persons)) nil))
      (is (= (:name (first (drop 3 persons))) nil)))))

(deftest read-person-parallel-test
  (testing "test parallel parsing"
    (let [file-path "resources/people-min.txt"
          persons (read-persons-from-file-parallel file-path 4)]
      (is (= 7 (count persons)))))
  (testing "test data in file."
    (let [file-path "resources/people-min.txt"  ;; Replace with the path to your file
          persons (read-persons-from-file-parallel file-path 4)]
      (is (= (:age (last persons)) nil))
      (is (= (:name (first (drop 3 persons))) nil)))))

(deftest write-persons
  (testing "write person list to a csv file with header"
    (let [persons [(->Person 1 "Alice" nil)
                   (->Person 2 nil 25)
                   (->Person nil "Charlie" 40)
                   (->Person 4 "Dirk" 42)]
          file-path "data/out/persons_test.csv"]
      (write-persons-to-csv file-path persons)
      ;; (println (str "Data written to " filename)
      (let [persons-from-file (read-persons-from-file-parallel file-path 4)]
        (is (= (:age (last persons-from-file)) 42))))))
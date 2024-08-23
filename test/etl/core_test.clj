(ns etl.core-test
  (:require [clojure.test :refer :all]
            [etl.core :refer :all]
            [models.person :refer [read-persons-from-file
                                   read-persons-from-file-parallel]]))

(deftest read-person-test
  (testing "test person csv file reader."
    (let [file-path "resources/people.txt"  ;; Replace with the path to your file
          persons (read-persons-from-file file-path)]
      (is (= 7 (count persons)))))
  (testing "test data in file."
    (let [file-path "resources/people.txt"  ;; Replace with the path to your file
          persons (read-persons-from-file file-path)]
      (is (= (:age (last persons)) nil))
      (is (= (:name (first (drop 3 persons))) nil)))))

(deftest read-person-parallel-test
  (testing "test parallel parsing"
    (let [file-path "resources/people.txt"
          persons (read-persons-from-file-parallel file-path 4)]
      (is (= 7 (count persons))))
  )
  (testing "test data in file."
    (let [file-path "resources/people.txt"  ;; Replace with the path to your file
          persons (read-persons-from-file-parallel file-path 4)]
      (is (= (:age (last persons)) nil))
      (is (= (:name (first (drop 3 persons))) nil))))
  )
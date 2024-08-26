(ns etl.validation-test
  
  (:require [clojure.test :refer :all]
            [etl.validation :refer [between-unbounded? between? parse-num]]))

(deftest test-between?
  (testing "test between?"
    (is (= true (between? 0 -1 1)))
    (is (= true (between? 0 0 0)))
    
    (is (= false (between? 1 0 0)))
    (is (= false (between? 1 -1 0)))
    (is (= false (between? 0 1 1)))
    (is (= false (between? 0 1 2)))
    ))


(deftest test-between-unbounded?
  (testing "test between-unbounded?"
    
    (is (= true (between-unbounded? nil nil nil)))

    (is (= true (between-unbounded? 1 nil nil)))
    (is (= true (between-unbounded? 0 nil nil)))
    (is (= true (between-unbounded? -1.2 nil nil)))

    (is (= true (between-unbounded? nil 1 nil)))
    (is (= true (between-unbounded? nil nil 1)))
    
    (is (= false (between-unbounded? 1 2 nil)))
    (is (= true (between-unbounded? 1 -2 nil)))
    (is (= true (between-unbounded? 1 0 2)))
    
  ))

(deftest test-parse-num
  (testing "test parse-num"
    
    (is (= 0 (parse-num "0" "int")))
    (is (= 0.0 (parse-num "0" "float")))
    (is (= 0.0 (parse-num "0" "double")))

    (is (= 3 (parse-num "3" "int")))
    (is (= 3.0 (parse-num "3" "float")))
    (is (= 3.0 (parse-num "3" "double"))) 

    (is (= -3 (parse-num "-3" "int")))
    (is (= -3.0 (parse-num "-3" "float")))
    (is (= -3.0 (parse-num "-3" "double"))) 

    (is (= nil (parse-num "" "int")))
    (is (= nil (parse-num "" "float")))
    (is (= nil (parse-num "" "double")))

    (is (= nil (parse-num "!" "int")))
    (is (= nil (parse-num "@" "float")))
    (is (= nil (parse-num "#" "double")))

    (is (= nil (parse-num "a" "int")))
    (is (= nil (parse-num "a" "float")))
    (is (= nil (parse-num "a" "double")))))
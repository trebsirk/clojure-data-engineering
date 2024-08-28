(ns etl.core
  (:use [models.person :refer [read-persons-from-file ->Person read-persons-from-file-parallel]])
  (:use [models.attr])
  (:use [models.person_attr])
  (:require [clojure.set :refer [intersection union]])
  (:gen-class))
;; (:require [clojure.set :refer [intersection]])

(defmacro log-and-eval
  ([expr]
   `(log-and-eval "INFO" ~expr))
  ([log-level expr]
   `(let [result# ~expr
          timestamp# (java.time.LocalDateTime/now)]
      (println (str "[" timestamp# "] " ~log-level " - Evaluating expression: " '~expr))
      (println (str "[" timestamp# "] " ~log-level " - Result: " result#))
      result#)))

;; (log-and-eval "WARN" (+ 1 1))
;; (log-and-eval (+ 1 1))
;; (log-and-eval (+ 1 2 3))
;; (log-and-eval "DEBUG" (* 2 3 4))


;; `(+ ~(list 1 2 3))
;; `(+ (list 1 2 3))
;; `(+ ~@(list 1 2 3))
;; (eval `(+ ~@(list 1 2 3)))

(def persons
  (let [file-path "resources/people.txt"  ;; Replace with the path to your file
        persons (read-persons-from-file file-path)]
    persons))

(def person (->Person 0 "Albert" 20))
(def personattr (->PersonAttribute 0 0))
(def attr (->Attr 0 "hacking"))

(def pattrs (read-person-attr-from-file "resources/people-attr.txt"))

;; (defrecord joined-rec PersonAttribute)

(defn join-person-and-person-attr-map
  "join record of two types: Person and PersonAttribute"
  [x-rec y-rec & join-keys]
  (let [xkeys (set (keys x-rec))
        ykeys (set (keys y-rec))
        uniquekeys (union xkeys ykeys)
        sharedkeys (intersection xkeys ykeys)]
    ;; (println (str "shared keys: " sharedkeys))
    ;; (println (str "unique keys: " uniquekeys)) 
    {:person-id (:id x-rec)
     :person-name (:name x-rec)
     :attr-id (:attr-id y-rec)}))
(join-person-and-person-attr-map person personattr)

(defn join-person-attributed-with-person
  "join record of two types: (Person and PersonAttribute) with Attribute"
  [x-rec y-rec & join-keys]
  {:person-id (:person-id x-rec)
   :person-name (:person-name x-rec)
   :attr-id (:attr-id x-rec)
   :attr-name (:name y-rec)})

(def joined (join-person-and-person-attr-map person personattr))
;; (println joined)
;; (println attr)
(join-person-attributed-with-person joined attr)

(defn -main [& args]
  ;; (println "Employees named Albert:") 
  (def people [(->Person 0 "Albert" 20)
               (->Person 1 "John" 21)
               (->Person 2 "Albert" 22)])
  ;; (println (first people))
  ;; (map #(println %) people)
  ;; (def als (filter #(= "Albert" (.name %)) people))
  ;; (println als)
  ;; (println als)
  ;; (map #(println %) als) 
  (let [file-path "resources/people.txt"  ;; Replace with the path to your file
        persons (read-persons-from-file file-path)]
    ;; (println persons)
    )

  (let [file-path "resources/attributes.txt"  ;; Replace with the path to your file
        attrs (read-attr-from-file file-path)]
    ;; (println attrs)
    )
  ;; (println people)
  ;; (let [file-path "resources/people.txt"  ;; Replace with the path to your file
  ;;       persons (read-persons-from-file file-path)]
  ;;   (println (count persons)))
  (let [file-path "resources/people.txt"  ;; Replace with the path to your file
        persons (read-persons-from-file-parallel file-path 1000)]
    (println (count persons) "records found in" file-path))
  (shutdown-agents))
;; (-main)



;; (defn cart [colls]
;;   (if (empty? colls)
;;     '(())
;;     (for [more (cart (rest colls))
;;           x (first colls)]
;;       (cons x more))))
;; (cart [[1 2 3] [\a \b]])

;; (def bees-per-colony 50000)
;; (def honey-per-colony 80)
;; (def honey-per-bee (/ honey-per-colony bees-per-colony))
;; (def honey-price (/ 11.89 (/ 24 16)));; per gallon
;; (def honey-per-mead 2.5)
;; (def price-per-gallon (* honey-price honey-per-mead))
;; (def honey-per-hive-per-year (/ (+ 100 25) 2))

;; (def mead-per-year (/ honey-per-hive-per-year honey-per-mead))
;; honey-per-hive-per-year
;; price-per-gallon
;; mead-per-year
;; honey-price
;; (def mead-sale-per-gallon 10)
;; (def profit (* mead-per-year mead-sale-per-gallon))
;; profit


;; (defn -map [f coll]
;;   (reduce
;;    (fn [acc v] (conj acc (f v)))
;;    []
;;    coll))

;; (-map #(* % %) [1 2 3])
;; (map #(* % %) [1 2 3])
;; (-map inc [1 2 3])

;; (defn -filter [f coll]
;;   (reduce
;;    (fn [acc v]
;;      (if (f v)
;;        (conj acc v)
;;        acc))
;;    []
;;    coll))

;; (apply list (-filter even? [1 2 3]))
;; (filter even? [1 2 3])

;; (->> (range 1 4)
;;      (-map inc)
;;      (-filter even?))

;; ; abstract away the job that is happening
;; (defn -mapping [f]
;;   (fn [xf]
;;     (fn [acc v]
;;       (xf acc (f v)))))

;; (defn -filtering [f]
;;   (fn [xf]
;;     (fn [acc v]
;;       (if (f v)
;;         (xf acc v)
;;         acc))))

;; (def rfn (comp
;;           (-mapping inc)
;;           (-filtering odd?)))

;; ; expects 42
;; ((rfn +) 42 1)
;; ; expects 45
;; ((rfn +) 42 2)

;; (reduce (rfn +)
;;         0
;;         [1 2 3])

;; (reduce (rfn conj)
;;         []
;;         [1 2 3 4 5])


;; (defn get-chars [s] (str/split s #""))

;; (defn diff? [a b] #(if (= a b) 0 1))
;; (def a "hello")
;; (def b "holle")
;; (get-chars a)
;; (map diff? ["a" "b"] ["a" "c"])
;; (interleave ["a" "b"] ["a" "c"])
;; (map diff? (get-chars a) (get-chars b))
;; (apply + (map diff? (get-chars a) (get-chars b)))
;; (let [[c1 c2] (map get-chars [a b])
;;       lens (map count [c1 c2])
;;       diff? #(if (= %1 %2) 0 1)]
;;   (map diff? c1 c2))
;; (defn hamming-distance [s1 s2]
;;   (let [[c1 c2] (map get-chars [s1 s2])
;;         lens (map count [c1 c2])
;;         diff? #(if (= %1 %2) 0 1)]
;;     (if (apply = lens)
;;       (apply + (map diff? c1 c2))
;;       0)))
;; (hamming-distance a b)

;; (defn isogram? [s]
;;   (->> (str/split s #"")
;;        frequencies
;;        vals
;;        (every? #(<= % 1))))
;; (seq (chars (char-array "abc")))
;; (- (int \a) 97)
;; (isogram? "helo")
;; (remove #(= % \o) a)
;; ((partial map alph-num-map) "a")

;; (defn combs
;;   "Generates all combinations of k elements from the given collection."
;;   [k coll]
;;   (cond
;;     (zero? k) '(())
;;     (> k (count coll)) '()
;;     :else (let [first-elem (first coll)
;;                 rest-elems (rest coll)]
;;             (concat
;;              (map #(cons first-elem %) (combs (dec k) rest-elems))
;;              (combs k rest-elems)))))

;; (defn partial-flatten [coll]
;;   "Flattens the input collection by one level."
;;   (mapcat identity coll))



;; (def x (concat (map #(combs % '(1 2 3)) (range 4))))

;; (partial-flatten x)
;; (identity '((1) (2) (3)))
;; (mapcat identity '((()) ((1) (2) (3))))


;; (apply concat (map identity '((()) ((1) (2) (3)))))

;; (concat '((()) ((1) (2) (3))))

;; '((()) ((1) (2) (3)))

;; (apply concat 
;;  (map identity 
;;       (map reverse [[3 2 1 0] [6 5 4] [9 8 7]])))

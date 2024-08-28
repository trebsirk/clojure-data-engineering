(ns etl.transformation
  (:require [models.person :refer [->Person]]))

(def p1 (->Person 0 "Albert" 20))
(def p2 (->Person 2 "Robert" 21))
(def p3 (->Person 3 "Robert" 20))

(defrecord nick-name [name nickname])
(def n1 (->nick-name "Albert" "Al"))
(def n2 (->nick-name "Robert" "Bob"))
(def n3 (->nick-name "Chanticleer" "Leer"))

(defrecord person-with-nick-name [id name age nickname])

(defn group-by-keys [keys coll & for-record]
  (cond
    for-record (group-by (fn [item] (mapv #(get item %) keys)) coll)
    :else (group-by (fn [item] (mapv item keys)) coll)))

(def data [{:name "Alice" :age 30 :city "New York"}
           {:name "Bob" :age 25 :city "San Francisco"}
           {:name "Alice" :age 30 :city "Los Angeles"}
           {:name "Bob" :age 25 :city "New York"}
           {:name "Charlie" :age 30 :city "New York"}])

(group-by-keys [:name :age] [p1 p2 p3] true)
(count (group-by-keys [:name :age] [p1 p2 p3] true))
(keys (group-by-keys [:name :age] [p1 p2 p3] true))
(group-by-keys [:name :age] data)

(select-keys (first data) [:name :age])
(mapv #(get p1 %) [:name :age])


(defn join-left
  "sql-like left join on two lists of records" 
  [l-coll r-coll join-keys]
  (let [l-groups (group-by-keys join-keys l-coll true)
        r-groups (group-by-keys join-keys r-coll true)
        l-keys (keys (first l-coll))
        r-keys (keys (first r-coll))
        all-keys (apply conj (keys (first l-coll)) (keys (first r-coll)))]
    (reduce (fn [result [k v1]]
              (let [v2 (get r-groups k [])]
                (conj result (merge (into {} (first v1)) (into {} (first v2))))))
            []
            l-groups)))

(def r (join-left [p1 p3] [n1 n2 n3] [:name]))
(println r)
(def rs (map #(map->person-with-nick-name %) r1))
(println rs)
(:name r)
(first r)
(keys (first [p1]))
(get p1 [:name :age])
(keys (second r))

; #etl.transformation.person-with-nick-name{:id 0, :name Albert, :age 20, :nickname Al} #etl.transformation.person-with-nick-name{:id 3, :name Robert, :age 21, :nickname Bob})

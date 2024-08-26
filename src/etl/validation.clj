(ns etl.validation)

 (defn parse-str-to-num [s parse-func]
   "takes a string and tries to parse it as a number"
   (try
     (parse-func s)
     (catch Exception e nil)))

 (defn parse-num [^String s ^String num-type]
   (cond
     (not (= (type s) String)) nil
     (= num-type "int") (parse-str-to-num s #(Integer/parseInt %))
     (= num-type "float") (parse-str-to-num s #(Float/parseFloat %))
     (= num-type "double") (parse-str-to-num s #(Double/parseDouble %))
     :else nil))

 (defn all-nums
   "returns true if every element in the list is a numeric type"
   [& coll]
   (every? number? coll))

 (defn between?
   "returns \n
     nil   if any parameter is not a number
     true  if lower <= x <= upper
     false otherwise"
   [n lower upper]
   (cond
     (not (all-nums n lower upper)) nil
     :else (and (<= lower n upper))))

 (defn between-unbounded?
   "returns \n
     true  if x is nil
     true  if lower and upper are nil
     true  if nil <= x <= upper
     true  if lower <= x <= nil
     false otherwise"
   [x lower upper]
   (cond
     (= x nil) true
     (and (= lower nil) (= upper nil)) true
     (number? lower) (>= x lower)
     (number? upper) (<= x upper)
     :else (between? x lower upper)))


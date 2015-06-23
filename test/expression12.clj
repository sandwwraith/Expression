(defn variable [name] (fn [vars] (get vars name)))
(defn constant [value] (fn [vars] value))

(defn binary [func] (
                      fn [op1,op2] (
                                     fn [vars] (
                                                 func (op1 vars) (op2 vars)
                                                 ))))

(defn multi [func] (
                     fn [& xs] (
                                 fn [vars] (
                                             apply func ((apply juxt xs) vars)
                                             ))))

(def add (multi +'))
(def subtract (multi -'))
(def multiply (multi *'))
(def divide (binary (fn [^double a ^double b] (/ a b))))
(def negate (multi (fn [a] (-' a))))
(def sin (multi (fn [^double a] (Math/sin a))))
(def cos (multi (fn [^double a] (Math/cos a))))

(def caseOper {'+ add '- subtract '* multiply '/ divide 'negate negate 'sin sin 'cos cos})

(defn parseList [expr] (
                         cond
                         ;If sequence, first is always an operator
                         (seq? expr) (apply (caseOper (first expr)) (map parseList (rest expr)))
                         ; "x", "y", "z" are parsed as symbols
                         (symbol? expr) (variable (name expr))
                         ;otherwise, this is a number
                         true (constant expr)
                         ))

(defn parseFunction [strng] (parseList (read-string strng)))

(println ((parseFunction "(+ (* x 2) 3)") {"x" 6}))
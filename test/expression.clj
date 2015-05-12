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
;(defn divide [op1,op2] (
;                        fn [vars] (
;                                    / (double (op1 vars)) (op2 vars)
;                                    )))
(def negate (multi -))

(def expr (subtract (multiply (constant 2) (variable "x")) (constant 3)))
(def expr1 (add (constant 3) (constant 2) (constant 10)))

;(println (expr {"x" 2}))

;(defn caseOper [symb] (
;                        cond
;                        (= (name symb) "+") add
;                        (= (name symb) "-") subtract
;                        (= (name symb) "*") multiply
;                        (= (name symb) "/") divide
;                        (= (name symb) "negate") negate
;                        ))

(def caseOper {'+ add '- subtract '* multiply '/ divide 'negate negate})

(defn parseList [expr] (
                         cond
                         ;If sequence, first is always an operator
                         (seq? expr) (apply (get caseOper (first expr)) (map parseList (rest expr)))
                         ; "x", "y", "z" are parsed as symbols
                         (symbol? expr) (variable (name expr))
                         ;otherwise, this is a number
                         true (constant expr)
                         ))

(defn parseFunction [strng] (parseList (read-string strng)))

(println ((parseFunction "(+ (* x 2) 3)") {"x" 6}))
;(println ((divide (constant 6.0) (constant 0.0)) {"x" 6.0}))
;(println (/ (double (identity 6.0)) (double (identity 0.0))))
;---------- Helper functions

(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (:prototype obj) key)))

(defn proto-call [obj key & args] (apply (proto-get obj key) (cons obj args)))
(defn method [key] (fn [obj & args] (apply (partial proto-call obj key) args)))
(defn constructor [ctor prototype] (fn [& args] (apply (partial ctor {:prototype prototype}) args)))


;---------- Declarations

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

;Forward declaration
(declare Constant)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Negate)
(declare Sin)
(declare Cos)


;---------- Constructors & Prototypes

(def ConstantProto
  {
    :evaluate (fn [this vars] (get this :value))
    :toString (fn [this] (.toString (get this :value)))
    :diff (fn [this, varr] (Constant 0))
    })
(def Constant (constructor (fn [this x] (assoc this :value x)) ConstantProto))

(def VariableProto
  {
    :evaluate (fn [this vars] (get vars (get this :n)))
    :toString (fn [this] (.toString (get this :n)))
    :diff (fn [this, varr] (if (= (get this :n) varr) (Constant 1) (Constant 0)))
    })
(def Variable (constructor (fn [this x] (assoc this :n x)) VariableProto))

(def OperationProto
  {
    :evaluate (fn [this vars] (apply (proto-get this :oper) (map (fn [x] (evaluate x vars)) (get this :operands))))
    :toString (fn [this]
                (clojure.string/join
                  (list "(" (proto-get this :stringForm) " " (apply print-str (map toString (get this :operands))) ")"))
                )
    })

(defn OperationHelper [func str dfunc] (assoc {} :prototype OperationProto :oper func :stringForm str :diff dfunc))
(def OperConstructor (fn [this & vars] (assoc this :operands vars)))

;---------- Differentiate functions

(def AddDiff (fn [this varr] (apply Add (map (fn [x] (diff x varr)) (get this :operands)))))
(def SubDiff (fn [this varr] (apply Subtract (map (fn [x] (diff x varr)) (get this :operands)))))
(def DivDiff (fn [this varr] (; Always two args
                               Divide
                               (Subtract
                                 (Multiply (diff (first (get this :operands)) varr) (second (get this :operands)))
                                 (Multiply (diff (second (get this :operands)) varr) (first (get this :operands)))
                                 )
                               (Multiply (second (this :operands)) (second (this :operands)))
                               )))

(def NegateDiff (fn [this varr] (Negate (diff (first (this :operands)) varr))))

(defn Differ [list varr] ;Helper for multi-diff
  (if (= 1 (count list)) (diff (first list) varr)
    (Add
      (apply Multiply (diff (first list) varr) (rest list))
      (Multiply (first list) (Differ (rest list) varr))
      )))

(def MulDiff (fn [this varr] (Differ (get this :operands) varr)))

(def SinDiff (fn [this varr] (Multiply (Cos (first (get this :operands))) (diff (first (get this :operands)) varr))))
(def CosDiff (fn [this varr] (Multiply (Negate (Sin (first (get this :operands)))) (diff (first (get this :operands)) varr))))

;---------- Classes

(def Add (constructor OperConstructor (OperationHelper + "+" AddDiff)))
(def Subtract (constructor OperConstructor (OperationHelper - "-" SubDiff)))
(def Multiply (constructor OperConstructor (OperationHelper * "*" MulDiff)))
(def Divide (constructor OperConstructor (OperationHelper (fn [^double a ^double b] (/ a b)) "/" DivDiff)))
(def Negate (constructor OperConstructor (OperationHelper (fn [b] (- b)) "negate" NegateDiff)))
(def Sin (constructor OperConstructor (OperationHelper (fn [b] (Math/sin b)) "sin" SinDiff)))
(def Cos (constructor OperConstructor (OperationHelper (fn [b] (Math/cos b)) "cos" CosDiff)))


;---------- Parser
(def caseOper {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sin Sin 'cos Cos})
(defn parseList [expr] (
                         cond
                         ;If sequence, first is always an operator
                         (seq? expr) (apply (caseOper (first expr)) (map parseList (rest expr)))
                         ; "x", "y", "z" are parsed as symbols
                         (symbol? expr) (Variable (name expr))
                         ;otherwise, this is a number
                         true (Constant expr)
                         ))
(defn parseObject [strng] (parseList (read-string strng)))

;(defn ex-const [n] (if (= n 21) (Constant 3) (Divide (ex-const (inc n)) (ex-const (inc n)))))
;(def test1 (ex-const 0))
/**
 * Created by sandwwraith(@gmail.com)
 * ITMO University, 2015
 **/

var addOp = function (a, b) {
    return a + b;
};
var subOp = function (a, b) {
    return a - b;
};
var mulOp = function (a, b) {
    return a * b;
};
var divOp = function (a, b) {
    return a / b;
};
var negateOp = function (a) {
    return -a;
};

var sinOp = Math.sin;
var cosOp = Math.cos;

function Variable(name) {
    this.toString = function () {
        return name;
    };
    this.evaluate = function () {
        switch (name) {
            case "x":
                return arguments[0];
            case "y":
                return arguments[1];
            default:
                return arguments[2];
        }
    };

    this.diff = function (p) {
        if (name == p) return new Const(1);
        return new Const(0);
    }
}

function Const(value) {
    this.toString = function () {
        return value.toString();
    };
    this.evaluate = function () {
        return value;
    };
    this.diff = function () {
        return new Const(0);
    }
}

function Operation(func, sign) {
    return function (op1, op2) {
        //noinspection JSPotentiallyInvalidUsageOfThis
        this.toString = function () {
            return op1.toString() + " " + (op2 !== undefined ? (op2.toString() + " ") : "") + sign;
        };

        //noinspection JSPotentiallyInvalidUsageOfThis
        this.evaluate = function () {
            return func(op1.evaluate.apply(op1, arguments), op2 && op2.evaluate.apply(op2, arguments));
        };

        //noinspection JSPotentiallyInvalidUsageOfThis
        this.diff = function (p) {
            switch (sign) {
                case '+':
                    return new Add(op1.diff(p), op2.diff(p));
                case '-':
                    return new Subtract(op1.diff(p), op2.diff(p));
                case '*':
                    return new Add(new Multiply(op1.diff(p), op2), new Multiply(op1, op2.diff(p)));
                case 'negate':
                    return new Negate(op1.diff(p));
                case '/':
                    return new Divide(new Subtract(new Multiply(op1.diff(p), op2), new Multiply(op1, op2.diff(p))),
                        new Multiply(op2, op2));
                case 'sin':
                    return new Multiply(new Cos(op1), op1.diff(p));
                case 'cos':
                    return new Multiply(new Negate(new Sin(op1)), op1.diff(p));
            }
        }
    }
}

var Add = Operation(addOp, "+");
var Subtract = Operation(subOp, "-");
var Multiply = Operation(mulOp, "*");
var Divide = Operation(divOp, "/");
var Negate = Operation(negateOp, "negate");
var Sin = Operation(sinOp, "sin");
var Cos = Operation(cosOp, "cos");

var binaryOperations = {
    "+ ": Add,
    "- ": Subtract,
    "* ": Multiply,
    "/ ": Divide
};

var unaryOperations = {
    "n": Negate,
    "s": Sin,
    "c": Cos
};

var unaryShifts = {
    "n": 6,
    "s": 3,
    "c": 3
};

function getNumPos(expression, i) {
    var c = expression[i];
    while (c >= "0" && c <= "9" && i < expression.length) {
        i++;
        c = expression[i];
    }
    return i;
}

function parse(expression) {
    var z;
    var stack = [];
    for (var i = 0; i < expression.length; i++) {
        var c = expression[i];
        var cc = c + (expression + " ")[i + 1];
        if (cc in binaryOperations) {
            var a = stack.pop();
            var b = stack.pop();
            stack.push(new binaryOperations[cc](b, a));
            i++;
        } else if (c in unaryOperations) {
            stack.push(new unaryOperations[c](stack.pop()));
            i += unaryShifts[c];
        } else if ((c == "x") || (c == "y") || (c == "z")) {
            stack.push(new Variable(c));
        } else if ((c >= "0" && c <= "9") || (c == "-")) {
            var acc = c;
            i++;
            z = getNumPos(expression, i);
            acc = acc + expression.substring(i, z);
            i = z;
            stack.push(new Const(parseInt(acc)));
        }
    }
    return stack.pop();
}

/* var expr = new Subtract(
 new Multiply(
 new Const(2),
 new Variable("x")
 ),
 new Const(3)
 );*/

/* var println = console.log;
 var simple = new Add(new Variable("x"),new Const(3));
 println(simple.diff().evaluate(100));

 var e = parse("2 x + 10 -");
 println(parse(e.diff("x").toString()).evaluate(10));*/

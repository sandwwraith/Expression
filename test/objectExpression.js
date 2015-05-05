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
var expOp = Math.exp;
var atanOp = Math.atan;

function Variable(name) {
    Object.defineProperty(this, "name", {value: name, enumerable: true});
}

Variable.prototype.toString = function () {
    return this.name;
};
Variable.prototype.evaluate = function () {
    switch (this.name) {
        case "x":
            return arguments[0];
        case "y":
            return arguments[1];
        default:
            return arguments[2];
    }
};
Variable.prototype.diff = function (p) {
    if (this.name == p) return new Const(1);
    return new Const(0);
};
Variable.prototype.prefix = Variable.prototype.toString;

function Const(Value) {
    Object.defineProperties(this, {
        value: {value: Value, enumerable: true, writable: false, configurable: false}
    });
}
Const.prototype.toString = function () {
    return this.value.toString();
};
Const.prototype.evaluate = function () {
    return this.value;
};
Const.prototype.diff = function () {
    return new Const(0);
};
Const.prototype.prefix = Const.prototype.toString;

function Operation(op11, op22) {
    Object.defineProperties(this, {
        op1: {value: op11, enumerable: true, writable: false, configurable: false},
        op2: {value: op22, enumerable: true, writable: false, configurable: false}
    });
}

var OpHelper = function (f, s) {
    var p = function (op1, op2) {
        Operation.call(this, op1, op2);
    };
    p.prototype.toString = function () {
        return this.op1.toString() + " " + (this.op2 !== undefined ? (this.op2.toString() + " ") : "") + s;
    };
    p.prototype.evaluate = function () {
        return f(this.op1.evaluate.apply(this.op1, arguments), this.op2 && this.op2.evaluate.apply(this.op2, arguments));
    };
    p.prototype.prefix = function () {
        return "(" + s + " " + this.op1.prefix() + (this.op2 !== undefined ? (" " + this.op2.prefix() + ")") : ")");
    };
    return p;
};

var Add = OpHelper(addOp, "+");
Add.prototype.diff = function (p) {
    return new Add(this.op1.diff(p), this.op2.diff(p));
};

var Subtract = OpHelper(subOp, "-");
Subtract.prototype.diff = function (p) {
    return new Subtract(this.op1.diff(p), this.op2.diff(p));
};

var Multiply = OpHelper(mulOp, "*");
Multiply.prototype.diff = function (p) {
    return new Add(new Multiply(this.op1.diff(p), this.op2), new Multiply(this.op1, this.op2.diff(p)));
};

var Divide = OpHelper(divOp, "/");
Divide.prototype.diff = function (p) {
    return new Divide(new Subtract(new Multiply(this.op1.diff(p), this.op2), new Multiply(this.op1, this.op2.diff(p))),
        new Multiply(this.op2, this.op2));
};

var Negate = OpHelper(negateOp, "negate");
Negate.prototype.diff = function (p) {
    return new Negate(this.op1.diff(p));
};

var Sin = OpHelper(sinOp, "sin");
Sin.prototype.diff = function (p) {
    return new Multiply(new Cos(this.op1), this.op1.diff(p));
};

var Cos = OpHelper(cosOp, "cos");
Cos.prototype.diff = function (p) {
    return new Multiply(new Negate(new Sin(this.op1)), this.op1.diff(p));
};

var Exp = OpHelper(expOp, "exp");
Exp.prototype.diff = function (p) {
    return new Multiply(new Exp(this.op1), this.op1.diff(p));
};

var ArcTan = OpHelper(atanOp, "atan");
ArcTan.prototype.diff = function (p) {
    return new Divide(this.op1.diff(p), new Add(
        new Const(1), new Multiply(this.op1, this.op1)
    ))
};

var binaryOperations = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide
};

var unaryOperations = {
    "negate": Negate,
    "exp": Exp,
    "atan": ArcTan,
    "sin": Sin,
    "cos": Cos
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
    var mass = expression.split(" ");
    for (var i = 0; i < mass.length; i++) {
        var c = mass[i];
        if (c.length == 0) continue;
        if (c in binaryOperations) {
            var a = stack.pop();
            var b = stack.pop();
            stack.push(new binaryOperations[c](b, a));
        } else if (c in unaryOperations) {
            stack.push(new unaryOperations[c](stack.pop()));
        } else if ((c == "x") || (c == "y") || (c == "z")) {
            stack.push(new Variable(c));
        } else if ((c[0] >= "0" && c[0] <= "9") || (c[0] == "-")) {
            stack.push(new Const(parseInt(c)));
        }
    }
    return stack.pop();
}

function ParserError(message) {
    this.name = "ParserError";
    this.message = message;
}
ParserError.prototype = Error.prototype;

function isFullNumber(str) {
    if (str.length == 0) return false;
    for (var i = 0; i < str.length; i++) {
        if (i == 0 && str[i] == '-' && str.length != 1) continue;
        if (str[i] < '0' || str[i] > '9') return false;
    }
    return true;
}

function parsePrefix(expression) {
    var pos = 0;
    if (expression.length == 0) throw new ParserError("Empty String");
    var moveNext = function () {
        if (expression[pos] == '(' || expression[pos] == ')') {
            pos++;
            return;
        }
        while (expression[pos] != " " && expression[pos] != ')' && expression[pos] != '(' && pos < expression.length) {
            pos++;
        }
    };
    var skip = function () {
        while (expression[pos] == " " && pos < expression.length) {
            pos++;
        }
    };
    var getToken = function () {
        skip();
        var oldPos = pos;
        moveNext();
        return expression.substring(oldPos, pos);
    };
    var inner = function () {
        var expr;
        var com = getToken();
        if (com == '(') {
            expr = inner();
            if (expression[pos] != ')') throw new ParserError("Expected closing parenthesis at pos " + pos);
            pos++;
        } else if (isFullNumber(com)) {
            expr = new Const(parseInt(com));
        } else if (com == 'x' || com == 'y' || com == 'z') {
            expr = new Variable(com);
        } else if (com in binaryOperations) {
            expr = new binaryOperations[com](inner(), inner());
            skip();
            if (expression[pos] != ')' && pos < expression.length) throw new ParserError(com + " is a two-arguments function");
        } else if (com in unaryOperations) {
            expr = new unaryOperations[com](inner());
            skip();
            if (expression[pos] != ')' && pos < expression.length) throw new ParserError(com + " is a one-arguments function");
        } else if (com == "") {
            throw new ParserError("Empty token");
        } else if (com == ")") {
            throw new ParserError("Unexpected closing parenthesis at pos " + pos);
        } else {
            throw new ParserError("Unrecognized operation or variable " + com + " at position " + (pos - com.length));
        }
        return expr;
    };
    var ans = inner();
    skip();
    if (pos != expression.length) throw new ParserError("String is invalid from pos " + pos);
    return ans;
}

//var println = console.log;
/*var simple = new Add(new Variable("x"),new Const(3));
 println(simple.toString());
 var e = new Add(new Const(3), new Const(5));
 println(e.toString());*/

/* var e = parse("2 x + 10 -");
 println(parse(e.diff("x").toString()).evaluate(10));*/

//println(parsePrefix("+ (* x 3) (/ 10 2)").evaluate(10));
//println(parsePrefix("atan 0").evaluate(10));

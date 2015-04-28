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

function Operation(func, sign, op11, op22) {
    Object.defineProperties(this, {
        op1: {value: op11, enumerable: true, writable: false, configurable: false},
        op2: {value: op22, enumerable: true, writable: false, configurable: false}
    });
    /* this.op1 = op1;
     this.op2 = op2;*/
        this.toString = function () {
            return this.op1.toString() + " " + (this.op2 !== undefined ? (this.op2.toString() + " ") : "") + sign;
        };

        this.evaluate = function () {
            return func(this.op1.evaluate.apply(this.op1, arguments), this.op2 && this.op2.evaluate.apply(this.op2, arguments));
        };
}

function Add(op1, op2) {
    Operation.call(this, addOp, "+", op1, op2);

}

Add.prototype.diff = function (p) {
    return new Add(this.op1.diff(p), this.op2.diff(p));
};

function Subtract(op1, op2) {
    Operation.call(this, subOp, "-", op1, op2);
}

Subtract.prototype.diff = function (p) {
    return new Subtract(this.op1.diff(p), this.op2.diff(p));
};

function Multiply(op1, op2) {
    Operation.call(this, mulOp, "*", op1, op2);
}

Multiply.prototype.diff = function (p) {
    return new Add(new Multiply(this.op1.diff(p), this.op2), new Multiply(this.op1, this.op2.diff(p)));
};

function Divide(op1, op2) {
    Operation.call(this, divOp, "/", op1, op2);
}

Divide.prototype.diff = function (p) {
    return new Divide(new Subtract(new Multiply(this.op1.diff(p), this.op2), new Multiply(this.op1, this.op2.diff(p))),
        new Multiply(this.op2, this.op2));
};

function Negate(op) {
    Operation.call(this, negateOp, "negate", op);
}

Negate.prototype.diff = function (p) {
    return new Negate(this.op1.diff(p));
};

function Sin(op) {
    Operation.call(this, sinOp, "sin", op);
}

Sin.prototype.diff = function (p) {
    return new Multiply(new Cos(this.op1), this.op1.diff(p));
};

function Cos(op) {
    Operation.call(this, cosOp, "cos", op);
}

Cos.prototype.diff = function (p) {
    return new Multiply(new Negate(new Sin(this.op1)), this.op1.diff(p));
};

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

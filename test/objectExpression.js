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

    this.diff = function () {
        return new Const(1);
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
            return func(op1.evaluate.apply(null, arguments), op2 && op2.evaluate.apply(null, arguments));
        };

        //noinspection JSPotentiallyInvalidUsageOfThis
        this.diff = function () {
            switch (sign) {
                case '+':
                    return new Add(op1.diff(), op2.diff());
            }
        }
    }
}

var Add = Operation(addOp, "+");
var Subtract = Operation(subOp, "-");
var Multiply = Operation(mulOp, "*");
var Divide = Operation(divOp, "/");
var Negate = Operation(negateOp, "negate");

/*
 var expr = new Subtract(
 new Multiply(
 new Const(2),
 new Variable("x")
 ),
 new Const(3)
 );

 var println = console.log;
 var simple = new Add(new Variable("x"),new Const(3));
 println(simple.diff().evaluate(100));*/

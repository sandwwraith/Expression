/**
 * Created by sandwwraith(@gmail.com)
 *
 * ITMO University, 2015
 **/
"use strict";

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
var modOp = function (a, b) {
    return a % b;
};

var powOp = Math.pow;
var absOp = Math.abs;
var logOp = Math.log;

var max2 = function (a, b) {
    return (a > b) ? a : b;
};
var min2 = function (a, b) {
    return (a < b) ? a : b;
};

var op = function (f) {
    return function (a, b) {
        return function () {
            return f(a.apply(null, arguments), b && b.apply(null, arguments)); //and we need to go deeper..
        }
    }
};

var add = op(addOp);
var subtract = op(subOp);
var multiply = op(mulOp);
var divide = op(divOp);
var power = op(powOp);
var mod = op(modOp);

var negate = op(negateOp);
var abs = op(absOp);
var log = op(logOp);

var cnst = function (a) {
    return function () {
        return a;
    }
};

var variable = function (name) {
    return function () {
        switch (name) {
            case "x":
                return arguments[0];
            case "y":
                return arguments[1];
            default:
                return arguments[2];
        }
    }
};

function foldL(f) {
    return function () {
        var mass = arguments;
        return function () {
            var res = mass[0].apply(null, arguments);
            for (var i = 1; i < mass.length; i++) {
                res = f(res, mass[i].apply(null, arguments));
            }
            return res;
        }
    }
}

var binaryOperations = {
    "+ ": add,
    "- ": subtract,
    "* ": multiply,
    "**": power,
    "/ ": divide,
    "% ": mod
};

var unaryOperations = {
    "n": negate,
    "a": abs,
    "l": log
};

var minN = foldL(min2);
var maxN = foldL(max2);

var multiOperations = {
    "mi": minN,
    "ma": maxN
};

var unaryShifts = {
    "n": 6,
    "a": 3,
    "l": 3
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
            stack.push(binaryOperations[cc](b, a));
            i++;
        } else if (typeof unaryOperations[c] != "undefined") {
            stack.push(unaryOperations[c](stack.pop()));
            i += unaryShifts[c];
        } else if (cc in multiOperations) {
            i += 3;
            z = getNumPos(expression, i);
            var accc = expression.substring(i, z);
            i = z;
            accc = parseInt(accc);
            var t = [];
            for (var j = 0; j < accc; j++) {
                t.push(stack.pop());
            }
            stack.push(multiOperations[cc].apply(null, t));
        } else if ((c == "x") || (c == "y") || (c == "z")) {
            stack.push(variable(c));
        } else if ((c >= "0" && c <= "9") || (c == "-")) {
            var acc = c;
            i++;
            z = getNumPos(expression, i);
            acc = acc + expression.substring(i, z);
            i = z;
            stack.push(cnst(parseInt(acc)));
        }
    }
    return stack.pop();
}

//var println = console.log;
//println(negate(add(variable("x"), cnst(5)))(1000));
//println(minN(cnst(2),cnst(3),cnst(0))(1));
//println(parse("2 3 0 min3")(10));
//println(parse("123456 22 +")(10));

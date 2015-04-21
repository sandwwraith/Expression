/**
 * Created by sandwwraith(@gmail.com)
 *
 * ITMO University, 2015
 **/

var addOp = function (a, b) {
    return a + b;
}
var subOp = function (a, b) {
    return a - b;
}
var mulOp = function (a, b) {
    return a * b;
}
var divOp = function (a, b) {
    return a / b;
}

var negateOp = function (a) {
    return -a;
}

var binary = function (f) {
    return function (a, b) {
        return function (x, y, z) {
            return f(a(x, y, z), b(x, y, z)); //and we need to go deeper..
        }
    }
}

var unary = function (f) {
    return function (a) {
        return function (x, y, z) {
            return f(a(x, y, z)); //and we need to go deeper..
        }
    }
}

var add = binary(addOp);
var subtract = binary(subOp);
var multiply = binary(mulOp);
var divide = binary(divOp);

var negate = unary(negateOp);

var cnst = function (a) {
    return function () {
        return a;
    }
}

var variable = function (name) {
    return function (x, y, z) {
        if (name == "x") {
            return x;
        } else if (name == "y") {
            return y;
        } else {
            return z;
        }
    }
}

var binaryOperations = {
    "+": add,
    "-": subtract,
    "*": multiply,
    "/": divide
};

var unaryOperations = {
    "n": negate
};

function parse(expression) {
    var stack = [];
    for (var i = 0; i < expression.length; i++) {
        var c = expression[i];
        if (typeof binaryOperations[c] != "undefined" && (i == expression.length - 1 || expression[i + 1] == " ")) {
            var a = stack.pop();
            var b = stack.pop();
            stack.push(binaryOperations[c](b, a));
        } else if (typeof unaryOperations[c] != "undefined") {
            stack.push(unaryOperations[c](stack.pop()));
        } else if ((c == "x") || (c == "y") || (c == "z")) {
            stack.push(variable(c));
        } else if ((c >= "0" && c <= "9") || (c == "-")) {
            var acc = c;
            i++;
            c = expression[i];
            while (c >= "0" && c <= "9" && i < expression.length) {
                acc = acc + c;
                i++;
                c = expression[i];
            }
            stack.push(cnst(parseInt(acc)));
        }
    }
    return stack.pop();
}

//var println = console.log;
//println(negate(add(variable("x"), cnst(5)))(1000));
//println(parse("x -100 /")(10, 10, 200));

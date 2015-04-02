package expression;


import expression.uncheked.*;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class ExpressionParser implements Parser {

    private static final TripleExpression NEGATE = new Const(-1);
    private String line;
    private int shift; // current pos.

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '<' || ch == '>';
    }

    private static boolean isVar(char ch) {
        return ch == 'x' || ch == 'y' || ch == 'z';
    }

    private static boolean isBracket(char ch) {
        return ch == '(' || ch == ')';
    }

    private static boolean isValidToken(char ch) {
        return Character.isLetterOrDigit(ch) || isOperator(ch) || isVar(ch) || isBracket(ch);
    }

    private static String filterString(String input) throws ParserException {
        StringBuilder res = new StringBuilder("");
        int balance = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (isValidToken(c)) {
                res.append(c);
                if (c == '(') {
                    balance++;
                }
                if (c == ')') {
                    balance--;
                }
                if (balance < 0) {
                    throw new UnbalancedBracketsException();
                }
                continue;
            }
            if (!Character.isWhitespace(c)) {
                throw new UnexpectedSymbolsException(i, c);
            }
        }
        if (balance != 0) {
            throw new UnbalancedBracketsException();
        }

        return res.toString();
    }

    private TripleExpression getVar() {
        TripleExpression res = new Variable(line.substring(shift, shift + 1));
        shift++;
        return res;
    }

    private TripleExpression bracket() {
        if (line.charAt(shift) == '(') {
            shift += 1;
            TripleExpression res = priorityZero();
            shift++; // to delete pair ')' ; pair exists due to isBalanced() check
            return res;
        } else {
            return getVar();
        }
    }

    private TripleExpression unary() { // -, abs, square
        char op = line.charAt(shift);
        if (op == '-' || op == 'a' || op == 's') {
            TripleExpression next = null;
            switch (op) {
                case '-': {
                    shift += 1;
                    next = new Multiply(getNum(), NEGATE);
                }
                break;
                case 'a': {
                    shift += 3;
                    next = new Absolute(getNum());
                }
                break;
                case 's': {
                    shift += 6;
                    next = new Square(getNum());
                }
            }
            return next;
        }
        return bracket();
    }

    private TripleExpression getNum() {
        int i = 0;
        boolean neg = false;
        if (line.charAt(shift) == '-') {
            neg = true;
            i++;
        }
        while (i + shift < line.length() && Character.isDigit(line.charAt(i + shift))) {
            i++;
        }

        if (i == 0 || (i == 1 && neg)) {
            //nothing found, go next
            return unary();
        }
        int num = Integer.parseInt(line.substring(shift, i + shift));
        shift += i;
        return new Const(num);
    }

    private TripleExpression priorityTwo() { //Mul | Div | Mod
        TripleExpression current = getNum();

        while (shift < line.length()) {
            char op = line.charAt(shift);
            if (op != '*' && op != '/' && op != 'm') {
                return current;
            }

            if (op == 'm') {
                shift += 3;
                current = new Module(current, getNum());
            } else {
                shift += 1;
                if (op == '*') {
                    current = new Multiply(current, getNum());
                } else {
                    current = new Divide(current, getNum());
                }
            }
        }
        return current;
    }

    private TripleExpression priorityOne() { //Plus | Minus
        TripleExpression current = priorityTwo();

        while (shift < line.length()) {
            char op = line.charAt(shift);
            if (op != '+' && op != '-') {
                return current;
            }

            shift += 1;// Next Token

            if (op == '+') {
                current = new Add(current, priorityTwo());
            } else {
                current = new Subtract(current, priorityTwo());
            }
        }
        return current;
    }

    private TripleExpression priorityZero() { //Shifts
        TripleExpression current = priorityOne();

        while (shift < line.length()) {
            char op = line.charAt(shift);
            if (op != '<' && op != '>') {
                return current;
            }

            shift += 2;// Next Token

            if (op == '<') {
                current = new ShiftLeft(current, priorityOne());
            } else {
                current = new ShiftRight(current, priorityOne());
            }
        }
        return current;
    }

    public TripleExpression parse(String s) throws Exception {
        s = filterString(s);
        if (s == null) {
            return null;
        }
        line = s;
        shift = 0;
        return priorityZero();
    }
}

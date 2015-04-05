package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedParser implements Parser {

    //private static final TripleExpression NEGATE = new Const(-1);
    private String line;
    private int shift; // current pos.

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
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

    private void movenext() {
        while (shift < line.length() && Character.isWhitespace(line.charAt(shift))) {
            shift++;
        }
    }

    private TripleExpression getVar() throws ParserException {
        movenext();
        String varName = line.substring(shift, shift + 1);
        if (!isVar(varName.charAt(0)) || (shift + 1 < line.length() && Character.isLetter(line.charAt(shift + 1)))) {
            throw new UnexpectedSymbolsException(shift,line.charAt(shift));
        } else {
            TripleExpression res = new Variable(varName);
            shift++;
            return res;
        }
    }

    private TripleExpression bracket() throws ParserException {
        movenext();
        if (line.charAt(shift) == '(') {
            shift += 1;
            TripleExpression res = priorityOne();
            if (line.charAt(shift) != ')') {
                throw new UnbalancedBracketsException("Expected closing bracket at pos " + shift);
            }
            shift++;
            return res;
        } else {
            movenext();
            return getVar();
        }
    }

    private TripleExpression unary() throws ParserException {// -
        movenext();
        char op = line.charAt(shift);
        if (op == '-' || op == 'a' || op == 's') {
            if (line.charAt(shift) == '-') {
                shift++;
                movenext();
                return new CheckedNegate(getNum());
            }
            if (line.charAt(shift) == 'a') {
                String abs = line.substring(shift, shift + 3);
                if (!abs.equals("abs")) {
                    throw new UnexpectedSymbolsException("Incorrect token");
                }
                shift += 3;
                movenext();
                return new CheckedAbsolute(getNum());
            }
            if (line.charAt(shift) == 's') {
                String abs = line.substring(shift, shift + 4);
                if (!abs.equals("sqrt")) {
                    throw new UnexpectedSymbolsException("Incorrect token");
                }
                shift += 4;
                movenext();
                return new CheckedSquareRoot(getNum());
            }
        }
        return bracket();
    }

    private TripleExpression getNum() throws ParserException {
        movenext();
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
        int num;
        try {
            num = Integer.parseInt(line.substring(shift, i + shift));
        } catch (NumberFormatException e) {
            throw new ParserException(line.substring(shift, i + shift) + " is not a valid Integer number");
        }
        shift += i;
        return new Const(num);
    }

    private TripleExpression priorityThree() throws ParserException { //Power | Log
        movenext();
        TripleExpression current = getNum();
        movenext();
        while (shift < line.length()) {
            char op = line.charAt(shift);
            char op2 = (shift + 1 < line.length()) ? line.charAt(shift + 1) : '?';
            if (!((op == '*' && op2 == '*') || (op == '/' && op2 == '/'))) {
                return current;
            }
            shift += 2;
            if (op == '*') {
                current = new CheckedPower(current, getNum());
            } else {
                current = new CheckedLog(current, getNum());
            }
            movenext();
        }
        return current;
    }

    private TripleExpression priorityTwo() throws ParserException { //Mul | Div
        movenext();
        TripleExpression current = priorityThree();
        movenext();
        while (shift < line.length()) {
            char op = line.charAt(shift);
            char op2 = (shift + 1 < line.length()) ? line.charAt(shift + 1) : '?';

            if ((op != '*' && op != '/') || op2 == '*' || op2 == '/') {
                return current;
            }
            shift += 1;
            if (op == '*') {
                current = new CheckedMultiply(current, priorityThree());
            } else {
                current = new CheckedDivide(current, priorityThree());
            }
            movenext();
        }
        return current;
    }

    private TripleExpression priorityOne() throws ParserException { //Plus | Minus
        movenext();
        TripleExpression current = priorityTwo();
        movenext();
        while (shift < line.length()) {
            char op = line.charAt(shift);
            if (op != '+' && op != '-') {
                return current;
            }
            shift += 1;
            if (op == '+') {
                current = new CheckedAdd(current, priorityTwo());
            } else {
                current = new CheckedSubtract(current, priorityTwo());
            }
            movenext();
        }
        return current;
    }

    public TripleExpression parse(String expression) throws ParserException {
        // expression = filterString(expression);
        line = expression;
        shift = 0;
        TripleExpression exp;
        try {
            exp = priorityOne();
        } catch (StringIndexOutOfBoundsException e) {
            throw new UnexpectedSymbolsException("Unexpected end of string");
        }
        movenext();
        if (shift < line.length()) {
            throw new ParserException("Incorrect symbols from pos " + shift);
        }
        return exp;
    }
}

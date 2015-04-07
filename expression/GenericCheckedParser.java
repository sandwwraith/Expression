package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericCheckedParser implements Parser {

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

    private void moveNext() {
        while (shift < line.length() && Character.isWhitespace(line.charAt(shift))) {
            shift++;
        }
    }

    private GenericTripleExpression getVar() throws ParserException {
        moveNext();
        String varName = line.substring(shift, shift + 1);
        if (!isVar(varName.charAt(0)) || (shift + 1 < line.length() && Character.isLetter(line.charAt(shift + 1)))) {
            throw new UnexpectedSymbolsException(shift, line.charAt(shift));
        } else {
            GenericTripleExpression res = new GenericVariable(varName);
            shift++;
            return res;
        }
    }

    private GenericTripleExpression bracket() throws ParserException {
        moveNext();
        if (line.charAt(shift) == '(') {
            shift += 1;
            GenericTripleExpression res = priorityOne();
            if (line.charAt(shift) != ')') {
                throw new UnbalancedBracketsException("Expected closing bracket at pos " + shift);
            }
            shift++;
            return res;
        } else {
            moveNext();
            return getVar();
        }
    }

    private GenericTripleExpression unary() throws ParserException {// -
        moveNext();
        char op = line.charAt(shift);
        if (op == '-' || op == 's' || op == 'a') {
            if (line.charAt(shift) == '-') {
                shift++;
                moveNext();
                return new GenericNegate(getNum());
            }
            if (line.charAt(shift) == 'a') {
                String abs = line.substring(shift, shift + 3);
                if (!abs.equals("abs")) {
                    throw new UnexpectedSymbolsException("Incorrect token");
                }
                shift += 3;
                moveNext();
                return new GenericAbsolute(getNum());
            }
            if (line.charAt(shift) == 's') {
                String abs = line.substring(shift, shift + 6);
                if (!abs.equals("square")) {
                    throw new UnexpectedSymbolsException("Incorrect token");
                }
                shift += 6;
                moveNext();
                return new GenericSquare(getNum());
            }
        }
        return bracket();
    }

    private GenericTripleExpression getNum() throws ParserException {
        moveNext();
        int i = 0;
        boolean neg = false;
        if (line.charAt(shift) == '-') {
            neg = true;
            i++;
        }
        while (i + shift < line.length() && (Character.isDigit(line.charAt(i + shift)) || line.charAt(i + shift) == '.')) {
            i++;
        }

        if (i == 0 || (i == 1 && neg)) {
            //nothing found, go next
            return unary();
        }
        String num = line.substring(shift, i + shift);
        shift += i;
        return new GenericConst(num);
    }

    /*private TripleExpression priorityThree() throws ParserException { //Power | Log
        moveNext();
        TripleExpression current = getNum();
        moveNext();
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
            moveNext();
        }
        return current;
    }*/

    private GenericTripleExpression priorityTwo() throws ParserException { //Mul | Div
        moveNext();
        GenericTripleExpression current = getNum();
        moveNext();
        while (shift < line.length()) {
            char op = line.charAt(shift);
            //char op2 = (shift + 1 < line.length()) ? line.charAt(shift + 1) : '?';

            //if ((op != '*' && op != '/') || op2 == '*' || op2 == '/') {
            if (op != '*' && op != '/' && op != 'm') {
                return current;
            }
            shift += 1;
            if (op == '*') {
                current = new GenericMultiply(current, getNum());
            } else {
                if (op == '/') {
                    current = new GenericDivide(current, getNum());
                } else {
                    String abs = line.substring(shift - 1, shift + 2);
                    if (!abs.equals("mod")) {
                        throw new UnexpectedSymbolsException("Incorrect token " + abs);
                    }
                    shift += 2;
                    moveNext();
                    return new GenericMod(current, getNum());
                }
            }
            moveNext();
        }
        return current;
    }

    private GenericTripleExpression priorityOne() throws ParserException { //Plus | Minus
        moveNext();
        GenericTripleExpression current = priorityTwo();
        moveNext();
        while (shift < line.length()) {
            char op = line.charAt(shift);
            if (op != '+' && op != '-') {
                return current;
            }
            shift += 1;
            if (op == '+') {
                current = new GenericAdd(current, priorityTwo());
            } else {
                current = new GenericSubtract(current, priorityTwo());
            }
            moveNext();
        }
        return current;
    }

    public GenericTripleExpression parse(String expression) throws ParserException {
        // expression = filterString(expression);
        line = expression;
        shift = 0;
        GenericTripleExpression exp;
        try {
            exp = priorityOne();
        } catch (StringIndexOutOfBoundsException e) {
            throw new UnexpectedSymbolsException("Unexpected end of string");
        }
        moveNext();
        if (shift < line.length()) {
            throw new UnexpectedSymbolsException("Incorrect symbols from pos " + shift);
        }
        return exp;
    }
}

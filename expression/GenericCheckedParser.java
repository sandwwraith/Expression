package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericCheckedParser implements Parser {

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

    private void movenext() {
        while (shift < line.length() && Character.isWhitespace(line.charAt(shift))) {
            shift++;
        }
    }

    private GenericTripleExpression getVar() throws ParserException {
        movenext();
        String varName = line.substring(shift, shift + 1);
        if (!isVar(varName.charAt(0)) || (shift + 1 < line.length() && Character.isLetter(line.charAt(shift + 1)))) {
            throw new UnexpectedSymbolsException("Incorrect token at pos " + shift);
        } else {
            GenericTripleExpression res = new GenericVariable(varName);
            shift++;
            return res;
        }
    }

    private GenericTripleExpression bracket() throws ParserException {
        movenext();
        if (line.charAt(shift) == '(') {
            shift += 1;
            GenericTripleExpression res = priorityOne();
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

    private GenericTripleExpression unary() throws ParserException {// -
        movenext();
        char op = line.charAt(shift);
        if (op == '-') {
            if (line.charAt(shift) == '-') {
                shift++;
                movenext();
                return new GenericNegate(getNum());
            }
            /*if (line.charAt(shift) == 'a') {
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
            }*/
        }
        return bracket();
    }

    private GenericTripleExpression getNum() throws ParserException {
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
        /*int num;
        try {
            num = Integer.parseInt(line.substring(shift, i + shift));
        } catch (NumberFormatException e) {
            throw new ParserException(line.substring(shift, i + shift) + " is not a valid Integer number");
        }
        shift += i;*/
        String num = line.substring(shift, i + shift);
        return new GenericConst(num);
    }

    /*private TripleExpression priorityThree() throws ParserException { //Power | Log
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
    }*/

    private GenericTripleExpression priorityTwo() throws ParserException { //Mul | Div
        movenext();
        GenericTripleExpression current = getNum();
        movenext();
        while (shift < line.length()) {
            char op = line.charAt(shift);
            char op2 = (shift + 1 < line.length()) ? line.charAt(shift + 1) : '?';

            if ((op != '*' && op != '/') || op2 == '*' || op2 == '/') {
                return current;
            }
            shift += 1;
            if (op == '*') {
                current = new GenericMultiply(current, getNum());
            } else {
                current = new GenericDivide(current, getNum());
            }
            movenext();
        }
        return current;
    }

    private GenericTripleExpression priorityOne() throws ParserException { //Plus | Minus
        movenext();
        GenericTripleExpression current = priorityTwo();
        movenext();
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
            movenext();
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
        movenext();
        if (shift < line.length()) {
            throw new ParserException("Cannot parse rest of string from pos " + shift);
        }
        return exp;
    }
}
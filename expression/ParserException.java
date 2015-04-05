package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class ParserException extends Exception {
    public ParserException(String msg) {
        super(msg);
    }
}

class UnbalancedBracketsException extends ParserException {
    public UnbalancedBracketsException() {
        super("Unbalanced brackets!");
    }

    public UnbalancedBracketsException(String msg) {
        super(msg);
    }
}

class UnexpectedSymbolsException extends ParserException {
    /*
    --Commented out by Inspection START (30.03.2015 04:22):
    public UnexpectedSymbolsException() {
    super("Unexpected symbols in input string!");
    }
    --Commented out by Inspection STOP (30.03.2015 04:22)
    */

    public UnexpectedSymbolsException(int pos, char ch) {
        super("Unexpected symbol in input string: " + ch + " at position: " + pos);
    }

    public UnexpectedSymbolsException(String msg) {
        super(msg);
    }
}
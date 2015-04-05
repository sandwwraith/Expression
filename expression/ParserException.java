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
    public UnbalancedBracketsException(String msg) {
        super(msg);
    }
}

class UnexpectedSymbolsException extends ParserException {

    public UnexpectedSymbolsException(int pos, char ch) {
        super("Unexpected symbol in input string: " + ch + " at position: " + pos);
    }

    public UnexpectedSymbolsException(String msg) {
        super(msg);
    }
}
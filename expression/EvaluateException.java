package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class EvaluateException extends RuntimeException {
    public EvaluateException(String msg) {
        super(msg);
    }
}

class DivisionByZero extends EvaluateException {
    public DivisionByZero() {
        super("division by zero");
    }
}

class OverflowException extends EvaluateException {
    public OverflowException() {
        super("overflow");
    }
}

class NegativeSquareRootException extends EvaluateException {
    public NegativeSquareRootException() {
        super("Square root from negative number");
    }
}
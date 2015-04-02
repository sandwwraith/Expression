package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedSquareRoot extends UnaryOperator {
    public CheckedSquareRoot(TripleExpression exp) {
        super(exp);
    }

    protected int calc(int x) {
        if (x < 0) {
            throw new NegativeSquareRootException();
        }
        return (int) Math.sqrt(x);
    }
}

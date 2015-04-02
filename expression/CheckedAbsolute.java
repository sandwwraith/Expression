package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedAbsolute extends UnaryOperator {
    public CheckedAbsolute(TripleExpression exp) {
        super(exp);
    }

    public int calc(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
        return (x > 0) ? x : -x;
    }
}

package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedNegate extends UnaryOperator implements TripleExpression {
    public CheckedNegate(TripleExpression exp) {
        super(exp);
    }

    @Override
    protected int calc(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
        return -x;
    }


}

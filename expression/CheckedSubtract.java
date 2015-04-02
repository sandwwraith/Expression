package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedSubtract extends BinaryOperator implements TripleExpression {
    public CheckedSubtract(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

    protected int calc(int x, int y) {
        if ((x >= 0 && y < 0 && (Integer.MAX_VALUE + y < x)) || (x < 0 && y > 0 && (Integer.MIN_VALUE + y > x))) {
            throw new OverflowException();
        }
        return x - y;
    }
}

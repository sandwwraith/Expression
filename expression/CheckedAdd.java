package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedAdd extends BinaryOperator implements TripleExpression {
    public CheckedAdd(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

    protected int calc(int x, int y) {
        if ((x > 0 && y > 0 && (Integer.MAX_VALUE - x < y)) || (x < 0 && y < 0 && (Integer.MIN_VALUE - x > y))) {
            throw new OverflowException();
        }
        return x + y;
    }
}

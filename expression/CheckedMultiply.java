package expression;


/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedMultiply extends BinaryOperator implements TripleExpression {
    public CheckedMultiply(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

    protected int calc(int x, int y) {
        int z = x * y;
        if ((x == Integer.MIN_VALUE && y == -1) || ((y != 0 && z / y != x))) {
            throw new OverflowException();
        }
        return z;
    }
}

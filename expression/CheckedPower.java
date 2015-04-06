package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedPower extends BinaryOperator {
    public CheckedPower(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

    protected int calc(int x, int y) {
        if (y < 0) {
            throw new EvaluateException("Negative natural power");
        }
        if (x == 0 && y == 0) {
            throw new EvaluateException("Undefined result of 0^0");
        }
        // if (r<0) throw new OverflowException();
        return power(x, y);
    }
}

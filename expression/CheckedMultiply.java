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
        return MultyCheck(x,y);
    }
}

package expression.uncheked;

import expression.BinaryOperator;
import expression.TripleExpression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class Module extends BinaryOperator implements TripleExpression {

    public Module(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

    protected int calc(int x, int y) {
        return x % y;
    }
}

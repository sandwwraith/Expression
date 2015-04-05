package expression.uncheked;

import expression.TripleExpression;
import expression.UnaryOperator;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class Absolute extends UnaryOperator implements TripleExpression {

    public Absolute(TripleExpression exp) {
        super(exp);
    }

    public int calc(int x) {
        return (x > 0) ? x : -x;
    }
}

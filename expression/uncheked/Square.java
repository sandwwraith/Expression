package expression.uncheked;

import expression.TripleExpression;
import expression.UnaryOperator;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class Square extends UnaryOperator implements TripleExpression {

    public Square(TripleExpression exp) {
        super(exp);
    }

    public int calc(int x) {
        return x * x;
    }
}

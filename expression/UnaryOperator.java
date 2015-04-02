package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public abstract class UnaryOperator implements TripleExpression {
    private final TripleExpression expression;

    protected UnaryOperator(TripleExpression exp) {
        expression = exp;
    }

    protected abstract int calc(int x);

    public int evaluate(int x, int y, int z) {
        return calc(expression.evaluate(x, y, z));
    }

}

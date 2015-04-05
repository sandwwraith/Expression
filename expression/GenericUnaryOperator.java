package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public abstract class GenericUnaryOperator implements GenericTripleExpression {
    private final GenericTripleExpression expression;

    protected GenericUnaryOperator(GenericTripleExpression exp) {
        expression = exp;
    }

    protected abstract <S, T extends Calculable<S>> S calc(S a, T calculator);

    public <S, T extends Calculable<S>> S evaluate(S x, S y, S z, T calculator) {
        return calc(expression.evaluate(x, y, z, calculator), calculator);
    }

}

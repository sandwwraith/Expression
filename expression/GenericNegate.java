package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericNegate extends GenericUnaryOperator implements GenericTripleExpression {
    public GenericNegate(GenericTripleExpression exp) {
        super(exp);
    }

    protected <S, T extends Calculator<S>> S calc(S a, T calculator) {
        return calculator.negate(a);
    }
}

package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericAbsolute extends GenericUnaryOperator implements GenericTripleExpression {

    public GenericAbsolute(GenericTripleExpression exp) {
        super(exp);
    }

    protected <S, T extends Calculator<S>> S calc(S a, T calculator) {
        return calculator.abs(a);
    }

}

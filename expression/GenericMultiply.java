package expression;


/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericMultiply extends GenericBinaryOperator implements GenericTripleExpression {
    public GenericMultiply(GenericTripleExpression op1, GenericTripleExpression op2) {
        super(op1, op2);
    }

    protected <S, T extends Calculator<S>> S calc(S a, S b, T calculator) {
        return calculator.mul(a, b);
    }
}

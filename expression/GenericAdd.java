package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericAdd extends GenericBinaryOperator implements GenericTripleExpression {
    public GenericAdd(GenericTripleExpression op1, GenericTripleExpression op2) {
        super(op1, op2);
    }

    protected <S, T extends Calculator<S>> S calc(S a, S b, T calculator) {
        return calculator.add(a, b);
    }
}

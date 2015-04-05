package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericSubtract extends GenericBinaryOperator implements GenericTripleExpression {
    public GenericSubtract(GenericTripleExpression op1, GenericTripleExpression op2) {
        super(op1, op2);
    }

    protected <S, T extends Calculable<S>> S calc(S a, S b, T calculator) {
        return calculator.subtract(a, b);
    }
}

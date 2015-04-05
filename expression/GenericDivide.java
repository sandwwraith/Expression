package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericDivide extends GenericBinaryOperator implements GenericTripleExpression {
    public GenericDivide(GenericTripleExpression op1, GenericTripleExpression op2) {
        super(op1, op2);
    }

    protected <S, T extends Calculable<S>> S calc(S a, S b, T calculator) {
        return calculator.div(a, b);
    }

}

package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * <p>
 * ITMO University, 2015
 */
public class GenericMod extends GenericBinaryOperator implements GenericTripleExpression {
    public GenericMod(GenericTripleExpression num1, GenericTripleExpression num2) {
        super(num1, num2);
    }

    protected <S, T extends Calculator<S>> S calc(S a, S b, T calculator) {
        return calculator.mod(a, b);
    }
}

package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * <p>
 * ITMO University, 2015
 */
public class GenericSquare extends GenericUnaryOperator implements GenericTripleExpression {
    public GenericSquare(GenericTripleExpression num) {
        super(num);
    }

    protected <S, T extends Calculator<S>> S calc(S a, T calculator) {
        return calculator.square(a);
    }
}

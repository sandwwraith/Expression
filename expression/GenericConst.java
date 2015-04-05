package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericConst implements GenericTripleExpression {
    private final String value;

    public GenericConst(String val) {
        value = val;
    }

    public <S, T extends Calculator<S>> S evaluate(S x, S y, S z, T calculator) {
        return calculator.extractConst(value);
    }
}

package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface GenericTripleExpression {
    public <S, T extends Calculator<S>> S evaluate(S x, S y, S z, T calculator);
}

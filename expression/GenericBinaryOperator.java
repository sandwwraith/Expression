package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public abstract class GenericBinaryOperator implements GenericTripleExpression {
    private final GenericTripleExpression operand1;
    private final GenericTripleExpression operand2;

    protected GenericBinaryOperator(GenericTripleExpression op1, GenericTripleExpression op2) {
        operand1 = op1;
        operand2 = op2;
    }

    protected abstract <S, T extends Calculator<S>> S calc(S a, S b, T calculator);

    public <S, T extends Calculator<S>> S evaluate(S x, S y, S z, T calculator) {
        return calc(operand1.evaluate(x, y, z, calculator), operand2.evaluate(x, y, z, calculator), calculator);
    }

}

package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class GenericVariable implements GenericTripleExpression {
    private final String name;

    public GenericVariable(String t) {
        name = t;
    }

    public <S, T extends Calculable<S>> S evaluate(S x, S y, S z, T calculator) {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        } else {
            return z;
        }
    }
}

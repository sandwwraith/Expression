package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class Variable implements TripleExpression {
    private final String name;

    public Variable(String t) {
        name = t;
    }

    public int evaluate(int x, int y, int z) {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        }
        if (name.equals("z")) {
            return z;
        }
        return -1;
    }
}

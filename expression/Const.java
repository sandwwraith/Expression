package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class Const implements TripleExpression {
    private int value = 0;

    public Const(int a) {
        value = a;
    }

    public int evaluate(int x, int y, int z) {
        return value;
    }
}

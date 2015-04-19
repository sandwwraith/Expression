package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public abstract class BinaryOperator implements TripleExpression {
    private final TripleExpression operand1;
    private final TripleExpression operand2;

    protected BinaryOperator(TripleExpression op1, TripleExpression op2) {
        operand1 = op1;
        operand2 = op2;
    }

    protected int MultyCheck(int x, int y) {
        int z = x * y;
        if ((x == Integer.MIN_VALUE && y == -1) || ((y != 0 && z / y != x))) {
            throw new OverflowException();
        }
        return z;
    }

    protected int power(int a, int n) {
        int r = 1;
        while (n != 0) {
            if (n % 2 == 1) {
                //r *= a;
                r = MultyCheck(r, a);
                n -= 1;
            }
            n /= 2;
            if (n != 0)
            //a *= a;
            {
                a = MultyCheck(a, a);
            }
        }
        return r;
    }

    protected abstract int calc(int a, int b);

    public int evaluate(int x, int y, int z) {
        return calc(operand1.evaluate(x, y, z), operand2.evaluate(x, y, z));
    }

}
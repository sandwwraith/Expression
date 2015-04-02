package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedPower extends BinaryOperator {
    public CheckedPower(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

    /*private int MultyCheck(int x, int y) {
        int z = x * y;
        if ((x == Integer.MIN_VALUE && y == -1) || ((y != 0 && z / y != x))) {
            throw new OverflowException();
        }
        return z;
    }*/

    private int power(int a, int n) {
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

    protected int calc(int x, int y) {
        if (y < 0) {
            throw new EvaluateException("Negative natural power");
        }
        if (x == 0 && y == 0) {
            throw new EvaluateException("Undefined result of 0^0");
        }
        int r = power(x, y);
        // if (r<0) throw new OverflowException();
        return power(x, y);
    }
}

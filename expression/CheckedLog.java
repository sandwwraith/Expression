package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Март.2015
 */
public class CheckedLog extends BinaryOperator {
    public CheckedLog(TripleExpression op1, TripleExpression op2) {
        super(op1, op2);
    }

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

    private int log2Search(int base, int num) {
        int cur = 1;
        int curpow = 0;
        for (int i = 1; i<32;i++) {
            try {
                cur = MultyCheck(cur,base);
            } catch (OverflowException e) {
                return curpow;
            }
            if (cur > num) return curpow;
            if (cur == num) return i;
            curpow = i;
        }
        throw new EvaluateException("No log");
    }

    private int logSearch(int base, int num) {

        int prev = 0;
        boolean exc = false;
        for (int i = 0; i < 32; i++) {
            exc = false;
            int r = 0;
            try {
                r = power(base, i);
            } catch (OverflowException e) {
                exc = true;
            }
            if (num == r) {
                return i;
            }
            if (r > num || exc) {
                return prev;
            }
            prev = i;
        }
        throw new EvaluateException("No log");
    }

    protected int calc(int x, int y) {
        if (y <= 1) {
            throw new EvaluateException("Negative or equals 1 log base");
        }
        if (x <= 0) {
            throw new EvaluateException("Incorrect argument of log func");
        }
        return log2Search(y, x);
    }
}

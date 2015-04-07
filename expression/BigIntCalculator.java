package expression;

import java.math.BigInteger;

/**
 * Created by sandwwraith(@gmail.com)
 * on Апр..2015
 */
public class BigIntCalculator implements Calculator<BigInteger> {
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    public BigInteger mul(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    public BigInteger div(BigInteger a, BigInteger b) {
        if (b.signum() == 0) {
            throw new DivisionByZero();
        }
        return a.divide(b);
    }

    public BigInteger negate(BigInteger a) {
        return a.negate();
    }

    public BigInteger abs(BigInteger a) {
        return a.abs();
    }

    public BigInteger extractConst(String s) {
        BigInteger num;
        try {
            num = new BigInteger(s);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException(s);
        }
        return num;
    }
}

package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * <p>
 * ITMO University, 2015
 */
public class ByteCalculator implements Calculator<Byte> {
    @Override
    public Byte add(Byte a, Byte b) {
        return (byte) (a + b);
    }

    @Override
    public Byte subtract(Byte a, Byte b) {
        return (byte) (a - b);
    }

    @Override
    public Byte mul(Byte a, Byte b) {
        return (byte) (a * b);
    }

    @Override
    public Byte div(Byte a, Byte b) {
        if (b == 0) {
            throw new DivisionByZero();
        }
        return (byte) (a / b);
    }

    @Override
    public Byte negate(Byte a) {
        return (byte) -a;
    }

    @Override
    public Byte abs(Byte a) {
        return (a > 0) ? a : (byte) -a;
    }

    @Override
    public Byte mod(Byte a, Byte b) {
        if (b == 0) {
            throw new DivisionByZero();
        }
        return (byte) (a % b);
    }

    @Override
    public Byte extractConst(String s) {
        byte num;
        try {
            num = Byte.parseByte(s);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException(s);
        }
        return num;
    }
}


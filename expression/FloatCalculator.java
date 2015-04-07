package expression;


/**
 * Created by sandwwraith(@gmail.com)
 * <p>
 * ITMO University, 2015
 */
public class FloatCalculator implements Calculator<Float> {
    @Override
    public Float add(Float a, Float b) {
        return a + b;
    }

    @Override
    public Float subtract(Float a, Float b) {
        return a - b;
    }

    @Override
    public Float mul(Float a, Float b) {
        return a * b;
    }

    @Override
    public Float div(Float a, Float b) {
        return a / b;
    }

    @Override
    public Float negate(Float a) {
        return -a;
    }

    @Override
    public Float abs(Float a) {
        return a >= 0 ? a : -a;
    }

    @Override
    public Float mod(Float a, Float b) {
        return a % b;
    }

    @Override
    public Float extractConst(String s) {
        float num;
        try {
            num = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException(s);
        }
        return num;
    }
}


package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * <p>
 * ITMO University, 2015
 */
public class IntNoOverflowCalculator implements Calculator<Integer> {
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    public Integer mul(Integer a, Integer b) {
        return a * b;
    }

    public Integer div(Integer a, Integer b) {
        return a / b;
    }

    public Integer negate(Integer a) {
        return -a;
    }

    public Integer abs(Integer a) {
        return a > 0 ? a : -a;
    }

    public Integer mod(Integer a, Integer b) {
        return a % b;
    }


    public Integer extractConst(String s) {
        int num;
        try {
            num = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException(s);
        }
        return num;
    }
}

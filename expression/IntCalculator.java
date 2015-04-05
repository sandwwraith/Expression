package expression;

public class IntCalculator implements Calculator<Integer> {

    public Integer add(Integer x, Integer y) {
        if ((x > 0 && y > 0 && (Integer.MAX_VALUE - x < y)) || (x < 0 && y < 0 && (Integer.MIN_VALUE - x > y))) {
            throw new OverflowException();
        }
        return x + y;
    }

    public Integer subtract(Integer x, Integer y) {
        if ((x >= 0 && y < 0 && (Integer.MAX_VALUE + y < x)) || (x < 0 && y > 0 && (Integer.MIN_VALUE + y > x))) {
            throw new OverflowException();
        }
        return x - y;
    }

    public Integer mul(Integer x, Integer y) {
        int z = x * y;
        if ((x == Integer.MIN_VALUE && y == -1) || ((y != 0 && z / y != x))) {
            throw new OverflowException();
        }
        return z;
    }

    public Integer div(Integer x, Integer y) {
        if (y == 0) {
            throw new DivisionByZero();
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException();
        }
        return x / y;
    }

    public Integer negate(Integer x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
        return -x;
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

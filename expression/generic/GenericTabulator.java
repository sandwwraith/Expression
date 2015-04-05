package expression.generic;

import expression.*;

import java.math.BigInteger;

/**
 * Created by sandwwraith(@gmail.com)
 * on Апр..2015
 */
public class GenericTabulator implements Tabulator {
    private enum EvalType {Int, Double, BigInt}

    ;

    private EvalType parseEvalType(String s) {
        if (s.equals("i")) {
            return EvalType.Int;
        }
        if (s.equals("d")) {
            return EvalType.Double;
        }
        return EvalType.BigInt;
    }
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        EvalType calc = parseEvalType(mode);
        GenericTripleExpression exp = new GenericCheckedParser().parse(expression);

        IntCalculator intCalculator = new IntCalculator();
        DoubleCalculator doubleCalculator = new DoubleCalculator();
        BigIntCalculator bigIntCalculator = new BigIntCalculator();

        Object[][][] mass = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Object res = null;
                    try {
                        switch (calc) {
                            case Int:
                                res = exp.evaluate(x, y, z, intCalculator);
                                break;
                            case Double:
                                res = exp.evaluate((double) x, (double) y, (double) z, doubleCalculator);
                                break;
                            case BigInt:
                                res = exp.evaluate(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z), bigIntCalculator);
                                break;
                        }
                    } catch (EvaluateException e) {
                        res = null;
                    }
                    mass[x][y][z] = res;
                }
            }
        }

        return mass;
    }
}

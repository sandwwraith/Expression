package expression;

import static expression.Util.*;

/**
 * @author Georgiy Korneev
 */
public class ParserHardTest extends ParserEasyTest {
    protected ParserHardTest() {
        unary.add(op(" abs ", Math::abs));
        unary.add(op(" square ", a -> a * a));

        tests.addAll(list(
                op("abs -5", (x, y, z) -> 5L),
                op("abs (x - y)", (x, y, z) -> Math.abs(x - y)),
                op("abs -x", (x, y, z) -> Math.abs(-x)),
                op("abs(x+y)", (x, y, z) -> Math.abs(x + y)),
                op("square -5", (x, y, z) -> 25L),
                op("square (x - y)", (x, y, z) -> (x - y) * (x - y)),
                op("square -x", (x, y, z) -> x * x),
                op("square(x+y)", (x, y, z) -> (x + y) * (x + y))
        ));
    }

    public static void main(final String[] args) {
        checkAssert(ParserHardTest.class);
        new ParserHardTest().test();
    }
}

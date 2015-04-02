package expression;

import static expression.Util.*;

/**
 * @author Georgiy Korneev
 */
public class ParserEasyTest extends ParserTest {
    protected ParserEasyTest() {
        levels.add(0, list(
                op("<<", (a, b) -> (long) (a.intValue() << b.intValue())),
                op(">>", (a, b) -> (long) (a.intValue() >> b.intValue()))
        ));

        levels.get(2).add(op(" mod ", (a, b) -> b == 0 ? null : a % b));

        tests.addAll(list(
                op("1 + 5 mod 3", (x, y, z) -> 3L),
                op("x + y mod (z + 1)", (x, y, z) -> x + y % (z + 1)),
                op("1 << 5 + 3", (x, y, z) -> 256L),
                op("x + y << z", (x, y, z) -> x + y << z),
                op("x * y << z", (x, y, z) -> x * y << z),
                op("x << y << z", (x, y, z) -> x << y << z),
                op("1024 >> 5 + 3", (x, y, z) -> 4L),
                op("x + y >> z", (x, y, z) -> x + y >> z),
                op("x * y >> z", (x, y, z) -> x * y >> z),
                op("x >> y >> z", (x, y, z) -> x >> y >> z)
        ));
    }

    public static void main(final String[] args) {
        checkAssert(ParserEasyTest.class);
        new ParserEasyTest().test();
    }
}

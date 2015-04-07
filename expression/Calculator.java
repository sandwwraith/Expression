package expression;

/**
 * Created by sandwwraith(@gmail.com)
 * on Апр..2015
 */
public interface Calculator<T> {
    public T add(T a, T b);

    public T subtract(T a, T b);

    public T mul(T a, T b);

    public T div(T a, T b);

    public T negate(T a);

    public T abs(T a);

    public T mod(T a, T b);

    default public T square(T a) {
        return mul(a, a);
    }

    public T extractConst(String s);
}


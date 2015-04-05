package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser {
    GenericTripleExpression parse(String expression) throws Exception;
}

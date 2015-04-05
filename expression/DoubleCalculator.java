package expression;

public class DoubleCalculator implements Calculator<Double> {

    public Double add(Double a, Double b) {
        return a + b;
    }

    public Double subtract(Double a, Double b) {
        return a - b;
    }

    public Double mul(Double a, Double b) {
        return a * b;
    }

    public Double div(Double a, Double b) {
        return a / b;
    }

    public Double negate(Double a) {
        return -a;
    }

    public Double extractConst(String s) {
        double num;
        try {
            num = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException(s);
        }
        return num;
    }
}

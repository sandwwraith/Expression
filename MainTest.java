/**
 * Created by sandwwraith(@gmail.com)
 *	on Март.2015
 */

import expression.DoubleCalculator;
import expression.GenericCheckedParser;
import expression.GenericTripleExpression;
import expression.Parser;

public class MainTest {

    /*class myInt extends Double {
        public myInt(int a) {
            super(a);
        }
    }*/



       /* public  T add(T a, T b) {
            return a+b;
        }*/




    //GG
    public static void main(String[] args) {
        Parser parser = new GenericCheckedParser();
        GenericTripleExpression expression;
        try {
            expression = parser.parse("  3*  z  ");
            System.out.println(expression.evaluate(0.0, 0.1, 1.1, new DoubleCalculator()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        // System.out.println(Math.multiplyExact(-1,Long.MIN_VALUE));
        /*if (expression == null) {
            System.out.println("Error in parsing");
            return;
        }*/
    }
}

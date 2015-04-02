/**
 * Created by sandwwraith(@gmail.com)
 *	on Март.2015
 */

import expression.CheckedParser;
import expression.Parser;
import expression.TripleExpression;

public class MainTest {

    public static void main(String[] args) {
        Parser parser = new CheckedParser();
        TripleExpression expression;
        try {
            expression = parser.parse("  3*  z  ");
            System.out.println(expression.evaluate(0,0,1));
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

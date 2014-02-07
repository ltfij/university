package com.hjwylde.uni.swen221.lab01.calculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/*
 * Code for Laboratory 1, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The Calculator class provides a simple implementation of a calculator that supports 4 possible
 * arithmetic operations: addition ('+'), subtraction ('-'), multiplication ('*') and division
 * ('/'), along with parenthesis supported ('(' and ')').
 * 
 * @author Henry J. Wylde
 * @author SWEN 221
 */
public class Calculator {

    private String input_;

    // Current position within the input string
    private int index_;

    private static Map<String, BigDecimal> variables = new HashMap<>();

    /**
     * Initializes the Calculator with the given input. The input represents a mathematical
     * expression that is wanted to be evaluated.
     * 
     * @param input the mathematical expression wanting to be evaluated.
     */
    public Calculator(String input) {
        input_ = input;
        index_ = 0;
    }

    /**
     * Attempts to evaluates this Calculator's <code>input_</code> to a numeric value, otherwise if
     * it is unable to parse it, it will throw a RuntimeException. If there is no input then it
     * returns 0. If the expression is evaluating a set call, then this method returns the new value
     * of the variable.
     * 
     * @return the numeric evaluation of <code>input_</code>.
     * @throws RuntimeException if the <code>input_</code> is invalid and unable to be parsed.
     */
    @SuppressWarnings("null")
    public BigDecimal evaluate() throws RuntimeException {
        skipWhitespace();

        if (input_.length() == 0)
            return new BigDecimal(0);

        BigDecimal value = null;

        char lookahead = input_.charAt(index_);
        if (lookahead == '(')
            value = evaluateBracketed();
        else if (Character.isDigit(lookahead) || (lookahead == '-'))
            value = readNumber();
        else if ((input_.length() > (index_ + 3)) && input_.substring(index_).startsWith("set ")) {
            match("set"); // We're setting a variable.
            skipWhitespace();

            String variable = readString(); // Next token is the name of the variable.
            Calculator.setVariable(variable, evaluate()); // Set the variable to the evaluated
                                                          // value.

            return Calculator.getVariable(variable); // Return the value of the variable.
        } else if (Character.isLetter(lookahead)) {
            String variable = readString();
            value = Calculator.getVariable(variable);
        } else
            error();

        if (value == null)
            error();

        skipWhitespace();

        if (index_ < input_.length()) {
            lookahead = input_.charAt(index_);

            /*
             * FIXME: BEDMAS problem This problem is rather big... Causes 4 * 2 + 2 to return 16
             * rather than 10. It can't easily be fixed (to my knowledge) with how the calculator
             * has been implemented so far. It would require a bit of restructuring of all the
             * methods and classes - so for now I have not fixed it.
             */
            if (lookahead == '+') {
                match("+");
                value = value.add(evaluate());
            } else if (lookahead == '*') {
                match("*");
                value = value.multiply(evaluate());
            } else if (lookahead == '/') {
                match("/");
                BigDecimal divisor = evaluate();

                // See JavaDoc for java.lang.BigDecimal for more information on why we need to use
                // the scale
                // and rounding mode here.
                value = value.divide(divisor, 10, BigDecimal.ROUND_HALF_UP);
            } else if (lookahead == '-') {
                match("-");
                value = value.subtract(evaluate());
            } else if (lookahead == ')') {
            } else
                error();
        }

        return value;
    }

    /**
     * Throws a RuntimeException with details about which character in the input being evaluated
     * could not be parsed.
     * 
     * @throws RuntimeException always with details about what character could not be parsed in
     *         <code>input_</code>.
     */
    private void error() throws RuntimeException {
        throw new RuntimeException("Cannot parse character '" + input_.charAt(index_)
                + "' at position " + index_ + " of input '" + input_ + "'\n");
    }

    /**
     * Attempts to evaluate the mathematical expression inside parenthesis by recursion.
     * 
     * @return the evaluated value of the expression inside the parenthesis.
     * @throws RuntimeException if the <code>input_</code> is invalid and unable to be parsed.
     */
    private BigDecimal evaluateBracketed() throws RuntimeException {
        match("(");
        BigDecimal value = evaluate();
        match(")");

        return value;
    }

    /**
     * Attempts to match a string of characters against the current position in the input (after
     * clearing whtiespace) and advances the index to skip it.
     * 
     * @param text the text to match the input against.
     * @throws RuntimeException if the <code>input_</code> is invalid and unable to be parsed.
     */
    private void match(String text) throws RuntimeException {
        skipWhitespace();

        if (input_.startsWith(text, index_))
            index_ += text.length();
        else
            error();
    }

    /**
     * Parses the next number in the string found and advances the index. The number is parsed only
     * while the characters are digits or is a decimal point.
     * 
     * @return the number parsed.
     */
    private BigDecimal readNumber() {
        int start = index_;

        if (input_.charAt(index_) == '-')
            index_++;

        while ((index_ < input_.length())
                && (Character.isDigit(input_.charAt(index_)) || (input_.charAt(index_) == '.')))
            index_++;

        String number = input_.substring(start, index_);
        if (number.indexOf(".") != number.lastIndexOf(".")) {
            index_ = number.lastIndexOf(".");
            error();
        }

        return new BigDecimal(number);
    }

    /**
     * Parses the next string in the string and advances the index. The string is parsed only while
     * the characters are letters.
     * 
     * @return the string parsed.
     */
    private String readString() {
        int start = index_;

        while ((index_ < input_.length()) && (Character.isLetter(input_.charAt(index_))))
            index_++;

        return (input_.substring(start, index_));
    }

    /**
     * Advances the index until it reaches a character that is not whitespace.
     */
    private void skipWhitespace() {
        while ((index_ < input_.length()) && Character.isWhitespace(input_.charAt(index_)))
            index_++;
    }

    /**
     * Returns the memory variable that has the name <code>name</code>. If this variable isn't set,
     * returns 0. This is so that it is similar to most hand calculators.
     * 
     * @param name the name of the variable.
     * @return the value of the variable, or 0 if it doesn't exist.
     */
    private static BigDecimal getVariable(String name) {
        if (Calculator.variables.containsKey(name))
            return Calculator.variables.get(name);

        return new BigDecimal(0);
    }

    /**
     * Resets all memory variables, effectively setting them to 0.
     */
    @SuppressWarnings("unused")
    private static void resetVariables() {
        Calculator.variables = new HashMap<String, BigDecimal>();
    }

    /**
     * Sets the specified variable to the input value. Overrides it if the variable already exists.
     * 
     * @param name the name of the variable to set.
     * @param value the new value of the variable.
     */
    private static void setVariable(String name, BigDecimal value) {
        Calculator.variables.put(name, value);
    }
}

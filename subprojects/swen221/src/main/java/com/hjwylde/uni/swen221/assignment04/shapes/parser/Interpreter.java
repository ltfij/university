package com.hjwylde.uni.swen221.assignment04.shapes.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.hjwylde.uni.swen221.assignment04.shapes.Canvas;
import com.hjwylde.uni.swen221.assignment04.shapes.Color;
import com.hjwylde.uni.swen221.assignment04.shapes.geometry.ComplexShape;
import com.hjwylde.uni.swen221.assignment04.shapes.geometry.Rectangle;
import com.hjwylde.uni.swen221.assignment04.shapes.geometry.Shape;

/*
 * Code for Assignment 4, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * The Interpreter class interprets some commands for creating and drawing or filling shapes to a
 * canvas object. It also provides methods for drawing or filling in a generic Shape object with a
 * line scanning algorithm.
 * 
 * The grammar used for shape parsing is:
 * 
 * <pre>
 * CANVAS ::= COMMAND*
 * 
 * COMMAND ::= "draw" EXPRESSION COLOR 
 *           | "fill" EXPRESSION COLOR 
 *           | VARIABLE "=" EXPRESSION
 * 
 * EXPRESSION ::= SHAPE [(+|-|&) SHAPE]*
 * 
 * SHAPE ::= "(" EXPRESSION ")" 
 *         | VARIABLE 
 *         | RECTANGLE
 * 
 * RECTANGLE ::= "[" INT "," INT "," POSITIVE_INT "," POSITIVE_INT "]"
 * 
 * VARIABLE ::= ^[a-zA-Z_][\w]*$
 * 
 * COLOR ::= "#" [0-9a-fA-F]{6}
 * 
 * INT ::= [-] POSITIVE_INT
 * 
 * POSITIVE_INT ::= [0-9]+
 * </pre>
 * 
 * @author Henry J. Wylde, djp
 * 
 * @see Shape
 */
public class Interpreter {

    private ParserScanner scan;

    /**
     * Creates a new interpreter for the given input.
     * 
     * @param input the input.
     */
    public Interpreter(String input) {
        scan = new ParserScanner(input);
    }

    /**
     * This method should return a canvas to which the input commands have been applied. If the
     * grammar is incorrect at all, a <code>ParseException</code> will be thrown (extends
     * <code>InvalidArgumentException</code>).
     * 
     * @return a canvas that shows the result of the input.
     */
    public Canvas run() {
        scan.reset();

        return parseCanvas();
    }

    /**
     * Parses the next CANVAS non terminal from the input and creates a canvas object from it. A
     * canvas is given by a blank canvas with some operations performed onto it.
     * 
     * Grammar:
     * 
     * <pre>
     * CANVAS ::= COMMAND*
     * </pre>
     * 
     * @return the canvas with operations performed on it.
     */
    private Canvas parseCanvas() {
        Canvas canvas = new Canvas();
        Map<String, Shape> variables = new HashMap<>();

        while (scan.hasNext())
            parseCommand(canvas, variables);

        return canvas;
    }

    /**
     * Parses the next color from the input. The color object is expected to be in hexadecimal
     * formed, eg. "#000000". The first 2 hexadecimal digits represent the red component, the next 2
     * the green component and the final 2 the blue component of the color.
     * 
     * Grammar:
     * 
     * <pre>
     * COLOR ::= "#" [0-9a-fA-F]{6}
     * </pre>
     * 
     * @return a color object.
     */
    private Color parseColor() {
        scan.clearWhitespace();

        return new Color((String) scan.next(7));
    }

    /**
     * Parses the next COMMAND non terminal from the input and will run it.
     * 
     * Grammar:
     * 
     * <pre>
     * COMMAND ::= "draw" EXPRESSION COLOR 
     *           | "fill" EXPRESSION COLOR 
     *           | VARIABLE "=" EXPRESSION
     * </pre>
     * 
     * @param canvas the canvas to perform the command onto.
     * @param variables the map of variables consistent between all commands for this canvas.
     */
    private void parseCommand(Canvas canvas, Map<String, Shape> variables) {
        scan.clearWhitespace();
        if (!scan.hasNext())
            return;

        String cmd = parseVariable(); // The grammar expects a word at least next.
        if (cmd.equalsIgnoreCase("draw")) // If it matches the reserved "draw" keyword...
            Interpreter.drawShape(canvas, parseExpression(variables), parseColor());
        else if (cmd.equalsIgnoreCase("fill")) // If it matches the reserved "fill" keyword...
            Interpreter.fillShape(canvas, parseExpression(variables), parseColor());
        else { // It is a variable name.
            scan.expect("=");
            variables.put(cmd, parseExpression(variables));
        }
    }

    /**
     * Parses the next EXPRESSION non terminal from the input and returns a <code>Shape</code> after
     * evaluating the expression. An expression consists of one or more SHAPE non terminals
     * separated by an operator.
     * 
     * Grammar:
     * 
     * <pre>
     * EXPRESSION ::= SHAPE [(+|-|&) SHAPE]*
     * </pre>
     * 
     * @param variables the map of variables consistent between all commands for this canvas.
     * @return the Shape of the evaluated expression.
     */
    private Shape parseExpression(Map<String, Shape> variables) {
        Shape shape = parseShape(variables);

        scan.clearWhitespace();

        /*
         * The way this loop has been written performs a left binding, equal operator precedence
         * evaluation of the expression.
         */

        EXPRESSION_LOOP: while (scan.hasNext())
            // While there is a next token...
            switch (scan.peek()) { // Lookahead 1.
                case '+': // Union operator.
                    scan.expect("+");

                    shape = new ComplexShape(shape);
                    ((ComplexShape) shape).union(parseShape(variables));

                    break;
                case '-': // Difference operator.
                    scan.expect("-");

                    shape = new ComplexShape(shape);
                    ((ComplexShape) shape).difference(parseShape(variables));

                    break;
                case '&': // Intersection operator.
                    scan.expect("&");

                    shape = new ComplexShape(shape);
                    ((ComplexShape) shape).intersect(parseShape(variables));

                    break;
                default: // Not an operator, break out of the loop because this has a different non
                         // terminal
                         // following the expression.
                    break EXPRESSION_LOOP;
            }

        return shape;
    }

    /**
     * Parses the next RECTANGLE non terminal from the input. It then returns a new
     * <code>Rectangle</code> object created from the input.
     * 
     * Grammar:
     * 
     * <pre>
     * RECTANGLE ::= "[" INT "," INT "," POSITIVE_INT "," POSITIVE_INT "]"
     * </pre>
     * 
     * @return the Rectangle.
     */
    private Rectangle parseRectangle() {
        int x, y, width, height;

        scan.expect("[");
        x = scan.nextInt();
        scan.expect(",");
        y = scan.nextInt();
        scan.expect(",");
        width = scan.nextInt();
        scan.expect(",");
        height = scan.nextInt();
        scan.expect("]");

        // Width and height have to be a natural number.
        if ((width < 0) || (height < 0))
            throw new ParseException(
                    "Expected a non-negative integer for the width and height of the rectangle.");

        return new Rectangle(x, y, width, height);
    }

    /**
     * Parses the next SHAPE non terminal from the input and returns the evaluated
     * <code>Shape</code> from it. A shape can be either a bracketed expression, a variable or an
     * actual shape object (currently the only implemented shape object is a rectangle).
     * 
     * Grammar:
     * 
     * <pre>
     * SHAPE ::= "(" EXPRESSION ")" 
     *         | VARIABLE 
     *         | RECTANGLE
     * </pre>
     * 
     * @param variables the map of variables consistent between all commands for this canvas.
     * @return the evaluated Shape.
     */
    private Shape parseShape(Map<String, Shape> variables) {
        scan.clearWhitespace();

        switch (scan.peek()) { // Look ahead 1.
            case '(': // Bracketed expression.
                scan.expect("(");
                Shape shape = parseExpression(variables);
                scan.expect(")");

                return shape;
            case '[': // Rectangle.
                return parseRectangle();
            default: // Variable
                String var = parseVariable();
                if (!variables.containsKey(var))
                    throw new ParseException("Attempted use of undeclared variable: " + var + ".");

                return variables.get(var);
        }
    }

    /**
     * Parses the next VARIABLE non terminal from the input.
     * 
     * Grammar:
     * 
     * <pre>
     * VARIABLE ::= ^[a-zA-Z_][\w]*$
     * </pre>
     * 
     * @return the string representation of the variable.
     */
    private String parseVariable() {
        scan.clearWhitespace();

        // First letter differs from rest of allowed letters.
        if (!Pattern.matches("[a-zA-Z_]", scan.peek() + ""))
            throw new ParseException(
                    "Expected a letter or underscore for the variable name to begin with.");

        StringBuilder sb = new StringBuilder();
        sb.append(scan.next());

        Pattern word = Pattern.compile("[\\w]");

        // Keep appending until we reach a character that doesn't match the regex.
        while (scan.hasNext() && word.matcher(scan.peek() + "").matches())
            sb.append(scan.next());

        return sb.toString();
    }

    /**
     * Draws the given shape to the specified canvas with the specified color. Drawing a shape
     * involves only coloring in the outline of the shape, not filling it in.
     * 
     * @param canvas the canvas to draw onto.
     * @param shape the shape to outline.
     * @param color the color.
     */
    private static void drawShape(Canvas canvas, Shape shape, Color color) {
        Rectangle bounds = shape.boundingBox();

        if ((bounds == null) || bounds.isEmpty())
            return;

        for (int x = bounds.getX(); x < (bounds.getX() + bounds.getWidth()); x++)
            for (int y = bounds.getY(); y < (bounds.getY() + bounds.getHeight()); y++)
                // Draw the point only if it it at an edge.
                if (shape.contains(x, y))
                    if (!shape.contains(x - 1, y) || !shape.contains(x + 1, y)
                            || !shape.contains(x, y - 1) || !shape.contains(x, y + 1))
                        canvas.draw(x, y, color);
    }

    /**
     * Fills the given shape to the specified canvas with the specified color. Filling a shape
     * involves coloring in the all the points that the shape contains.
     * 
     * @param canvas the canvas to render to.
     * @param shape the shape to fill.
     * @param color the color.
     */
    private static void fillShape(Canvas canvas, Shape shape, Color color) {
        Rectangle bounds = shape.boundingBox();

        if ((bounds == null) || bounds.isEmpty())
            return;

        for (int x = bounds.getX(); x < (bounds.getX() + bounds.getWidth()); x++)
            for (int y = bounds.getY(); y < (bounds.getY() + bounds.getHeight()); y++)
                // Fill the point if the shape contains it.
                if (shape.contains(x, y))
                    canvas.draw(x, y, color);
    }
}

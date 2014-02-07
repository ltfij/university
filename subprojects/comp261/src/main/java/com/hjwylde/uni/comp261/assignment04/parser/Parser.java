package com.hjwylde.uni.comp261.assignment04.parser;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.vecmath.Vector3d;

import com.hjwylde.uni.comp261.assignment04.graphics.lights.PointLight3d;
import com.hjwylde.uni.comp261.assignment04.parser.nodes.LightSourceNode;
import com.hjwylde.uni.comp261.assignment04.parser.nodes.Polytope2dNode;
import com.hjwylde.uni.comp261.assignment04.parser.nodes.Polytope3dNode;

/*
 * Code for Assignment 4, COMP 261 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

/**
 * A parser to parse specific object files in the format of the Comp261 assignment objects.
 * 
 * Grammar:
 * 
 * <pre>
 * POLYTOPE3D ::= LIGHT_SOURCE POLYTOPE2D+
 * 
 * LIGHT_SOURCE ::= VECTOR3D
 * 
 * POLYTOPE2D ::= VECTOR3D VECTOR3D VECTOR3D COLOR
 * 
 * VECTOR3D ::= DOUBLE DOUBLE DOUBLE
 * 
 * COLOR ::= INTEGER(0-255) INTEGER(0-255) INTEGER(0-255)
 * 
 * DOUBLE ::= [-][0-9]+[.][0-9]+
 * 
 * INTEGER(0-255) ::= [0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5]
 * </pre>
 * 
 * @author Henry J. Wylde
 */
public class Parser {

    /**
     * Parse the given <code>CharSequence</code>. Returns a new <code>Polytope3dNode</code> from the
     * given <code>CharSequence</code>.
     * 
     * @param cs the CharSequence to parse.
     * @return a new Polytope3dNode
     * @throws ParseException if the CharSequence doesn't match the expected grammar.
     */
    public static Polytope3dNode parse(CharSequence cs) throws ParseException {
        return Parser.parsePolytope3d(new ParserScanner(cs));
    }

    /**
     * Parse the given <code>File</code>. Returns a new <code>Polytope3dNode</code> from the given
     * <code>File</code>.
     * 
     * @param f the File to parse.
     * @return a new Polytope3dNode
     * @throws ParseException if the File doesn't match the expected grammar.
     */
    public static Polytope3dNode parse(File f) throws IOException, ParseException {
        String str = new String();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null)
                str += line + " ";
        }

        return Parser.parse(str);
    }

    /**
     * Parse a <i>COLOR</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new Color from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the <i>COLOR</i>
     *         grammar.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.parser.ParserScanner
     */
    private static Color parseColor(ParserScanner s) throws ParseException {
        s.clearWhitespace();

        if (!s.hasNextInt())
            throw new ParseException("Expected an integer.");

        int r = s.nextInt();
        s.clearWhitespace();
        int g = s.nextInt();
        s.clearWhitespace();
        int b = s.nextInt();
        s.clearWhitespace();

        if ((r < 0) || (r > 255))
            throw new ParseException("Expected an integer within the range of 0-255. Received \""
                    + r + "\".");
        if ((g < 0) || (g > 255))
            throw new ParseException("Expected an integer within the range of 0-255. Received \""
                    + g + "\".");
        if ((b < 0) || (b > 255))
            throw new ParseException("Expected an integer within the range of 0-255. Received \""
                    + b + "\".");

        return new Color(r, g, b);
    }

    /**
     * Parse a <i>LIGHT_SOURCE</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new LightSourceNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>LIGHT_SOURCE</i> grammar.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.parser.nodes.LightSourceNode
     */
    private static LightSourceNode parseLightSource(ParserScanner s) throws ParseException {
        s.clearWhitespace();

        return new LightSourceNode(new PointLight3d(Parser.parseVector3d(s)));
    }

    /**
     * Parse a <i>POLYTOPE2D</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new Polytope2dNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>POLYTOPE2D</i> grammar.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.parser.nodes.Polytope2dNode
     */
    private static Polytope2dNode parsePolytope2d(ParserScanner s) throws ParseException {
        s.clearWhitespace();

        Polytope2dNode poly = new Polytope2dNode();

        /*
         * At minimum a Polytope2d must have 3 vectors, then it may have as many more as wanted.
         */
        poly.addVector3d(Parser.parseVector3d(s));
        s.clearWhitespace();
        poly.addVector3d(Parser.parseVector3d(s));
        s.clearWhitespace();
        poly.addVector3d(Parser.parseVector3d(s));
        s.clearWhitespace();
        while (s.hasNextDouble() && !s.hasNextInt()) {
            poly.addVector3d(Parser.parseVector3d(s));
            s.clearWhitespace();
        }
        poly.setColor(Parser.parseColor(s));
        s.clearWhitespace();

        return poly;
    }

    /**
     * Parse a <i>POLYTOPE3d</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new Polytope3dNode from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the
     *         <i>POLYTOPE3d</i> grammar.
     * 
     * @see com.hjwylde.uni.comp261.assignment04.parser.nodes.Polytope3dNode
     */
    private static Polytope3dNode parsePolytope3d(ParserScanner s) throws ParseException {
        s.clearWhitespace();

        Polytope3dNode poly = new Polytope3dNode();

        poly.setLight(Parser.parseLightSource(s));
        s.clearWhitespace();

        do {
            poly.addPolytope2d(Parser.parsePolytope2d(s));
            s.clearWhitespace();
        } while (s.hasNext());

        return poly;
    }

    /**
     * Parse a <i>VECTOR3D</i> from the <code>ParserScanner</code>.
     * 
     * @param s the ParserScanner.
     * @return a new Vector3d from the given ParserScanner.
     * @throws ParseException if the ParserScanner's next values does not match the <i>VECTOR3D</i>
     *         grammar.
     */
    private static Vector3d parseVector3d(ParserScanner s) throws ParseException {
        s.clearWhitespace();

        if (!s.hasNextDouble())
            throw new ParseException("Expected a double.");

        double d1 = s.nextDouble();
        s.clearWhitespace();
        double d2 = s.nextDouble();
        s.clearWhitespace();
        double d3 = s.nextDouble();
        s.clearWhitespace();

        return new Vector3d(d1, d2, d3);
    }
}

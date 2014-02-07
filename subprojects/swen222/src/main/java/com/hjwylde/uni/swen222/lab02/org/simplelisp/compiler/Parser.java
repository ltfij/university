// This file is part of the Simple Lisp Interpreter.
//
// The Simple Lisp Interpreter is free software; you can
// redistribute it and/or modify it under the terms of the
// GNU General Public License as published by the Free Software
// Foundation; either version 2 of the License, or (at your
// option) any later version.
//
// The Simpular Program Interpreter is distributed in the hope
// that it will be useful, but WITHOUT ANY WARRANTY; without
// even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License
// for more details.
//
// You should have received a copy of the GNU General Public
// License along with the Simpular Program Interpreter; if not,
// write to the Free Software Foundation, Inc., 59 Temple Place,
// Suite 330, Boston, MA 02111-1307 USA
//
// (C) David James Pearce, 2005.

// ========================================================
// COMP205: YOU DO NOT NEED TO MODIFY THIS CLASS VERY MUCH!
// ========================================================

package com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler;

import java.util.List;

import com.hjwylde.uni.swen222.lab02.org.simplelisp.error.ParseException;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.*;

public class Parser {

    private final List<Lexer.Token> tokens;
    private int pos;

    private Parser(String text) {
        tokens = Lexer.tokenise(text, false);
        pos = 0;
    }

    public LispExpr parse() throws ParseException {
        LispExpr t;
        LispList r = new LispList();
        r.add(new LispSymbol("progn"));

        while ((t = parseExpr()) != null)
            r.add(t);

        return r;
    }

    /**
     * Expr ::= list | integer | boolean | string | identifier
     */
    private LispExpr parseExpr() throws ParseException {
        if (pos >= tokens.size())
            return null;

        Lexer.Token lookahead = tokens.get(pos);
        if (lookahead instanceof Lexer.Quote) {
            ++pos;
            LispExpr l = new LispQuote(parseExpr());
            return l;
        } else if (lookahead instanceof Lexer.LeftBrace)
            return parseList();
        else if (lookahead instanceof Lexer.Strung)
            return parseString();
        else if (lookahead instanceof Lexer.Integer) {
            ++pos;
            return new LispInteger(Integer.parseInt(lookahead.toString()));
        } else if (lookahead instanceof Lexer.Char) {
            ++pos;
            String c = lookahead.toString();
            if (c.length() > 3)
                // special character form
                if (c.equals("#\\Newline"))
                    return new LispChar('\n');
                else if (c.equals("#\\Tab"))
                    return new LispChar('\t');
                else
                    throw new ParseException("Unrecognised character: " + lookahead,
                            lookahead.getLine(), lookahead.getColumn());

            return new LispChar(c.charAt(2));
        } else if (lookahead instanceof Lexer.Identifier) {
            // catch the special forms
            ++pos;
            String sym = lookahead.toString();
            if (sym.equals("nil"))
                return LispNil.INSTANCE;

            return new LispSymbol(sym);
        } else
            throw new ParseException("Unrecognised token: " + lookahead, lookahead.getLine(),
                    lookahead.getColumn());
    }

    private LispList parseList() throws ParseException {
        // list ::= '(' Expr* ')' | nil
        Lexer.Token tok = tokens.get(pos++);

        if (pos >= tokens.size())
            throw new ParseException("End of file after '('", tok.getLine(), tok.getColumn());

        LispList l = new LispList();

        while ((pos < tokens.size()) && !(tokens.get(pos) instanceof Lexer.RightBrace))
            l.add(parseExpr());

        if (pos >= tokens.size())
            throw new ParseException("Missing ')'", tok.getLine(), tok.getColumn());

        ++pos;

        return l;
    }

    private LispString parseString() throws ParseException {
        // a string consists of any sequence of characters
        // terminated by a '"'.
        StringBuffer buf = new StringBuffer();
        boolean escaped = false;
        Lexer.Token tok = tokens.get(pos++);
        String str = tok.toString();
        int p = 1;
        while ((p < (str.length() - 1)) || escaped) {
            // deal with escape codes here
            char c = str.charAt(p);
            if (escaped) {
                if (c == 'n')
                    // new line
                    buf.append('\n');
                else if (c == 't')
                    // tab
                    buf.append('\t');
                else if (c == '"')
                    // inverted comma
                    buf.append('\"');
                else if (c == '\\')
                    // back slash
                    buf.append('\\');
                else if (c == 'r')
                    // carriage return
                    buf.append('\r');
                else
                    // don't recognise this escape character
                    throw new ParseException("Invalid escape sequence: " + c, tok.getLine(),
                            tok.getColumn());
                escaped = false;
                ++p;
            } else {
                if (c == '\\')
                    escaped = true;
                else
                    buf.append(c);
                ++p;
            }
        }
        return new LispString(buf.toString());
    }

    public static LispExpr parse(String s) throws ParseException {
        return new Parser(s).parse();
    }
}

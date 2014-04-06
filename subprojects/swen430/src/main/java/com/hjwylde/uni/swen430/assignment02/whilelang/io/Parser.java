// This file is part of the WhileLang Compiler (wlc).
//
// The WhileLang Compiler is free software; you can redistribute
// it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
//
// The WhileLang Compiler is distributed in the hope that it
// will be useful, but WITHOUT ANY WARRANTY; without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE. See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public
// License along with the WhileLang Compiler. If not, see
// <http://www.gnu.org/licenses/>
//
// Copyright 2013, David James Pearce.

package com.hjwylde.uni.swen430.assignment02.whilelang.io;

import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Bar;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Dot;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Equals;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.EqualsEquals;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.GreaterEquals;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Identifier;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Int;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Keyword;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LeftAngle;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LeftBrace;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LeftCurly;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LeftSquare;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LessEquals;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LogicalAnd;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.LogicalOr;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Minus;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.NotEquals;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Percent;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Plus;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.PlusPlus;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Real;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.RightAngle;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.RightBrace;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.RightCurly;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.RightSlash;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.RightSquare;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.SemiColon;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Shreak;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Star;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Strung;
import com.hjwylde.uni.swen430.assignment02.whilelang.io.Lexer.Token;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.Expr;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.Stmt;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.Type;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.WhileFile;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.WhileFile.ConstDecl;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.WhileFile.Decl;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.WhileFile.FunDecl;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.WhileFile.Parameter;
import com.hjwylde.uni.swen430.assignment02.whilelang.lang.WhileFile.TypeDecl;
import com.hjwylde.uni.swen430.assignment02.whilelang.util.Attribute;
import com.hjwylde.uni.swen430.assignment02.whilelang.util.Pair;
import com.hjwylde.uni.swen430.assignment02.whilelang.util.SyntaxError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Parser {

    private String filename;
    private ArrayList<Token> tokens;
    private HashSet<String> userDefinedTypes;
    private Map<String, Expr> constants;
    private int index;

    public Parser(String filename, List<Token> tokens) {
        this.filename = filename;
        this.tokens = new ArrayList<Token>(tokens);
        this.userDefinedTypes = new HashSet<String>();
    }

    public WhileFile read() {
        ArrayList<Decl> decls = new ArrayList<Decl>();
        constants = new HashMap<String, Expr>();

        while (index < tokens.size()) {
            Token t = tokens.get(index);
            if (t instanceof Keyword) {
                Keyword k = (Keyword) t;
                if (t.text.equals("type")) {
                    decls.add(parseTypeDeclaration());
                } else if (t.text.equals("const")) {
                    ConstDecl decl = parseConstantDeclaration();
                    decls.add(decl);
                    constants.put(decl.name, decl.constant);
                } else {
                    decls.add(parseFunction());
                }
            } else {
                decls.add(parseFunction());
            }
        }

        return new WhileFile(filename, decls);
    }

    private void checkNotEof() {
        if (index >= tokens.size()) {
            throw new SyntaxError("unexpected end-of-file", filename, index - 1, index - 1);
        }
        return;
    }

    private boolean isType(int index) {
        if (index >= tokens.size()) {
            return false;
        }
        Token lookahead = tokens.get(index);
        if (lookahead instanceof Keyword) {
            return lookahead.text.equals("null") || lookahead.text.equals("bool") || lookahead.text
                    .equals("int") || lookahead.text.equals("real") || lookahead.text.equals("char")
                    || lookahead.text.equals("string");
        } else if (lookahead instanceof Identifier) {
            Identifier id = (Identifier) lookahead;
            return userDefinedTypes.contains(id.text);
        } else if (lookahead instanceof LeftCurly) {
            return isType(index + 1);
        } else if (lookahead instanceof LeftSquare) {
            return isType(index + 1);
        }

        return false;
    }

    private Token match(String op) {
        checkNotEof();
        Token t = tokens.get(index);
        if (!t.text.equals(op)) {
            syntaxError("expecting '" + op + "', found '" + t.text + "'", t);
        }
        index = index + 1;
        return t;
    }

    @SuppressWarnings("unchecked")
    private <T extends Token> T match(Class<T> c, String name) {
        checkNotEof();
        Token t = tokens.get(index);
        if (!c.isInstance(t)) {
            syntaxError("expecting " + name + ", found '" + t.text + "'", t);
        }
        index = index + 1;
        return (T) t;
    }

    private Identifier matchIdentifier() {
        checkNotEof();
        Token t = tokens.get(index);
        if (t instanceof Identifier) {
            Identifier i = (Identifier) t;
            index = index + 1;
            return i;
        }
        syntaxError("identifier expected", t);
        return null; // unreachable.
    }

    private Keyword matchKeyword(String keyword) {
        checkNotEof();
        Token t = tokens.get(index);
        if (t instanceof Keyword) {
            if (t.text.equals(keyword)) {
                index = index + 1;
                return (Keyword) t;
            }
        }
        syntaxError("keyword " + keyword + " expected.", t);
        return null;
    }

    private Expr parseAddSubExpression() {
        int start = index;
        Expr lhs = parseMulDivExpression();

        if (index < tokens.size() && tokens.get(index) instanceof Plus) {
            match("+");
            Expr rhs = parseAddSubExpression();
            return new Expr.Binary(Expr.BOp.ADD, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof Minus) {
            match("-");
            Expr rhs = parseAddSubExpression();
            return new Expr.Binary(Expr.BOp.SUB, lhs, rhs, sourceAttr(start, index - 1));
        }

        return lhs;
    }

    private Expr parseAppendExpression() {
        int start = index;
        Expr lhs = parseAddSubExpression();

        if (index < tokens.size() && tokens.get(index) instanceof PlusPlus) {
            match("++");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.APPEND, lhs, rhs, sourceAttr(start, index - 1));
        }

        return lhs;
    }

    private Stmt parseAssign() {
        // standard assignment
        int start = index;
        Expr lhs = parseCondition();
        if (!(lhs instanceof Expr.LVal)) {
            syntaxError("expecting lval, found " + lhs + ".", lhs);
        }
        match("=");
        Expr rhs = parseCondition();
        int end = index;
        return new Stmt.Assign((Expr.LVal) lhs, rhs, sourceAttr(start, end - 1));
    }

    private Type parseBaseType() {
        checkNotEof();
        int start = index;
        Token token = tokens.get(index);
        Type t;

        if (token.text.equals("null")) {
            matchKeyword("null");
            t = new Type.Null(sourceAttr(start, index - 1));
        } else if (token.text.equals("int")) {
            matchKeyword("int");
            t = new Type.Int(sourceAttr(start, index - 1));
        } else if (token.text.equals("real")) {
            matchKeyword("real");
            t = new Type.Real(sourceAttr(start, index - 1));
        } else if (token.text.equals("void")) {
            matchKeyword("void");
            t = new Type.Void(sourceAttr(start, index - 1));
        } else if (token.text.equals("bool")) {
            matchKeyword("bool");
            t = new Type.Bool(sourceAttr(start, index - 1));
        } else if (token.text.equals("char")) {
            matchKeyword("char");
            t = new Type.Char(sourceAttr(start, index - 1));
        } else if (token.text.equals("string")) {
            matchKeyword("string");
            t = new Type.Strung(sourceAttr(start, index - 1));
        } else if (token instanceof LeftCurly) {
            // record type
            match("{");
            HashMap<String, Type> types = new HashMap<String, Type>();
            token = tokens.get(index);
            boolean firstTime = true;
            while (!(token instanceof RightCurly)) {
                if (!firstTime) {
                    match(",");
                }
                firstTime = false;

                checkNotEof();
                token = tokens.get(index);
                Type tmp = parseType();

                Identifier n = matchIdentifier();

                if (types.containsKey(n.text)) {
                    syntaxError("duplicate tuple key", n);
                }
                types.put(n.text, tmp);
                checkNotEof();
                token = tokens.get(index);
            }
            match("}");
            t = new Type.Record(types, sourceAttr(start, index - 1));
        } else if (token instanceof LeftSquare) {
            match("[");
            t = parseType();
            match("]");
            t = new Type.List(t, sourceAttr(start, index - 1));
        } else {
            Identifier id = matchIdentifier();
            t = new Type.Named(id.text, sourceAttr(start, index - 1));
        }

        return t;
    }

    private List<Stmt> parseBlock() {
        match("{");

        ArrayList<Stmt> stmts = new ArrayList<Stmt>();
        while (index < tokens.size() && !(tokens.get(index) instanceof RightCurly)) {
            stmts.add(parseStatement(true));
        }

        match("}");

        return stmts;
    }

    /**
     * Parses a {@code break} statement.
     * <p/>
     * The break statement does not support named or labelled breaks.
     *
     * @return the break statement.
     */
    private Stmt.Break parseBreak() {
        int start = index;

        matchKeyword("break");
        match(";");

        return new Stmt.Break(sourceAttr(start, index - 1));
    }

    private Expr parseCondition() {
        checkNotEof();
        int start = index;
        Expr c1 = parseConditionExpression();

        if (index < tokens.size() && tokens.get(index) instanceof LogicalAnd) {
            match("&&");
            Expr c2 = parseCondition();
            return new Expr.Binary(Expr.BOp.AND, c1, c2, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof LogicalOr) {
            match("||");
            Expr c2 = parseCondition();
            return new Expr.Binary(Expr.BOp.OR, c1, c2, sourceAttr(start, index - 1));
        }
        return c1;
    }

    private Expr parseConditionExpression() {
        int start = index;

        Expr lhs = parseAppendExpression();

        if (index < tokens.size() && tokens.get(index) instanceof LessEquals) {
            match("<=");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.LTEQ, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof LeftAngle) {
            match("<");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.LT, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof GreaterEquals) {
            match(">=");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.GTEQ, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof RightAngle) {
            match(">");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.GT, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof EqualsEquals) {
            match("==");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.EQ, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof NotEquals) {
            match("!=");
            Expr rhs = parseAppendExpression();
            return new Expr.Binary(Expr.BOp.NEQ, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index).text.equals("is")) {
            matchKeyword("is");
            Type rhs = parseType();
            return new Expr.Is(lhs, rhs, sourceAttr(start, index - 1));
        } else {
            return lhs;
        }
    }

    private ConstDecl parseConstantDeclaration() {
        int start = index;

        matchKeyword("const");
        Identifier name = matchIdentifier();
        matchKeyword("is");

        Expr e = parseCondition();
        int end = index;
        return new ConstDecl(e, name.text, sourceAttr(start, end - 1));
    }

    private Expr parseConstantListVal() {
        int start = index;
        ArrayList<Expr> exprs = new ArrayList<Expr>();
        match("[");
        boolean firstTime = true;
        checkNotEof();
        Token token = tokens.get(index);
        while (!(token instanceof RightSquare)) {
            if (!firstTime) {
                match(",");
            }
            firstTime = false;
            exprs.add(parseConstantTerm());

            checkNotEof();
            token = tokens.get(index);
        }
        match("]");
        return new Expr.ListConstructor(exprs, sourceAttr(start, index - 1));
    }

    private Expr parseConstantRecordVal() {
        int start = index;
        match("{");
        HashSet<String> keys = new HashSet<String>();
        ArrayList<Pair<String, Expr>> exprs = new ArrayList<Pair<String, Expr>>();
        checkNotEof();
        Token token = tokens.get(index);
        boolean firstTime = true;
        while (!(token instanceof RightCurly)) {
            if (!firstTime) {
                match(",");
            }
            firstTime = false;

            checkNotEof();
            token = tokens.get(index);
            Identifier n = matchIdentifier();

            if (keys.contains(n.text)) {
                syntaxError("duplicate tuple key", n);
            }

            match(":");

            Expr e = parseConstantTerm();
            exprs.add(new Pair<String, Expr>(n.text, e));
            keys.add(n.text);
            checkNotEof();
            token = tokens.get(index);
        }
        match("}");
        return new Expr.RecordConstructor(exprs, sourceAttr(start, index - 1));
    }

    private Expr parseConstantTerm() {
        checkNotEof();

        int start = index;
        Token token = tokens.get(index);

        if (token.text.equals("null")) {
            matchKeyword("null");
            return new Expr.Constant(null, sourceAttr(start, index - 1));
        } else if (token.text.equals("true")) {
            matchKeyword("true");
            return new Expr.Constant(true, sourceAttr(start, index - 1));
        } else if (token.text.equals("false")) {
            matchKeyword("false");
            return new Expr.Constant(false, sourceAttr(start, index - 1));
        } else if (token instanceof Lexer.Char) {
            char val = match(Lexer.Char.class, "a character").value;
            return new Expr.Constant(new Character(val), sourceAttr(start, index - 1));
        } else if (token instanceof Int) {
            int val = match(Int.class, "an integer").value;
            return new Expr.Constant(val, sourceAttr(start, index - 1));
        } else if (token instanceof Real) {
            double val = match(Real.class, "a real").value;
            return new Expr.Constant(val, sourceAttr(start, index - 1));
        } else if (token instanceof Strung) {
            return parseString();
        } else if (token instanceof LeftSquare) {
            return parseConstantListVal();
        } else if (token instanceof LeftCurly) {
            return parseConstantRecordVal();
        } else if (token instanceof Identifier) {
            Token id = matchIdentifier();

            if (constants.containsKey(id.text)) {
                return constants.get(id.text);
            }
        }

        syntaxError("unrecognised constant term (\"" + token.text + "\")", token);
        return null;
    }

    private Stmt parseFor() {
        int start = index;
        matchKeyword("for");
        match("(");
        Stmt.VariableDeclaration declaration = parseVariableDeclaration();
        match(";");
        Expr condition = parseCondition();
        match(";");
        Stmt increment = parseStatement(false);
        int end = index;
        match(")");
        List<Stmt> blk = parseBlock();

        return new Stmt.For(declaration, condition, increment, blk, sourceAttr(start, end - 1));
    }

    private FunDecl parseFunction() {
        int start = index;

        Type ret = parseType();
        Identifier name = matchIdentifier();

        match("(");

        // Now build up the parameter types
        List<Parameter> paramTypes = new ArrayList<Parameter>();
        boolean firstTime = true;
        while (index < tokens.size() && !(tokens.get(index) instanceof RightBrace)) {
            if (!firstTime) {
                match(",");
            }
            firstTime = false;
            int pstart = index;
            Type t = parseType();
            Identifier n = matchIdentifier();
            paramTypes.add(new Parameter(t, n.text, sourceAttr(pstart, index - 1)));
        }

        match(")");
        List<Stmt> stmts = parseBlock();
        return new FunDecl(name.text, ret, paramTypes, stmts, sourceAttr(start, index - 1));
    }

    private Stmt parseIf() {
        int start = index;
        matchKeyword("if");
        match("(");
        Expr c = parseCondition();
        match(")");
        int end = index;
        List<Stmt> tblk = parseBlock();
        List<Stmt> fblk = Collections.emptyList();

        if ((index + 1) < tokens.size() && tokens.get(index).text.equals("else")) {
            matchKeyword("else");

            if (index < tokens.size() && tokens.get(index).text.equals("if")) {
                Stmt if2 = parseIf();
                fblk = new ArrayList<Stmt>();
                fblk.add(if2);
            } else {
                fblk = parseBlock();
            }
        }

        return new Stmt.IfElse(c, tblk, fblk, sourceAttr(start, end - 1));
    }

    private Expr parseIndexTerm() {
        checkNotEof();
        int start = index;
        Expr lhs = parseTerm();

        Token lookahead = tokens.get(index);

        while (lookahead instanceof LeftSquare || lookahead instanceof Dot
                || lookahead instanceof LeftBrace) {
            if (lookahead instanceof LeftSquare) {
                match("[");
                Expr rhs = parseAddSubExpression();
                match("]");
                lhs = new Expr.IndexOf(lhs, rhs, sourceAttr(start, index - 1));
            } else {
                match(".");
                String name = matchIdentifier().text;
                lhs = new Expr.RecordAccess(lhs, name, sourceAttr(start, index - 1));
            }
            if (index < tokens.size()) {
                lookahead = tokens.get(index);
            } else {
                lookahead = null;
            }
        }

        return lhs;
    }

    private Expr.Invoke parseInvokeExpr() {
        int start = index;
        Identifier name = matchIdentifier();
        match("(");
        boolean firstTime = true;
        ArrayList<Expr> args = new ArrayList<Expr>();
        while (index < tokens.size() && !(tokens.get(index) instanceof RightBrace)) {
            if (!firstTime) {
                match(",");
            } else {
                firstTime = false;
            }
            Expr e = parseCondition();

            args.add(e);
        }
        match(")");
        return new Expr.Invoke(name.text, args, sourceAttr(start, index - 1));
    }

    private Expr.Invoke parseInvokeStmt() {
        int start = index;
        Identifier name = matchIdentifier();
        match("(");
        boolean firstTime = true;
        ArrayList<Expr> args = new ArrayList<Expr>();
        while (index < tokens.size() && !(tokens.get(index) instanceof RightBrace)) {
            if (!firstTime) {
                match(",");
            } else {
                firstTime = false;
            }
            Expr e = parseCondition();
            args.add(e);
        }
        match(")");

        return new Expr.Invoke(name.text, args, sourceAttr(start, index - 1));
    }

    private Expr parseLengthOf() {
        int start = index;
        match("|");
        Expr e = parseIndexTerm();
        match("|");
        return new Expr.Unary(Expr.UOp.LENGTHOF, e, sourceAttr(start, index - 1));
    }

    private Expr parseListVal() {
        int start = index;
        ArrayList<Expr> exprs = new ArrayList<Expr>();
        match("[");
        boolean firstTime = true;
        checkNotEof();
        Token token = tokens.get(index);
        while (!(token instanceof RightSquare)) {
            if (!firstTime) {
                match(",");
            }
            firstTime = false;
            exprs.add(parseCondition());

            checkNotEof();
            token = tokens.get(index);
        }
        match("]");
        return new Expr.ListConstructor(exprs, sourceAttr(start, index - 1));
    }

    private Expr parseMulDivExpression() {
        int start = index;
        Expr lhs = parseIndexTerm();

        if (index < tokens.size() && tokens.get(index) instanceof Star) {
            match("*");
            Expr rhs = parseMulDivExpression();
            return new Expr.Binary(Expr.BOp.MUL, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof RightSlash) {
            match("/");
            Expr rhs = parseMulDivExpression();
            return new Expr.Binary(Expr.BOp.DIV, lhs, rhs, sourceAttr(start, index - 1));
        } else if (index < tokens.size() && tokens.get(index) instanceof Percent) {
            match("%");
            Expr rhs = parseMulDivExpression();
            return new Expr.Binary(Expr.BOp.REM, lhs, rhs, sourceAttr(start, index - 1));
        }

        return lhs;
    }

    private Expr parseNegation() {
        int start = index;
        match("-");
        Expr e = parseIndexTerm();

        if (e instanceof Expr.Constant) {
            Expr.Constant c = (Expr.Constant) e;
            if (c.getValue() instanceof Integer) {
                int bi = (Integer) c.getValue();
                return new Expr.Constant(-bi, sourceAttr(start, index));
            } else if (c.getValue() instanceof Double) {
                double br = (Double) c.getValue();
                return new Expr.Constant(-br, sourceAttr(start, index));
            }
        }

        return new Expr.Unary(Expr.UOp.NEG, e, sourceAttr(start, index));
    }

    private Stmt.Print parsePrint() {
        int start = index;
        matchKeyword("print");
        checkNotEof();
        Expr e = parseCondition();
        int end = index;
        return new Stmt.Print(e, sourceAttr(start, end - 1));
    }

    private Expr parseRecordVal() {
        int start = index;
        match("{");
        HashSet<String> keys = new HashSet<String>();
        ArrayList<Pair<String, Expr>> exprs = new ArrayList<Pair<String, Expr>>();
        checkNotEof();
        Token token = tokens.get(index);
        boolean firstTime = true;
        while (!(token instanceof RightCurly)) {
            if (!firstTime) {
                match(",");
            }
            firstTime = false;

            checkNotEof();
            token = tokens.get(index);
            Identifier n = matchIdentifier();

            if (keys.contains(n.text)) {
                syntaxError("duplicate tuple key", n);
            }

            match(":");

            Expr e = parseCondition();
            exprs.add(new Pair<String, Expr>(n.text, e));
            keys.add(n.text);
            checkNotEof();
            token = tokens.get(index);
        }
        match("}");
        return new Expr.RecordConstructor(exprs, sourceAttr(start, index - 1));
    }

    private Stmt.Return parseReturn() {
        int start = index;
        // Every return statement begins with the return keyword!
        matchKeyword("return");
        Expr e = null;
        // A return statement may optionally have a return expression.
        if (index < tokens.size() && !(tokens.get(index) instanceof SemiColon)) {
            e = parseCondition();
        }
        // Done.
        return new Stmt.Return(e, sourceAttr(start, index - 1));
    }

    /**
     * Parse a given statement.
     *
     * @param withSemiColon Indicates whether to match semi-colons after the statement (where
     * appropriate). This is useful as in some very special cases (e.g. for-loops) we don't want to
     * match semi-colons.
     */
    private Stmt parseStatement(boolean withSemiColon) {
        checkNotEof();
        Token token = tokens.get(index);
        Stmt stmt;
        if (token.text.equals("return")) {
            stmt = parseReturn();
            if (withSemiColon) {
                match(";");
            }
        } else if (token.text.equals("print")) {
            stmt = parsePrint();
            if (withSemiColon) {
                match(";");
            }
        } else if (token.text.equals("break")) {
            stmt = parseBreak();
        } else if (token.text.equals("switch")) {
            stmt = parseSwitch();
        } else if (token.text.equals("if")) {
            stmt = parseIf();
        } else if (token.text.equals("while")) {
            stmt = parseWhile();
        } else if (token.text.equals("for")) {
            stmt = parseFor();
        } else if ((index + 1) < tokens.size() && tokens.get(index + 1) instanceof LeftBrace) {
            // must be a method invocation
            stmt = parseInvokeStmt();
            if (withSemiColon) {
                match(";");
            }
        } else if (isType(index)) {
            stmt = parseVariableDeclaration();
            if (withSemiColon) {
                match(";");
            }
        } else {
            // invocation or assignment
            int start = index;
            Expr t = parseCondition();
            if (t instanceof Expr.Invoke) {
                stmt = (Expr.Invoke) t;
            } else {
                index = start;
                stmt = parseAssign();
            }
            if (withSemiColon) {
                match(";");
            }
        }
        return stmt;
    }

    private Expr parseString() {
        int start = index;
        String s = match(Strung.class, "a string").string;
        return new Expr.Constant(s, sourceAttr(start, index - 1));
    }

    /**
     * Parses a {@code switch} statement. A switch statement is applied on a expression. It has an
     * arbitrary number of {@code case} statements and at most one default statement. The {@code
     * default} statement may occur at any point within the switch statement, but is only triggered
     * if none of the case statements occur.
     * <p/>
     * A case statement may only be a constant value or a named constant. There must be at least one
     * {@code case} or {@code default} statement in a switch.
     *
     * @return the switch statement.
     */
    private Stmt.Switch parseSwitch() {
        int start = index;

        // Match the boilerplate switch tokens
        matchKeyword("switch");

        match("(");
        Expr condition = parseCondition();
        match(")");
        match("{");

        List<Stmt> stmts = new ArrayList<Stmt>();
        Map<Expr, Integer> cases = new HashMap<Expr, Integer>();
        int defaultCase = -1;

        // For easy checks of what the next token may be
        List<String> switchKeywords = Arrays.asList("case", "default", "}");

        // While there are more tokens and we haven't reached the end of the switch
        while (index < tokens.size() && !(tokens.get(index) instanceof RightCurly)) {
            if (tokens.get(index).text.equals("case")) {
                matchKeyword("case");
                Expr caseCondition = parseConstantTerm();
                match(":");

                cases.put(caseCondition, stmts.size());

                // While there are more tokens and the next token isn't a 'case' or 'default'
                while (index < tokens.size() && !(switchKeywords.contains(tokens.get(
                        index).text))) {
                    stmts.add(parseStatement(true));
                }
            } else {
                // Check that we haven't already got a default statement
                if (defaultCase >= 0) {
                    syntaxError("switch statement already contains a default label", tokens.get(index));
                }

                matchKeyword("default");
                match(":");

                defaultCase = stmts.size();

                // While there are more tokens and the next token isn't a 'case' or 'default'
                while (index < tokens.size() && !switchKeywords.contains(tokens.get(index).text)) {
                    stmts.add(parseStatement(true));
                }
            }
        }

        match("}");

        // Check that there is at least one case or default statement
        if (cases.isEmpty() && defaultCase < 0) {
            Token token = index < tokens.size() ? tokens.get(index) : null;
            syntaxError("switch statement does not contain any cases or default", token);
        }

        return new Stmt.Switch(condition, stmts, cases, defaultCase, sourceAttr(start, index - 1));
    }

    private Expr parseTerm() {
        checkNotEof();

        int start = index;
        Token token = tokens.get(index);

        if (token instanceof LeftBrace) {
            match("(");
            if (isType(index)) {
                // indicates a cast
                Type t = parseType();
                checkNotEof();
                match(")");
                Expr e = parseCondition();
                return new Expr.Cast(t, e, sourceAttr(start, index - 1));
            } else {
                Expr e = parseCondition();
                checkNotEof();
                match(")");
                return e;
            }
        } else if ((index + 1) < tokens.size() && token instanceof Identifier && tokens.get(
                index + 1) instanceof LeftBrace) {
            // must be a method invocation
            return parseInvokeExpr();
        } else if (token.text.equals("null")) {
            matchKeyword("null");
            return new Expr.Constant(null, sourceAttr(start, index - 1));
        } else if (token.text.equals("true")) {
            matchKeyword("true");
            return new Expr.Constant(true, sourceAttr(start, index - 1));
        } else if (token.text.equals("false")) {
            matchKeyword("false");
            return new Expr.Constant(false, sourceAttr(start, index - 1));
        } else if (token instanceof Identifier) {
            Token id = matchIdentifier();

            if (constants.containsKey(id.text)) {
                return constants.get(id.text);
            }

            return new Expr.Variable(id.text, sourceAttr(start, index - 1));
        } else if (token instanceof Lexer.Char) {
            char val = match(Lexer.Char.class, "a character").value;
            return new Expr.Constant(new Character(val), sourceAttr(start, index - 1));
        } else if (token instanceof Int) {
            int val = match(Int.class, "an integer").value;
            return new Expr.Constant(val, sourceAttr(start, index - 1));
        } else if (token instanceof Real) {
            double val = match(Real.class, "a real").value;
            return new Expr.Constant(val, sourceAttr(start, index - 1));
        } else if (token instanceof Strung) {
            return parseString();
        } else if (token instanceof Minus) {
            return parseNegation();
        } else if (token instanceof Bar) {
            return parseLengthOf();
        } else if (token instanceof LeftSquare) {
            return parseListVal();
        } else if (token instanceof LeftCurly) {
            return parseRecordVal();
        } else if (token instanceof Shreak) {
            match("!");
            return new Expr.Unary(Expr.UOp.NOT, parseTerm(), sourceAttr(start, index - 1));
        }
        syntaxError("unrecognised term (\"" + token.text + "\")", token);
        return null;
    }

    private Type parseType() {
        int start = index;
        Type t = parseBaseType();

        // Now, attempt to look for union or intersection types.
        if (index < tokens.size() && tokens.get(index) instanceof Bar) {
            // this is a union type
            ArrayList<Type> types = new ArrayList<Type>();
            types.add(t);
            while (index < tokens.size() && tokens.get(index) instanceof Bar) {
                match("|");
                types.add(parseBaseType());
            }
            return new Type.Union(types, sourceAttr(start, index - 1));
        } else {
            return t;
        }
    }

    private Decl parseTypeDeclaration() {
        int start = index;
        matchKeyword("type");

        Identifier name = matchIdentifier();

        matchKeyword("is");

        Type t = parseType();
        int end = index;
        userDefinedTypes.add(name.text);
        return new TypeDecl(t, name.text, sourceAttr(start, end - 1));
    }

    private Stmt.VariableDeclaration parseVariableDeclaration() {
        int start = index;
        // Every variable declaration consists of a declared type and variable
        // name.
        Type type = parseType();
        Identifier id = matchIdentifier();
        // A variable declaration may optionally be assigned an initialiser
        // expression.
        Expr initialiser = null;
        if (index < tokens.size() && tokens.get(index) instanceof Equals) {
            match("=");
            initialiser = parseCondition();
        }
        // Done.
        return new Stmt.VariableDeclaration(type, id.text, initialiser, sourceAttr(start,
                index - 1));
    }

    private Stmt parseWhile() {
        int start = index;
        matchKeyword("while");
        match("(");
        Expr condition = parseCondition();
        match(")");
        int end = index;
        List<Stmt> blk = parseBlock();

        return new Stmt.While(condition, blk, sourceAttr(start, end - 1));
    }

    private Attribute.Source sourceAttr(int start, int end) {
        Token t1 = tokens.get(start);
        Token t2 = tokens.get(end);
        return new Attribute.Source(t1.start, t2.end());
    }

    private void syntaxError(String msg, Expr e) {
        Attribute.Source loc = e.attribute(Attribute.Source.class);
        throw new SyntaxError(msg, filename, loc.start, loc.end);
    }

    private void syntaxError(String msg, Token t) {
        throw new SyntaxError(msg, filename, t.start, t.start + t.text.length() - 1);
    }
}

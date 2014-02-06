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

package com.hjwylde.uni.swen222.lab02.org.simplelisp.util;

import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.*;

public class PrettyPrinter {
    
    public static String padNewline(int length) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = length; i > 0; i--)
            sb.append(" ");
        
        return sb.toString();
    }
    
    public static String prettyPrint(LispExpr e) {
        // First list is always a program
        LispList es = (LispList) e;
        String r = "";
        for (int i = 1; i != es.size(); ++i)
            r += print(es.get(i), 0) + "\n";
        
        return r;
    }
    
    /**
     * This function recursively traverses the Abstract Syntax Tree and prints it out, whilst making
     * an effort to do this neatly.
     * 
     * @param e the lisp expression.
     * @param indent the space indentation for each line.
     * @return a pretty string.
     */
    private static String print(LispExpr e, int indent) {
        if ((e instanceof LispInteger) || (e instanceof LispChar)
            || (e instanceof LispNil) || (e instanceof LispSymbol))
            return e.toString();
        
        if (e instanceof LispString)
            return "\"" + e.toString() + "\"";
        
        if (e instanceof LispQuote)
            return "'" + print(((LispQuote) e).getExpr(), indent);
        
        if (e instanceof LispList)
            return print((LispList) e, indent);
        
        if (e instanceof LispVector) {
            String ret = "#(";
            boolean firstTime = true;
            for (LispExpr le : (LispVector) e) {
                if (!firstTime)
                    ret += " ";
                firstTime = false;
                ret += print(le, indent);
            }
            return ret + ")";
        }
        
        return "UNKNOWN";
    }
    
    private static String print(LispList e, int indent) {
        if (e.size() == 0)
            return "()";
        
        LispExpr head = e.get(0);
        String ret = "(";
        
        // Catch special cases
        if (head instanceof LispSymbol) {
            LispSymbol fn = (LispSymbol) head;
            
            if (fn.name().equals("progn")) {
                ret += "progn ";
                for (int i = 1; i < e.size(); ++i)
                    ret += padNewline(indent + 6)
                        + print(e.get(i), indent + 6);
                return ret + ")";
            }
            
            if (fn.name().equals("defun") && (e.size() > 1)) {
                ret += "defun ";
                ret += print(e.get(1), indent) + " ";
                ret += print(e.get(2), indent);
                for (int i = 3; i < e.size(); ++i)
                    ret += padNewline(indent + 6)
                        + print(e.get(i), indent + 6);
                return ret + ")" + padNewline(indent) + padNewline(indent);
            }
            
            if (fn.name().equals("if"))
                if (e.size() == 4) {
                    ret += "if ";
                    ret += print(e.get(1), indent);
                    ret += padNewline(indent + 6)
                        + print(e.get(2), indent + 6);
                    ret += padNewline(indent + 3)
                        + print(e.get(3), indent + 3);
                    return ret + ")";
                } else if (e.size() == 3) {
                    ret += "if ";
                    ret += print(e.get(1), indent);
                    ret += padNewline(indent + 6)
                        + print(e.get(2), indent + 6);
                    return ret + ")";
                }
            
            if (fn.name().equals("let") && (e.size() == 3)) {
                ret += "let (";
                LispList vars = (LispList) e.get(1);
                for (LispExpr v : vars) {
                    ret += padNewline(indent + 3);
                    ret += print(v, indent + 3);
                }
                ret += ")" + padNewline(indent + 6);
                ret += print(e.get(2), indent);
                return ret + ")";
            }
        }
        
        // Deal with normal list by just printing elements with spaces in between, but no
        // newlines.
        boolean firstTime = true;
        for (LispExpr le : e) {
            if (!firstTime)
                ret += " ";
            firstTime = false;
            ret += print(le, indent);
        }
        return ret + ")";
    }
}
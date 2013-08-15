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

// ==============================================
// COMP205: YOU DO NOT NEED TO MODIFY THIS CLASS!
// ==============================================

package com.hjwylde.uni.swen222.lab02.org.simplelisp.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler.Interpreter;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.*;

public class InternalFunctions {
    
    private InternalFunctions() {}
    
    public static void setupInternalFunctions(final Interpreter interpreter) {
        // Setup internal dispatch table this is the minimum set of internal functions that the
        // Interpreter supports. Additional functions can be added via addFunction, however.
        
        // --- I/O FUNCTIONS ---
        interpreter.setGlobalExpr("print", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                interpreter.typeCheck("print", es, LispExpr.class);
                System.out.print(es[0]);
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("read-line", new LispFunction(0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(System.in));
                try {
                    String v = input.readLine();
                    return new LispString(v);
                } catch (IOException e) {
                    return LispNil.INSTANCE;
                }
            }
        });
        
        interpreter.setGlobalExpr("parse-integer", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                interpreter.typeCheck("parse-integer", es, LispString.class);
                LispString s = (LispString) es[0];
                
                return new LispInteger(Integer.parseInt(s.toString()));
            }
        });
        
        // --- ARITHMETIC FUNCTIONS ---
        interpreter.setGlobalExpr("+", new LispFunction(2, 999999) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                int result = 0;
                for (int i = 0; i != es.length; ++i)
                    if (es[i] instanceof LispInteger) {
                        LispInteger e = (LispInteger) es[i];
                        result += e.value();
                    } else
                        throw new Error("type error in \"+\"");
                
                return new LispInteger(result);
            }
        });
        
        interpreter.setGlobalExpr("-", new LispFunction(2, 999999) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                int result = 0;
                
                if (es[0] instanceof LispInteger) {
                    LispInteger e = (LispInteger) es[0];
                    result = e.value();
                } else
                    throw new Error("type error in \"-\"");
                
                for (int i = 1; i != es.length; ++i)
                    if (es[i] instanceof LispInteger) {
                        LispInteger e = (LispInteger) es[i];
                        result -= e.value();
                    } else
                        throw new Error("type error in \"-\"");
                
                return new LispInteger(result);
            }
        });
        
        interpreter.setGlobalExpr("*", new LispFunction(2, 999999) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                int result = 1;
                for (int i = 0; i != es.length; ++i)
                    if (es[i] instanceof LispInteger) {
                        LispInteger e = (LispInteger) es[i];
                        result *= e.value();
                    } else
                        throw new Error("type error in \"*\"");
                
                return new LispInteger(result);
            }
        });
        
        interpreter.setGlobalExpr("/", new LispFunction(2, 999999) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                int result = 0;
                
                if (es[0] instanceof LispInteger) {
                    LispInteger e = (LispInteger) es[0];
                    result = e.value();
                } else
                    throw new Error("type errorin \"/\"");
                
                for (int i = 1; i != es.length; ++i)
                    if (es[i] instanceof LispInteger) {
                        LispInteger e = (LispInteger) es[i];
                        result /= e.value();
                    } else
                        throw new Error("type error in \"/\"");
                
                return new LispInteger(result);
            }
        });
        
        interpreter.setGlobalExpr("%", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                interpreter.typeCheck("%", es, LispInteger.class,
                    LispInteger.class);
                LispInteger dividend = (LispInteger) es[0];
                LispInteger divisor = (LispInteger) es[1];
                return new LispInteger(dividend.value() % divisor.value());
            }
        });
        
        // / --- COMPARISON OPERATORS
        interpreter.setGlobalExpr("equal", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                if (es[0].equals(es[1]))
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("=", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("=", es, LispInteger.class,
                    LispInteger.class);
                
                LispInteger i1 = (LispInteger) es[0];
                LispInteger i2 = (LispInteger) es[1];
                if (i1.value() == i2.value())
                    return LispTrue.INSTANCE;
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("<", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("<", es, LispInteger.class,
                    LispInteger.class);
                
                LispInteger i1 = (LispInteger) es[0];
                LispInteger i2 = (LispInteger) es[1];
                if (i1.value() < i2.value())
                    return LispTrue.INSTANCE;
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("<=", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("<=", es, LispInteger.class,
                    LispInteger.class);
                
                LispInteger i1 = (LispInteger) es[0];
                LispInteger i2 = (LispInteger) es[1];
                if (i1.value() <= i2.value())
                    return LispTrue.INSTANCE;
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr(">", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck(">", es, LispInteger.class,
                    LispInteger.class);
                
                LispInteger i1 = (LispInteger) es[0];
                LispInteger i2 = (LispInteger) es[1];
                if (i1.value() > i2.value())
                    return LispTrue.INSTANCE;
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr(">=", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck(">=", es, LispInteger.class,
                    LispInteger.class);
                
                LispInteger i1 = (LispInteger) es[0];
                LispInteger i2 = (LispInteger) es[1];
                if (i1.value() >= i2.value())
                    return LispTrue.INSTANCE;
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("/=", new LispFunction(2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("/=", es, LispInteger.class,
                    LispInteger.class);
                
                LispInteger i1 = (LispInteger) es[0];
                LispInteger i2 = (LispInteger) es[1];
                if (i1.value() != i2.value())
                    return LispTrue.INSTANCE;
                return LispNil.INSTANCE;
            }
        });
        
        // --- INTERNAL DEFUN ---
        interpreter.setGlobalExpr("defun", new LispFunction(3, 0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("defun", es, LispSymbol.class,
                    LispList.class, LispList.class);
                
                // create the new function.
                String name = ((LispSymbol) es[0]).name();
                LispList pls = (LispList) es[1];
                ArrayList<String> params = new ArrayList<>();
                ArrayList<LispExpr> body = new ArrayList<>();
                
                for (LispExpr e : pls)
                    if (e instanceof LispSymbol)
                        params.add(((LispSymbol) e).name());
                    else
                        throw new Error("invalid parameter name in \"defun\"");
                
                for (int i = 2; i != es.length; ++i)
                    body.add(es[i]);
                
                LispExternal f = new LispExternal(params, body);
                interpreter.setGlobalExpr(name, f);
                return f;
            }
        });
        
        // --- INTERNAL LAMBDA ---
        
        interpreter.setGlobalExpr("lambda", new LispFunction(2, 0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                interpreter.typeCheck("lambda", es, LispList.class,
                    LispExpr.class);
                
                LispList pls = (LispList) es[0];
                ArrayList<String> params = new ArrayList<>();
                ArrayList<LispExpr> body = new ArrayList<>();
                
                for (LispExpr e : pls)
                    if (e instanceof LispSymbol)
                        params.add(((LispSymbol) e).name());
                    else
                        throw new Error("invalid parameter name in \"lambda\"");
                
                for (int i = 1; i != es.length; ++i)
                    body.add(es[i]);
                // create the new function.
                return new LispExternal(params, body);
            }
        });
        
        // --- INTERNAL SET ---
        interpreter.setGlobalExpr("set", new LispFunction(2, 0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter
                    .typeCheck("set", es, LispSymbol.class, Object.class);
                
                String name = ((LispSymbol) es[0]).name();
                LispExpr value = es[1].evaluate(locals, globals);
                interpreter.setGlobalExpr(name, value);
                return value;
            }
        });
        
        interpreter.setGlobalExpr("setq", new LispFunction(2, 0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("setq", es, LispSymbol.class,
                    Object.class);
                
                String name = ((LispSymbol) es[0]).name();
                LispExpr value = es[1];
                interpreter.setGlobalExpr(name, value);
                return value;
            }
        });
        
        // --- INTERNAL LET ---
        interpreter.setGlobalExpr("let", new LispFunction(2, 0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter
                    .typeCheck("let", es, LispList.class, LispExpr.class);
                locals = new HashMap<>(locals);
                LispList vardefs = (LispList) es[0];
                
                for (LispExpr _vd : vardefs) {
                    // really need a "interpreter.type_check" here.
                    LispList vd = (LispList) _vd;
                    LispSymbol sym = (LispSymbol) vd.get(0);
                    LispExpr value = vd.get(1).evaluate(locals, globals);
                    locals.put(sym.name(), value);
                }
                return es[1].evaluate(locals, globals);
            }
        });
        
        // --- INTERNAL IF ---
        interpreter.setGlobalExpr("if", new LispFunction(2, 0) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("if", es, LispExpr.class, LispExpr.class);
                es[0] = es[0].evaluate(locals, globals);
                interpreter.typeCheck("if", es, LispExpr.class, LispExpr.class);
                
                if (!es[0].equals(LispNil.INSTANCE))
                    return es[1].evaluate(locals, globals);
                else if (es.length > 2)
                    return es[2].evaluate(locals, globals);
                else
                    return null;
            }
        });
        
        // --- SEQUENCE MANIPULATORS ---
        interpreter.setGlobalExpr("length", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("length", es, LispSequence.class);
                
                LispSequence s = (LispSequence) es[0];
                return s.length();
            }
        });
        
        interpreter.setGlobalExpr("subseq", new LispFunction(3) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("subseq", es, LispSequence.class,
                    LispInteger.class, LispInteger.class);
                
                LispSequence s = (LispSequence) es[0];
                LispInteger low = (LispInteger) es[1];
                LispInteger high = (LispInteger) es[2];
                return s.sublist(low.value(), high.value());
            }
        });
        
        interpreter.setGlobalExpr("elt", new LispFunction(2) {
            
            // elt stands for element [at]
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("elt", es, LispSequence.class,
                    LispInteger.class);
                
                LispSequence s = (LispSequence) es[0];
                LispInteger index = (LispInteger) es[1];
                if (index.value() >= s.length().value())
                    return LispNil.INSTANCE;
                
                return s.getValue(index.value());
            }
        });
        
        interpreter.setGlobalExpr("reverse", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("reverse", es, LispSequence.class);
                LispSequence s = (LispSequence) es[0];
                return s.reverse();
            }
        });
        
        // --- LIST MANIPULATORS ---
        interpreter.setGlobalExpr("car", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                interpreter.typeCheck("car", es, LispList.class);
                LispList l = (LispList) es[0];
                if (l.size() < 1)
                    throw new Error(
                        "cannot take head of empty list in \"car\"!");
                return l.get(0);
            }
        });
        
        interpreter.setGlobalExpr("cdr", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("cdr", es, LispList.class);
                LispList l = (LispList) es[0];
                if (l.size() <= 1)
                    return LispNil.INSTANCE;
                LispList r = new LispList();
                for (int i = 1; i != l.size(); ++i)
                    r.add(l.get(i));
                return r;
            }
        });
        
        interpreter.setGlobalExpr("cons", new LispFunction(2, 2) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("cons", es, LispExpr.class,
                    LispExpr.class);
                LispExpr head = es[0];
                LispList r = new LispList();
                if (es[1] instanceof LispList) {
                    LispList tail = (LispList) es[1];
                    r.add(head);
                    for (LispExpr e : tail)
                        r.add(e);
                } else if (es[1] instanceof LispNil)
                    r.add(head);
                else
                    throw new Error("type error in \"cons\"");
                return r;
            }
        });
        
        // --- TYPE PREDICATES ---
        interpreter.setGlobalExpr("integerp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispInteger)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("stringp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispString)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("characterp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                if (es[0] instanceof LispChar)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("listp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispList)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("sequencep", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispSequence)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("vectorp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispVector)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("symbolp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispSymbol)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        interpreter.setGlobalExpr("functionp", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                if (es[0] instanceof LispFunction)
                    return LispTrue.INSTANCE;
                
                return LispNil.INSTANCE;
            }
        });
        
        // --- INTERNAL LOAD ---
        
        interpreter.setGlobalExpr("load", new LispFunction(1) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("load", es, LispString.class);
                LispString filename = (LispString) es[0];
                
                try {
                    interpreter.load(filename.toString());
                } catch (FileNotFoundException e) {
                    throw new Error("Unable to load file \"" + filename
                        + "\" (" + e.getMessage() + ")");
                }
                return LispTrue.INSTANCE;
            }
        });
        
        // --- MISC FUNCTIONS
        
        interpreter.setGlobalExpr("progn", new LispFunction(1, 999999) {
            
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                return es[es.length - 1];
            }
        });
        
        interpreter.setGlobalExpr("sleep", new LispFunction(1) {
            
            // sleep for X milli-seconds
            @Override
            public LispExpr internalInvoke(LispExpr[] es,
                HashMap<String, LispExpr> locals,
                HashMap<String, LispExpr> globals) {
                
                interpreter.typeCheck("sleep", es, LispInteger.class);
                
                LispInteger s = (LispInteger) es[0];
                try {
                    Thread.sleep(s.value());
                } catch (InterruptedException e) {
                    // Ok to do nothing here
                }
                
                return LispNil.INSTANCE;
            }
        });
    }
}
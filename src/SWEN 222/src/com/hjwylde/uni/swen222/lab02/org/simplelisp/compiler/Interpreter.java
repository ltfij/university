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

package com.hjwylde.uni.swen222.lab02.org.simplelisp.compiler;

import java.io.*;
import java.util.HashMap;

import com.hjwylde.uni.swen222.lab02.org.simplelisp.error.ParseException;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.LispExpr;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.lang.LispNil;
import com.hjwylde.uni.swen222.lab02.org.simplelisp.util.InternalFunctions;

public class Interpreter {
    
    private HashMap<String, LispExpr> globals = new HashMap<>();
    
    public Interpreter() {
        InternalFunctions.setupInternalFunctions(this);
    }
    
    public LispExpr evaluate(LispExpr e) {
        return e.evaluate(new HashMap<String, LispExpr>(), globals);
    }
    
    public LispExpr getGlobalExpr(String name) {
        LispExpr r = globals.get(name);
        if (r == null)
            return LispNil.INSTANCE;
        
        return r;
    }
    
    public void load(String filename) throws FileNotFoundException {
        // attempt to load file.
        File file = new File(filename); // will need to be changes to URLReader thing
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
            
            StringBuffer text = new StringBuffer();
            while (input.ready()) {
                text.append(input.readLine());
                text.append("\n");
            }
            // Ok, successful load; run the library code!
            try {
                LispExpr root = Parser.parse(text.toString());
                evaluate(root);
            } catch (ParseException e) {
                // unrecoverable syntax error.
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } catch (IOException e) {
            // This happens if e.g. file already exists and
            // we do not have write permissions
            System.err.println("Unable to load file " + file.getName() + ": "
                + e.getMessage());
            System.exit(1);
        }
    }
    
    public void setGlobalExpr(String name, LispExpr e) {
        globals.put(name, e);
    }
    
    public void typeCheck(String fn, LispExpr[] es, Class... cs) {
        for (int i = 0; i != cs.length; ++i) {
            if (es.length <= i)
                throw new Error("insufficient parameters!");
            if (!cs[i].isInstance(es[i]))
                throw new Error("type error in \"" + fn + "\"");
        }
    }
}
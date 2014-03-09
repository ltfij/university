// This file is part of the Simple Lisp Interpreter.
//
// The Simple Lisp Interpreter is free software; you can redistribute it and/or modify it under the
// terms of the GNU General Public License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
//
// The Simpular Program Interpreter is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE. See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with the Simpular Program
// Interpreter; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
// Boston, MA 02111-1307 USA
//
// (C) David James Pearce, 2005.

package com.hjwylde.uni.swen221.lab07.org.simplelisp.interpreter;

import java.util.HashMap;
import java.util.List;

/*
 * Code for Laboratory 7, SWEN 221 Name: Henry J. Wylde Usercode: wyldehenr ID: 300224283
 */

public final class LispExternal extends LispFunction {

    private final List<String> params;
    private final List<LispExpr> body;

    public LispExternal(List<String> p, List<LispExpr> b) {
        super(p.size(), b.size());
        params = p;
        body = b;
    }

    public List<LispExpr> body() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        // easy checks first
        if (!(o instanceof LispExternal))
            return false;
        LispExternal f = (LispExternal) o;
        if (f.params.size() != params.size())
            return false;
        if (f.body.size() != body.size())
            return false;

        // check parameters are equal
        for (int i = 0; i != params.size(); ++i)
            if (!params.get(i).equals(f.params.get(i)))
                return false;

        // check bodies are equal
        for (int i = 1; i != body.size(); ++i)
            if (!body.get(i).equals(f.body.get(i)))
                return false;

        // if we get this far, it must be true!
        return true;
    }

    @Override
    public LispExpr evaluate(HashMap<String, LispExpr> locals, HashMap<String, LispExpr> globals) {
        return this;
    }

    @Override
    public int hashCode() {
        return (params.hashCode() * 31) + body.hashCode();
    }

    @Override
    public LispExpr internal_invoke(LispExpr[] vals, HashMap<String, LispExpr> locals,
            HashMap<String, LispExpr> globals) {
        int i = 0;

        locals = new HashMap<>(locals);
        // apply the parameters
        for (String p : params)
            locals.put(p, vals[i++]);

        LispExpr r = null;
        // execute bodies now, taking last result
        // as result of this expression
        for (LispExpr b : body)
            r = b.evaluate(locals, globals);

        return r;
    }

    public List<String> parameters() {
        return params;
    }

    @Override
    public String toString() {
        return "? => ?";
    }
}

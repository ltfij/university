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

package com.hjwylde.uni.swen222.lab02.org.simplelisp.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        if (!(o instanceof LispExternal) || !super.equals(o))
            return false;

        LispExternal external = (LispExternal) o;

        return Objects.equals(params, external.params) && Objects.equals(body, external.body);
    }

    @Override
    public LispExpr evaluate(HashMap<String, LispExpr> locals, HashMap<String, LispExpr> globals) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(params, body);
    }

    @Override
    public LispExpr internalInvoke(LispExpr[] vals, HashMap<String, LispExpr> locals,
            HashMap<String, LispExpr> globals) {
        int i = 0;
        locals = (HashMap<String, LispExpr>) locals.clone();
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

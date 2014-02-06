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
import java.util.Objects;

public final class LispSymbol implements LispExpr {
    
    private final String symbol;
    
    public LispSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LispSymbol))
            return false;
        
        return Objects.equals(symbol, ((LispSymbol) o).symbol);
    }
    
    @Override
    public LispExpr evaluate(HashMap<String, LispExpr> locals,
        HashMap<String, LispExpr> globals) {
        LispExpr r = locals.get(symbol);
        if (r == null)
            r = globals.get(symbol);
        if (r == null)
            throw new Error("void variable \"" + symbol + "\"");
        
        return r;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
    
    public String name() {
        return symbol;
    }
    
    @Override
    public String toString() {
        return symbol;
    }
}

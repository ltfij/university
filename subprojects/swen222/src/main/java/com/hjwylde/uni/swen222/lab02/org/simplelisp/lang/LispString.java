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

public final class LispString implements LispSequence {
    
    private final String value;
    
    public LispString(String value) {
        this.value = value;
    }
    
    @Override
    public LispExpr getValue(int i) {
        return new LispChar(value.charAt(i));
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LispString))
            return false;
        
        return Objects.equals(value, ((LispString) o).value);
    }
    
    @Override
    public LispExpr evaluate(HashMap<String, LispExpr> locals,
        HashMap<String, LispExpr> globals) {
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public LispInteger length() {
        return new LispInteger(value.length());
    }
    
    @Override
    public LispString reverse() {
        StringBuilder sb = new StringBuilder(value).reverse();
        return new LispString(sb.toString());
    }
    
    @Override
    public LispString sublist(int from, int to) {
        return new LispString(value.substring(from, to));
    }
    
    @Override
    public String toString() {
        return value;
    }
}
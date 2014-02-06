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

import java.util.*;

public class LispList implements LispSequence, Iterable<LispExpr> {
    
    private final List<LispExpr> exprs = new LinkedList<>();
    
    public LispList() {}
    
    public LispList(LinkedList<LispExpr> es) {
        for (LispExpr e : es)
            add(e);
    }
    
    public void add(LispExpr e) {
        exprs.add(e);
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LispList))
            return false;
        
        return Objects.equals(exprs, ((LispList) o).exprs);
    }
    
    @Override
    public LispExpr evaluate(HashMap<String, LispExpr> locals,
        HashMap<String, LispExpr> globals) {
        if (size() == 0)
            return LispNil.INSTANCE;
        
        // must evaluate head to determine target function
        LispExpr head = get(0).evaluate(locals, globals);
        
        // sanity check!
        if (head instanceof LispFunction) {
            LispFunction fn = (LispFunction) head;
            LinkedList<LispExpr> l = new LinkedList<>();
            for (LispExpr e : this)
                l.add(e);
            return fn.invoke(l, locals, globals);
        } else
            throw new Error("unable to dispatch on \"" + head
                + "\" - it's not a function!");
    }
    
    public LispExpr get(int i) {
        return exprs.get(i);
    }
    
    @Override
    public LispExpr getValue(int i) {
        return get(i);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return exprs.hashCode();
    }
    
    // --- SEQUENCE METHODS ---
    // (these are required by the LispSequence
    // interface)
    
    @Override
    public Iterator<LispExpr> iterator() {
        return exprs.iterator();
    }
    
    @Override
    public LispInteger length() {
        return new LispInteger(size());
    }
    
    @Override
    public LispSequence reverse() {
        LinkedList<LispExpr> l = new LinkedList<>(exprs);
        Collections.reverse(l);
        return new LispList(l);
    }
    
    public int size() {
        return exprs.size();
    }
    
    // --- EVALUATE METHOD ---
    
    @Override
    public LispList sublist(int l, int u) {
        LispList r = new LispList();
        for (int i = l; i != u; ++i)
            r.add(get(i));
        return r;
    }
    
    @Override
    public String toString() {
        StringBuffer r = new StringBuffer("(");
        boolean firstTime = true;
        
        // go through each element
        // and add it to the string!
        for (LispExpr e : this) {
            if (!firstTime)
                r.append(" ");
            firstTime = false;
            r.append(e.toString());
        }
        r.append(")");
        return r.toString();
    }
}
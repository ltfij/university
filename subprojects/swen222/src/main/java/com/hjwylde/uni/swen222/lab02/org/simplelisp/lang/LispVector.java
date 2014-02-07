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

public final class LispVector implements LispSequence, Iterable<LispExpr> {

    private final List<LispExpr> es;

    public LispVector() {
        es = new ArrayList<>();
    }

    public LispVector(List<LispExpr> es) {
        this.es = es;
    }

    public void add(LispExpr e) {
        es.add(e);
    }

    @Override
    public LispExpr getValue(int i) {
        return es.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LispVector))
            return false;

        return Objects.equals(es, ((LispVector) o).es);
    }

    @Override
    public LispExpr evaluate(HashMap<String, LispExpr> locals, HashMap<String, LispExpr> globals) {
        return this;
    }

    public LispExpr get(int i) {
        return es.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return es.hashCode();
    }

    @Override
    public Iterator<LispExpr> iterator() {
        return es.iterator();
    }

    @Override
    public LispInteger length() {
        return new LispInteger(es.size());
    }

    @Override
    public LispVector reverse() {
        List<LispExpr> l = new ArrayList<>(es);
        Collections.reverse(l);
        return new LispVector(l);
    }

    public int size() {
        return es.size();
    }

    @Override
    public LispVector sublist(int from, int to) {
        return new LispVector(es.subList(from, to));
    }

    @Override
    public String toString() {
        StringBuffer r = new StringBuffer("#(");
        boolean firstTime = true;

        // Go through each element and add it to the string!
        for (LispExpr e : es) {
            if (!firstTime)
                r.append(" ");
            firstTime = false;
            r.append(e.toString());
        }
        r.append(")");
        return r.toString();
    }
}

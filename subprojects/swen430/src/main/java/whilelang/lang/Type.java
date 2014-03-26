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

package whilelang.lang;

import whilelang.util.Attribute;
import whilelang.util.SyntacticElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> Represents a type as denoted in a source file (a.k.a a <i>syntactic type</i>). As such types
 * come directly from source code, they may be incorrect in some fashion. For example, the type
 * <code>{void f}</code> could be written by a programmer, but is invalid type and should
 * (eventually) result in a syntax error. </p> <p/> <p> Types are not necessarily represented in
 * their minimal form. For example, the programmer may write <code>int|int</code>, which is a valid
 * type that is equivalent to <code>int</code>. Thus, further processing on types is necessary if
 * they are to be represeted in a minimal form. </p>
 *
 * @author David J. Pearce
 */
public interface Type extends SyntacticElement {

    /**
     * Represents the special <code>void</code> type which can only be used in special circumstance
     * (e.g. for a function return).
     *
     * @author David J. Pearce
     */
    public static final class Void extends SyntacticElement.Impl implements Type {

        public Void(Attribute... attributes) {
            super(attributes);
        }

        public String toString() {
            return "void";
        }
    }

    /**
     * Represents the special <code>null</code> type which can be thought of as describing a set of
     * size one that contains the value <code>null</code>.
     *
     * @author David J. Pearce
     */
    public static final class Null extends SyntacticElement.Impl implements Type {

        public Null(Attribute... attributes) {

            super(attributes);
        }

        public String toString() {
            return "null";
        }
    }

    /**
     * Represents the <code>bool</code> type which contains the values <code>true</code> and
     * <code>false</code>.
     *
     * @author David J. Pearce
     */
    public static final class Bool extends SyntacticElement.Impl implements Type {

        public Bool(Attribute... attributes) {
            super(attributes);
        }

        public String toString() {
            return "bool";
        }
    }

    /**
     * Represents the <code>int</code> type which describes the set of all integers described in
     * 32bit twos compliment form. For example, this is identical to a Java <code>int</code>.
     *
     * @author David J. Pearce
     */
    public static final class Int extends SyntacticElement.Impl implements Type {

        public Int(Attribute... attributes) {
            super(attributes);
        }

        public String toString() {
            return "int";
        }
    }

    /**
     * Represents the <code>real</code> type which describess the set of all 64bit IEEE754 floating
     * point numbers. For example, this is identical to a Java <code>double</code>.
     *
     * @author David J. Pearce
     */
    public static final class Real extends SyntacticElement.Impl implements Type {

        public Real(Attribute... attributes) {
            super(attributes);
        }

        public String toString() {
            return "real";
        }
    }

    /**
     * Represents the <code>char</code> type which describes the set of all 7bit ASCII characters.
     * Observe that this is stricly less than that described by Java's <code>char</code> type, which
     * represents the set of UTF16 values.
     *
     * @author David J. Pearce
     */
    public static final class Char extends SyntacticElement.Impl implements Type {

        public Char(Attribute... attributes) {
            super(attributes);
        }

        public String toString() {
            return "char";
        }
    }

    /**
     * Represents the <code>string</code> type which describes any sequence of <code>char</code>
     * values.
     *
     * @author David J. Pearce
     */
    public static final class Strung extends SyntacticElement.Impl implements Type {

        public Strung(Attribute... attributes) {
            super(attributes);
        }

        public String toString() {
            return "string";
        }
    }

    /**
     * Represents a named type which has yet to be expanded in the given context.
     *
     * @author David J. Pearce
     */
    public static final class Named extends SyntacticElement.Impl implements Type {

        private final String name;

        public Named(String name, Attribute... attributes) {
            super(attributes);
            this.name = name;
        }

        public String toString() {
            return getName();
        }

        /**
         * Get the name used by this type.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Represents the type <code>[T]</code> which describes any sequence of zero or more values of
     * type <code>T</code>.
     *
     * @author David J. Pearce
     */
    public static final class List extends SyntacticElement.Impl implements Type {

        private final Type element;

        public List(Type element, Attribute... attributes) {
            super(attributes);
            this.element = element;
        }

        /**
         * Get the element type of this list.
         */
        public Type getElement() {
            return element;
        }

        public String toString() {
            return "[" + element + "]";
        }
    }

    /**
     * Represents a record type, such as <code>{int x, int y}</code>, which consists of one or more
     * (named) field types. Observe that records exhibit <i>depth</i> subtyping, but not
     * <i>width</i> subtyping.
     *
     * @author David J. Pearce
     */
    public static final class Record extends SyntacticElement.Impl implements Type {

        private final HashMap<String, Type> fields;

        public Record(Map<String, Type> fields, Attribute... attributes) {
            super(attributes);
            if (fields.size() == 0) {
                throw new IllegalArgumentException("Cannot create type tuple with no fields");
            }
            this.fields = new HashMap<String, Type>(fields);
        }

        /**
         * Get the fields which make up this record type.
         */
        public Map<String, Type> getFields() {
            return fields;
        }

        public String toString() {
            String r = "";

            ArrayList<String> fs = new ArrayList<String>(fields.keySet());
            Collections.sort(fs);
            for (int i = 0; i != fs.size(); ++i) {
                if (i != 0) {
                    r = r + ",";
                }
                String field = fs.get(i);
                r = r + fields.get(field) + " " + field;
            }

            return "{" + r + "}";
        }
    }

    /**
     * Represents a union type, such as <code>T1|T2</code>, which describes the set union of two (or
     * more) types. For example, the type <code>bool|null</code> describes the set
     * <code>{true,false,null}</code>.
     *
     * @author David J. Pearce
     */
    public static final class Union extends SyntacticElement.Impl implements Type {

        private final ArrayList<Type> bounds;

        public Union(Collection<Type> bounds, Attribute... attributes) {
            if (bounds.size() < 2) {
                new IllegalArgumentException(
                        "Cannot construct a type union with fewer than two bounds");
            }
            this.bounds = new ArrayList<Type>(bounds);
        }

        /**
         * Get the individual types which are being unioned together.
         */
        public java.util.List<Type> getBounds() {
            return bounds;
        }

        public String toString() {
            String r = "";
            for (int i = 0; i != bounds.size(); ++i) {
                if (i != 0) {
                    r = r + "|";
                }
                r = r + bounds.get(i);
            }
            return r;
        }
    }
}

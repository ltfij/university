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
import whilelang.util.Pair;
import whilelang.util.SyntacticElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents an expression in the source code of a While program. Many standard expression kinds
 * are provided, including unary operations (e.g. <code>!e</code>, <code>-e</code>,
 * <code>|e|</code>), binary operations (e.g. <code>x==y</code>, <code>x!=y</code>,
 * <code>x+y</code>, etc), list expressions (e.g. <code>ls[i]</code>, <code>[1,2,3]</code>, etc),
 * record expressions (e.g. <code>r.f</code>, <code>{x: 1, y: 2}</code>, etc).
 */
public interface Expr extends SyntacticElement {

    public enum BOp {
        AND {
            public String toString() {
                return "&&";
            }
        },
        OR {
            public String toString() {
                return "||";
            }
        },
        ADD {
            public String toString() {
                return "+";
            }
        },
        SUB {
            public String toString() {
                return "-";
            }
        },
        MUL {
            public String toString() {
                return "*";
            }
        },
        DIV {
            public String toString() {
                return "/";
            }
        },
        REM {
            public String toString() {
                return "%";
            }
        },
        EQ {
            public String toString() {
                return "==";
            }
        },
        NEQ {
            public String toString() {
                return "!=";
            }
        },
        LT {
            public String toString() {
                return "<";
            }
        },
        LTEQ {
            public String toString() {
                return "<=";
            }
        },
        GT {
            public String toString() {
                return ">";
            }
        },
        GTEQ {
            public String toString() {
                return ">=";
            }
        },
        APPEND {
            public String toString() {
                return "++";
            }
        }
    }

    public enum UOp {
        NOT, NEG, LENGTHOF,
    }

    /**
     * Captures the expression kinds which are permitted on the left-side of an assignment
     * statement.
     *
     * @author David J. Pearce
     */
    public interface LVal extends Expr {

    }

    /**
     * Represents a binary expression, composed recursively from a left-hand side and right-hand
     * side. For example, in the expression <code>1+x</code> we have an instance of
     * <code>Binary</code> to represent the addition whose left-hand side is an instance of
     * <code>Constant</code> and right-hand side is an instance of <code>Variable</code>.
     *
     * @author David J. Pearce
     */
    public static class Binary extends SyntacticElement.Impl implements Expr {

        private final BOp op;
        private final Expr lhs;
        private final Expr rhs;

        /**
         * Construct a binary expression from a given left-hand expression and right-hand
         * expression.
         *
         * @param op The operation this expression descibes; may not be null.
         * @param lhs The left-hand side; may not be null.
         * @param rhs The right-hand side; may not be null.
         */
        public Binary(BOp op, Expr lhs, Expr rhs, Attribute... attributes) {
            super(attributes);
            if (op == null) {
                throw new NullPointerException("op cannot be null");
            }
            if (lhs == null) {
                throw new NullPointerException("lhs cannot be null");
            }
            if (rhs == null) {
                throw new NullPointerException("rhs cannot be null");
            }

            this.op = op;
            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Construct a binary expression from a given left-hand expression and right-hand
         * expression.
         *
         * @param op The operation this expression descibes; may not be null.
         * @param lhs The left-hand side; may not be null.
         * @param rhs The right-hand side; may not be null.
         */
        public Binary(BOp op, Expr lhs, Expr rhs, Collection<Attribute> attributes) {
            super(attributes);
            if (op == null) {
                throw new NullPointerException("op cannot be null");
            }
            if (lhs == null) {
                throw new NullPointerException("lhs cannot be null");
            }
            if (rhs == null) {
                throw new NullPointerException("rhs cannot be null");
            }

            this.op = op;
            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Get the left-hand side of this binary expression.
         *
         * @return Guaranteed to be non-null.
         */
        public Expr getLhs() {
            return lhs;
        }

        /**
         * Get the operation that this binary expression represents.
         *
         * @return Guaranteed to be non-null.
         */
        public BOp getOp() {
            return op;
        }

        /**
         * Get the right-hand side of this binary expression.
         *
         * @return Guaranteed to be non-null.
         */
        public Expr getRhs() {
            return rhs;
        }

        public String toString() {
            return "(" + getOp() + " " + getLhs() + " " + getRhs() + ")";
        }
    }

    /**
     * Represents a cast expression, which converts an expression of one type to an expression of
     * another.
     *
     * @author David J. Pearce
     */
    public static class Cast extends SyntacticElement.Impl implements Expr {

        private final Type type;
        private final Expr source;

        public Cast(Type type, Expr src, Attribute... attributes) {
            super(attributes);
            if (type == null) {
                throw new NullPointerException("type cannot be null");
            }
            if (src == null) {
                throw new NullPointerException("src cannot be null");
            }

            this.type = type;
            this.source = src;
        }

        public Cast(Type type, Expr src, Collection<Attribute> attributes) {
            super(attributes);
            if (type == null) {
                throw new NullPointerException("type cannot be null");
            }
            if (src == null) {
                throw new NullPointerException("src cannot be null");
            }

            this.type = type;
            this.source = src;
        }

        public Expr getSource() {
            return source;
        }

        public Type getType() {
            return type;
        }
    }

    ;

    /**
     * Represents the occurrence of a constant value within an expression. For example, in the
     * expression <code>1+x</code>, an instance of <code>Constant</code> is used to represent the
     * value <code>1</code>.
     *
     * @author David J. Pearce
     */
    public static class Constant extends SyntacticElement.Impl implements Expr {

        private Object value;

        /**
         * Construct a constant expression from a given (primitive) value. The value must be either
         * a boolean, character, integer, real, or string constant; alternative, it can be null (to
         * signal the null constant).
         *
         * @param value Must be an instance of <code>java.lang.Boolean</code>,
         * <code>java.lang.Character</code>, <code>java.lang.Integer</code>,
         * <code>java.lang.Double</code>, <code>java.lang.String</code> or <code>null</code>.
         */
        public Constant(Object value, Attribute... attributes) {
            super(attributes);

            this.value = value;
        }

        /**
         * Get the value represented by this constant, which must be an instance of
         * <code>java.lang.Boolean</code>, <code>java.lang.Character</code>,
         * <code>java.lang.Integer</code>, <code>java.lang.Double</code>,
         * <code>java.lang.String</code> or <code>null</code>.
         */
        public Object getValue() {
            return value;
        }

        public String toString() {
            if (value == null) {
                return "null";
            } else {
                return value.toString();
            }
        }
    }

    /**
     * <p> Represents a list or string access expression, which may also form the left-hand side of
     * an assignment. For example, <code>return 1 + xs[i]</code> and <code>ls[i] = 1</code> are
     * valid uses of a list access expression. <p> <p> List or string access expressions can give
     * rise to index-out-of-bounds exceptions, in the case that the index is negative or larger or
     * equal to the size of the source list. </p>
     *
     * @author David J. Pearce
     */
    public static class IndexOf extends SyntacticElement.Impl implements Expr, LVal {

        private final Expr source;
        private final Expr index;

        /**
         * Create a list access expression from a given source expression (which must evaluate to a
         * list value) and index expression (which must evaluate to an integer value).
         *
         * @param source The source expression which generates a list value; may not be null.
         * @param index The index expression which determines the list element to return; may not be
         * null.
         */
        public IndexOf(Expr source, Expr index, Attribute... attributes) {
            super(attributes);
            if (source == null) {
                throw new NullPointerException("source cannot be null");
            }
            if (index == null) {
                throw new NullPointerException("index cannot be null");
            }

            this.source = source;
            this.index = index;
        }

        /**
         * Create a list access expression from a given source expression (which must evaluate to a
         * list value) and index expression (which must evaluate to an integer value).
         *
         * @param index The index expression which determines the list element to return; may not be
         * null.
         */
        public IndexOf(Expr src, Expr index, Collection<Attribute> attributes) {
            super(attributes);
            if (src == null) {
                throw new NullPointerException("source cannot be null");
            }
            if (index == null) {
                throw new NullPointerException("index cannot be null");
            }

            this.source = src;
            this.index = index;
        }

        /**
         * Get the index expression for this list access.
         *
         * @return Guaranteed to be non-null.
         */
        public Expr getIndex() {
            return index;
        }

        /**
         * Get the source expression for this list access.
         *
         * @return Guaranteed to be non-null.
         */
        public Expr getSource() {
            return source;
        }

        public String toString() {
            return source + "[" + index + "]";
        }
    }

    /**
     * Represents a function invocation, which is composed of a <i>function name</i> and zero or
     * more <i>argument expressions</i>. For example, <code>f(1)</code> is an invocation expression
     * for the function named <code>f</code> which accepts a single argument.
     *
     * @author David J. Pearce
     */
    public static class Invoke extends SyntacticElement.Impl implements Expr, Stmt {

        private final String name;
        private final ArrayList<Expr> arguments;

        /**
         * Construct a function invocation expression from a given function name and list of zero or
         * more argument expressions.
         *
         * @param name The function name that this invocation will call; may not be null.
         * @param arguments The list of zero or more argument expressions; may not be null.
         */
        public Invoke(String name, List<Expr> arguments, Attribute... attributes) {
            super(attributes);
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }
            if (arguments.contains(null)) {
                throw new NullPointerException("arguments cannot contain null");
            }

            this.name = name;
            this.arguments = new ArrayList<Expr>(arguments);
        }

        /**
         * Get the list of zero or more argument expression being pass to the function being invoked
         * in this expression; guaranteed to be non-null.
         */
        public List<Expr> getArguments() {
            return arguments;
        }

        /**
         * Get the function name being invoked in this expression; guaranteed to be non-null.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Represents an {@code is} operation. An {@code is} operation is like Javas <i>instanceof</i>
     * operator, it checks whether an expression is an instance of some type and returns true if it
     * is.
     *
     * @author Henry J. Wylde
     */
    public static class Is extends SyntacticElement.Impl implements Expr {

        private final Expr lhs;
        private final Type rhs;

        /**
         * Creates a new {@code Is} operator with the given expression and type to check.
         *
         * @param lhs the left hand side of the operation (the expression).
         * @param rhs the right hand side of the operation (the type).
         * @param attributes the attributes of this expression.
         */
        public Is(Expr lhs, Type rhs, Collection<Attribute> attributes) {
            super(attributes);
            if (lhs == null) {
                throw new NullPointerException("lhs cannot be null");
            }
            if (rhs == null) {
                throw new NullPointerException("rhs cannot be null");
            }

            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Creates a new {@code Is} operator with the given expression and type to check.
         *
         * @param lhs the left hand side of the operation (the expression).
         * @param rhs the right hand side of the operation (the type).
         * @param attributes the attributes of this expression.
         */
        public Is(Expr lhs, Type rhs, Attribute... attributes) {
            super(attributes);
            if (lhs == null) {
                throw new NullPointerException("lhs cannot be null");
            }
            if (rhs == null) {
                throw new NullPointerException("rhs cannot be null");
            }

            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Gets the left hand side (the expression) of this operation.
         *
         * @return the left hand side (the expression).
         */
        public Expr getLhs() {
            return lhs;
        }

        /**
         * Gets the right hand side (the type) of this operation.
         *
         * @return the right hand side (the type).
         */
        public Type getRhs() {
            return rhs;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "(" + lhs + " is " + rhs + ")";
        }
    }

    /**
     * Represents a list constructor which constructs a list value from zero or more element
     * expressions. For example, <code>[1,2,3]</code>, <code>[1,x]</code> and <code>[]</code> are
     * valid list constructor expressions.
     *
     * @author David J. Pearce
     */
    public static class ListConstructor extends SyntacticElement.Impl implements Expr {

        private final ArrayList<Expr> arguments;

        /**
         * Construct a list constructor expression from a list of zero or more element expressions.
         *
         * @param arguments A list of zero or more element expressions; may not be null.
         */
        public ListConstructor(Collection<Expr> arguments, Attribute... attributes) {
            super(attributes);
            if (arguments.contains(null)) {
                throw new NullPointerException("arguments cannot contain null");
            }

            this.arguments = new ArrayList<Expr>(arguments);
        }

        /**
         * Construct a list constructor expression from a list of zero or more element expressions.
         *
         * @param arguments A list of zero or more element expressions; may not be null.
         */
        public ListConstructor(Attribute attribute, Expr... arguments) {
            this(Arrays.asList(arguments), attribute);
        }

        /**
         * Get the list of element expressions used in this list constructor expression.
         *
         * @return A list of zero or more expression; guaranteed to be non-null.
         */
        public List<Expr> getArguments() {
            return arguments;
        }
    }

    /**
     * Represents a record access expression, which is composed of a <i>source expression</i> and
     * <i>field name</i>. For example, in the expression <code>x.f</code> the variable
     * <code>x</code> is the source expression, whilst the field name is <code>f</code>.
     *
     * @author David J. Pearce
     */
    public static class RecordAccess extends SyntacticElement.Impl implements LVal {

        private final Expr source;
        private final String name;

        /**
         * Construct a record access expression from a given source expression and field name.
         *
         * @param source An expression which must evaluate to a record containing a field with the
         * given name; may not be null.
         * @param name The name of a field contained within the record that the source expression
         * evaluates to; may not be null.
         */
        public RecordAccess(Expr source, String name, Attribute... attributes) {
            super(attributes);
            if (source == null) {
                throw new NullPointerException("source cannot be null");
            }
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }

            this.source = source;
            this.name = name;
        }

        /**
         * Get the field name for this record access.
         */
        public String getName() {
            return name;
        }

        /**
         * Get the source expression for this record access.
         */
        public Expr getSource() {
            return source;
        }

        public String toString() {
            return getSource() + "." + getName();
        }
    }

    /**
     * Represents a record construct expression, which constructs a list value from a number of
     * <i>field expressions</i>. For example, the expression <code>{x: 1, y: 2}</code> evaluates to
     * produce a record containing the fields <code>x</code> and <code>y</code> which (respectively)
     * hold the values <code>1</code> and <code>2</code>.
     *
     * @author David J. Pearce
     */
    public static class RecordConstructor extends SyntacticElement.Impl implements Expr {

        private final ArrayList<Pair<String, Expr>> fields;

        /**
         * Construct a record constructor expression from a given mapping of field names to their
         * generating expressions.
         *
         * @param fields A map of zero or more field names to generating expressions; may not be
         * null.
         */
        public RecordConstructor(List<Pair<String, Expr>> fields, Attribute... attributes) {
            super(attributes);
            if (fields.contains(null)) {
                throw new NullPointerException("fields cannot contain null");
            }

            this.fields = new ArrayList<Pair<String, Expr>>(fields);
        }

        /**
         * Get the mapping from field names to generating expressions; guaranteed to be non-null.
         */
        public List<Pair<String, Expr>> getFields() {
            return fields;
        }
    }

    /**
     * Represents a unary expression, composed recursively from a single expression. For example, in
     * the expression <code>!x</code> which have a <code>Unary</code> instance representing the
     * logical inversion whose sub-expression is a <code>Variable</code> instance.
     *
     * @author David J. Pearce
     */
    public static class Unary extends SyntacticElement.Impl implements Expr {

        private final UOp op;
        private final Expr expr;

        /**
         * Construct a unary expression from a given unary operation, and sub-expression.
         *
         * @param op The unary operation this expression represents; may not be null.
         * @param expr The sub-expression whose value will be manipulated by the unary operation;
         * may not be null.
         */
        public Unary(UOp op, Expr expr, Attribute... attributes) {
            super(attributes);
            if (op == null) {
                throw new NullPointerException("op cannot be null");
            }
            if (op ==null) {
                throw new NullPointerException("expr cannot be null");
            }

            this.op = op;
            this.expr = expr;
        }

        /**
         * Get the sub-expression this unary expression operates over; guaranteed to be non-null.
         */
        public Expr getExpr() {
            return expr;
        }

        /**
         * Get the operation this unary expression represents; guaranteed to be non-null.
         */
        public UOp getOp() {
            return op;
        }

        public String toString() {
            return op + expr.toString();
        }
    }

    /**
     * Represents a single occurrence of a variable within a expression. For example, the expression
     * <code>x+x+y</code> will contain three instances of <code>Variable</code> --- one for each
     * variable usage.
     *
     * @author David J. Pearce
     */
    public static class Variable extends SyntacticElement.Impl implements Expr, LVal {

        private String name;

        /**
         * Construct a variable expression from a given variable name.
         *
         * @param name Must be non-null.
         */
        public Variable(String name, Attribute... attributes) {
            super(attributes);
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }

            this.setVar(name);
        }

        /**
         * Get the name of the variable in question.
         *
         * @return Guaranteed to be non-null.
         */
        public String getName() {
            return name;
        }

        public void setVar(String var) {
            this.name = var;
        }

        public String toString() {
            return getName();
        }
    }
}

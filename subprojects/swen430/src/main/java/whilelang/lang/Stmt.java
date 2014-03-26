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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a statement in the source code of a While program. Many standard statement kinds are
 * provided, including <code>if</code>, <code>while</code>, <code>for</code>, etc.
 *
 * @author David J. Pearce
 */
public interface Stmt extends SyntacticElement {

    /**
     * Represents an assignment statement of the form <code>lhs = rhs</code>. Here, the
     * <code>rhs</code> is any expression, whilst the <code>lhs</code> must be an <code>LVal</code>
     * --- that is, an expression permitted on the left-side of an assignment. The following
     * illustrates different possible assignment statements:
     * <p/>
     * <pre>
     * x = y; // variable assignment
     * x.f = y; // field assignment
     * x[i] = y; // list assignment
     * x[i].f = y; // compound assignment
     * </pre>
     * <p/>
     * The last assignment here illustrates that the left-hand side of an assignment can be
     * arbitrarily complex, involving nested assignments into lists and records.
     *
     * @author David J. Pearce
     */
    public static final class Assign extends SyntacticElement.Impl implements Stmt {

        private final Expr.LVal lhs;
        private final Expr rhs;

        /**
         * Create an assignment from a given <code>lhs</code> and <code>rhs</code>.
         *
         * @param lhs --- left-hand side, which may not be <code>null</code>.
         * @param rhs --- right-hand side, which may not be <code>null</code>.
         */
        public Assign(Expr.LVal lhs, Expr rhs, Attribute... attributes) {
            super(attributes);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Create an assignment from a given <code>lhs</code> and <code>rhs</code>.
         *
         * @param lhs left-hand side, which may not be <code>null</code>.
         * @param rhs right-hand side, which may not be <code>null</code>.
         */
        public Assign(Expr.LVal lhs, Expr rhs, Collection<Attribute> attributes) {
            super(attributes);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Get the left-hand side of this assignment.
         *
         * @return Guaranteed non-null.
         */
        public Expr.LVal getLhs() {
            return lhs;
        }

        /**
         * Get the right-hand side of this assignment.
         *
         * @return Guaranteed non-null.
         */
        public Expr getRhs() {
            return rhs;
        }

        public String toString() {
            return getLhs() + " = " + getRhs();
        }
    }

    /**
     * Represents a break statement. A break statement is used within loops to exit the closest one,
     * or within a switch statement to break out of the current case. The language does not support
     * named break statements.
     *
     * @author Henry J. Wylde
     */
    public static final class Break extends SyntacticElement.Impl implements Stmt {

        /**
         * Creates a new {@code Break} statement with the given attributes.
         *
         * @param attributes the attributes.
         */
        public Break(Attribute... attributes) {
            super(attributes);
        }

        /**
         * Creates a new {@code Break} statement with the given attributes.
         *
         * @param attributes the attributes.
         */
        public Break(Collection<Attribute> attributes) {
            super(attributes);
        }
    }

    /**
     * Represents a classical for statement made up from a <i>variable declaration</i>, a <i>loop
     * condition</i> and an <i>increment statement</i>. The following illustrates:
     * <p/>
     * <pre>
     * int sum([int] xs) {
     *   int r = 0;
     *   for(int i=0;i<|xs|;i=i+1) {
     *     r = r + xs[i];
     *   }
     *   return r;
     * }
     * </pre>
     * <p/>
     * Observe that the variable declaration does not need to supply an initialiser expression.
     * Furthermore, like C and Java, the variable declaration, condition and increment statements
     * are all optional. Thus, we can safely rewrite the above as follows:
     * <p/>
     * <pre>
     * int sum([int] xs) {
     *   int r = 0;
     *   for(int i=0;i<|xs|;) {
     *     r = r + xs[i];
     *   }
     *   return r;
     * }
     * </pre>
     *
     * @author David J. Pearce
     */
    public static final class For extends SyntacticElement.Impl implements Stmt {

        private final VariableDeclaration declaration;
        private final Expr condition;
        private final Stmt increment;
        private final ArrayList<Stmt> body;

        /**
         * Construct a for loop from a given declaration, condition and increment. Note that the
         * declaration, conditional and increment are all optional.
         *
         * @param declaration An variable declation, which may be null.
         * @param condition A loop condition which may not be null.
         * @param increment An increment statement, which may be null.
         * @param body A list of zero or more statements, which may not be null.
         */
        public For(VariableDeclaration declaration, Expr condition, Stmt increment,
                Collection<Stmt> body, Attribute... attributes) {
            super(attributes);
            this.declaration = declaration;
            this.condition = condition;
            this.increment = increment;
            this.body = new ArrayList<Stmt>(body);
        }

        /**
         * Construct a for loop from a given declaration, condition and increment. Note that the
         * declaration, conditional and increment are all optional.
         *
         * @param declaration An variable declation, which may be null.
         * @param condition A loop condition which may be null.
         * @param increment An increment statement, which may be null.
         * @param body A list of zero or more statements, which may not be null.
         */
        public For(VariableDeclaration declaration, Expr condition, Stmt increment,
                Collection<Stmt> body, Collection<Attribute> attributes) {
            super(attributes);
            this.declaration = declaration;
            this.condition = condition;
            this.increment = increment;
            this.body = new ArrayList<Stmt>(body);
        }

        /**
         * Get the loop body.
         *
         * @return May not be null.
         */
        public ArrayList<Stmt> getBody() {
            return body;
        }

        /**
         * Get the loop condition.
         *
         * @return May be null.
         */
        public Expr getCondition() {
            return condition;
        }

        /**
         * Get the variable declaration for this loop.
         *
         * @return May be null.
         */
        public VariableDeclaration getDeclaration() {
            return declaration;
        }

        /**
         * Get the increment statement.
         *
         * @return May be null.
         */
        public Stmt getIncrement() {
            return increment;
        }
    }

    /**
     * Represents a classical if-else statement, made up from a <i>condition</i>, <i>true branch</i>
     * and <i>false branch</i>. The following illustrates:
     * <pre>
     * int max(int x, int y) {
     *   if(x > y) {
     *     return x;
     *   } else {
     *     return y;
     *   }
     * }
     * </pre>
     *
     * @author David J. Pearce
     */
    public static final class IfElse extends SyntacticElement.Impl implements Stmt {

        private final Expr condition;
        private final ArrayList<Stmt> trueBranch;
        private final ArrayList<Stmt> falseBranch;

        /**
         * Construct an if-else statement from a condition, true branch and optional false branch.
         *
         * @param condition May not be null.
         * @param trueBranch A list of zero or more statements to be executed when the condition
         * holds; may not be null.
         * @param falseBranch A list of zero of more statements to be executed when the condition
         * does not hold; may not be null.
         */
        public IfElse(Expr condition, List<Stmt> trueBranch, List<Stmt> falseBranch,
                Attribute... attributes) {
            super(attributes);
            this.condition = condition;
            this.trueBranch = new ArrayList<Stmt>(trueBranch);
            this.falseBranch = new ArrayList<Stmt>(falseBranch);
        }

        /**
         * Construct an if-else statement from a condition, true branch and optional false branch.
         *
         * @param condition May not be null.
         * @param trueBranch A list of zero or more statements to be executed when the condition
         * holds; may not be null.
         * @param falseBranch A list of zero of more statements to be executed when the condition
         * does not hold; may not be null.
         */
        public IfElse(Expr condition, List<Stmt> trueBranch, List<Stmt> falseBranch,
                Collection<Attribute> attributes) {
            super(attributes);
            this.condition = condition;
            this.trueBranch = new ArrayList<Stmt>(trueBranch);
            this.falseBranch = new ArrayList<Stmt>(falseBranch);
        }

        /**
         * Get the if-condition.
         *
         * @return May not be null.
         */
        public Expr getCondition() {
            return condition;
        }

        /**
         * Get the false branch, which consists of zero or more statements.
         *
         * @return May not be null.
         */
        public List<Stmt> getFalseBranch() {
            return falseBranch;
        }

        /**
         * Get the true branch, which consists of zero or more statements.
         *
         * @return May not be null.
         */
        public List<Stmt> getTrueBranch() {
            return trueBranch;
        }
    }

    /**
     * Represents a print statement which writes expressions (as strings) to the console. The
     * following illustrates:
     * <p/>
     * <pre>
     * void f(int x) {
     *  print x+1;
     * }
     * </pre>
     * <p/>
     * Observe that the computed value for the expression to be printed is the implicitly coerced
     * into a string.
     *
     * @author David J. Pearce
     */
    public static final class Print extends SyntacticElement.Impl implements Stmt {

        private final Expr expr;

        /**
         * Construct a print statement from a given expression.
         *
         * @param expr May not be null.
         */
        public Print(Expr expr, Attribute... attributes) {
            super(attributes);
            this.expr = expr;
        }

        /**
         * Construct a print statement from a given expression.
         *
         * @param expr May not be null.
         */
        public Print(Expr expr, Collection<Attribute> attributes) {
            super(attributes);
            this.expr = expr;
        }

        /**
         * Get the expression whose value is to be printed.
         *
         * @return Guaranteed to be non-null.
         */
        public Expr getExpr() {
            return expr;
        }

        public String toString() {
            return "print " + getExpr();
        }
    }

    /**
     * Represents a return statement which (optionally) returns a value. The following illustrates:
     * <p/>
     * <pre>
     * int f(int x) {
     * 	return x + 1;
     * }
     * </pre>
     * <p/>
     * Here, we see a simple <code>return</code> statement which returns an <code>int</code> value.
     *
     * @author David J. Pearce
     */
    public static final class Return extends SyntacticElement.Impl implements Stmt {

        private final Expr expr;

        /**
         * Create a given return statement with an optional return value.
         *
         * @param expr the return value, which may be <code>null</code>.
         */
        public Return(Expr expr, Attribute... attributes) {
            super(attributes);
            this.expr = expr;
        }

        /**
         * Create a given return statement with an optional return value.
         *
         * @param expr the return value, which may be <code>null</code>.
         */
        public Return(Expr expr, Collection<Attribute> attributes) {
            super(attributes);
            this.expr = expr;
        }

        /**
         * Get the optional return value.
         *
         * @return --- May be <code>null</code>.
         */
        public Expr getExpr() {
            return expr;
        }

        public String toString() {
            if (getExpr() != null) {
                return "return " + getExpr();
            } else {
                return "return";
            }
        }
    }

    /**
     * Represents a switch statement. A switch statement operates on a single condition and may have
     * any number of case statements. A case statement is a mapping of an expression to a list of
     * statements for that case. A body statement may appear anywhere inside a switch statement, but
     * may only appear once.
     * <p/>
     * The switch statement is modelled using a single list of all the statements contained within.
     * Case statements point to an index in the list for where to start. This allows for proper
     * modelling of "fall-through" cases where code from more than one case may be executed. The
     * default case acts in this way as well, but may optionally be {@code -1}, indicating it does
     * not exist.
     *
     * @author Henry J. Wylde
     */
    public static final class Switch extends SyntacticElement.Impl implements Stmt {

        private final Expr condition;

        private final List<Stmt> stmts;
        /**
         * A map of the case constant to index into the list of statements.
         */
        private final Map<Expr, Integer> cases;
        /**
         * An index into the list of statements.
         */
        private final int defaultCase;

        /**
         * Creates a new {@code Switch} statement with the given statements and cases. The
         * statements should be provided as a list of all statements, while the cases should be a
         * map from the case expression to an index into the statement list. When interpreting the
         * switch statement, a case statement will run all the statements from its index to the end
         * of statements, or until it reaches a control flow alteration statement.
         *
         * @param condition the switch dontiion.
         * @param stmts the list of statements.
         * @param cases the map from case constant to stmt start index.
         * @param defaultCase the stmt start index for the default case.
         * @param attributes the attributes.
         */
        public Switch(Expr condition, List<Stmt> stmts, Map<Expr, Integer> cases, int defaultCase,
                Attribute... attributes) {
            this(condition, stmts, cases, defaultCase, Arrays.asList(attributes));
        }

        /**
         * Creates a new {@code Switch} statement with the given statements and cases. The
         * statements should be provided as a list of all statements, while the cases should be a
         * map from the case expression to an index into the statement list. When interpreting the
         * switch statement, a case statement will run all the statements from its index to the end
         * of statements, or until it reaches a control flow alteration statement.
         *
         * @param condition the switch dontiion.
         * @param stmts the list of statements.
         * @param cases the map from case constant to stmt start index.
         * @param defaultCase the stmt start index for the default case.
         * @param attributes the attributes.
         */
        public Switch(Expr condition, List<Stmt> stmts, Map<Expr, Integer> cases, int defaultCase,
                Collection<Attribute> attributes) {
            super(attributes);

            this.condition = condition;

            this.stmts = new ArrayList<Stmt>(stmts);
            this.cases = new HashMap<Expr, Integer>(cases);
            this.defaultCase = defaultCase;
        }

        /**
         * Gets the case statements map.
         *
         * @return the case statements map.
         */
        public Map<Expr, Integer> getCases() {
            return cases;
        }

        /**
         * Gets the switch condition.
         *
         * @return the condition.
         */
        public Expr getCondition() {
            return condition;
        }

        /**
         * Gets the default case start index into the list of statements.
         *
         * @return the default case start index.
         */
        public int getDefaultCase() {
            return defaultCase;
        }

        /**
         * Gets the list of statements for this switch.
         *
         * @return the list of statements.
         */
        public List<Stmt> getStmts() {
            return stmts;
        }
    }

    /**
     * Represents a variable declaration which is made up from a type, a variable name and an
     * (optional) initialiser expression. If an initialiser is given, then this will be evaluated
     * and assigned to the variable when the declaration is executed. Some example declarations:
     * <p/>
     * <pre>
     * int x;
     * int y = 1;
     * int z = x + y;
     * </pre>
     * <p/>
     * Observe that, unlike C and Java, declarations that declare multiple variables (separated by
     * commas) are not permitted.
     *
     * @author David J. Pearce
     */
    public static final class VariableDeclaration extends SyntacticElement.Impl implements Stmt {

        private final Type type;
        private final String name;
        private final Expr expr;

        /**
         * Construct a variable declaration from a given type, variable name and optional
         * initialiser expression.
         *
         * @param type Type of variable being declared.
         * @param name Name of varaible being declared.
         * @param expr Optional initialiser expression, which may be null.
         */
        public VariableDeclaration(Type type, String name, Expr expr, Attribute... attributes) {
            super(attributes);
            this.type = type;
            this.name = name;
            this.expr = expr;
        }

        /**
         * Construct a variable declaration from a given type, variable name and optional
         * initialiser expression.
         *
         * @param type Type of variable being declared.
         * @param name Name of varaible being declared.
         * @param expr Optional initialiser expression, which may be null.
         */
        public VariableDeclaration(Type type, String name, Expr expr,
                Collection<Attribute> attributes) {
            super(attributes);
            this.type = type;
            this.name = name;
            this.expr = expr;
        }

        /**
         * Get the initialiser expression of the variable being declared (if present).
         *
         * @return May be null.
         */
        public Expr getExpr() {
            return expr;
        }

        /**
         * Get the name of the variable being declared.
         *
         * @return Guaranteed to be non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * Get the type of the variable being declared.
         *
         * @return Guaranteed to be non-null.
         */
        public Type getType() {
            return type;
        }

        public String toString() {
            String r = getType() + " " + getName();
            if (getExpr() != null) {
                r = r + " = " + getExpr();
            }
            return r;
        }
    }

    /**
     * Represents a while statement whose body is made up from a block of statements separated by
     * curly braces. Note that, unlike C or Java, the body must be contained within curly braces. As
     * an example:
     * <p/>
     * <pre>
     * int sum([int] xs) {
     *   int r = 0;
     *   int i = 0;
     *   while(i < |xs|) {
     *     r = r + xs[i];
     *     i = i + 1;
     *   }
     *   return r;
     * }
     * </pre>
     *
     * @author David J. Pearce
     */
    public static final class While extends SyntacticElement.Impl implements Stmt {

        private final Expr condition;
        private final ArrayList<Stmt> body;

        /**
         * Construct a While statement from a given condition and body of statements.
         *
         * @param condition non-null expression.
         * @param body non-null collection which contains zero or more statements.
         */
        public While(Expr condition, Collection<Stmt> body, Attribute... attributes) {
            super(attributes);
            this.condition = condition;
            this.body = new ArrayList<Stmt>(body);
        }

        /**
         * Construct a While statement from a given condition and body of statements.
         *
         * @param condition non-null expression.
         * @param body non-null collection which contains zero or more statements.
         */
        public While(Expr condition, Expr invariant, Collection<Stmt> body,
                Collection<Attribute> attributes) {
            super(attributes);
            this.condition = condition;
            this.body = new ArrayList<Stmt>(body);
        }

        /**
         * Get the statements making up the loop body.
         *
         * @return Guarantted to be non-null.
         */
        public List<Stmt> getBody() {
            return body;
        }

        /**
         * Get the condition which controls the while loop.
         *
         * @return Guaranteed to be non-null.
         */
        public Expr getCondition() {
            return condition;
        }
    }
}

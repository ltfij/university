package whilelang.util;

import static whilelang.util.SyntaxError.internalFailure;
import static whilelang.util.SyntaxError.syntaxError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import whilelang.lang.Expr;
import whilelang.lang.Stmt;
import whilelang.lang.WhileFile;

/**
 * Responsible for checking that all variables are defined before they are used. The algorithm for
 * checking this involves a depth-first search through the control-flow graph of the method.
 * Throughout this, a list of the defined variables is maintained.
 *
 * @author David J. Pearce
 */
public class DefiniteAssignment {

    private WhileFile file;
    private WhileFile.FunDecl function;
    private HashSet<String> constants;

    public void check(WhileFile wf) {
        this.file = wf;
        this.constants = new HashSet<String>();
        for (WhileFile.Decl declaration : wf.declarations) {
            if (declaration instanceof WhileFile.ConstDecl) {
                WhileFile.ConstDecl cd = (WhileFile.ConstDecl) declaration;
                this.constants.add(cd.name());
            }
        }

        for (WhileFile.Decl declaration : wf.declarations) {
            if (declaration instanceof WhileFile.FunDecl) {
                check((WhileFile.FunDecl) declaration);
            }
        }
    }

    public void check(WhileFile.FunDecl fd) {
        this.function = fd;

        // First, initialise the environment with all parameters (since these
        // are assumed to be definitely assigned)
        // The environment is a map of variable names to boolean
        // If the variable has been declared, it is present but false
        // If the variable has been initialised, it is present and true
        Map<String, Boolean> environment = new HashMap<String, Boolean>();
        for (String constant : constants) {
            setInitialised(constant, environment);
        }
        for (WhileFile.Parameter p : fd.parameters) {
            setInitialised(p.name(), environment);
        }

        // Second, check all statements in the function body
        try {
            check(fd.statements, environment);
        } catch (ControlFlowException.Return e) {
            // Do nothing
        } catch (ControlFlowException.Break e) {
            syntaxError("out of place break statement", file.filename, e.stmt);
        }
    }

    /**
     * Check that all variables used in a given list of statements are definitely assigned.
     * Furthermore, update the set of definitely assigned variables to include any which are
     * definitely assigned at the end of these statements.
     *
     * @param stmts The list of statements to check.
     * @param environment The set of variables which are definitely assigned.
     */
    public void check(List<Stmt> stmts, Map<String, Boolean> environment)
            throws ControlFlowException.Return, ControlFlowException.Break {
        for (int i = 0; i < stmts.size(); i++) {
            try {
                check(stmts.get(i), environment);
            } catch (ControlFlowException.Return e) {
                if (i < stmts.size() - 1) {
                    syntaxError("dead code detected", file.filename, stmts.get(i + 1));
                }

                throw e;
            } catch (ControlFlowException.Break e) {
                if (i < stmts.size() - 1) {
                    syntaxError("dead code detected", file.filename, stmts.get(i + 1));
                }

                throw e;
            }
        }
    }

    /**
     * Check that all variables used in a given statement are definitely assigned. Furthermore,
     * update the set of definitely assigned variables to include any which are definitely assigned
     * after this statement.
     *
     * @param stmt The statement to check.
     * @param environment The set of variables which are definitely assigned.
     */
    public void check(Stmt stmt, Map<String, Boolean> environment)
            throws ControlFlowException.Return, ControlFlowException.Break {
        if (stmt instanceof Stmt.Assign) {
            check((Stmt.Assign) stmt, environment);
        } else if (stmt instanceof Stmt.Break) {
            check((Stmt.Break) stmt, environment);
        } else if (stmt instanceof Stmt.Print) {
            check((Stmt.Print) stmt, environment);
        } else if (stmt instanceof Stmt.Return) {
            check((Stmt.Return) stmt, environment);
        } else if (stmt instanceof Stmt.VariableDeclaration) {
            check((Stmt.VariableDeclaration) stmt, environment);
        } else if (stmt instanceof Expr.Invoke) {
            check((Expr.Invoke) stmt, environment);
        } else if (stmt instanceof Stmt.IfElse) {
            check((Stmt.IfElse) stmt, environment);
        } else if (stmt instanceof Stmt.For) {
            check((Stmt.For) stmt, environment);
        } else if (stmt instanceof Stmt.Switch) {
            check((Stmt.Switch) stmt, environment);
        } else if (stmt instanceof Stmt.While) {
            check((Stmt.While) stmt, environment);
        } else {
            internalFailure("unknown statement encountered (" + stmt + ")", file.filename, stmt);
        }
    }

    public void check(Stmt.Assign stmt, Map<String, Boolean> environment) {
        check(stmt.getRhs(), environment);

        if (stmt.getLhs() instanceof Expr.Variable) {
            Expr.Variable var = (Expr.Variable) stmt.getLhs();
            environment.put(var.getName(), true);
        } else {
            check(stmt.getLhs(), environment);
        }
    }

    public void check(Stmt.Break stmt, Map<String, Boolean> environment)
            throws ControlFlowException.Break {
        throw new ControlFlowException.Break(stmt);
    }

    public void check(Stmt.Print stmt, Map<String, Boolean> environment) {
        check(stmt.getExpr(), environment);
    }

    public void check(Stmt.Return stmt, Map<String, Boolean> environment)
            throws ControlFlowException.Return {
        if (stmt.getExpr() != null) {
            check(stmt.getExpr(), environment);
        }

        throw new ControlFlowException.Return(stmt);
    }

    public void check(Stmt.VariableDeclaration stmt, Map<String, Boolean> environment) {
        if (isDeclared(stmt.getName(), environment)) {
            syntaxError("variable already declared: " + stmt.getName(), file.filename, stmt);
        }

        setDeclared(stmt.getName(), environment);

        if (stmt.getExpr() != null) {
            check(stmt.getExpr(), environment);

            setInitialised(stmt.getName(), environment);
        }
    }

    public void check(Stmt.IfElse stmt, Map<String, Boolean> environment)
            throws ControlFlowException.Return, ControlFlowException.Break {
        check(stmt.getCondition(), environment);

        Map<String, Boolean> trueEnv = new HashMap<String, Boolean>(environment);
        Map<String, Boolean> falseEnv = new HashMap<String, Boolean>(environment);

        ControlFlowException trueError = null;
        ControlFlowException falseError = null;

        try {
            check(stmt.getTrueBranch(), trueEnv);
        } catch (ControlFlowException.Return e) {
            trueError = e;
            trueEnv = null;
        } catch (ControlFlowException.Break e) {
            trueError = e;
            trueEnv = null;
        }

        try {
            check(stmt.getFalseBranch(), falseEnv);
        } catch (ControlFlowException.Return e) {
            falseError = e;
            falseEnv = null;
        } catch (ControlFlowException.Break e) {
            falseError = e;
            falseEnv = null;
        }

        // TODO: Fix this section... It just does not work that well!

        // Due to the very poor way of doing this, we can only infer facts about the the control flow if something happens on both branches
        // Would be much nicer to have actually used a control flow graph
        if (trueError instanceof ControlFlowException.Break) {
            if (falseError instanceof ControlFlowException.Break) {
                throw (ControlFlowException.Break) trueError;
            } else if (falseError instanceof ControlFlowException.Return) {
                throw (ControlFlowException.Break) trueError;
            }
        } else if (trueError instanceof ControlFlowException.Return) {
            if (falseError instanceof ControlFlowException.Break) {
                throw (ControlFlowException.Break) falseError;
            } else if (falseError instanceof ControlFlowException.Return) {
                throw ((ControlFlowException.Return) trueError);
            }
        }

        // Add all items defined on both branches to environment
        if (trueEnv == null) {
            environment.putAll(falseEnv);
        } else if (falseEnv == null) {
            environment.putAll(trueEnv);
        } else {
            environment.putAll(mergeEnvironments(trueEnv, falseEnv));
        }
    }

    public void check(Stmt.For stmt, Map<String, Boolean> environment) {
        check(stmt.getDeclaration(), environment);
        check(stmt.getCondition(), environment);
        try {
            check(stmt.getIncrement(), environment);
        } catch (ControlFlowException e) {
            syntaxError(
                    "control flow modifier (return | break) should not be called in a for statement incrementor",
                    file.filename, stmt.getIncrement());
        }
        try {
            check(stmt.getBody(), new HashMap<String, Boolean>(environment));
        } catch (ControlFlowException e) {
            // Do nothing
        }
    }

    public void check(Stmt.Switch stmt, Map<String, Boolean> environment)
            throws ControlFlowException.Return {
        check(stmt.getCondition(), environment);

        // Record the indices of all the statements that we have checked
        Set<Integer> stmtsChecked = new HashSet<Integer>();

        List<Map<String, Boolean>> environments = new ArrayList<Map<String, Boolean>>();

        for (Map.Entry<Expr, Integer> entry : stmt.getCases().entrySet()) {
            check(entry.getKey(), environment);

            Map<String, Boolean> caseEnvironment = new HashMap<String, Boolean>(environment);
            environments.add(caseEnvironment);

            for (int i = entry.getValue(); i < stmt.getStmts().size(); i++) {
                stmtsChecked.add(i);
                Stmt s = stmt.getStmts().get(i);

                try {
                    check(s, caseEnvironment);
                } catch (ControlFlowException.Return e) {
                    // Can ignore this case environment as it ends control flow for the method
                    environments.remove(caseEnvironment);
                    break;
                } catch (ControlFlowException.Break e) {
                    break;
                }
            }
        }

        ControlFlowException.Return error = null;

        // Check the default case if it is present
        if (stmt.getDefaultCase() >= 0) {
            Map<String, Boolean> defaultEnvironment = new HashMap<String, Boolean>(environment);
            environments.add(defaultEnvironment);

            for (int i = stmt.getDefaultCase(); i < stmt.getStmts().size(); i++) {
                stmtsChecked.add(i);
                Stmt s = stmt.getStmts().get(i);

                try {
                    check(s, defaultEnvironment);
                } catch (ControlFlowException.Return e) {
                    error = e;
                    environments.remove(defaultEnvironment);
                    break;
                } catch (ControlFlowException.Break e) {
                    break;
                }
            }
        }

        // If we haven't checked all the statements, then that means there is some deadcode in the list
        if (stmtsChecked.size() != stmt.getStmts().size()) {
            // Find which statement is deadcode
            int expected = 0;
            for (int next : stmtsChecked) {
                if (next != expected) {
                    syntaxError("dead code detected", file.filename, stmt.getStmts().get(expected));
                }

                expected++;
            }
        }

        // Only time this occurs is if every statement has a return in it, so we should propagate that information
        // We also need to check if the default case was defined, if it wasn't then we can't say all inputs are accounted for!
        if (environments.size() == 0 && stmt.getDefaultCase() >= 0) {
            throw error;
        }

        // Retain only the variables defined in all the environments
        environment.putAll(mergeEnvironments(environments));
    }

    public void check(Stmt.While stmt, Map<String, Boolean> environment) {
        check(stmt.getCondition(), environment);
        try {
            check(stmt.getBody(), new HashMap<String, Boolean>(environment));
        } catch (ControlFlowException e) {
            // Do nothing
        }
    }

    /**
     * Check that all variables used in a given expression are definitely assigned.
     *
     * @param expr The expression to check.
     * @param environment The set of variables which are definitely assigned.
     */
    public void check(Expr expr, Map<String, Boolean> environment) {
        if (expr instanceof Expr.Binary) {
            check((Expr.Binary) expr, environment);
        } else if (expr instanceof Expr.Cast) {
            check((Expr.Cast) expr, environment);
        } else if (expr instanceof Expr.Constant) {
            check((Expr.Constant) expr, environment);
        } else if (expr instanceof Expr.IndexOf) {
            check((Expr.IndexOf) expr, environment);
        } else if (expr instanceof Expr.Invoke) {
            check((Expr.Invoke) expr, environment);
        } else if (expr instanceof Expr.Is) {
            check((Expr.Is) expr, environment);
        } else if (expr instanceof Expr.ListConstructor) {
            check((Expr.ListConstructor) expr, environment);
        } else if (expr instanceof Expr.RecordAccess) {
            check((Expr.RecordAccess) expr, environment);
        } else if (expr instanceof Expr.RecordConstructor) {
            check((Expr.RecordConstructor) expr, environment);
        } else if (expr instanceof Expr.Unary) {
            check((Expr.Unary) expr, environment);
        } else if (expr instanceof Expr.Variable) {
            check((Expr.Variable) expr, environment);
        } else {
            internalFailure("unknown expression encountered (" + expr + ")", file.filename, expr);
        }
    }

    public void check(Expr.Binary expr, Map<String, Boolean> environment) {
        check(expr.getLhs(), environment);
        check(expr.getRhs(), environment);
    }

    public void check(Expr.Cast expr, Map<String, Boolean> environment) {
        check(expr.getSource(), environment);
    }

    public void check(Expr.Constant expr, Map<String, Boolean> environment) {
        // Constants are obviousy already defined ;)
    }

    public void check(Expr.IndexOf expr, Map<String, Boolean> environment) {
        check(expr.getIndex(), environment);
        check(expr.getSource(), environment);
    }

    public void check(Expr.Invoke expr, Map<String, Boolean> environment) {
        for (Expr arg : expr.getArguments()) {
            check(arg, environment);
        }
    }

    public void check(Expr.Is expr, Map<String, Boolean> environment) {
        check(expr.getLhs(), environment);
    }

    public void check(Expr.ListConstructor expr, Map<String, Boolean> environment) {
        for (Expr arg : expr.getArguments()) {
            check(arg, environment);
        }
    }

    public void check(Expr.RecordAccess expr, Map<String, Boolean> environment) {
        check(expr.getSource(), environment);
    }

    public void check(Expr.RecordConstructor expr, Map<String, Boolean> environment) {
        for (Pair<String, Expr> field : expr.getFields()) {
            check(field.second(), environment);
        }
    }

    public void check(Expr.Unary expr, Map<String, Boolean> environment) {
        check(expr.getExpr(), environment);
    }

    public void check(Expr.Variable expr, Map<String, Boolean> environment) {
        if (!isInitialised(expr.getName(), environment)) {
            // This variable is not definitely assigned.
            syntaxError("variable " + expr.getName() + " is not definitely assigned", file.filename,
                    expr);
        }
    }

    private static boolean isDeclared(String var, Map<String, Boolean> environment) {
        return environment.containsKey(var);
    }

    private static boolean isInitialised(String var, Map<String, Boolean> environment) {
        return isDeclared(var, environment) && environment.get(var);
    }

    private static Map<String, Boolean> mergeEnvironments(Map<String, Boolean>... environments) {
        return mergeEnvironments(Arrays.asList(environments));
    }

    private static Map<String, Boolean> mergeEnvironments(
            Collection<Map<String, Boolean>> environments) {
        Map<String, Boolean> merged = new HashMap<String, Boolean>();

        Iterator<Map<String, Boolean>> it = environments.iterator();

        if (it.hasNext()) {
            merged.putAll(it.next());
        }

        while (it.hasNext()) {
            Map<String, Boolean> next = it.next();
            for (Map.Entry<String, Boolean> entry : next.entrySet()) {
                String var = entry.getKey();

                // If it's not declared in the merged, then do nothing
                if (!isDeclared(var, merged)) {
                    continue;
                }

                // If it's not declared in the next one, remove it from the merged
                if (!isDeclared(var, next)) {
                    merged.remove(var);
                }

                // Set the appropriate declared / initialised value
                // INIT && INIT == INIT, INIT && DECL == DECL, DECL && INIT == DECL,
                // DECL && DECL = DECL
                merged.put(var, merged.get(var) && entry.getValue());
            }
        }

        return merged;
    }

    private static void setDeclared(String var, Map<String, Boolean> environment) {
        environment.put(var, false);
    }

    private static void setInitialised(String var, Map<String, Boolean> environment) {
        environment.put(var, true);
    }

    private abstract static class ControlFlowException extends Exception {

        public final Stmt stmt;

        public ControlFlowException(Stmt stmt) {
            this.stmt = stmt;
        }

        private static final class Break extends ControlFlowException {

            public Break(Stmt stmt) {
                super(stmt);
            }
        }

        private static final class Return extends ControlFlowException {

            public Return(Stmt stmt) {
                super(stmt);
            }
        }
    }
}

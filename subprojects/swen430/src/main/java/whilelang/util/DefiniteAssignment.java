package whilelang.util;

import static whilelang.util.SyntaxError.internalFailure;
import static whilelang.util.SyntaxError.syntaxError;

import java.util.HashSet;
import java.util.List;
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
        HashSet<String> environment = new HashSet<String>(constants);
        for (WhileFile.Parameter p : fd.parameters) {
            environment.add(p.name());
        }

        // Second, check all statements in the function body
        check(fd.statements, environment);
    }

    /**
     * Check that all variables used in a given list of statements are definitely assigned.
     * Furthermore, update the set of definitely assigned variables to include any which are
     * definitely assigned at the end of these statements.
     *
     * @param statements The list of statements to check.
     * @param environment The set of variables which are definitely assigned.
     */
    public void check(List<Stmt> statements, Set<String> environment) {
        for (Stmt s : statements) {
            check(s, environment);
        }
    }

    /**
     * Check that all variables used in a given statement are definitely assigned. Furthermore,
     * update the set of definitely assigned variables to include any which are definitely assigned
     * after this statement.
     *
     * @param statement The statement to check.
     * @param environment The set of variables which are definitely assigned.
     */
    public void check(Stmt stmt, Set<String> environment) {
        if (stmt instanceof Stmt.Assign) {
            check((Stmt.Assign) stmt, environment);
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
        } else if (stmt instanceof Stmt.While) {
            check((Stmt.While) stmt, environment);
        } else {
            internalFailure("unknown statement encountered (" + stmt + ")", file.filename, stmt);
        }
    }

    public void check(Stmt.Assign stmt, Set<String> environment) {
        if (stmt.getLhs() instanceof Expr.Variable) {
            Expr.Variable var = (Expr.Variable) stmt.getLhs();
            environment.add(var.getName());
        } else {
            check(stmt.getLhs(), environment);
        }

        check(stmt.getRhs(), environment);
    }

    public void check(Stmt.Print stmt, Set<String> environment) {
        check(stmt.getExpr(), environment);
    }

    public void check(Stmt.Return stmt, Set<String> environment) {
        check(stmt.getExpr(), environment);
    }

    public void check(Stmt.VariableDeclaration stmt, Set<String> environment) {
        if (environment.contains(stmt.getName())) {
            syntaxError("variable already declared: " + stmt.getName(), file.filename, stmt);
        } else if (stmt.getExpr() != null) {
            check(stmt.getExpr(), environment);
            environment.add(stmt.getName());
        }
    }

    public void check(Stmt.IfElse stmt, Set<String> environment) {
        check(stmt.getCondition(), environment);
        HashSet<String> trueEnv = new HashSet<String>(environment);
        HashSet<String> falseEnv = new HashSet<String>(environment);
        check(stmt.getTrueBranch(), trueEnv);
        check(stmt.getFalseBranch(), falseEnv);

        // add all items defined on both branches to environment
        for (String var : trueEnv) {
            if (falseEnv.contains(var)) {
                environment.add(var);
            }
        }
    }

    public void check(Stmt.For stmt, Set<String> environment) {
        check(stmt.getDeclaration(), environment);
        check(stmt.getCondition(), environment);
        check(stmt.getIncrement(), environment);
        check(stmt.getBody(), new HashSet<String>(environment));
    }

    public void check(Stmt.While stmt, Set<String> environment) {
        check(stmt.getCondition(), environment);
        check(stmt.getBody(), new HashSet<String>(environment));
    }

    /**
     * Check that all variables used in a given expression are definitely assigned.
     *
     * @param expr The expression to check.
     * @param environment The set of variables which are definitely assigned.
     */
    public void check(Expr expr, Set<String> environment) {
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

    public void check(Expr.Binary expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.Cast expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.Constant expr, Set<String> environment) {
        // Constants are obviousy already defined ;)
    }

    public void check(Expr.IndexOf expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.Invoke expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.ListConstructor expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.RecordAccess expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.RecordConstructor expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.Unary expr, Set<String> environment) {
        // TODO: implement me!
    }

    public void check(Expr.Variable expr, Set<String> environment) {
        if (!environment.contains(expr.getName())) {
            // This variable is not definitely assigned.
            syntaxError("variable " + expr.getName() + " is not definitely assigned", file.filename,
                    expr);
        }
    }
}

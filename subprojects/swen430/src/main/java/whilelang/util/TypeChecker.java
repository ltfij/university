package whilelang.util;

import static whilelang.util.SyntaxError.internalFailure;
import static whilelang.util.SyntaxError.syntaxError;

import whilelang.lang.Expr;
import whilelang.lang.Stmt;
import whilelang.lang.Type;
import whilelang.lang.Types;
import whilelang.lang.WhileFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Responsible for ensuring that all types are used appropriately. For example, that we only
 * perform arithmetic operations on arithmetic types; that we only access fields in records
 * guaranteed to have those fields, etc. </p>
 *
 * @author David J. Pearce
 */
public class TypeChecker {

    /**
     * A constant that is used to store the current methods return type in the environment so that
     * type checking may occur on return statements.
     */
    private static final String METHOD = "~CURRENT_METHOD";

    private WhileFile file;
    private WhileFile.FunDecl function;
    private HashMap<String, WhileFile.FunDecl> functions;
    private HashMap<String, WhileFile.TypeDecl> types;

    public void check(WhileFile wf) {
        this.file = wf;
        this.functions = new HashMap<String, WhileFile.FunDecl>();
        this.types = new HashMap<String, WhileFile.TypeDecl>();

        for (WhileFile.Decl declaration : wf.declarations) {
            if (declaration instanceof WhileFile.FunDecl) {
                WhileFile.FunDecl fd = (WhileFile.FunDecl) declaration;
                this.functions.put(fd.name(), fd);
            } else if (declaration instanceof WhileFile.TypeDecl) {
                WhileFile.TypeDecl fd = (WhileFile.TypeDecl) declaration;
                this.types.put(fd.name(), fd);
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

        // First, initialise the typing environment
        HashMap<String, Type> environment = new HashMap<String, Type>();
        for (WhileFile.Parameter p : fd.parameters) {
            environment.put(p.name(), p.type);
        }

        environment.put(METHOD, fd.ret);

        // Second, check all statements in the function body
        check(fd.statements, environment);
    }

    public void check(List<Stmt> statements, Map<String, Type> environment) {
        for (Stmt s : statements) {
            check(s, environment);
        }
    }

    public void check(Stmt stmt, Map<String, Type> environment) {
        if (stmt instanceof Stmt.Assign) {
            check((Stmt.Assign) stmt, environment);
        } else if (stmt instanceof Stmt.Break) {
            // Do nothing
        } else if (stmt instanceof Stmt.Switch) {
            check((Stmt.Switch) stmt, environment);
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

    public void check(Stmt.Switch stmt, Map<String, Type> environment) {
        Type type = check(stmt.getCondition(), environment);

        // Clone the environment so new variables aren't scoped outside of the switch statement
        Map<String, Type> environmentClone = new HashMap<String, Type>(environment);

        for (Map.Entry<Expr, Integer> entry : stmt.getCases().entrySet()) {
            Type caseType = check(entry.getKey(), environmentClone);
            // Check that each case value is a subtype of the condition type
            checkSubtype(type, caseType, entry.getKey());
        }

        for (Stmt s : stmt.getStmts()) {
            check(s, environmentClone);
        }
    }

    public void check(Stmt.VariableDeclaration stmt, Map<String, Type> environment) {
        if (environment.containsKey(stmt.getName())) {
            syntaxError("variable already declared: " + stmt.getName(), file.filename, stmt);
        } else if (stmt.getExpr() != null) {
            Type type = check(stmt.getExpr(), environment);
            checkSubtype(stmt.getType(), type, stmt.getExpr());
        }
        environment.put(stmt.getName(), stmt.getType());
    }

    public void check(Stmt.Assign stmt, Map<String, Type> environment) {
        Type var = check(stmt.getLhs(), environment);
        Type expr = check(stmt.getRhs(), environment);

        checkInstance(var, expr, stmt.getRhs());
    }

    public void check(Stmt.Print stmt, Map<String, Type> environment) {
        check(stmt.getExpr(), environment);
    }

    public void check(Stmt.Return stmt, Map<String, Type> environment) {
        if (stmt.getExpr() != null) {
            Type ret = check(stmt.getExpr(), environment);

            checkInstance(environment.get(METHOD), ret, stmt.getExpr());
        } else if (!(environment.get(METHOD) instanceof Type.Void)) {
            // Got to take this as a separate account because we're using Type.Void as a special
            // marker as the "any" type for lists
            syntaxError("expected type " + environment.get(METHOD) + ", found " + new Type.Void(),
                    file.filename, stmt);
        }
    }

    public void check(Stmt.IfElse stmt, Map<String, Type> environment) {
        Type condition = check(stmt.getCondition(), environment);
        checkSubtype(new Type.Bool(), condition, stmt.getCondition());

        check(stmt.getTrueBranch(), environment);
        check(stmt.getFalseBranch(), environment);
    }

    public void check(Stmt.For stmt, Map<String, Type> environment) {
        Stmt.VariableDeclaration vd = stmt.getDeclaration();
        check(vd, environment);

        // Clone the environment in order that the loop variable is only scoped
        // for the life of the loop itself.
        environment = new HashMap<String, Type>(environment);
        environment.put(vd.getName(), vd.getType());

        Type condition = check(stmt.getCondition(), environment);
        checkSubtype(new Type.Bool(), condition, stmt.getCondition());

        check(stmt.getIncrement(), environment);
        check(stmt.getBody(), environment);
    }

    public void check(Stmt.While stmt, Map<String, Type> environment) {
        Type condition = check(stmt.getCondition(), environment);
        checkSubtype(new Type.Bool(), condition, stmt.getCondition());

        check(stmt.getBody(), environment);
    }

    public Type check(Expr expr, Map<String, Type> environment) {
        Type type;

        if (expr instanceof Expr.Binary) {
            type = check((Expr.Binary) expr, environment);
        } else if (expr instanceof Expr.Cast) {
            type = check((Expr.Cast) expr, environment);
        } else if (expr instanceof Expr.Constant) {
            type = check((Expr.Constant) expr, environment);
        } else if (expr instanceof Expr.IndexOf) {
            type = check((Expr.IndexOf) expr, environment);
        } else if (expr instanceof Expr.Invoke) {
            type = check((Expr.Invoke) expr, environment);
        } else if (expr instanceof Expr.Is) {
            type = check((Expr.Is) expr, environment);
        } else if (expr instanceof Expr.ListConstructor) {
            type = check((Expr.ListConstructor) expr, environment);
        } else if (expr instanceof Expr.RecordAccess) {
            type = check((Expr.RecordAccess) expr, environment);
        } else if (expr instanceof Expr.RecordConstructor) {
            type = check((Expr.RecordConstructor) expr, environment);
        } else if (expr instanceof Expr.Unary) {
            type = check((Expr.Unary) expr, environment);
        } else if (expr instanceof Expr.Variable) {
            type = check((Expr.Variable) expr, environment);
        } else {
            internalFailure("unknown expression encountered (" + expr + ")", file.filename, expr);
            return null; // dead code
        }

        // Save the type attribute so that subsequent stages can use it without
        // having to recalculate it from scratch.
        expr.attributes().add(new Attribute.Type(type));

        return type;
    }

    public Type check(Expr.Is expr, Map<String, Type> environment) {
        check(expr.getLhs(), environment);

        return new Type.Bool();
    }

    public Type check(Expr.Binary expr, Map<String, Type> environment) {
        Type leftType = check(expr.getLhs(), environment);
        Type rightType = check(expr.getRhs(), environment);

        if (expr.getOp() != Expr.BOp.APPEND && !isEquivalent(leftType, rightType)) {
            syntaxError(
                    "operands must have identical types, found " + leftType + " and " + rightType,
                    file.filename, expr);
        }

        switch (expr.getOp()) {
            case AND:
            case OR:
                checkSubtype(new Type.Bool(), leftType, expr.getLhs());
                checkSubtype(new Type.Bool(), rightType, expr.getRhs());
                return leftType;
            case ADD:
            case SUB:
            case DIV:
            case MUL:
            case REM:
                checkInstanceOf(leftType, expr, Type.Int.class, Type.Real.class);
                return leftType;
            case EQ:
            case NEQ:
                return new Type.Bool();
            case LT:
            case LTEQ:
            case GT:
            case GTEQ:
                checkInstanceOf(leftType, expr, Type.Int.class, Type.Real.class);
                return new Type.Bool();
            case APPEND:
                leftType = checkInstanceOf(leftType, expr.getLhs(), Type.List.class,
                        Type.Strung.class);
                if (leftType instanceof Type.Strung) {
                    return leftType;
                }

                rightType = checkInstanceOf(rightType, expr.getRhs(), Type.List.class,
                        Type.Strung.class);

                if (isEquivalent(leftType, rightType)) {
                    return leftType;
                }

                return new Type.Union(Arrays.asList(leftType, rightType));
            default:
                internalFailure("unknown unary expression encountered (" + expr + ")",
                        file.filename, expr);
                return null; // dead code
        }
    }

    public Type check(Expr.Cast expr, Map<String, Type> environment) {
        Type srcType = check(expr.getSource(), environment);
        checkCast(expr.getType(), srcType, expr.getSource());

        return expr.getType();
    }

    public Type check(Expr.Constant expr, Map<String, Type> environment) {
        Object constant = expr.getValue();

        if (constant instanceof Boolean) {
            return new Type.Bool();
        } else if (constant instanceof Character) {
            return new Type.Char();
        } else if (constant instanceof Integer) {
            return new Type.Int();
        } else if (constant instanceof Double) {
            return new Type.Real();
        } else if (constant instanceof String) {
            return new Type.Strung();
        } else if (constant == null) {
            return new Type.Null();
        } else {
            internalFailure("unknown constant encountered (" + expr + ")", file.filename, expr);
            return null; // dead code
        }
    }

    public Type check(Expr.IndexOf expr, Map<String, Type> environment) {
        Type srcType = check(expr.getSource(), environment);
        Type indexType = check(expr.getIndex(), environment);
        checkSubtype(new Type.Int(), indexType, expr.getIndex());
        srcType = checkInstanceOf(srcType, expr.getSource(), Type.List.class, Type.Strung.class);
        if (srcType instanceof Type.Strung) {
            return new Type.Char();
        } else {
            return ((Type.List) srcType).getElement();
        }
    }

    public Type check(Expr.Invoke expr, Map<String, Type> environment) {
        WhileFile.FunDecl fn = functions.get(expr.getName());
        List<Expr> arguments = expr.getArguments();
        List<WhileFile.Parameter> parameters = fn.parameters;
        if (arguments.size() != parameters.size()) {
            syntaxError("incorrect number of arguments to function", file.filename, expr);
        }
        for (int i = 0; i != parameters.size(); ++i) {
            Type argument = check(arguments.get(i), environment);
            Type parameter = parameters.get(i).type;
            checkSubtype(parameter, argument, arguments.get(i));
        }
        return fn.ret;
    }

    public Type check(Expr.ListConstructor expr, Map<String, Type> environment) {
        ArrayList<Type> types = new ArrayList<Type>();
        List<Expr> arguments = expr.getArguments();
        for (Expr argument : arguments) {
            types.add(check(argument, environment));
        }
        // Now, simplify the list of types (note this is not the best way to do
        // this, but it is sufficient for our purposes here).
        ArrayList<Type> ntypes = new ArrayList<Type>();
        for (int i = 0; i < types.size(); ++i) {
            Type iType = types.get(i);
            boolean subsumed = false;
            for (int j = i + 1; j < types.size(); ++j) {
                Type jType = types.get(j);
                if (isEquivalent(iType, jType)) {
                    subsumed = true;
                    break;
                }
            }
            if (!subsumed) {
                ntypes.add(iType);
            }
        }
        if (ntypes.size() > 1) {
            return new Type.List(new Type.Union(ntypes));
        } else if (ntypes.size() == 1) {
            return new Type.List(ntypes.get(0));
        } else {
            return new Type.List(new Type.Void());
        }
    }

    public Type check(Expr.RecordAccess expr, Map<String, Type> environment) {
        Type srcType = check(expr.getSource(), environment);
        Type.Record recordType = (Type.Record) checkInstanceOf(srcType, expr.getSource(),
                Type.Record.class);
        if (!recordType.getFields().containsKey(expr.getName())) {
            syntaxError("expected type to contain field: " + expr.getName(), file.filename, expr);
        }
        return recordType.getFields().get(expr.getName());
    }

    public Type check(Expr.RecordConstructor expr, Map<String, Type> environment) {
        HashMap<String, Type> types = new HashMap<String, Type>();
        List<Pair<String, Expr>> arguments = expr.getFields();

        for (Pair<String, Expr> p : arguments) {
            types.put(p.first(), check(p.second(), environment));
        }

        return new Type.Record(types);
    }

    public Type check(Expr.Unary expr, Map<String, Type> environment) {
        Type type = check(expr.getExpr(), environment);
        switch (expr.getOp()) {
            case NEG:
                checkInstanceOf(type, expr.getExpr(), Type.Int.class, Type.Real.class);
                return type;
            case NOT:
                checkSubtype(new Type.Bool(), type, expr.getExpr());
                return type;
            case LENGTHOF:
                checkInstanceOf(type, expr.getExpr(), Type.List.class, Type.Strung.class);
                return new Type.Int();
            default:
                internalFailure("unknown unary expression encountered (" + expr + ")",
                        file.filename, expr);
                return null; // dead code
        }
    }

    public Type check(Expr.Variable expr, Map<String, Type> environment) {
        Type type = environment.get(expr.getName());
        if (type == null) {
            syntaxError("unknown variable encountered: " + expr.getName(), file.filename, expr);
        }
        return type;
    }

    /**
     * Check that a given type t2 is a castable to another type t1.
     *
     * @param t1 Supertype to check
     * @param t2 Subtype to check
     * @param element Used for determining where to report syntax errors.
     */
    public void checkCast(Type t1, Type t2, SyntacticElement element) {
        // A subtype should be checked both ways, you can technically cast to a supertype
        // E.g., casting an int to a real is casting to a supertype (int subtypes real)
        // but casting a int|string to a string is casting to a subtype (string subtypes int|string)
        if (!isSubtype(t1, t2) && !isSubtype(t2, t1)) {
            syntaxError("expected type " + t1 + ", found " + t2, file.filename, element);
        }
    }

    /**
     * Determine whether two given types are euivalent. Identical types are always equivalent.
     * Furthermore, e.g. "int|null" is equivalent to "null|int".
     *
     * @param t1 first type to compare
     * @param t2 second type to compare
     */
    public void checkEquivalent(Type t1, Type t2, SyntacticElement element) {
        if (!isEquivalent(t1, t2)) {
            syntaxError("expected type " + t1 + ", found " + t2, file.filename, element);
        }
    }

    /**
     * Determines whether the given type {@code t2} is able to be assigned to the former type,
     * {@code t1}. An acceptable assignable type is either: <ul> <li>an equivalent type</li> <li>an
     * equivalent type of one of the union bounds of the former type</li> </ul>
     * <p/>
     * This is because the language does not support implicit conversions, so we only take into
     * account equivalent types and unions.
     */
    public void checkInstance(Type t1, Type t2, SyntacticElement element) {
        if (!isInstance(t1, t2)) {
            syntaxError("expected type " + t1 + ", found " + t2, file.filename, element);
        }
    }

    /**
     * Check that a given type t2 is an instance of of another type t1. This method is useful for
     * checking that a type is, for example, a List type.
     *
     * @param element Used for determining where to report syntax errors.
     */
    public Type checkInstanceOf(Type type, SyntacticElement element, Class<?>... instances) {
        if (type instanceof Type.Named) {
            Type.Named tn = (Type.Named) type;
            if (types.containsKey(tn.getName())) {
                Type body = types.get(tn.getName()).type;
                return checkInstanceOf(body, element, instances);
            } else {
                syntaxError("unknown type encountered: " + type, file.filename, element);
            }
        }
        for (Class<?> instance : instances) {
            if (instance.isInstance(type)) {
                // This cast is clearly unsafe. It relies on the caller of this
                // method to do the right thing.
                return type;
            }
        }

        // Ok, we're going to fail with an error message. First, let's build up
        // a useful human-readable message.

        String msg = "";
        boolean firstTime = true;
        for (Class<?> instance : instances) {
            if (!firstTime) {
                msg = msg + " or ";
            }
            firstTime = false;

            if (instance.getName().endsWith("Bool")) {
                msg += "bool";
            } else if (instance.getName().endsWith("Char")) {
                msg += "char";
            } else if (instance.getName().endsWith("Int")) {
                msg += "int";
            } else if (instance.getName().endsWith("Real")) {
                msg += "real";
            } else if (instance.getName().endsWith("Strung")) {
                msg += "string";
            } else if (instance.getName().endsWith("List")) {
                msg += "list";
            } else if (instance.getName().endsWith("Record")) {
                msg += "record";
            } else {
                internalFailure("unknown type instanceof encountered (" + instance.getName() + ")",
                        file.filename, element);
                return null;
            }
        }

        syntaxError("expected instance of " + msg + ", found " + type, file.filename, element);
        return null;
    }

    /**
     * Check that a given type t2 is a subtype of another type t1.
     *
     * @param t1 Supertype to check
     * @param t2 Subtype to check
     * @param element Used for determining where to report syntax errors.
     */
    public void checkSubtype(Type t1, Type t2, SyntacticElement element) {
        if (!isSubtype(t1, t2)) {
            syntaxError("expected type " + t1 + ", found " + t2, file.filename, element);
        }
    }

    /**
     * Check that a given type t2 is a subtype of another type t1.
     *
     * @param t1 Supertype to check
     * @param t2 Subtype to check
     * @return true if {@code t2} is a subtype of {@code t1}.
     */
    public boolean isSubtype(Type t1, Type t2) {
        return Types.isSubtype(t2, t1, file);
    }

    /**
     * Checks whether two types are equivalent. Identical types are always equivalent, futhermore
     * {@code int|null} is equivalent to {@code null|int}.
     *
     * @param t1 the first type.
     * @param t2 the second type.
     * @return true if the types are equivalent.
     */
    private boolean isEquivalent(Type t1, Type t2) {
        return Types.isEquivalent(t1, t2, file);
    }

    /**
     * Determines whether the given type {@code t2} is an instance of the former type, Another way
     * of saying this is whether an object of type {@code t2} can by assigned to a type of {@code
     * t1}. An acceptable assignable type is either: <ul> <li>an equivalent type</li> <li>an
     * equivalent type of one of the union bounds of the former type</li> </ul>
     * <p/>
     * This is because the language does not support implicit conversions, so we only take into
     * account equivalent types and unions.
     */
    private boolean isInstance(Type t1, Type t2) {
        return Types.isInstance(t2, t1, file);
    }
}

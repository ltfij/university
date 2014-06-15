package whilelang.io;

import static jasm.lang.JvmTypes.JAVA_LANG_BOOLEAN;
import static jasm.lang.JvmTypes.JAVA_LANG_CHARACTER;
import static jasm.lang.JvmTypes.JAVA_LANG_DOUBLE;
import static jasm.lang.JvmTypes.JAVA_LANG_INTEGER;
import static jasm.lang.JvmTypes.JAVA_LANG_OBJECT;
import static jasm.lang.JvmTypes.JAVA_LANG_STRING;
import static jasm.lang.JvmTypes.T_BOOL;
import static jasm.lang.JvmTypes.T_CHAR;
import static jasm.lang.JvmTypes.T_DOUBLE;
import static jasm.lang.JvmTypes.T_INT;
import static jasm.lang.JvmTypes.T_VOID;
import static whilelang.util.SyntaxError.internalFailure;
import static whilelang.util.SyntaxError.syntaxError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasm.attributes.Code;
import jasm.attributes.ConstantValue;
import jasm.lang.Bytecode;
import jasm.lang.ClassFile;
import jasm.lang.JvmType;
import jasm.lang.JvmTypes;
import jasm.lang.Modifier;
import whilelang.lang.Expr;
import whilelang.lang.Stmt;
import whilelang.lang.Type;
import whilelang.lang.Types;
import whilelang.lang.WhileFile;
import whilelang.util.Attribute;
import whilelang.util.Pair;
import whilelang.util.SyntacticElement;

/**
 * TODO: Documentation
 *
 * @author Henry J. Wylde
 */
public final class While2ClassTranslator {

    private static final JvmType.Clazz JAVA_IO_PRINTSTREAM = new JvmType.Clazz("java.io",
            "PrintStream");
    private static final JvmType.Function JAVA_LANG_BOOLEAN_BOOLEANVALUE = new JvmType.Function(
            T_BOOL);
    private static final JvmType.Function JAVA_LANG_BOOLEAN_VALUEOF = new JvmType.Function(
            JAVA_LANG_BOOLEAN, T_BOOL);
    private static final JvmType.Function JAVA_LANG_CHARACTER_CHARVALUE = new JvmType.Function(
            T_CHAR);
    private static final JvmType.Function JAVA_LANG_CHARACTER_VALUEOF = new JvmType.Function(
            JAVA_LANG_CHARACTER, T_CHAR);
    private static final JvmType.Function JAVA_LANG_DOUBLE_DOUBLEVALUE = new JvmType.Function(
            T_DOUBLE);
    private static final JvmType.Function JAVA_LANG_DOUBLE_VALUEOF = new JvmType.Function(
            JAVA_LANG_DOUBLE, T_DOUBLE);
    private static final JvmType.Function JAVA_LANG_INTEGER_INTVALUE = new JvmType.Function(T_INT);
    private static final JvmType.Function JAVA_LANG_INTEGER_VALUEOF = new JvmType.Function(
            JAVA_LANG_INTEGER, T_INT);
    private static final JvmType.Function JAVA_LANG_STRING_CHARAT = new JvmType.Function(T_CHAR,
            T_INT);
    private static final JvmType.Clazz JAVA_LANG_STRINGBUILDER = new JvmType.Clazz("java.lang",
            "StringBuilder");
    private static final JvmType.Function JAVA_LANG_STRINGBUILDER_APPEND = new JvmType.Function(
            JAVA_LANG_STRINGBUILDER, JAVA_LANG_OBJECT);
    private static final JvmType.Function JAVA_LANG_STRINGBUILDER_SETCHARAT = new JvmType.Function(
            T_VOID, T_INT, T_CHAR);
    private static final JvmType.Function JAVA_LANG_STRINGBUILDER_TOSTRING = new JvmType.Function(
            JAVA_LANG_STRING);
    private static final JvmType.Clazz JAVA_LANG_SYSTEM = new JvmType.Clazz("java.lang", "System");
    private static final JvmType.Clazz JAVA_UTIL_ARRAYLIST = new JvmType.Clazz("java.util",
            "ArrayList");
    private static final JvmType.Clazz JAVA_UTIL_COLLECTION = new JvmType.Clazz("java.util",
            "Collection");
    private static final JvmType.Function JAVA_UTIL_LIST_ADDALL = new JvmType.Function(T_BOOL,
            JAVA_UTIL_COLLECTION);
    private static final JvmType.Clazz JAVA_UTIL_HASHMAP = new JvmType.Clazz("java.util",
            "HashMap");
    private static final JvmType.Clazz JAVA_UTIL_LIST = new JvmType.Clazz("java.util", "List");
    private static final JvmType.Function JAVA_UTIL_LIST_ADD = new JvmType.Function(T_BOOL,
            JAVA_LANG_OBJECT);
    private static final JvmType.Function JAVA_UTIL_LIST_GET = new JvmType.Function(
            JAVA_LANG_OBJECT, T_INT);
    private static final JvmType.Function JAVA_UTIL_LIST_SET = new JvmType.Function(
            JAVA_LANG_OBJECT, T_INT, JAVA_LANG_OBJECT);
    private static final JvmType.Clazz JAVA_UTIL_MAP = new JvmType.Clazz("java.util", "Map");
    private static final JvmType.Function JAVA_UTIL_MAP_PUT = new JvmType.Function(JAVA_LANG_OBJECT,
            JAVA_LANG_OBJECT, JAVA_LANG_OBJECT);
    private static final JvmType.Clazz WHILELANG_LANG_TYPE = new JvmType.Clazz("whilelang.lang",
            "Type");
    private static final JvmType.Function WHILELANG_RUNTIME_TYPES_CAST = new JvmType.Function(
            JAVA_LANG_OBJECT, JAVA_LANG_OBJECT, WHILELANG_LANG_TYPE);
    private static final JvmType.Function WHILELANG_RUNTIME_TYPES_ISSUBTYPE = new JvmType.Function(
            T_BOOL, JAVA_LANG_OBJECT, WHILELANG_LANG_TYPE);
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$ANY = new JvmType.Clazz("whilelang.lang",
            "Type$Any");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$BOOL = new JvmType.Clazz(
            "whilelang.lang", "Type$Bool");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$CHAR = new JvmType.Clazz(
            "whilelang.lang", "Type$Char");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$INT = new JvmType.Clazz("whilelang.lang",
            "Type$Int");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$LIST = new JvmType.Clazz(
            "whilelang.lang", "Type$List");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$NULL = new JvmType.Clazz(
            "whilelang.lang", "Type$Null");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$REAL = new JvmType.Clazz(
            "whilelang.lang", "Type$Real");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$RECORD = new JvmType.Clazz(
            "whilelang.lang", "Type$Record");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$STRUNG = new JvmType.Clazz(
            "whilelang.lang", "Type$Strung");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$UNION = new JvmType.Clazz(
            "whilelang.lang", "Type$Union");
    private static final JvmType.Clazz WHILELANG_LANG_TYPE$VOID = new JvmType.Clazz(
            "whilelang.lang", "Type$Void");
    private static final JvmType.Clazz WHILELANG_RUNTIME_NULL = new JvmType.Clazz(
            "whilelang.runtime", "Null");
    private static final JvmType.Clazz WHILELANG_RUNTIME_RECORD = new JvmType.Clazz(
            "whilelang.runtime", "Record");
    private static final JvmType.Function WHILELANG_RUNTIME_RECORD_PUTALL = new JvmType.Function(
            T_VOID, WHILELANG_RUNTIME_RECORD);
    private static final JvmType.Clazz WHILELANG_RUNTIME_TYPES = new JvmType.Clazz(
            "whilelang.runtime", "Types");
    private static final JvmType.Clazz WHILELANG_UTIL_ATTRIBUTE = new JvmType.Clazz(
            "whilelang.util", "Attribute");
    private static final JvmType.Function INIT_ATTRIBUTE_ARRAY = new JvmType.Function(T_VOID,
            new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE));
    private static final JvmType.Function INIT_COLLECTION_ATTRIBUTE_ARRAY = new JvmType.Function(
            T_VOID, JAVA_UTIL_COLLECTION, new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE));
    private static final JvmType.Function INIT_MAP_ATTRIBUTE_ARRAY = new JvmType.Function(T_VOID,
            JAVA_UTIL_MAP, new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE));
    private static final JvmType.Function INIT_TYPE_ATTRIBUTE_ARRAY = new JvmType.Function(T_VOID,
            WHILELANG_LANG_TYPE, new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE));
    private static final JvmType.Function WHILELANG_RUNTIME_RECORD_GET = new JvmType.Function(
            JAVA_LANG_OBJECT, JAVA_LANG_STRING);
    private static final JvmType.Function WHILELANG_RUNTIME_RECORD_PUT = new JvmType.Function(
            T_VOID, JAVA_LANG_STRING, JAVA_LANG_OBJECT);

    private static final JvmType.Function INIT = new JvmType.Function(T_VOID);
    private static final JvmType.Function CLINIT = new JvmType.Function(T_VOID);
    private static final JvmType.Function EQUALS = new JvmType.Function(T_BOOL, JAVA_LANG_OBJECT);
    private static final JvmType.Function MAIN = new JvmType.Function(T_VOID, new JvmType.Array(
            JAVA_LANG_STRING));

    /**
     * The current while file.
     */
    private WhileFile wf;

    /**
     * The current class file.
     */
    private ClassFile cf;

    /**
     * The current function declaration.
     */
    private WhileFile.FunDecl fd;

    /**
     * A mapping of constant field names to types.
     */
    private Map<String, JvmType> constants = new HashMap<>();
    /**
     * A mapping of variable names to slots and types.
     */
    private Map<String, Pair<Integer, JvmType>> environment = new HashMap<>();

    /**
     * A label generation count.
     */
    private int label = 0;
    /**
     * A slot generation count.
     */
    private int slot = 0;
    /**
     * A variable generation count.
     */
    private int gen = 0;

    /**
     * The current label that break statements should point to.
     */
    private String breakLabel = null;

    /**
     * Translates the given While file into a {@link ClassFile}.
     *
     * @param wf the while file to translate.
     * @return the translated Java class file.
     */
    public ClassFile translate(WhileFile wf) {
        this.wf = wf;

        JvmType.Clazz type = getJvmType(wf);
        JvmType.Clazz superClass = JAVA_LANG_OBJECT;
        List<JvmType.Clazz> interfaces = Collections.emptyList();
        List<Modifier> modifiers = Arrays.asList(Modifier.ACC_PUBLIC, Modifier.ACC_FINAL);

        cf = new ClassFile(49, type, superClass, interfaces, modifiers);

        // We need to sort the declarations so that constants are added before functions are translated
        // Order ConstDecl first, then FunDecl, then TypeDecl
        Collections.sort(wf.declarations, new Comparator<WhileFile.Decl>() {
            @Override
            public int compare(WhileFile.Decl first, WhileFile.Decl second) {
                return first.getClass().getName().compareTo(second.getClass().getName());
            }
        });

        // Create a static initialiser for use for constant fields
        // This lets complex constants initialise using bytecodes rather than loading constants from the constant pool
        ClassFile.Method initialiser = new ClassFile.Method("<clinit>", CLINIT, Arrays.asList(
                Modifier.ACC_STATIC));
        initialiser.attributes().add(new Code(new ArrayList<Bytecode>(), Collections.EMPTY_LIST,
                initialiser));
        for (WhileFile.Decl decl : wf.declarations) {
            if (decl instanceof WhileFile.ConstDecl) {
                constants.put(decl.name(), getJvmType(((WhileFile.ConstDecl) decl).constant));

                translate((WhileFile.ConstDecl) decl, initialiser);
            } else if (decl instanceof WhileFile.FunDecl) {
                translate((WhileFile.FunDecl) decl);
            }
            // Ignore type declarations
        }

        // Add the initialiser
        initialiser.attribute(Code.class).bytecodes().add(new Bytecode.Return(null));
        cf.methods().add(initialiser);

        return cf;
    }

    /**
     * Generates a new unique label.
     *
     * @return the label name.
     */
    private String generateLabel() {
        return "gen#" + label++;
    }

    /**
     * Generates a new unique variable in the local stack of the given type.
     *
     * @param type the type of the variable.
     * @return the variable name.
     */
    private String generateVariable(JvmType type) {
        String name = "$gen" + gen++;

        Pair<Integer, JvmType> info = new Pair<>(slot++, type);
        environment.put(name, info);

        // Skip the second double slot
        if (type instanceof JvmType.Double) {
            slot++;
        }

        return name;
    }

    /**
     * Generates a new unique variable in the local stack of the given type.
     *
     * @param type the type of the variable.
     * @return the variable name.
     */
    private String generateVariable(Type type) {
        return generateVariable(getJvmType(type));
    }

    /**
     * Gets the equivalent boxed JVM type of the given expression. This method will return the
     * equivalent JVM type of the expression even if it doesn't need boxing.
     *
     * @param expr the expression to get the type of.
     * @return the JVM type, guaranteed to be boxed.
     */
    private JvmType getJvmBoxedType(Expr expr) {
        JvmType type = getJvmType(getType(expr));

        if (type instanceof JvmType.Primitive) {
            return JvmTypes.boxedType((JvmType.Primitive) type);
        }

        return type;
    }

    /**
     * Gets the equivalent JVM function type of the given function declaration.
     *
     * @param function the function declaration to get the type of.
     * @return the JVM function type.
     */
    private JvmType.Function getJvmType(WhileFile.FunDecl function) {
        List<JvmType> parameterTypes = new ArrayList<>();
        for (WhileFile.Parameter parameter : function.parameters) {
            parameterTypes.add(getJvmType(parameter.type));
        }

        return new JvmType.Function(getJvmType(function.ret), parameterTypes);
    }

    /**
     * Gets the equivalent JVM type of the given While language type.
     *
     * @param type the type to get the JVM type of.
     * @return the JVM type.
     */
    private JvmType getJvmType(Type type) {
        type = Types.normalise(type, wf);

        if (type instanceof Type.Any) {
            return JAVA_LANG_OBJECT;
        } else if (type instanceof Type.Bool) {
            return T_BOOL;
        } else if (type instanceof Type.Char) {
            return T_CHAR;
        } else if (type instanceof Type.Int) {
            return T_INT;
        } else if (type instanceof Type.List) {
            return JAVA_UTIL_LIST;
        } else if (type instanceof Type.Null) {
            return WHILELANG_RUNTIME_NULL;
        } else if (type instanceof Type.Real) {
            return T_DOUBLE;
        } else if (type instanceof Type.Record) {
            return WHILELANG_RUNTIME_RECORD;
        } else if (type instanceof Type.Strung) {
            return JAVA_LANG_STRING;
        } else if (type instanceof Type.Union) {
            return JAVA_LANG_OBJECT;
        } else if (type instanceof Type.Void) {
            return T_VOID;
        }

        throw new InternalError("getType(Type) not fully implemented: " + type.getClass());
    }

    /**
     * Gets the equivalent JVM type of the given syntactic element. It will first attempt to get the
     * type of the element by analysing its attributes. If a type does not exist then an error is
     * thrown.
     *
     * @param element the element to get the type of.
     * @return the JVM type.
     */
    private JvmType getJvmType(SyntacticElement element) {
        return getJvmType(getType(element));
    }

    /**
     * Gets the equivalent JVM type of the given while file.
     *
     * @param file the while file to get the type of.
     * @return the equivalent JVM type.
     */
    private JvmType.Clazz getJvmType(WhileFile file) {
        return new JvmType.Clazz("", file.filename.substring(0, file.filename.indexOf('.')));
    }

    /**
     * Gets the type of the given syntactic element. It will attempt to get the type of the element
     * by analysing its attributes. If a type does not exist then an error is thrown.
     *
     * @param element the element to get the type of.
     * @return the type.
     */
    private Type getType(SyntacticElement element) {
        Attribute.Type type = element.attribute(Attribute.Type.class);

        if (type == null) {
            internalFailure("element does not contain a type: " + element.getClass(), wf.filename,
                    element);
        }

        return Types.normalise(type.type, wf);
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt stmt) {
        if (stmt instanceof Stmt.Assign) {
            return translate((Stmt.Assign) stmt);
        } else if (stmt instanceof Stmt.Break) {
            return translate((Stmt.Break) stmt);
        } else if (stmt instanceof Stmt.For) {
            return translate((Stmt.For) stmt);
        } else if (stmt instanceof Stmt.IfElse) {
            return translate((Stmt.IfElse) stmt);
        } else if (stmt instanceof Stmt.Print) {
            return translate((Stmt.Print) stmt);
        } else if (stmt instanceof Stmt.Return) {
            return translate((Stmt.Return) stmt);
        } else if (stmt instanceof Stmt.Switch) {
            return translate((Stmt.Switch) stmt);
        } else if (stmt instanceof Stmt.VariableDeclaration) {
            return translate((Stmt.VariableDeclaration) stmt);
        } else if (stmt instanceof Stmt.While) {
            return translate((Stmt.While) stmt);
        } else if (stmt instanceof Expr.Invoke) {
            List<Bytecode> bytecodes = new ArrayList<>();

            bytecodes.addAll(translate((Expr.Invoke) stmt));
            if (!(getType(stmt) instanceof Type.Void)) {
                bytecodes.add(new Bytecode.Pop(getJvmType(stmt)));
            }

            return bytecodes;
        }

        internalFailure("translate(Stmt) not fully implemented: " + stmt.getClass(), wf.filename,
                stmt);
        throw new RuntimeException("DEADCODE");
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.Assign stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        if (stmt.getLhs() instanceof Expr.Variable) {
            Pair<Integer, JvmType> info = environment.get(
                    ((Expr.Variable) stmt.getLhs()).getName());

            bytecodes.addAll(translate(stmt.getRhs()));
            bytecodes.addAll(translateClone(stmt.getRhs()));
            if (info.second() instanceof JvmType.Reference) {
                bytecodes.addAll(translateBoxing(stmt.getRhs()));
            }

            bytecodes.add(new Bytecode.Store(info.first(), info.second()));
        } else if (stmt.getLhs() instanceof Expr.IndexOf) {
            Expr.IndexOf lhs = (Expr.IndexOf) stmt.getLhs();

            if (getType(lhs.getSource()) instanceof Type.Strung) {
                bytecodes.add(new Bytecode.New(JAVA_LANG_STRINGBUILDER));
                bytecodes.add(new Bytecode.Dup(JAVA_LANG_STRINGBUILDER));
                bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "<init>", INIT,
                        Bytecode.InvokeMode.SPECIAL));
                bytecodes.add(new Bytecode.Dup(JAVA_LANG_STRINGBUILDER));
                bytecodes.addAll(translate(lhs.getSource()));
                bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "append",
                        JAVA_LANG_STRINGBUILDER_APPEND, Bytecode.InvokeMode.VIRTUAL));
                bytecodes.addAll(translate(lhs.getIndex()));
                bytecodes.addAll(translate(stmt.getRhs()));
                bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "setCharAt",
                        JAVA_LANG_STRINGBUILDER_SETCHARAT, Bytecode.InvokeMode.VIRTUAL));
                bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "toString",
                        JAVA_LANG_STRINGBUILDER_TOSTRING, Bytecode.InvokeMode.VIRTUAL));

                if (lhs.getSource() instanceof Expr.Variable) {
                    Pair<Integer, JvmType> info = environment.get(
                            ((Expr.Variable) lhs.getSource()).getName());

                    bytecodes.add(new Bytecode.Store(info.first(), info.second()));
                } else if (lhs.getSource() instanceof Expr.IndexOf || lhs
                        .getSource() instanceof Expr.RecordAccess) {
                    String tmp = generateVariable(new Type.Strung());

                    Pair<Integer, JvmType> info = environment.get(tmp);

                    bytecodes.add(new Bytecode.Store(info.first(), info.second()));
                    bytecodes.addAll(translate(new Stmt.Assign((Expr.LVal) lhs.getSource(),
                            new Expr.Variable(tmp, new Attribute.Type(new Type.Strung())),
                            stmt.attributes())));
                } else {
                    syntaxError("cannot perform indexof on a " + lhs.getSource().getClass(),
                            wf.filename, stmt);
                }
            } else {
                // Must be a list
                bytecodes.addAll(translate(lhs.getSource()));
                bytecodes.addAll(translate(lhs.getIndex()));
                bytecodes.addAll(translate(stmt.getRhs()));
                bytecodes.addAll(translateClone(stmt.getRhs()));
                bytecodes.addAll(translateBoxing(stmt.getRhs()));
                bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "set", JAVA_UTIL_LIST_SET,
                        Bytecode.InvokeMode.INTERFACE));
                bytecodes.add(new Bytecode.Pop(JAVA_LANG_OBJECT));
            }
        } else if (stmt.getLhs() instanceof Expr.RecordAccess) {
            Expr.RecordAccess lhs = (Expr.RecordAccess) stmt.getLhs();

            bytecodes.addAll(translate(lhs.getSource()));
            bytecodes.add(new Bytecode.LoadConst(lhs.getName()));
            bytecodes.addAll(translate(stmt.getRhs()));
            bytecodes.addAll(translateClone(stmt.getRhs()));
            bytecodes.addAll(translateBoxing(stmt.getRhs()));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_RECORD, "put",
                    WHILELANG_RUNTIME_RECORD_PUT, Bytecode.InvokeMode.VIRTUAL));
        } else {
            internalFailure(
                    "translate(Stmt.Assign) not fully implemented: " + stmt.getLhs().getClass(),
                    wf.filename, stmt.getLhs());
        }

        return bytecodes;
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.Break stmt) {
        return Arrays.<Bytecode>asList(new Bytecode.Goto(breakLabel));
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.For stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        String startLabel = generateLabel();
        String endLabel = generateLabel();

        breakLabel = endLabel;

        // Initialisation
        bytecodes.addAll(translate(stmt.getDeclaration()));
        // Condition check
        bytecodes.add(new Bytecode.Label(startLabel));
        bytecodes.addAll(translate(stmt.getCondition()));
        bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, endLabel));
        // Body
        bytecodes.addAll(translate(stmt.getBody()));
        // Increment
        bytecodes.addAll(translate(stmt.getIncrement()));
        bytecodes.add(new Bytecode.Goto(startLabel));
        bytecodes.add(new Bytecode.Label(endLabel));

        return bytecodes;
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.IfElse stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        String trueLabel = generateLabel();
        String endLabel = generateLabel();

        // Condition check
        bytecodes.addAll(translate(stmt.getCondition()));
        bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, trueLabel));
        // True branch
        bytecodes.addAll(translate(stmt.getTrueBranch()));
        bytecodes.add(new Bytecode.Goto(endLabel));
        // False branch
        bytecodes.add(new Bytecode.Label(trueLabel));
        bytecodes.addAll(translate(stmt.getFalseBranch()));
        bytecodes.add(new Bytecode.Label(endLabel));

        return bytecodes;
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.Print stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.add(new Bytecode.GetField(JAVA_LANG_SYSTEM, "out", JAVA_IO_PRINTSTREAM,
                Bytecode.FieldMode.STATIC));
        bytecodes.addAll(translate(stmt.getExpr()));
        JvmType type = getJvmType(stmt.getExpr());
        bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRING, "valueOf", new JvmType.Function(
                JAVA_LANG_STRING, type instanceof JvmType.Reference ? JAVA_LANG_OBJECT : type),
                Bytecode.InvokeMode.STATIC
        ));
        bytecodes.add(new Bytecode.Invoke(JAVA_IO_PRINTSTREAM, "println", new JvmType.Function(
                T_VOID, JAVA_LANG_STRING), Bytecode.InvokeMode.VIRTUAL
        ));

        return bytecodes;
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.Return stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        JvmType returnType = getJvmType(fd.ret);

        if (stmt.getExpr() != null) {
            bytecodes.addAll(translate(stmt.getExpr()));
            if (returnType instanceof JvmType.Reference) {
                bytecodes.addAll(translateBoxing(stmt.getExpr()));
            }
        }

        bytecodes.add(new Bytecode.Return(returnType instanceof JvmType.Void ? null : returnType));

        return bytecodes;
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.Switch stmt) {
        // Need to translate int, char, bool, [int], {f: int}, [int|null], int|bool, str
        // int, char, bool are simple
        // The rest require using the hashcode method (like Java7s method for Strings)

        Type type = getType(stmt);
        if (type instanceof Type.Bool || type instanceof Type.Char || type instanceof Type.Int) {
            return translateSwitchSimple(stmt);
        }

        // Looks like got to do it the complex way...
        return translateSwitchComplex(stmt);
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.VariableDeclaration stmt) {
        Pair<Integer, JvmType> info = new Pair<>(slot++, getJvmType(stmt.getType()));
        environment.put(stmt.getName(), info);

        // Skip the second double slot
        if (stmt.getType() instanceof Type.Real) {
            slot++;
        }

        if (stmt.getExpr() != null) {
            return translate(new Stmt.Assign(new Expr.Variable(stmt.getName()), stmt.getExpr()));
        }

        return Collections.emptyList();
    }

    /**
     * Translates the given statement into a list of bytecodes.
     *
     * @param stmt the statement to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Stmt.While stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        String startLabel = generateLabel();
        String endLabel = generateLabel();

        breakLabel = endLabel;

        bytecodes.add(new Bytecode.Label(startLabel));
        bytecodes.addAll(translate(stmt.getCondition()));
        bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, endLabel));
        bytecodes.addAll(translate(stmt.getBody()));
        bytecodes.add(new Bytecode.Goto(startLabel));
        bytecodes.add(new Bytecode.Label(endLabel));

        return bytecodes;
    }

    /**
     * Translates the given statements into a list of bytecodes.
     *
     * @param stmts the statements to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(List<Stmt> stmts) {
        List<Bytecode> bytecodes = new ArrayList<>();

        for (Stmt stmt : stmts) {
            bytecodes.addAll(translate(stmt));
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr expr) {
        if (expr instanceof Expr.Binary) {
            return translate((Expr.Binary) expr);
        } else if (expr instanceof Expr.Cast) {
            return translate((Expr.Cast) expr);
        } else if (expr instanceof Expr.Constant) {
            return translate((Expr.Constant) expr);
        } else if (expr instanceof Expr.IndexOf) {
            return translate((Expr.IndexOf) expr);
        } else if (expr instanceof Expr.Invoke) {
            return translate((Expr.Invoke) expr);
        } else if (expr instanceof Expr.Is) {
            return translate((Expr.Is) expr);
        } else if (expr instanceof Expr.ListConstructor) {
            return translate((Expr.ListConstructor) expr);
        } else if (expr instanceof Expr.RecordAccess) {
            return translate((Expr.RecordAccess) expr);
        } else if (expr instanceof Expr.RecordConstructor) {
            return translate((Expr.RecordConstructor) expr);
        } else if (expr instanceof Expr.Unary) {
            return translate((Expr.Unary) expr);
        } else if (expr instanceof Expr.Variable) {
            return translate((Expr.Variable) expr);
        }

        internalFailure("translate(Expr) not fully implemented: " + expr.getClass(), wf.filename,
                expr);
        throw new RuntimeException("DEADCODE");
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Binary expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        switch (expr.getOp()) {
            case ADD:
                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.BinOp(Bytecode.BinOp.ADD, getJvmType(expr.getLhs())));
                break;
            case AND:
                String trueLabel = generateLabel();
                String endLabel = generateLabel();

                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, trueLabel));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, trueLabel));
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case APPEND:
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.addAll(translateBoxing(expr.getRhs()));
                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translateBoxing(expr.getLhs()));

                if (getType(expr.getLhs()) instanceof Type.Strung) {
                    bytecodes.add(new Bytecode.New(JAVA_LANG_STRINGBUILDER));
                    bytecodes.add(new Bytecode.DupX2());
                    bytecodes.add(new Bytecode.DupX2());
                    bytecodes.add(new Bytecode.DupX1());
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "<init>", INIT,
                            Bytecode.InvokeMode.SPECIAL));
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "append",
                            JAVA_LANG_STRINGBUILDER_APPEND, Bytecode.InvokeMode.VIRTUAL));
                    bytecodes.add(new Bytecode.Pop(JAVA_LANG_STRINGBUILDER));
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "append",
                            JAVA_LANG_STRINGBUILDER_APPEND, Bytecode.InvokeMode.VIRTUAL));
                    bytecodes.add(new Bytecode.Pop(JAVA_LANG_STRINGBUILDER));
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRINGBUILDER, "toString",
                            JAVA_LANG_STRINGBUILDER_TOSTRING, Bytecode.InvokeMode.VIRTUAL));
                } else {
                    // Type must be a list

                    bytecodes.add(new Bytecode.New(JAVA_UTIL_ARRAYLIST));
                    bytecodes.add(new Bytecode.DupX2());
                    bytecodes.add(new Bytecode.DupX2());
                    bytecodes.add(new Bytecode.DupX1());
                    bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_ARRAYLIST, "<init>", INIT,
                            Bytecode.InvokeMode.SPECIAL));
                    bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "addAll",
                            JAVA_UTIL_LIST_ADDALL, Bytecode.InvokeMode.INTERFACE));
                    bytecodes.add(new Bytecode.Pop(T_BOOL));
                    bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "addAll",
                            JAVA_UTIL_LIST_ADDALL, Bytecode.InvokeMode.INTERFACE));
                    bytecodes.add(new Bytecode.Pop(T_BOOL));
                }
                break;
            case DIV:
                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.BinOp(Bytecode.BinOp.DIV, getJvmType(expr.getLhs())));
                break;
            case EQ:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                Type type = getType(expr.getLhs());

                if (type instanceof Type.Real) {
                    bytecodes.addAll(translate(expr.getLhs()));
                    bytecodes.addAll(translate(expr.getRhs()));
                    bytecodes.add(new Bytecode.Cmp(T_DOUBLE, Bytecode.Cmp.LT));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.NE, trueLabel));
                } else if (type instanceof Type.Bool || type instanceof Type.Char
                        || type instanceof Type.Int) {
                    bytecodes.addAll(translate(expr.getLhs()));
                    bytecodes.addAll(translate(expr.getRhs()));
                    bytecodes.add(new Bytecode.IfCmp(Bytecode.IfCmp.NE, getJvmType(type),
                            trueLabel));
                } else {
                    bytecodes.addAll(translate(expr.getLhs()));
                    bytecodes.addAll(translateBoxing(expr.getLhs()));
                    bytecodes.addAll(translate(expr.getRhs()));
                    bytecodes.addAll(translateBoxing(expr.getRhs()));
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_OBJECT, "equals", EQUALS,
                            Bytecode.InvokeMode.VIRTUAL));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, trueLabel));
                }

                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case GT:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));

                if (getType(expr.getLhs()) instanceof Type.Real) {
                    bytecodes.add(new Bytecode.Cmp(T_DOUBLE, Bytecode.Cmp.LT));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.LE, trueLabel));
                } else {
                    bytecodes.add(new Bytecode.IfCmp(Bytecode.IfCmp.LE, getJvmType(expr.getLhs()),
                            trueLabel));
                }
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case GTEQ:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));

                if (getType(expr.getLhs()) instanceof Type.Real) {
                    bytecodes.add(new Bytecode.Cmp(T_DOUBLE, Bytecode.Cmp.LT));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.LT, trueLabel));
                } else {
                    bytecodes.add(new Bytecode.IfCmp(Bytecode.IfCmp.LT, getJvmType(expr.getLhs()),
                            trueLabel));
                }
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case LT:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));

                if (getType(expr.getLhs()) instanceof Type.Real) {
                    bytecodes.add(new Bytecode.Cmp(T_DOUBLE, Bytecode.Cmp.LT));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.GE, trueLabel));
                } else {
                    bytecodes.add(new Bytecode.IfCmp(Bytecode.IfCmp.GE, getJvmType(expr.getLhs()),
                            trueLabel));
                }
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case LTEQ:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));

                if (getType(expr.getLhs()) instanceof Type.Real) {
                    bytecodes.add(new Bytecode.Cmp(T_DOUBLE, Bytecode.Cmp.LT));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.GT, trueLabel));
                } else {
                    bytecodes.add(new Bytecode.IfCmp(Bytecode.IfCmp.GT, getJvmType(expr.getLhs()),
                            trueLabel));
                }
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case MUL:
                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.BinOp(Bytecode.BinOp.MUL, getJvmType(expr.getLhs())));
                break;
            case NEQ:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                type = getType(expr.getLhs());

                if (type instanceof Type.Real) {
                    bytecodes.addAll(translate(expr.getLhs()));
                    bytecodes.addAll(translate(expr.getRhs()));
                    bytecodes.add(new Bytecode.Cmp(T_DOUBLE, Bytecode.Cmp.LT));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.NE, trueLabel));
                } else if (type instanceof Type.Bool || type instanceof Type.Char
                        || type instanceof Type.Int) {
                    bytecodes.addAll(translate(expr.getLhs()));
                    bytecodes.addAll(translate(expr.getRhs()));
                    bytecodes.add(new Bytecode.IfCmp(Bytecode.IfCmp.NE, getJvmType(type),
                            trueLabel));
                } else {
                    bytecodes.addAll(translate(expr.getLhs()));
                    bytecodes.addAll(translateBoxing(expr.getLhs()));
                    bytecodes.addAll(translate(expr.getRhs()));
                    bytecodes.addAll(translateBoxing(expr.getRhs()));
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_OBJECT, "equals", EQUALS,
                            Bytecode.InvokeMode.VIRTUAL));
                    bytecodes.add(new Bytecode.If(Bytecode.IfMode.EQ, trueLabel));
                }

                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case OR:
                trueLabel = generateLabel();
                endLabel = generateLabel();

                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.add(new Bytecode.If(Bytecode.IfMode.NE, trueLabel));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.If(Bytecode.IfMode.NE, trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            case REM:
                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.BinOp(Bytecode.BinOp.REM, getJvmType(expr.getLhs())));
                break;
            case SUB:
                bytecodes.addAll(translate(expr.getLhs()));
                bytecodes.addAll(translate(expr.getRhs()));
                bytecodes.add(new Bytecode.BinOp(Bytecode.BinOp.SUB, getJvmType(expr.getLhs())));
                break;
            default:
                internalFailure("translate(Expr.Binary) not fully implemented: " + expr.getOp(),
                        wf.filename, expr);
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Cast expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.addAll(translate(expr.getSource()));
        bytecodes.addAll(translateBoxing(expr.getSource()));
        bytecodes.addAll(translate(expr.getType()));
        bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_TYPES, "cast",
                WHILELANG_RUNTIME_TYPES_CAST, Bytecode.InvokeMode.STATIC));
        bytecodes.addAll(translateUnboxing(expr));

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Constant expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        if (expr.getValue() == null) {
            bytecodes.add(new Bytecode.GetField(WHILELANG_RUNTIME_NULL, "INSTANCE",
                    WHILELANG_RUNTIME_NULL, Bytecode.FieldMode.STATIC));
        } else {
            bytecodes.add(new Bytecode.LoadConst(expr.getValue()));
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.IndexOf expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.addAll(translate(expr.getSource()));
        bytecodes.addAll(translate(expr.getIndex()));
        if (getType(expr.getSource()) instanceof Type.Strung) {
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRING, "charAt", JAVA_LANG_STRING_CHARAT,
                    Bytecode.InvokeMode.VIRTUAL));
        } else {
            // Must be a list type
            bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "get", JAVA_UTIL_LIST_GET,
                    Bytecode.InvokeMode.INTERFACE));
            bytecodes.addAll(translateUnboxing(expr));
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Invoke expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        JvmType.Function type = getJvmType(wf.functions(expr.getName()).get(0));

        for (int i = 0; i < expr.getArguments().size(); i++) {
            bytecodes.addAll(translate(expr.getArguments().get(i)));
            bytecodes.addAll(translateClone(expr.getArguments().get(i)));

            if (type.parameterTypes().get(i) instanceof JvmType.Reference) {
                bytecodes.addAll(translateBoxing(expr.getArguments().get(i)));
            }
        }
        bytecodes.add(new Bytecode.Invoke(getJvmType(wf), expr.getName(), getJvmType(wf.functions(
                expr.getName()).get(0)), Bytecode.InvokeMode.STATIC));

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Is expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.addAll(translate(expr.getLhs()));
        bytecodes.addAll(translateBoxing(expr.getLhs()));
        bytecodes.addAll(translate(expr.getRhs()));
        bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_TYPES, "isSubtype",
                WHILELANG_RUNTIME_TYPES_ISSUBTYPE, Bytecode.InvokeMode.STATIC));

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.ListConstructor expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.add(new Bytecode.New(JAVA_UTIL_ARRAYLIST));
        bytecodes.add(new Bytecode.Dup(JAVA_UTIL_ARRAYLIST));
        bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_ARRAYLIST, "<init>", INIT,
                Bytecode.InvokeMode.SPECIAL));
        for (Expr argument : expr.getArguments()) {
            bytecodes.add(new Bytecode.Dup(JAVA_UTIL_ARRAYLIST));
            bytecodes.addAll(translate(argument));
            bytecodes.addAll(translateBoxing(argument));
            bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "add", JAVA_UTIL_LIST_ADD,
                    Bytecode.InvokeMode.INTERFACE));
            bytecodes.add(new Bytecode.Pop(T_BOOL));
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.RecordAccess expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.addAll(translate(expr.getSource()));
        bytecodes.add(new Bytecode.LoadConst(expr.getName()));
        bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_RECORD, "get",
                WHILELANG_RUNTIME_RECORD_GET, Bytecode.InvokeMode.VIRTUAL));
        bytecodes.addAll(translateUnboxing(expr));

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.RecordConstructor expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.add(new Bytecode.New(WHILELANG_RUNTIME_RECORD));
        bytecodes.add(new Bytecode.Dup(WHILELANG_RUNTIME_RECORD));
        bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_RECORD, "<init>", INIT,
                Bytecode.InvokeMode.SPECIAL));
        for (Pair<String, Expr> argument : expr.getFields()) {
            bytecodes.add(new Bytecode.Dup(WHILELANG_RUNTIME_RECORD));
            bytecodes.add(new Bytecode.LoadConst(argument.first()));
            bytecodes.addAll(translate(argument.second()));
            bytecodes.addAll(translateBoxing(argument.second()));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_RECORD, "put",
                    WHILELANG_RUNTIME_RECORD_PUT, Bytecode.InvokeMode.VIRTUAL));
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Unary expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        bytecodes.addAll(translate(expr.getExpr()));

        switch (expr.getOp()) {
            case LENGTHOF:
                if (getType(expr.getExpr()) instanceof Type.Strung) {
                    bytecodes.add(new Bytecode.Invoke(JAVA_LANG_STRING, "length",
                            new JvmType.Function(T_INT), Bytecode.InvokeMode.VIRTUAL));
                } else {
                    bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "size", new JvmType.Function(
                            T_INT), Bytecode.InvokeMode.INTERFACE));
                }
                break;
            case NEG:
                bytecodes.add(new Bytecode.Neg(getJvmType(expr.getExpr())));
                break;
            case NOT:
                String trueLabel = generateLabel();
                String endLabel = generateLabel();

                bytecodes.add(new Bytecode.If(Bytecode.IfMode.NE, trueLabel));
                bytecodes.add(new Bytecode.LoadConst(1));
                bytecodes.add(new Bytecode.Goto(endLabel));
                bytecodes.add(new Bytecode.Label(trueLabel));
                bytecodes.add(new Bytecode.LoadConst(0));
                bytecodes.add(new Bytecode.Label(endLabel));
                break;
            default:
                internalFailure("translate(Expr.Unary) not fully implemented: " + expr.getOp(),
                        wf.filename, expr);
        }

        return bytecodes;
    }

    /**
     * Translates the given expression into a list of bytecodes.
     *
     * @param expr the expression to translate.
     * @return the list of bytecodes.
     */
    private List<Bytecode> translate(Expr.Variable expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        String name = expr.getName();

        // Check if the variable is a constant or not
        if (constants.containsKey(name)) {
            JvmType type = constants.get(name);

            bytecodes.add(new Bytecode.GetField(getJvmType(wf), name, type,
                    Bytecode.FieldMode.STATIC));
        } else {
            Pair<Integer, JvmType> info = environment.get(name);

            bytecodes.add(new Bytecode.Load(info.first(), info.second()));
        }

        return bytecodes;
    }


    private void translate(WhileFile.ConstDecl constant, ClassFile.Method initialiser) {
        String name = constant.name;
        JvmType type = getJvmType(constant.constant);
        List<Modifier> modifiers = Arrays.asList(Modifier.ACC_PUBLIC, Modifier.ACC_FINAL,
                Modifier.ACC_STATIC);

        ClassFile.Field field = new ClassFile.Field(name, type, modifiers);

        if (constant.constant instanceof Expr.Constant) {
            // A simple constant, can use a ConstantValue attribute
            field.attributes().add(new ConstantValue(
                    ((Expr.Constant) constant.constant).getValue()));
        } else {
            // Complicated constant (i.e. list), need to use the static initialiser
            List<Bytecode> bytecodes = new ArrayList<>();

            bytecodes.addAll(translate(constant.constant));
            bytecodes.add(new Bytecode.PutField(getJvmType(wf), name, type,
                    Bytecode.FieldMode.STATIC));

            initialiser.attribute(Code.class).bytecodes().addAll(bytecodes);
        }

        cf.fields().add(field);
    }

    private void translate(WhileFile.FunDecl function) {
        String name = function.name;
        JvmType.Function type = getJvmType(function);
        List<Modifier> modifiers = Arrays.asList(Modifier.ACC_PUBLIC, Modifier.ACC_STATIC);

        // Handle the special main case so that we can simply call "java" on the compiled class file
        if (name.equals("main")) {
            type = MAIN;
        }

        ClassFile.Method method = new ClassFile.Method(name, type, modifiers);

        // Reset helper variables
        fd = function;
        environment.clear();
        label = 0;
        slot = 0;
        gen = 0;

        // Add in a filler to account for the [str] parameter
        if (name.equals("main")) {
            slot++;
        }
        for (WhileFile.Parameter parameter : function.parameters) {
            Pair<Integer, JvmType> info = new Pair<>(slot++, getJvmType(parameter.type));
            environment.put(parameter.name(), info);

            // Skip the second double slot
            if (parameter.type instanceof Type.Real) {
                slot++;
            }
        }

        List<Bytecode> bytecodes = translate(function.statements);
        if (function.ret instanceof Type.Void) {
            bytecodes.add(new Bytecode.Return(null));
        }

        method.attributes().add(new Code(bytecodes, Collections.EMPTY_LIST, method));

        cf.methods().add(method);
    }

    private List<Bytecode> translate(Type type) {
        List<Bytecode> bytecodes = new ArrayList<>();

        type = Types.normalise(type, wf);

        if (type instanceof Type.Any) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$ANY));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$ANY));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$ANY, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Bool) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$BOOL));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$BOOL));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$BOOL, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Char) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$CHAR));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$CHAR));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$CHAR, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Int) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$INT));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$INT));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$INT, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.List) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$LIST));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$LIST));
            bytecodes.addAll(translate(((Type.List) type).getElement()));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$LIST, "<init>",
                    INIT_TYPE_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Null) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$NULL));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$NULL));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$NULL, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Real) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$REAL));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$REAL));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$REAL, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Record) {
            Type.Record record = (Type.Record) type;

            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$RECORD));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$RECORD));
            bytecodes.add(new Bytecode.New(JAVA_UTIL_HASHMAP));
            bytecodes.add(new Bytecode.Dup(JAVA_UTIL_HASHMAP));
            bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_HASHMAP, "<init>", INIT,
                    Bytecode.InvokeMode.SPECIAL));
            for (Map.Entry<String, Type> field : record.getFields().entrySet()) {
                bytecodes.add(new Bytecode.Dup(JAVA_UTIL_HASHMAP));
                bytecodes.add(new Bytecode.LoadConst(field.getKey()));
                bytecodes.addAll(translate(field.getValue()));
                bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_MAP, "put", JAVA_UTIL_MAP_PUT,
                        Bytecode.InvokeMode.INTERFACE));
                bytecodes.add(new Bytecode.Pop(JAVA_LANG_OBJECT));
            }
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$RECORD, "<init>",
                    INIT_MAP_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Strung) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$STRUNG));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$STRUNG));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$STRUNG, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Union) {
            Type.Union union = (Type.Union) type;

            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$UNION));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$UNION));
            bytecodes.add(new Bytecode.New(JAVA_UTIL_ARRAYLIST));
            bytecodes.add(new Bytecode.Dup(JAVA_UTIL_ARRAYLIST));
            bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_ARRAYLIST, "<init>", INIT,
                    Bytecode.InvokeMode.SPECIAL));
            for (Type bound : union.getBounds()) {
                bytecodes.add(new Bytecode.Dup(JAVA_UTIL_ARRAYLIST));
                bytecodes.addAll(translate(bound));
                bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "add", JAVA_UTIL_LIST_ADD,
                        Bytecode.InvokeMode.INTERFACE));
                bytecodes.add(new Bytecode.Pop(T_BOOL));
            }
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$UNION, "<init>",
                    INIT_COLLECTION_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else if (type instanceof Type.Void) {
            bytecodes.add(new Bytecode.New(WHILELANG_LANG_TYPE$VOID));
            bytecodes.add(new Bytecode.Dup(WHILELANG_LANG_TYPE$VOID));
            bytecodes.add(new Bytecode.LoadConst(0));
            bytecodes.add(new Bytecode.New(new JvmType.Array(WHILELANG_UTIL_ATTRIBUTE), 1));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_LANG_TYPE$VOID, "<init>",
                    INIT_ATTRIBUTE_ARRAY, Bytecode.InvokeMode.SPECIAL));
        } else {
            throw new InternalError("translate(Type) not fully implemented: " + type.getClass());
        }

        return bytecodes;
    }

    private List<Bytecode> translateBoxing(Expr expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        Type type = getType(expr);
        if (type instanceof Type.Bool) {
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_BOOLEAN, "valueOf",
                    JAVA_LANG_BOOLEAN_VALUEOF, Bytecode.InvokeMode.STATIC));
        } else if (type instanceof Type.Char) {
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_CHARACTER, "valueOf",
                    JAVA_LANG_CHARACTER_VALUEOF, Bytecode.InvokeMode.STATIC));
        } else if (type instanceof Type.Int) {
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_INTEGER, "valueOf",
                    JAVA_LANG_INTEGER_VALUEOF, Bytecode.InvokeMode.STATIC));
        } else if (type instanceof Type.Real) {
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_DOUBLE, "valueOf", JAVA_LANG_DOUBLE_VALUEOF,
                    Bytecode.InvokeMode.STATIC));
        }

        return bytecodes;
    }

    private List<Bytecode> translateClone(Expr expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        // Clone the values if it is a list or record
        if (getType(expr) instanceof Type.List) {
            bytecodes.add(new Bytecode.New(JAVA_UTIL_ARRAYLIST));
            bytecodes.add(new Bytecode.DupX1());
            bytecodes.add(new Bytecode.DupX1());
            bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_ARRAYLIST, "<init>", INIT,
                    Bytecode.InvokeMode.SPECIAL));
            bytecodes.add(new Bytecode.Invoke(JAVA_UTIL_LIST, "addAll", JAVA_UTIL_LIST_ADDALL,
                    Bytecode.InvokeMode.INTERFACE));
            bytecodes.add(new Bytecode.Pop(T_BOOL));
        } else if (getType(expr) instanceof Type.Record) {
            bytecodes.add(new Bytecode.New(WHILELANG_RUNTIME_RECORD));
            bytecodes.add(new Bytecode.DupX1());
            bytecodes.add(new Bytecode.DupX1());
            bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_RECORD, "<init>", INIT,
                    Bytecode.InvokeMode.SPECIAL));
            bytecodes.add(new Bytecode.Invoke(WHILELANG_RUNTIME_RECORD, "putAll",
                    WHILELANG_RUNTIME_RECORD_PUTALL, Bytecode.InvokeMode.VIRTUAL));
        }

        return bytecodes;
    }

    private List<Bytecode> translateSwitchComplex(Stmt.Switch stmt) {
        // I got lazy and couldn't be bothered implementing the switch using hashcodes... Instead I just used a series of if statements

        List<Bytecode> bytecodes = new ArrayList<>();

        // Mapping of statement indices to labels
        Map<Integer, String> labels = new HashMap<>();

        // Populate the labels map
        for (Map.Entry<Expr, Integer> entry : stmt.getCases().entrySet()) {
            if (!labels.containsKey(entry.getValue())) {
                labels.put(entry.getValue(), generateLabel());
            }
        }

        // Create the default label
        if (!labels.containsKey(stmt.getDefaultCase())) {
            labels.put(stmt.getDefaultCase(), generateLabel());
        }
        String defaultLabel = labels.get(stmt.getDefaultCase());

        // Set up the break label statements should target
        String endLabel = generateLabel();
        breakLabel = endLabel;

        // Add the bytecodes
        bytecodes.addAll(translate(stmt.getCondition()));
        bytecodes.addAll(translateBoxing(stmt.getCondition()));

        String condition = generateVariable(getJvmBoxedType(stmt.getCondition()));
        Pair<Integer, JvmType> info = environment.get(condition);
        bytecodes.add(new Bytecode.Store(info.first(), info.second()));

        for (Map.Entry<Expr, Integer> entry : stmt.getCases().entrySet()) {
            String target = labels.get(entry.getValue());

            bytecodes.add(new Bytecode.Load(info.first(), info.second()));
            bytecodes.addAll(translate(entry.getKey()));
            bytecodes.addAll(translateBoxing(entry.getKey()));
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_OBJECT, "equals", EQUALS,
                    Bytecode.InvokeMode.VIRTUAL));
            bytecodes.add(new Bytecode.If(Bytecode.IfMode.NE, target));
        }

        bytecodes.add(new Bytecode.Goto(defaultLabel));

        // Add the statement bytecodes with their labels
        for (int i = 0; i < stmt.getStmts().size(); i++) {
            if (labels.containsKey(i)) {
                bytecodes.add(new Bytecode.Label(labels.get(i)));
            }

            bytecodes.addAll(translate(stmt.getStmts().get(i)));
        }
        // If the default case wasn't added in the previous loop, add it now at the end
        if (stmt.getDefaultCase() < 0 || stmt.getDefaultCase() >= stmt.getStmts().size()) {
            bytecodes.add(new Bytecode.Label(defaultLabel));
        }
        bytecodes.add(new Bytecode.Label(endLabel));

        return bytecodes;
    }

    private List<Bytecode> translateSwitchSimple(Stmt.Switch stmt) {
        List<Bytecode> bytecodes = new ArrayList<>();

        // Mapping of statement indices to labels
        Map<Integer, String> labels = new HashMap<>();

        // List of switch values to labels
        List<jasm.util.Pair<Integer, String>> cases = new ArrayList<>();

        // Populate the labels map and cases list
        for (Map.Entry<Expr, Integer> entry : stmt.getCases().entrySet()) {
            Object value = ((Expr.Constant) entry.getKey()).getValue();

            // Get the integer value to switch on
            Integer key;
            if (value instanceof Boolean) {
                key = ((Boolean) value).booleanValue() ? 1 : 0;
            } else if (value instanceof Character) {
                key = Integer.valueOf(((Character) value).charValue());
            } else {
                key = (Integer) value;
            }

            // Create a label for the statement index
            if (!labels.containsKey(entry.getValue())) {
                labels.put(entry.getValue(), generateLabel());
            }
            String label = labels.get(entry.getValue());

            // Create a case for the integer value and label
            cases.add(new jasm.util.Pair<>(key, label));
        }

        // Create the default label
        if (!labels.containsKey(stmt.getDefaultCase())) {
            labels.put(stmt.getDefaultCase(), generateLabel());
        }
        String defaultLabel = labels.get(stmt.getDefaultCase());

        // Set up the break label statements should target
        String endLabel = generateLabel();
        breakLabel = endLabel;

        // Add the bytecodes
        bytecodes.addAll(translate(stmt.getCondition()));
        bytecodes.add(new Bytecode.Switch(defaultLabel, cases));
        for (int i = 0; i < stmt.getStmts().size(); i++) {
            if (labels.containsKey(i)) {
                bytecodes.add(new Bytecode.Label(labels.get(i)));
            }

            bytecodes.addAll(translate(stmt.getStmts().get(i)));
        }
        // If the default case wasn't added in the previous loop, add it now at the end
        if (stmt.getDefaultCase() < 0 || stmt.getDefaultCase() >= stmt.getStmts().size()) {
            bytecodes.add(new Bytecode.Label(defaultLabel));
        }
        bytecodes.add(new Bytecode.Label(endLabel));

        return bytecodes;
    }

    private List<Bytecode> translateUnboxing(Expr expr) {
        List<Bytecode> bytecodes = new ArrayList<>();

        Type type = getType(expr);
        if (type instanceof Type.Bool) {
            bytecodes.add(new Bytecode.CheckCast(JAVA_LANG_BOOLEAN));
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_BOOLEAN, "booleanValue",
                    JAVA_LANG_BOOLEAN_BOOLEANVALUE, Bytecode.InvokeMode.VIRTUAL));
        } else if (type instanceof Type.Char) {
            bytecodes.add(new Bytecode.CheckCast(JAVA_LANG_CHARACTER));
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_CHARACTER, "charValue",
                    JAVA_LANG_CHARACTER_CHARVALUE, Bytecode.InvokeMode.VIRTUAL));
        } else if (type instanceof Type.Int) {
            bytecodes.add(new Bytecode.CheckCast(JAVA_LANG_INTEGER));
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_INTEGER, "intValue",
                    JAVA_LANG_INTEGER_INTVALUE, Bytecode.InvokeMode.VIRTUAL));
        } else if (type instanceof Type.Real) {
            bytecodes.add(new Bytecode.CheckCast(JAVA_LANG_DOUBLE));
            bytecodes.add(new Bytecode.Invoke(JAVA_LANG_DOUBLE, "doubleValue",
                    JAVA_LANG_DOUBLE_DOUBLEVALUE, Bytecode.InvokeMode.VIRTUAL));
        } else {
            bytecodes.add(new Bytecode.CheckCast(getJvmType(expr)));
        }

        return bytecodes;
    }
}

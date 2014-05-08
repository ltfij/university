package whilelang.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jx86.lang.Constant;
import jx86.lang.Instruction;
import jx86.lang.Register;
import jx86.lang.Target;
import jx86.lang.X86File;
import whilelang.lang.Expr;
import whilelang.lang.Stmt;
import whilelang.lang.Type;
import whilelang.lang.WhileFile;
import whilelang.util.Attribute;
import whilelang.util.Pair;
import whilelang.util.SyntacticElement;

public class X86FileWriter {

    private static int labelIndex = 0;

    private final jx86.lang.Target target;

    private final Register HAX;
    private final Register HBX;
    private final Register HCX;
    private final Register HDX;
    private final Register HDI;
    private final Register HSI;
    private final Register HBP;
    private final Register HSP;
    private final Register HIP;
    private final Register H8;
    private final Register H9;
    private final Register H10;
    private final Register H11;
    private final Register H12;
    private final Register H13;
    private final Register H14;
    private final Register H15;
    private final Register XMM0 = Register.XMM0;
    private final Register XMM1 = Register.XMM1;
    private final Register XMM2 = Register.XMM2;
    private final Register XMM3 = Register.XMM3;
    private final Register XMM4 = Register.XMM4;
    private final Register XMM5 = Register.XMM5;
    private final Register XMM6 = Register.XMM6;
    private final Register XMM7 = Register.XMM7;
    private final Register XMM8 = Register.XMM8;
    private final Register XMM9 = Register.XMM9;
    private final Register XMM10 = Register.XMM10;
    private final Register XMM11 = Register.XMM11;
    private final Register XMM12 = Register.XMM12;
    private final Register XMM13 = Register.XMM13;
    private final Register XMM14 = Register.XMM14;
    private final Register XMM15 = Register.XMM15;

    private final List<Register> REGISTER_POOL;
    private final List<Register> XMM_REGISTER_POOL;
    private final List<Register> CALLEE_SAVED_REGISTERS;

    private final int NULL_TAG = -1;
    private final int VOID_TAG = 0;
    private final int BOOL_TAG = 1;
    private final int CHAR_TAG = 2;
    private final int INT_TAG = 3;
    private final int REAL_TAG = 4;
    private final int STRING_TAG = 5;
    private final int RECORD_TAG = 6;
    private final int LIST_TAG = 7;

    private HashMap<String, WhileFile.FunDecl> functions;
    private HashMap<String, WhileFile.TypeDecl> types;

    public X86FileWriter(jx86.lang.Target target) {
        this.target = target;

        // Initialise register heads --- the largest register available in a
        // given family for the target platform.
        HAX = headOfFamily(Register.AX);
        HBX = headOfFamily(Register.BX);
        HCX = headOfFamily(Register.CX);
        HDX = headOfFamily(Register.DX);
        HDI = headOfFamily(Register.DI);
        HSI = headOfFamily(Register.SI);
        HBP = headOfFamily(Register.BP);
        HSP = headOfFamily(Register.SP);
        HIP = headOfFamily(Register.IP);
        H8 = headOfFamily(Register.R8);
        H9 = headOfFamily(Register.R9);
        H10 = headOfFamily(Register.R10);
        H11 = headOfFamily(Register.R11);
        H12 = headOfFamily(Register.R12);
        H13 = headOfFamily(Register.R13);
        H14 = headOfFamily(Register.R14);
        H15 = headOfFamily(Register.R15);

        // Initialise the default register pool
        REGISTER_POOL = new ArrayList<Register>();
        REGISTER_POOL.add(HBX);
        //        REGISTER_POOL.add(HCX);
        //        REGISTER_POOL.add(HDX);
        //        REGISTER_POOL.add(HDI);
        //        REGISTER_POOL.add(HSI);
        //        REGISTER_POOL.add(H8);
        //        REGISTER_POOL.add(H9);
        REGISTER_POOL.add(H10);
        REGISTER_POOL.add(H11);
        REGISTER_POOL.add(H12);
        REGISTER_POOL.add(H13);
        REGISTER_POOL.add(H14);
        REGISTER_POOL.add(H15);

        // Initialise the default xmm register pool
        XMM_REGISTER_POOL = new ArrayList<Register>();
        //        XMM_REGISTER_POOL.add(XMM0);
        //        XMM_REGISTER_POOL.add(XMM1);
        //        XMM_REGISTER_POOL.add(XMM2);
        //        XMM_REGISTER_POOL.add(XMM3);
        //        XMM_REGISTER_POOL.add(XMM4);
        //        XMM_REGISTER_POOL.add(XMM5);
        //        XMM_REGISTER_POOL.add(XMM6);
        //        XMM_REGISTER_POOL.add(XMM7);
        XMM_REGISTER_POOL.add(XMM8);
        XMM_REGISTER_POOL.add(XMM9);
        XMM_REGISTER_POOL.add(XMM10);
        XMM_REGISTER_POOL.add(XMM11);
        XMM_REGISTER_POOL.add(XMM12);
        XMM_REGISTER_POOL.add(XMM13);
        XMM_REGISTER_POOL.add(XMM14);
        XMM_REGISTER_POOL.add(XMM15);

        // Initialise the default register pool
        CALLEE_SAVED_REGISTERS = new ArrayList<Register>();
        CALLEE_SAVED_REGISTERS.add(HBP);
        CALLEE_SAVED_REGISTERS.add(HBX);
        CALLEE_SAVED_REGISTERS.add(H12);
        CALLEE_SAVED_REGISTERS.add(H13);
        CALLEE_SAVED_REGISTERS.add(H14);
        CALLEE_SAVED_REGISTERS.add(H15);
    }

    public X86File build(WhileFile wf) {
        X86File.Code code = new X86File.Code();
        X86File.Data data = new X86File.Data();

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

        for (WhileFile.Decl d : wf.declarations) {
            if (d instanceof WhileFile.FunDecl) {
                translate((WhileFile.FunDecl) d, code, data);
            }
        }

        addMainLauncher(code);

        return new X86File(code, data);
    }

    /**
     * Add a standard main method which will be called by the operating system when this process is
     * executed. This sequence is operating system dependent, and simply calls the translated
     * <code>main()</code> method from the original while source file.
     */
    private void addMainLauncher(X86File.Code code) {
        List<Instruction> instructions = code.instructions;
        instructions.add(new Instruction.Label("main", 1, true));
        instructions.add(new Instruction.Reg(Instruction.RegOp.push, HBP));
        instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "wl_main"));
        instructions.add(new Instruction.Reg(Instruction.RegOp.pop, HBP));
        instructions.add(new Instruction.Unit(Instruction.UnitOp.ret));
    }

    private void addNaturalWordConstant(long value, String label, X86File.Data data) {
        Constant item;

        switch (target.arch) {
            case X86_32:
                item = new Constant.Long(label, value);
                break;
            case X86_64:
                item = new Constant.Quad(label, value);
                break;
            default:
                throw new IllegalArgumentException("Unknown architecture encountered");
        }

        data.constants.add(item);
    }

    private void addStringConstant(String value, String label, X86File.Data data) {
        data.constants.add(new Constant.String(label, value));
    }

    /**
     * <p> Create a data constant representing a While type. This encodes all information necessary
     * to decode the type including, for example, field names etc. </p> <p> Each type begins with a
     * tag which identifies what kind it is, followed by the payload (if applicable) which may
     * contain other nested types. </p>
     */
    private void addTypeConstant(Type type, String label, X86File.Data data) {
        type = unwrap(type);

        if (type instanceof Type.Null) {
            addNaturalWordConstant(NULL_TAG, label, data);
        } else if (type instanceof Type.Void) {
            addNaturalWordConstant(VOID_TAG, label, data);
        } else if (type instanceof Type.Bool) {
            addNaturalWordConstant(BOOL_TAG, label, data);
        } else if (type instanceof Type.Char) {
            addNaturalWordConstant(CHAR_TAG, label, data);
        } else if (type instanceof Type.Int) {
            addNaturalWordConstant(INT_TAG, label, data);
        } else if (type instanceof Type.Real) {
            addNaturalWordConstant(REAL_TAG, label, data);
        } else if (type instanceof Type.Strung) {
            addNaturalWordConstant(STRING_TAG, label, data);
        } else if (type instanceof Type.Record) {
            Type.Record r = (Type.Record) type;
            // First, get sorted fields
            Map<String, Type> fields = r.getFields();
            ArrayList<String> fieldNames = new ArrayList<String>(r.getFields().keySet());
            Collections.sort(fieldNames);
            // Second, write type tag
            addNaturalWordConstant(RECORD_TAG, label, data);
            // Third, write the number of fields
            addNaturalWordConstant(fieldNames.size(), null, data);
            for (String field : fieldNames) {
                // Fourth, each field consists of a
                addNaturalWordConstant(field.length(), null, data);
                data.constants.add(new Constant.String(null, field));
                addTypeConstant(fields.get(field), null, data);
            }
        } else if (type instanceof Type.List) {
            Type.List l = (Type.List) type;
            addNaturalWordConstant(LIST_TAG, label, data);
            addTypeConstant(l.getElement(), null, data);
        } else if (type instanceof Type.Any) {
            // I got lazy...
            addNaturalWordConstant(INT_TAG, label, data);
        } else {
            throw new IllegalArgumentException("Unknown type encountered - " + type);
        }
    }

    private void allocateRegister(Register register) {
        XMM_REGISTER_POOL.remove(register);
        REGISTER_POOL.remove(register);
    }

    /**
     * <p> Allocate every local variable declared in a function to an appropriate amount of space on
     * the stack. The space is allocated in "slots", where each slot corresponds to an amount of
     * space given by the architecture's "natural" size (i.e. 64bits on x86_64, etc). Obviously,
     * this is not the most efficient approach and we could do better (since e.g. int's in While
     * need only 32bits). </p> <p> <b>NOTE:</b> parameters are also allocated here, although they do
     * not contribution to an increase in stack size since they are caller-allocated. </p>
     *
     * @param function Function for which to create the stack frame
     * @param allocation Map of variable names to their allocated position on the stack
     * @return The total number of bytes which were allocated
     */
    private int allocateStackFrame(WhileFile.FunDecl function, Map<String, Integer> allocation) {

        // First, allocate parameters. We need to include two natural words
        // to account for the caller return address, and the frame pointer.
        // Also, there maybe some padding of some sort.
        int offset = determineCallerEnvironmentAlignedWidth(function) + (target.widthInBytes() * 2);

        List<WhileFile.Parameter> fun_params = function.parameters;
        for (int i = 0; i < fun_params.size(); i++) {
            WhileFile.Parameter p = fun_params.get(i);
            offset -= determineWidth(p.type);
            allocation.put(p.name, offset);
        }

        // Second, allocate special return value
        if (!(function.ret instanceof Type.Void)) {
            offset -= determineWidth(function.ret);
            allocation.put("$", offset);
        }

        // Finally, allocate remaining local variables
        return allocateStackFrame(function.statements, allocation);
    }

    // ==========================================
    // Other Helpers
    // ==========================================

    private int allocateStackFrame(List<Stmt> statements, Map<String, Integer> allocation) {

        // First, we go through and determine the type of all declared
        // variables. During this process if we have two declarations for
        // variables with the same name, we retain the larger type. This
        // guarantees there is enough space for the variable in question.
        HashMap<String, Type> variables = new HashMap<String, Type>();
        extractLocalVariableTypes(statements, variables);

        int count = 0;
        for (Map.Entry<String, Type> e : variables.entrySet()) {
            int width = determineWidth(e.getValue());
            count += width;
            allocation.put(e.getKey(), -count);
        }

        // Finally, round the size of the stack here, depending on the
        // architecture. That is, on x86_64 it needs to a multiple of
        // 16 bytes.

        return determineAlignedStackWidth(count);
    }

    /**
     * On some operating systems, the stack must be aligned to a specific amount. For example, on
     * x86_64/MacOS it must be aligned to multiples of 16 bytes.
     *
     * @param minimum The minumum number of bytes required for the stack frame to hold all the
     * necessary local variables, etc.
     */
    private int determineAlignedStackWidth(int minimum) {
        if (target == Target.MACOS_X86_64) {
            // round up to nearest 16 bytes
            int tmp = (minimum / 16) * 16;
            if (tmp < minimum) {
                tmp = tmp + 16;
            }
            return tmp;
        }
        return minimum;
    }

    private int determineCallerEnvironmentAlignedWidth(WhileFile.FunDecl fd) {
        int width = 0;
        for (WhileFile.Parameter p : fd.parameters) {
            width += determineWidth(p.type);
        }
        if (!(fd.ret instanceof Type.Void)) {
            width += determineWidth(fd.ret);
        }
        return determineAlignedStackWidth(width);
    }

    /**
     * Determine the offset of a given field in a given type.
     */
    private int determineFieldOffset(Type.Record type, String field) {

        // First, get fields in sorted order!
        ArrayList<String> fields = new ArrayList<String>();
        for (Map.Entry<String, Type> entry : type.getFields().entrySet()) {
            fields.add(entry.getKey());
        }
        Collections.sort(fields);

        // Second, calculate offset of field we are reading
        int offset = determineWidth(type);
        for (int i = fields.size() - 1; i >= 0; --i) {
            String f = fields.get(i);
            offset -= determineWidth(type.getFields().get(field));
            if (field.equals(f)) {
                break;
            }
        }

        return offset;
    }

    /**
     * Determine the width (in bytes) of this type. For simplicity we round everything to the
     * nearest "natural" word size for the given architecture. For example, on x86_64, this function
     * returns 8 for type bool. Obviously, this is not the most efficient.
     */
    private int determineWidth(Type type) {
        if (type instanceof Type.Bool || type instanceof Type.Char || type instanceof Type.Int
                || type instanceof Type.Real || type instanceof Type.Null) {
            // The size of a machine word.
            return target.widthInBytes();
        } else if (type instanceof Type.Record) {
            Type.Record r = (Type.Record) type;
            int total = 0;
            for (Map.Entry<String, Type> e : r.getFields().entrySet()) {
                total += determineWidth(e.getValue());
            }
            return total;
        } else if (type instanceof Type.Strung) {
            // Always the size of a machine pointer.
            return target.widthInBytes();
        } else if (type instanceof Type.List) {
            // Always the size of a machine pointer.
            return target.widthInBytes();
        } else if (type instanceof Type.Union) {
            Type.Union r = (Type.Union) type;
            // Compute the maximum size of any bound.
            int width = 0;
            for (Type b : r.getBounds()) {
                width = Math.max(width, determineWidth(b));
            }
            // FIXME: this is broken because it does not include the type tag
            // itself.
            return width;
        } else if (type instanceof Type.Named) {
            return determineWidth(unwrap(type));
        } else {
            throw new IllegalArgumentException("Unknown type encountered: " + type);
        }
    }

    /**
     * Determine the type of every declared local variable. In cases where we have two local
     * variables with the same name but different types, choose the physically largest type (in
     * bytes).
     */
    private void extractLocalVariableTypes(List<Stmt> statements, Map<String, Type> allocation) {
        for (Stmt stmt : statements) {
            if (stmt instanceof Stmt.VariableDeclaration) {
                Stmt.VariableDeclaration vd = (Stmt.VariableDeclaration) stmt;
                Type ot = allocation.get(vd.getName());
                Type nt = vd.getType();
                if (ot != null && determineWidth(ot) > determineWidth(nt)) {
                    nt = ot;
                }
                allocation.put(vd.getName(), nt);
            } else if (stmt instanceof Stmt.IfElse) {
                Stmt.IfElse ife = (Stmt.IfElse) stmt;
                extractLocalVariableTypes(ife.getTrueBranch(), allocation);
                extractLocalVariableTypes(ife.getFalseBranch(), allocation);
            } else if (stmt instanceof Stmt.For) {
                Stmt.For fe = (Stmt.For) stmt;

                // Allocate loop variable
                Stmt.VariableDeclaration vd = fe.getDeclaration();
                Type ot = allocation.get(vd.getName());
                Type nt = vd.getType();
                if (ot != null && determineWidth(ot) > determineWidth(nt)) {
                    nt = ot;
                }
                allocation.put(vd.getName(), nt);

                // Explore loop body
                extractLocalVariableTypes(fe.getBody(), allocation);
            } else if (stmt instanceof Stmt.While) {
                Stmt.While fe = (Stmt.While) stmt;
                extractLocalVariableTypes(fe.getBody(), allocation);
            }
        }
    }

    private void freeRegister(Register register) {
        if (Arrays.asList(Register.XMM_FAMILY).contains(register)) {
            XMM_REGISTER_POOL.add(register);
        } else {
            REGISTER_POOL.add(register);
        }
    }

    private static String freshLabel() {
        return "label" + labelIndex++;
    }

    private Register getParameterRegister(Type type, Register... used) {
        return getParameterRegister(type, Arrays.asList(used));
    }

    private Register getParameterRegister(Type type, List<Register> used) {
        Register[] registers;
        if (type instanceof Type.Real) {
            registers = new Register[] {XMM0, XMM1, XMM2, XMM3, XMM4, XMM5, XMM6, XMM7};
        } else {
            registers = new Register[] {HDI, HSI, HDX, HCX, H8, H9};
        }

        for (Register register : registers) {
            if (!used.contains(register)) {
                return register;
            }
        }

        throw new InternalError("no free parameter registers available for type " + type);
    }

    private Register getRegister(Type type) {
        if (type instanceof Type.Real) {
            return XMM_REGISTER_POOL.get(0);
        } else {
            return REGISTER_POOL.get(0);
        }
    }

    private Type getType(SyntacticElement element) {
        return element.attribute(Attribute.Type.class).type;
    }

    /**
     * Returns the head of a given registers family. For example, on <code>x86_64</code> the head of
     * the <code>bx</code> family is <code>rbx</code>. Conversely, the head of the <code>bx</code>
     * family is <code>ebx</code> on <code>x86_32</code>.
     */
    private Register headOfFamily(Register register) {
        Register.Width width;
        switch (target.arch) {
            case X86_32:
                width = Register.Width.Long;
                break;
            case X86_64:
                width = Register.Width.Quad;
                break;
            default:
                throw new IllegalArgumentException("Invalid architecture: " + target.arch);
        }
        return register.sibling(width);
    }

    private void optRestoreRegister(Register register, List<Instruction> instructions) {
        if (CALLEE_SAVED_REGISTERS.contains(register)) {
            return;
        }

        if (Arrays.asList(Register.XMM_FAMILY).contains(register)) {
            // TODO
        } else {
            instructions.add(new Instruction.Reg(Instruction.RegOp.pop, register));
        }
    }

    private void optSaveRegister(Register register, List<Instruction> instructions) {
        if (CALLEE_SAVED_REGISTERS.contains(register)) {
            return;
        }

        if (Arrays.asList(Register.XMM_FAMILY).contains(register)) {
            // TODO
        } else {
            instructions.add(new Instruction.Reg(Instruction.RegOp.push, register));
        }
    }

    /**
     * Copy a data value to a given register from a stack location offset from a base pointer. In
     * the case of a compound value, then we need to store the address of the structure into the
     * target register.
     *
     * @param type Type of data being assigned
     * @param source Based pointer into stack for assignment
     * @param offset Stack location offset from base pointer to read from
     * @param target Register to write data value / pointer to
     */
    private void readFromStack(Type type, Register source, int offset, Register target,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // There are two cases we need to consider here: primitive values;
        // and, compound values. For the former, we write their value directly
        // to the target register. For the latter, we write their address as a
        // pointer to the target register.

        type = unwrap(type); // remove named types

        if (type instanceof Type.Bool || type instanceof Type.Char || type instanceof Type.Int
                || type instanceof Type.Strung || type instanceof Type.List) {
            // These are the primitive types. Therefore, we just read their
            // value directly from the stack into the target register.
            instructions.add(new Instruction.ImmIndReg(Instruction.ImmIndRegOp.mov, offset, source,
                    target));
        } else if (type instanceof Type.Real) {
            instructions.add(new Instruction.ImmIndReg(Instruction.ImmIndRegOp.movsd, offset,
                    source, target));
        } else {
            // These are all compound types. Therefore, we load the address of
            // their value on the stack into the target register.
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, source, target));
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.add, offset, target));
        }
    }

    private static void sortFields(List<Pair<String, Expr>> ofields) {
        Collections.sort(ofields, new Comparator<Pair<String, Expr>>() {
            public int compare(Pair<String, Expr> p1, Pair<String, Expr> p2) {
                return p1.first().compareTo(p2.first());
            }
        });
    }

    /**
     * Translate a list of While statements into their corresponding machine code instructions.
     * Observe that we implicitly assume all registers are available for use between statements.
     */
    private void translate(List<Stmt> statements, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        for (Stmt statement : statements) {
            translate(statement, localVariables, code, data);
        }
    }

    private void translate(Stmt.Assign statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        Expr lhs = statement.getLhs();

        // Translate the right-hand side and load result into target register
        Type rhsType = getType(statement.getRhs());
        Register rhsTarget = getParameterRegister(rhsType);

        translate(statement.getRhs(), rhsTarget, localVariables, code, data);
        allocateRegister(rhsTarget);

        if (!(rhsType instanceof Type.Real)) {
            String rhsTypeLabel = freshLabel();
            addTypeConstant(rhsType, rhsTypeLabel, data);
            Register typeTarget = getParameterRegister(null, rhsTarget);
            instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, rhsTypeLabel,
                    HIP, typeTarget));
            instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "dup"));
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, rhsTarget));
        }

        // Translate assignment from target to left-hand side
        if (lhs instanceof Expr.Variable) {
            Expr.Variable var = (Expr.Variable) lhs;

            // Determine the offset within the stack of this local variable.
            int offset = localVariables.get(var.getName());

            // Extract the variable's type to help determine which case to handle
            Type lhsType = getType(var);

            // Implement the assignment
            writeToStack(lhsType, rhsTarget, HBP, offset, code, data);
        } else if (lhs instanceof Expr.RecordAccess) {
            Expr.RecordAccess rec = (Expr.RecordAccess) lhs;

            // First, determine the field offset
            Type.Record lhsType = (Type.Record) unwrap(getType(rec));
            int offset = determineFieldOffset(lhsType, rec.getName());

            // Translate source expression to give pointer to structure.
            Register lhsTarget = getRegister(lhsType);
            translate(rec.getSource(), lhsTarget, localVariables, code, data);

            // Finally, perform indirect write
            instructions.add(new Instruction.RegImmInd(Instruction.RegImmIndOp.mov, rhsTarget,
                    offset, lhsTarget));
        } else if (lhs instanceof Expr.IndexOf) {
            Expr.IndexOf indexOf = (Expr.IndexOf) lhs;

            Type sourceType = getType(indexOf.getSource());
            Type indexType = getType(indexOf.getIndex());

            Register sourceTarget = getRegister(sourceType);
            optSaveRegister(rhsTarget, instructions);
            translate(indexOf.getSource(), sourceTarget, localVariables, code, data);
            optRestoreRegister(rhsTarget, instructions);
            allocateRegister(sourceTarget);

            Register indexTarget = getRegister(indexType);
            optSaveRegister(rhsTarget, instructions);
            optSaveRegister(sourceTarget, instructions);
            translate(indexOf.getIndex(), indexTarget, localVariables, code, data);
            optRestoreRegister(sourceTarget, instructions);
            optRestoreRegister(rhsTarget, instructions);

            Register par2 = getParameterRegister(sourceType, rhsTarget);
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, sourceTarget, par2));
            Register par3 = getParameterRegister(indexType, rhsTarget, par2);
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, indexTarget, par3));

            if (sourceType instanceof Type.Strung) {
                instructions.add(new Instruction.Addr(Instruction.AddrOp.call,
                        "str_indexof_assign"));
            } else {
                // TODO: list_indexof_assignd
                instructions.add(new Instruction.Addr(Instruction.AddrOp.call,
                        "list_indexof_assign"));
            }

            freeRegister(sourceTarget);
        }

        freeRegister(rhsTarget);
    }

    private void translate(Stmt.Print statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // Translate the target expression and load result into HDI register
        Register target = getParameterRegister(getType(statement.getExpr()));
        translate(statement.getExpr(), target, localVariables, code, data);

        // Determine type of expression so as to determine appropriate print call.
        Type type = getType(statement.getExpr());

        if (type instanceof Type.Real) {
            instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "printd"));
        } else {
            String typeLabel = freshLabel();
            addTypeConstant(type, typeLabel, data);

            instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, typeLabel,
                    HIP, HSI));

            instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "print"));
        }
    }

    /**
     * Translate a given function declaration into a sequence of assembly language instructions.
     *
     * @param fd Function Declaration to translate.
     * @param code x86 code section where translation should be added.
     */
    private void translate(WhileFile.FunDecl fd, X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // NOTE: prefix name with "wl_" to avoid potential name clashes.
        instructions.add(new Instruction.Label("wl_" + fd.name));

        // Save the old frame pointer
        instructions.add(new Instruction.Reg(Instruction.RegOp.push, HBP));

        // Create new frame pointer for this function
        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HSP, HBP));

        // Create stack frame and ensure every variable has a known position on
        // the stack. Parameters are passed on the stack by the caller.
        HashMap<String, Integer> localVariables = new HashMap<String, Integer>();
        int widthOfLocals = allocateStackFrame(fd, localVariables);

        // Create the label for return statements. This is the point where
        // return statements will branch to, so we can avoid repeating the code
        // necessary for restoring the stack.
        int exitLabel = labelIndex++;
        localVariables.put("$$", exitLabel); // sneaky

        // Create space for the stack frame, which consists of the local
        // variables.
        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.sub, widthOfLocals, HSP));

        // translate the statements
        translate(fd.statements, localVariables, code, data);

        // Add the return label
        instructions.add(new Instruction.Label("label" + exitLabel));

        // Restore stack pointer
        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HBP, HSP));

        // Restore old frame pointer
        instructions.add(new Instruction.Reg(Instruction.RegOp.pop, HBP));

        // Return from procedure
        instructions.add(new Instruction.Unit(Instruction.UnitOp.ret));
    }

    /**
     * Translate a given While statement into its corresponding corresponding machine code
     * instructions. Observe that we implicitly assume all registers are available for use between
     * statements.
     *
     * @param statement Statement to be translated
     * @param code X86 code section to write machine code instructions to.
     */
    private void translate(Stmt statement, Map<String, Integer> localVariables, X86File.Code code,
            X86File.Data data) {
        if (statement instanceof Stmt.Assign) {
            translate((Stmt.Assign) statement, localVariables, code, data);
        } else if (statement instanceof Stmt.For) {
            translate((Stmt.For) statement, localVariables, code, data);
        } else if (statement instanceof Stmt.IfElse) {
            translate((Stmt.IfElse) statement, localVariables, code, data);
        } else if (statement instanceof Expr.Invoke) {
            // We assign result of invoke to HDI, but this is just discarded.
            translate((Expr.Invoke) statement, HDI, localVariables, code, data);
        } else if (statement instanceof Stmt.Print) {
            translate((Stmt.Print) statement, localVariables, code, data);
        } else if (statement instanceof Stmt.Return) {
            translate((Stmt.Return) statement, localVariables, code, data);
        } else if (statement instanceof Stmt.VariableDeclaration) {
            translate((Stmt.VariableDeclaration) statement, localVariables, code, data);
        } else if (statement instanceof Stmt.While) {
            translate((Stmt.While) statement, localVariables, code, data);
        } else {
            throw new IllegalArgumentException("Unknown statement encountered: " + statement);
        }
    }

    private void translate(Stmt.For statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;
        String headLabel = freshLabel();
        String exitLabel = freshLabel();

        // 1. Translate Variable Declaration
        translate(statement.getDeclaration(), localVariables, code, data);

        // 2. Start loop, and translate condition
        instructions.add(new Instruction.Label(headLabel));
        // TODO: reals
        translate(statement.getCondition(), HDI, localVariables, code, data);
        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.cmp, 0, HDI));
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jz, exitLabel));

        // 3. Translate Loop Body
        translate(statement.getBody(), localVariables, code, data);

        // 4. Translate Increment and loop around
        translate(statement.getIncrement(), localVariables, code, data);
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jmp, headLabel));

        // 5. Exit ...
        instructions.add(new Instruction.Label(exitLabel));
    }

    private void translate(Stmt.IfElse statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;
        String falseLabel = freshLabel();
        String exitLabel = freshLabel();

        // 1. Translate Condition
        // TODO: reals
        translate(statement.getCondition(), HDI, localVariables, code, data);

        // 2. Translate Check and jump
        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.cmp, 0, HDI));
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jz, falseLabel));

        // 3. Translate True Body
        translate(statement.getTrueBranch(), localVariables, code, data);
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jmp, exitLabel));

        // 4. Translate False Body
        instructions.add(new Instruction.Label(falseLabel));
        translate(statement.getFalseBranch(), localVariables, code, data);

        // 5. Exit ...
        instructions.add(new Instruction.Label(exitLabel));
    }

    private void translate(Stmt.Return statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;
        Expr expr = statement.getExpr();

        if (expr != null) {
            // Translate the right-hand side and load result into HDI register
            Type type = getType(expr);
            Register target = getParameterRegister(type);
            translate(expr, target, localVariables, code, data);

            // Determine the offset within the stack of this local variable.
            int offset = localVariables.get("$");

            // Extract the variable's type to help determine which case to
            // handle

            // Implement the assignment
            writeToStack(type, target, HBP, offset, code, data);
        }

        // Finally, we branch to the end of the function where the code
        // necessary for restoring the stack is located.
        int exitLabel = localVariables.get("$$"); // sneaky ;)
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jmp, "label" + exitLabel));
    }

    private void translate(Stmt.VariableDeclaration statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        Expr expr = statement.getExpr();

        if (expr != null) {
            // Translate the right-hand side and load result into HDI register
            Type type = getType(expr);
            Register target = getParameterRegister(type);
            translate(expr, target, localVariables, code, data);

            if (!(type instanceof Type.Real)) {
                String typeLabel = freshLabel();
                addTypeConstant(type, typeLabel, data);
                Register typeTarget = getParameterRegister(type, target);
                code.instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea,
                        typeLabel, HIP, typeTarget));
                code.instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "dup"));
                code.instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX,
                        target));
            }

            // Determine the offset within the stack of this local variable.
            int offset = localVariables.get(statement.getName());

            // Extract the variable's type to help determine which case to
            // handle
            Type varType = statement.getType();

            // Implement the assignment
            writeToStack(varType, target, HBP, offset, code, data);
        }
    }

    private void translate(Stmt.While statement, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;
        String headLabel = freshLabel();
        String exitLabel = freshLabel();

        instructions.add(new Instruction.Label(headLabel));

        // Translate the condition expression and load result into HDI register
        // TODO: reals
        translate(statement.getCondition(), HDI, localVariables, code, data);
        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.cmp, 0, HDI));
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jz, exitLabel));

        // Translate Loop Body
        translate(statement.getBody(), localVariables, code, data);
        instructions.add(new Instruction.Addr(Instruction.AddrOp.jmp, headLabel));

        // Loop exit..
        instructions.add(new Instruction.Label(exitLabel));
    }

    /**
     * Translate a given expression into the corresponding machine code instructions. The expression
     * is expected to return its result in the target register or, if that is null, on the stack.
     * The set of free registers is provided to identify the pool from which target registers can be
     * taken.
     *
     * @param expression Expression to be translated.
     * @param target Register to store result in; if this is <code>null</code> then result stored on
     * stack
     * @param localVariables Mapping of local variable names to their byte offset from the frame
     * pointer.
     * @param code Code section to add on those instructions corresponding to this expression
     * @param data Data section to store any constants required by instructions generated for this
     * expression (e.g. string constants)
     */
    private void translate(Expr expression, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        if (expression instanceof Expr.Binary) {
            translate((Expr.Binary) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.Constant) {
            translate((Expr.Constant) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.Cast) {
            translate((Expr.Cast) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.IndexOf) {
            translate((Expr.IndexOf) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.Invoke) {
            translate((Expr.Invoke) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.ListConstructor) {
            translate((Expr.ListConstructor) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.RecordAccess) {
            translate((Expr.RecordAccess) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.RecordConstructor) {
            translate((Expr.RecordConstructor) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.Unary) {
            translate((Expr.Unary) expression, target, localVariables, code, data);
        } else if (expression instanceof Expr.Variable) {
            translate((Expr.Variable) expression, target, localVariables, code, data);
        } else {
            throw new IllegalArgumentException("Unknown expression encountered: " + expression);
        }
    }

    private void translate(Expr.Binary e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        Type lhsType = e.getLhs().attribute(Attribute.Type.class).type;
        Type rhsType = e.getRhs().attribute(Attribute.Type.class).type;

        Register lhsTarget = getRegister(lhsType);

        // First, translate lhs and store result in the target register.
        translate(e.getLhs(), lhsTarget, localVariables, code, data);
        allocateRegister(lhsTarget);

        // Second, determine register into which to store rhs, and create new
        // free registers list which doesn't include the target register for
        // this expression (since this will currently hold the stored result of
        // the lhs).
        Register rhsTarget = getRegister(rhsType);

        optSaveRegister(lhsTarget, instructions);
        translate(e.getRhs(), rhsTarget, localVariables, code, data);
        optRestoreRegister(lhsTarget, instructions);

        // Finally, perform the binary operation.
        switch (e.getOp()) {
            case AND:
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.and, rhsTarget,
                        lhsTarget));
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget,
                        target));
                break;
            case OR:
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.or, rhsTarget,
                        lhsTarget));
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget,
                        target));
                break;
            case ADD:
                if (lhsType instanceof Type.Real) {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.addsd, rhsTarget,
                            lhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, lhsTarget,
                            target));
                } else {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.add, rhsTarget,
                            lhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget,
                            target));
                }
                break;
            case SUB:
                if (lhsType instanceof Type.Real) {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.subsd, rhsTarget,
                            lhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, lhsTarget,
                            target));
                } else {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.sub, rhsTarget,
                            lhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget,
                            target));
                }
                break;
            case MUL:
                if (lhsType instanceof Type.Real) {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mulsd, rhsTarget,
                            lhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, lhsTarget,
                            target));
                } else {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.imul, rhsTarget,
                            lhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget,
                            target));
                }
                break;
            case DIV:
                if (lhsType instanceof Type.Real) {
                    Register par1 = getParameterRegister(lhsType);
                    Register par2 = getParameterRegister(rhsType, par1);

                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, lhsTarget,
                            par1));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, lhsTarget,
                            par2));
                    instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "divd"));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, XMM0,
                            target));
                } else {
                    // The idiv instruction is curious because you cannot control where
                    // the result is stored. That is, the result is always stored into
                    // the hdx:hax register pairing (where hdx = remainder, hax = quotient).
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget,
                            HAX));
                    // TODO: I'm not completely sure why we need this!
                    instructions.add(new Instruction.Unit(Instruction.UnitOp.cltd));
                    // FIXME: this trashes the HDX register which could be in use!
                    instructions.add(new Instruction.Reg(Instruction.RegOp.idiv, rhsTarget));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, target));
                }
                break;
            case REM:
                // TODO: reals
                // The idiv instruction is curious because you cannot control where
                // the result is stored. That is, the result is always stored into
                // the hdx:has register pairing (where hdx = remainder, hax = quotient).
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget, HAX));
                // TODO: I'm not completely sure why we need this!
                instructions.add(new Instruction.Unit(Instruction.UnitOp.cltd));
                // FIXME: this trashes the HDX register which could be in use!
                instructions.add(new Instruction.Reg(Instruction.RegOp.idiv, rhsTarget));
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HDX, target));
                break;
            case EQ:
            case NEQ:
            case LT:
            case LTEQ:
            case GT:
            case GTEQ: {
                // NOTE: this could be implemented more efficiently in many cases.
                // For example, if true is any non-zero number then we can just
                // perform a subtraction for equality and inequality.
                String trueLabel = freshLabel();
                String exitLabel = freshLabel();
                if (lhsType instanceof Type.Real) {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.cmpsd, rhsTarget,
                            lhsTarget));
                } else {
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.cmp, rhsTarget,
                            lhsTarget));
                }

                switch (e.getOp()) {
                    case EQ:
                        // FIXME: problem with compound types
                        instructions.add(new Instruction.Addr(Instruction.AddrOp.jz, trueLabel));
                        break;
                    case NEQ:
                        // FIXME: problem with compound types
                        instructions.add(new Instruction.Addr(Instruction.AddrOp.jnz, trueLabel));
                        break;
                    case LT:
                        instructions.add(new Instruction.Addr(Instruction.AddrOp.jl, trueLabel));
                        break;
                    case LTEQ:
                        instructions.add(new Instruction.Addr(Instruction.AddrOp.jle, trueLabel));
                        break;
                    case GT:
                        instructions.add(new Instruction.Addr(Instruction.AddrOp.jg, trueLabel));
                        break;
                    case GTEQ:
                        instructions.add(new Instruction.Addr(Instruction.AddrOp.jge, trueLabel));
                        break;
                }

                instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov, 0, target));
                instructions.add(new Instruction.Addr(Instruction.AddrOp.jmp, exitLabel));
                instructions.add(new Instruction.Label(trueLabel));
                instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov, 1, target));
                instructions.add(new Instruction.Label(exitLabel));
                break;
            }
            case APPEND:
                Register par1 = getParameterRegister(lhsType);
                Register par2 = getParameterRegister(rhsType, par1);

                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, lhsTarget, par1));
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, rhsTarget, par2));

                if (lhsType instanceof Type.Strung && rhsType instanceof Type.Strung) {
                    instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "str_append"));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, target));
                } else if (lhsType instanceof Type.Strung) {
                    // Straightforward String concatenation
                    String typeLabel = freshLabel();
                    addTypeConstant(rhsType, typeLabel, data);
                    instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea,
                            typeLabel, HIP, getParameterRegister(null, par1, par2)));
                    instructions.add(new Instruction.Addr(Instruction.AddrOp.call,
                            "str_left_append"));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, target));
                } else {
                    instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "list_append"));
                    instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, target));
                }

                break;
            default:
                throw new IllegalArgumentException("Unknown binary operator: " + e);
        }

        freeRegister(lhsTarget);
    }

    private void translate(Expr.Constant e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        if (e.getValue() == null) {
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov, 0, target));
        } else if (e.getValue() instanceof Integer) {
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov,
                    (Integer) e.getValue(), target));
        } else if (e.getValue() instanceof Double) {
            double value = (Double) e.getValue();

            String label = freshLabel();
            addStringConstant(String.valueOf(value), label, data);

            instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, label, HIP,
                    HDI));
            instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "atof"));
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, XMM0, target));
        } else if (e.getValue() instanceof String) {
            String label = freshLabel();
            addStringConstant((String) e.getValue(), label, data);

            instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, label, HIP,
                    target));
        } else if (e.getValue() instanceof Character) {
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov,
                    (Character) e.getValue(), target));
        } else if (e.getValue() instanceof Boolean) {
            int value = (Boolean) e.getValue() ? 1 : 0;
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov, value, target));
        } else {
            throw new InternalError(
                    "translate(Expr.Constant, Register, List<Register>, Map<String, Integer>, X86File.Code, X86File.Data) not fully implemented: "
                            + e.getValue().getClass());
        }
    }

    private void translate(Expr.Cast e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        Register sourceTarget = getRegister(getType(e.getSource()));
        translate(e.getSource(), sourceTarget, localVariables, code, data);

        if (getType(e.getSource()) instanceof Type.Int && e.getType() instanceof Type.Real) {
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, sourceTarget, HDI));
            instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "cast"));
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, XMM0, target));
        } else if (getType(e.getSource()) instanceof Type.Real) {
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.movsd, sourceTarget,
                    target));
        } else {
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, sourceTarget,
                    target));
        }
    }

    private void translate(Expr.IndexOf e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        Type sourceType = getType(e.getSource());
        Register sourceTarget = getRegister(sourceType);

        translate(e.getSource(), sourceTarget, localVariables, code, data);
        allocateRegister(sourceTarget);

        Type indexType = getType(e.getIndex());
        Register indexTarget = getRegister(indexType);

        optSaveRegister(sourceTarget, instructions);
        translate(e.getIndex(), indexTarget, localVariables, code, data);
        optRestoreRegister(sourceTarget, instructions);

        Register par1 = getParameterRegister(sourceType);
        Register par2 = getParameterRegister(indexType, par1);
        Register par3 = getParameterRegister(null, par1, par2);

        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, sourceTarget, par1));
        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, indexTarget, par2));
        String typeLabel = freshLabel();
        // TODO: Verify if it is sourcetype or indextype here
        addTypeConstant(sourceType, typeLabel, data);
        instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, typeLabel, HIP,
                par3));
        instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "indexof"));
        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, target));

        freeRegister(sourceTarget);
    }

    private void translate(Expr.Invoke e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // First, determine the amount of space to reserve on the stack for
        // parameters and the return value (if applicable).
        WhileFile.FunDecl fd = functions.get(e.getName());
        int alignedWidth = determineCallerEnvironmentAlignedWidth(fd);

        // Second, create space on the stack for parameters and return value
        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.sub, alignedWidth, HSP));

        // Third, translate invocation arguments and load them onto the stack.
        int offset = alignedWidth;

        List<Expr> arguments = e.getArguments();
        List<WhileFile.Parameter> parameters = fd.parameters;
        for (int i = 0; i != arguments.size(); ++i) {
            // TODO: reals
            WhileFile.Parameter parameter = parameters.get(i);
            Expr argument = arguments.get(i);
            Type argType = argument.attribute(Attribute.Type.class).type;

            Register par1 = getParameterRegister(argType);

            translate(argument, par1, localVariables, code, data);
            if (!(argType instanceof Type.Real)) {
                String typeLabel = freshLabel();
                addTypeConstant(argType, typeLabel, data);
                Register par2 = getParameterRegister(argType, par1);
                instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, typeLabel,
                        HIP, par2));
                instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "dup"));
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, par1));
            }
            offset -= determineWidth(parameter.type);
            writeToStack(parameter.type, par1, HSP, offset, code, data);
        }

        // Fourth, actually invoke the function
        String fn_name = "wl_" + fd.name;
        instructions.add(new Instruction.Addr(Instruction.AddrOp.call, fn_name));

        if (!(fd.ret instanceof Type.Void)) {
            // Fifth, extract the return value
            offset -= determineWidth(fd.ret);
            readFromStack(fd.ret, HSP, offset, target, code, data);
        }

        // In principle, we'd like to return the stack pointer to its original
        // position here. However, in the case of a compound data type who's
        // address has been take we can't.
        // instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.add,
        //		alignedWidth, HSP));
    }

    private void translate(Expr.ListConstructor e, Register target,
            Map<String, Integer> localVariables, X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        Type.List listType = (Type.List) getType(e);
        Type innerType = listType.getElement();

        Register listTarget = getRegister(listType);

        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov, e.getArguments().size(),
                HDI));
        String typeLabel = freshLabel();
        addTypeConstant(innerType, typeLabel, data);
        instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, typeLabel, HIP,
                HSI));
        instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "list_init"));
        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, listTarget));

        allocateRegister(listTarget);

        for (int i = 0; i < e.getArguments().size(); i++) {
            // TODO: reals and target
            Expr argument = e.getArguments().get(i);

            translate(argument, target, localVariables, code, data);

            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, target, HDI));
            instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, listTarget, HSI));
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.mov, i, HDX));
            optSaveRegister(listTarget, instructions);
            // TODO: list_indexof_assignd
            instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "list_indexof_assign"));
            optRestoreRegister(listTarget, instructions);
        }

        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, listTarget, target));

        freeRegister(listTarget);
    }

    // TODO: Everythhing below here

    private void translate(Expr.RecordAccess e, Register target,
            Map<String, Integer> localVariables, X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // First, determine the field offset
        Type.Record type = (Type.Record) unwrap(e.getSource().attribute(Attribute.Type.class).type);
        int offset = determineFieldOffset(type, e.getName());

        // Second, translate source expression
        translate(e.getSource(), target, localVariables, code, data);

        // Finally, perform indirect read
        instructions.add(new Instruction.ImmIndReg(Instruction.ImmIndRegOp.mov, offset, target,
                target));
    }

    private void translate(Expr.RecordConstructor e, Register target,
            Map<String, Integer> localVariables, X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // First, get fields in sorted order!
        ArrayList<Pair<String, Expr>> fields = new ArrayList<Pair<String, Expr>>(e.getFields());
        sortFields(fields);

        // Create space on the stack for the resulting record
        Type.Record type = (Type.Record) unwrap(e.attribute(Attribute.Type.class).type);
        int width = determineWidth(type);
        int paddedWidth = determineAlignedStackWidth(width);
        // Create space for the stack frame, which consists of the local
        // variables.
        instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.sub, paddedWidth, HSP));

        // Second, translate fields in the appropriate order and push them onto
        // the stack in their appropriate order. This is a little tricky because
        // we need to flatten nested fields appropriately.
        int offset = paddedWidth;
        for (int i = fields.size() - 1; i >= 0; --i) {
            Pair<String, Expr> p = fields.get(i);
            // TODO: HDI and target mismatch?
            translate(p.second(), target, localVariables, code, data);
            // Implement the assignment
            offset -= determineWidth(type.getFields().get(p.first()));
            // TODO: I changed from HDI to target here
            writeToStack(type.getFields().get(p.first()), target, HSP, offset, code, data);
        }

        // Finally, create the target pointer from the stack pointer
        instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HSP, target));
        if (paddedWidth != width) {
            // This is necessary because we've had to ensure the stack is
            // aligned properly, which means there may be some extra padding
            // after the end of the record.
            instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.add, paddedWidth - width,
                    target));
        }
    }

    private void translate(Expr.Unary e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // First, translate lhs and store result in the target register.
        Type exprType = getType(e.getExpr());
        translate(e.getExpr(), target, localVariables, code, data);

        // Finally, perform the binary operation.
        switch (e.getOp()) {
            case LENGTHOF:
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, target, HDI));
                String typeLabel = freshLabel();
                addTypeConstant(exprType, typeLabel, data);
                instructions.add(new Instruction.AddrRegReg(Instruction.AddrRegRegOp.lea, typeLabel,
                        HIP, HSI));
                instructions.add(new Instruction.Addr(Instruction.AddrOp.call, "lengthof"));
                instructions.add(new Instruction.RegReg(Instruction.RegRegOp.mov, HAX, target));
                break;
            case NOT:
                // First, perform logical not of all bites
                instructions.add(new Instruction.Reg(Instruction.RegOp.not, target));
                // Second, ensure only bit 1 may be set
                instructions.add(new Instruction.ImmReg(Instruction.ImmRegOp.and, 1, target));
                break;
            case NEG:
                // TODO: Reals
                instructions.add(new Instruction.Reg(Instruction.RegOp.neg, target));
                break;
            default:
                throw new IllegalArgumentException("Unknown unary operator: " + e);
        }
    }

    private void translate(Expr.Variable e, Register target, Map<String, Integer> localVariables,
            X86File.Code code, X86File.Data data) {
        List<Instruction> instructions = code.instructions;

        // There are two cases we need to consider here: primitive values; and,
        // compound values. For the former, we load their value directly into
        // the target register. For the latter, we load a pointer to their value
        // directly into the target register.

        // Determine the offset within the stack of this local variable.
        int offset = localVariables.get(e.getName());

        // Extract the variable's type to help determine which case to handle
        Type type = e.attribute(Attribute.Type.class).type;

        // Finally, read the target into the target register
        readFromStack(type, HBP, offset, target, code, data);
    }

    private Type unwrap(Type type) {
        if (type instanceof Type.Named) {
            Type.Named tn = (Type.Named) type;
            WhileFile.TypeDecl td = types.get(tn.getName());
            return td.type;
        } else {
            return type;
        }
    }

    /**
     * Copy a data value from a given register into a stack location offset from a base pointer. In
     * the case of a compound value, then the source register is a pointer to a memory location and
     * we need to perform an indirect copy.
     *
     * @param type Type of data being assigned
     * @param source Register to read data value from
     * @param target Based pointer into stack for assignment
     * @param offset Stack location offset from base pointer to assign to
     */
    private void writeToStack(Type type, Register source, Register target, int offset,
            X86File.Code code, X86File.Data data) {

        List<Instruction> instructions = code.instructions;

        // There are two cases we need to consider here: primitive values;
        // and, compound values. For the former, we write their value directly
        // from the target register. For the latter, we copy data from a pointer
        // to their value over the given target.

        type = unwrap(type); // remove named types

        if (type instanceof Type.Bool || type instanceof Type.Char || type instanceof Type.Int
                || type instanceof Type.Strung || type instanceof Type.List) {
            // These are the primitive types. Therefore, we just read their
            // value directly from the stack into the target register.
            instructions.add(new Instruction.RegImmInd(Instruction.RegImmIndOp.mov, source, offset,
                    target));
        } else if (type instanceof Type.Real) {
            instructions.add(new Instruction.RegImmInd(Instruction.RegImmIndOp.movsd, source,
                    offset, target));
        } else {
            // These are compound types. Basically, we just perform a bitwise
            // copy of each slot.
            int width = determineWidth(type);

            // Determine the number of slots required (note, width is guaranteed
            // to be a multiple of the natural word size).
            int nSlots = width / this.target.widthInBytes();
            int sourceOffset = width;
            int targetOffset = offset + width;

            for (int i = 0; i != nSlots; ++i) {
                // decrement offset by one slot
                targetOffset -= this.target.widthInBytes();
                sourceOffset -= this.target.widthInBytes();
                // Read slot references by source register into temporary
                // register.
                instructions.add(new Instruction.ImmIndReg(Instruction.ImmIndRegOp.mov,
                        sourceOffset, source, HAX));
                // Writer temporary register into slot referenced by target register.
                instructions.add(new Instruction.RegImmInd(Instruction.RegImmIndOp.mov, HAX,
                        targetOffset, target));
            }
        }
    }
}

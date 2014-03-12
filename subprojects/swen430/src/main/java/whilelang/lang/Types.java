package whilelang.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class for providing methods relating to types.
 *
 * @author Henry J. Wylde
 */
public final class Types {

    /**
     * This class cannot be instantiated.
     */
    private Types() {
    }

    public static Object cast(Object obj, Type type, WhileFile file) {
        if (type instanceof Type.Named) {
            return cast(obj, file.type(((Type.Named) type).getName()).type, file);
        } else if (obj instanceof Integer) {
            if (type instanceof Type.Real) {
                return ((Integer) obj).doubleValue();
            }

            return obj;
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            List<Object> nlist = new ArrayList<Object>();

            for (Object item : list) {
                nlist.add(cast(item, ((Type.List) type).getElement(), file));
            }

            return nlist;
        } else if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            Map<String, Type> fields = ((Type.Record) type).getFields();

            Map<String, Object> nmap = new HashMap<String, Object>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                nmap.put(entry.getKey(), cast(entry.getValue(), fields.get(entry.getKey()), file));
            }

            return nmap;
        }

        // No other cast available, assume the type checker validated this cast
        return obj;
    }

    public static Type getType(Object obj) {
        if (obj instanceof Boolean) {
            return new Type.Bool();
        } else if (obj instanceof Character) {
            return new Type.Char();
        } else if (obj instanceof Integer) {
            return new Type.Int();
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;

            // Empty lists can be used for any type of lists, there is no inner type
            // Type.Void here is used to indicate any type
            if (list.isEmpty()) {
                return new Type.List(new Type.Void());
            }

            return new Type.List(getType(list.get(0)));
        } else if (obj == null) {
            return new Type.Null();
        } else if (obj instanceof Double) {
            return new Type.Real();
        } else if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            HashMap<String, Type> fields = new HashMap<String, Type>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                fields.put(entry.getKey(), getType(entry.getValue()));
            }

            return new Type.Record(fields);
        } else if (obj instanceof String) {
            return new Type.Strung();
        } else {
            // Ignore Type.Named, Type.Void and Type.Union as they should never occur
            throw new InternalError("obj instanceof not fully implemented: " + obj.getClass());
        }
    }

    public static boolean isInstance(Object obj, Type type, WhileFile file) {
        return isSubtype(getType(obj), type, file);
    }

    public static boolean isInstance(Type lhs, Type rhs, WhileFile file) {
        lhs = normalise(lhs, file);
        rhs = normalise(rhs, file);

        // Check the lhs instanceof Type.Union case first
        // In this case, all bounds need to be an instance of the rhs
        if (lhs instanceof Type.Union) {
            for (Type bound : ((Type.Union) lhs).getBounds()) {
                if (!(isInstance(bound, rhs, file))) {
                    return false;
                }
            }

            return true;
        }

        // Check for equivalence
        if (isSubtype(lhs, rhs, file) && isSubtype(rhs, lhs, file)) {
            return true;
        }

        if (rhs instanceof Type.Union) {
            for (Type bound : ((Type.Union) rhs).getBounds()) {
                if (isInstance(lhs, bound, file)) {
                    return true;
                }
            }
        } else if (rhs instanceof Type.Record) {
            if (!(lhs instanceof Type.Record)) {
                return false;
            }

            return isInstance((Type.Record) lhs, (Type.Record) rhs, file);
        } else if (rhs instanceof Type.List) {
            if (!(lhs instanceof Type.List)) {
                return false;
            }

            return isInstance(((Type.List) lhs).getElement(), ((Type.List) rhs).getElement(), file);
        }

        return false;
    }

    public static boolean isSubtype(Type lhs, Type rhs, WhileFile file) {
        lhs = normalise(lhs, file);
        rhs = normalise(rhs, file);

        if (lhs instanceof Type.Union) {
            for (Type type : ((Type.Union) lhs).getBounds()) {
                if (!isSubtype(type, rhs, file)) {
                    return false;
                }
            }

            return true;
        } else if (rhs instanceof Type.Union) {
            for (Type type : ((Type.Union) rhs).getBounds()) {
                if (isSubtype(lhs, type, file)) {
                    return true;
                }
            }

            return false;
        } else if (lhs instanceof Type.Void) {
            return true;
        } else if (lhs instanceof Type.Int) {
            return rhs instanceof Type.Int || rhs instanceof Type.Real;
        } else if (lhs instanceof Type.List) {
            // Subtyping checks that the elements are subtypes
            // This is allowed because While has pass-by-value semantics
            // In Java it would not work
            if (rhs instanceof Type.List) {
                return isSubtype(((Type.List) lhs).getElement(), ((Type.List) rhs).getElement(),
                        file);
            }

            return false;
        } else if (lhs instanceof Type.Record) {
            // Support depth subtyping but not width subtyping
            // I.e. {int a, B b} subtypes {real a, B b} but not {int a, B b, C c}

            if (rhs instanceof Type.Record) {
                return isSubtype((Type.Record) lhs, (Type.Record) rhs, file);
            }

            return false;
        } else {
            // Type.Null | Type.Real | Type.Bool | Type.Char | Type.Void | Type.Strung
            return lhs.getClass() == rhs.getClass();
        }
    }

    public static Type normalise(Type type, WhileFile file) {
        if (type instanceof Type.Named) {
            return normalise(file.type(((Type.Named) type).getName()).type, file);
        }

        return type;
    }

    private static boolean isInstance(Type.Record lhs, Type.Record rhs, WhileFile file) {
        Map<String, Type> lhsFields = lhs.getFields();
        Map<String, Type> rhsFields = rhs.getFields();

        // Has to at least have all of the fields names
        if (!lhsFields.keySet().equals(rhsFields.keySet())) {
            return false;
        }

        for (Map.Entry<String, Type> field : lhsFields.entrySet()) {
            if (!isInstance(field.getValue(), rhsFields.get(field.getKey()), file)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSubtype(Type.Record lhs, Type.Record rhs, WhileFile file) {
        // Support depth subtyping but not width subtyping
        // I.e. {int a, B b} subtypes {real a, B b} but not {int a, B b, C c}

        Map<String, Type> lhsFields = lhs.getFields();
        Map<String, Type> rhsFields = rhs.getFields();

        // Has to at least have all of the fields names
        if (!lhsFields.keySet().equals(rhsFields.keySet())) {
            return false;
        }

        for (Map.Entry<String, Type> field : lhsFields.entrySet()) {
            if (!isSubtype(field.getValue(), rhsFields.get(field.getKey()), file)) {
                return false;
            }
        }

        return true;
    }
}

package whilelang.lang;

import java.util.HashMap;
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

    public static Type getType(Object obj) {
        if (obj instanceof Boolean) {
            return new Type.Bool();
        } else if (obj instanceof Character) {
            return new Type.Char();
        } else if (obj instanceof Integer) {
            return new Type.Int();
        } else if (obj instanceof java.util.List) {
            java.util.List<?> list = (java.util.List<?>) obj;

            // Empty lists can be used for any type of lists, there is no inner type
            if (list.isEmpty()) {
                // TODO: Fixme, need an "any" or "object" type that we can return
                throw new InternalError("unable to determine list objects inner type");
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

    public static boolean isInstance(Object obj, Type type) {
        return isSubtype(getType(obj), type);
    }

    public static boolean isSubtype(Type lhs, Type rhs) {
        if (lhs instanceof Type.Bool) {
            if (lhs.getClass() == rhs.getClass()) {
                return true;
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.Char) {
            if (lhs.getClass() == rhs.getClass()) {
                return true;
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.Void) {
            return lhs.getClass() == rhs.getClass();
        } else if (lhs instanceof Type.Union) {
            for (Type type : ((Type.Union) lhs).getBounds()) {
                if (!isSubtype(type, rhs)) {
                    return false;
                }
            }

            return true;
        } else if (lhs instanceof Type.Strung) {
            if (lhs.getClass() == rhs.getClass()) {
                return true;
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.Int) {
            if (lhs.getClass() == rhs.getClass()) {
                return true;
            }

            if (rhs instanceof Type.Real) {
                return true;
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.List) {
            // Going to go with the subtyping semantics of Javas Generics, i.e. a list is only
            // subtypes a list of the same inner type
            if (lhs.getClass() == rhs.getClass()) {
                return ((Type.List) lhs).getElement().getClass() == ((Type.List) rhs).getElement()
                        .getClass();
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.Null) {
            if (lhs.getClass() == rhs.getClass()) {
                return true;
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.Real) {
            if (lhs.getClass() == rhs.getClass()) {
                return true;
            }

            return rhs instanceof Type.Union && isSubtype(lhs, (Type.Union) rhs);
        } else if (lhs instanceof Type.Record) {
            // Support depth subtyping but not width subtyping
            // I.e. {int a, B b} subtypes {real a, B b} but not {int a, B b, C c}

            if (rhs instanceof Type.Union) {
                return isSubtype(lhs, (Type.Union) rhs);
            }

            if (!(rhs instanceof Type.Record)) {
                return false;
            }

            Map<String, Type> lhsFields = ((Type.Record) lhs).getFields();
            Map<String, Type> rhsFields = ((Type.Record) rhs).getFields();

            // Has to at least have all of the fields names
            if (!lhsFields.keySet().equals(rhsFields.keySet())) {
                return false;
            }

            for (Map.Entry<String, Type> field : lhsFields.entrySet()) {
                if (!isSubtype(field.getValue(), rhsFields.get(field.getKey()))) {
                    return false;
                }
            }

            return true;
        } else {
            // Ignore Type.Named as it should be resolved before here
            throw new InternalError("lhs instanceof not fully implemented: " + lhs.getClass());
        }
    }

    private static boolean isSubtype(Type lhs, Type.Union union) {
        for (Type type : union.getBounds()) {
            if (isSubtype(lhs, type)) {
                return true;
            }
        }

        return false;
    }
}

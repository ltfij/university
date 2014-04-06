package com.hjwylde.uni.swen430.assignment01.whilelang.lang;

import com.hjwylde.uni.swen430.assignment01.whilelang.util.Attribute;
import com.hjwylde.uni.swen430.assignment01.whilelang.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A utility class for providing methods relating to types.
 *
 * @author Henry J. Wylde
 */
public final class Types {

    /**
     * This class cannot be instantiated.
     */
    private Types() {}

    /**
     * Attempts to cast this object to the given type. It is assumed that the cast is valid, as if
     * it is not possible to perform a case the provided {@code obj} is returned as is.
     *
     * @param obj the object to cast.
     * @param type the type to cast it to.
     * @param file the while file to perform name resolution.
     * @return a casted version of the object or the object itself.
     */
    public static Object cast(Object obj, Type type, WhileFile file) {
        // Normalise named types. Because a cast type should not contain any union types, we can
        // assume that this is safe to do
        type = normalise(type, file);

        // TODO: Does not support casting to a union type

        if (obj instanceof Integer) {
            // We can cast an int to a real
            if (type instanceof Type.Real) {
                return ((Integer) obj).doubleValue();
            }

            // Assume we're just casting to an int then
            return obj;
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;

            // We need to cast every element inside the list
            List<Object> nlist = new ArrayList<Object>();

            for (Object item : list) {
                nlist.add(cast(item, ((Type.List) type).getElement(), file));
            }

            return nlist;
        } else if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            Map<String, Type> fields = ((Type.Record) type).getFields();

            // We need to cast every element inside the record
            Map<String, Object> nmap = new HashMap<String, Object>();

            // Check and cast each element inside the record
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                nmap.put(entry.getKey(), cast(entry.getValue(), fields.get(entry.getKey()), file));
            }

            return nmap;
        }

        // No other cast available, assume the type checker validated this cast
        return obj;
    }

    /**
     * Attempts to get the type of the given object. There are some cases where the type cannot be
     * determined, these are when the object is an empty list. In this scenario, a list of type
     * {@code void} is returned. The subtype checks use the {@code void} type to mean {@code any}.
     *
     * @param obj the object to get the type of.
     * @return the type of the object.
     */
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

            // Very hacky using the toString method for equality, but one of the easiest options for
            // now
            Map<String, Type> inners = new HashMap<String, Type>();

            for (Object o : list) {
                Type inner = getType(o);

                inners.put(inner.toString(), inner);
            }

            if (inners.size() == 1) {
                return new Type.List(inners.values().iterator().next());
            }

            return new Type.List(new Type.Union(inners.values()));
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
            // Type.Named, Type.Void and Type.Union should never occur
            throw new InternalError("obj instanceof not fully implemented: " + obj.getClass());
        }
    }

    /**
     * Determine whether two given types are equivalent. Identical types are always equivalent.
     * Furthermore, e.g. "int|null" is equivalent to "null|int".
     *
     * @param t1 first type to compare
     * @param t2 second type to compare
     * @param file the while file for name resolution.
     */
    public static boolean isEquivalent(Type t1, Type t2, WhileFile file) {
        return isSubtype(t1, t2, file) && isSubtype(t2, t1, file);
    }

    /**
     * Determines whether the given type {@code lhs} is an instance of the former type, Another way
     * of saying this is whether an object of type {@code lhs} can by assigned to a type of {@code
     * rhs}. An acceptable assignable type is either: <ul> <li>an equivalent type</li> <li>an
     * equivalent type of one of the union bounds of the former type</li> </ul>
     * <p/>
     * This is because the language does not support implicit conversions, so we only take into
     * account equivalent types and unions.
     */
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
        if (isEquivalent(lhs, rhs, file)) {
            return true;
        }

        // Check special cases
        // TODO: Verify is the isEquivalent already takes these into account
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

    /**
     * Checks to see whether the {@code lhs} type is a subtype of the {@code rhs} type. The subtype
     * definition varies between types, but in general the following applies: <ul> <li>each type
     * subtypes itself</li> <li>a type subtypes a union type if it subtypes one of its bounds</li>
     * <li>a record or list type subtypes another if all of its inner types subtype the equivalent
     * inner types</li> <li>the void type is treated as an any type</li> </ul>
     *
     * @param lhs the left hand side (or subtype) to check.
     * @param rhs the right hand side (or supertype) to check.
     * @param file the while file for name resolution.
     * @return true if {@code lhs} subtypes {@code rhs}.
     */
    public static boolean isSubtype(Type lhs, Type rhs, WhileFile file) {
        lhs = normalise(lhs, file);
        rhs = normalise(rhs, file);

        if (lhs instanceof Type.Union) {
            // We need every bound to be a subtype of the rhs
            for (Type type : ((Type.Union) lhs).getBounds()) {
                if (!isSubtype(type, rhs, file)) {
                    return false;
                }
            }

            return true;
        } else if (rhs instanceof Type.Union) {
            // We need to be a subtype of at least one of the rhs bounds
            for (Type type : ((Type.Union) rhs).getBounds()) {
                if (isSubtype(lhs, type, file)) {
                    return true;
                }
            }

            return false;
        } else if (lhs instanceof Type.Void) {
            // Treat void as any (to account for empty lists)
            return true;
        } else if (lhs instanceof Type.Int) {
            // Int subtypes real and int
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

    /**
     * Checks to see whether the given object is a subtype of the given type. This is equivalent to
     * calling:
     * <pre>
     *     isSubtype(getType(obj), type, file);
     * </pre>
     *
     * @param obj the object to check.
     * @param type the type to check.
     * @param file the file for name resolution.
     * @return true if the object is an instance of the given type.
     */
    public static boolean isSubtype(Object obj, Type type, WhileFile file) {
        return isSubtype(getType(obj), type, file);
    }

    /**
     * Attempts to normalise the type to allow for ease of type checks (e.g. equivalence or
     * subtypes).
     *
     * @param type the type to normalise.
     * @param file the while file for name resolution.
     * @return the normalised type.
     */
    public static Type normalise(Type type, WhileFile file) {
        if (type instanceof Type.Named) {
            return normalise(file.type(((Type.Named) type).getName()).type, file);
        } else if (type instanceof Type.List) {
            return new Type.List(normalise(((Type.List) type).getElement(), file),
                    type.attributes().toArray(new Attribute[0]));
        } else if (type instanceof Type.Union) {
            return normalise((Type.Union) type, file);
        } else if (type instanceof Type.Record) {
            return normalise((Type.Record) type, file);
        }

        // Everything else is already normalised
        return type;
    }

    /**
     * Computers the cartesian product of the given sets.
     * <p/>
     * Note: This code was taken from a <i>stackoverflow</i> question, unfortunately I didn't write
     * down the URL to cite.
     *
     * @param sets the sets.
     * @param <T> the type of items in the sets.
     * @return the cartesian product of the sets.
     */
    private static <T> Set<List<T>> cartesianProduct(List<Set<T>> sets) {
        return cartesianProduct(0, sets);
    }

    /**
     * Computers the cartesian product of the given sets.
     * <p/>
     * Note: This code was taken from a <i>stackoverflow</i> question, unfortunately I didn't write
     * down the URL to cite.
     *
     * @param index the index of the set we are computing from.
     * @param sets the sets.
     * @param <T> the type of items in the sets.
     * @return the cartesian product of the sets.
     */
    private static <T> Set<List<T>> cartesianProduct(int index, List<Set<T>> sets) {
        Set<List<T>> ret = new HashSet<List<T>>();
        if (index == sets.size()) {
            ret.add(new ArrayList<T>());
        } else {
            for (T t : sets.get(index)) {
                for (List<T> list : cartesianProduct(index + 1, sets)) {
                    list.add(t);
                    ret.add(list);
                }
            }
        }

        return ret;
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

    /**
     * Checks whether the given record ({@code lhs}) is a subtype of the second record ({@code rhs})
     * . A record is considered to be a subtype if it contains all the fields of the other one and
     * the type of each of them is a subtype in turn. Note that this does not support width
     * subtyping, where a record can subtype a record that has more fields than the former.
     *
     * @param lhs the subtype to check.
     * @param rhs the supertype to check.
     * @param file the while file for name resolution.
     * @return true if {@code lhs} subtypes {@code rhs}.
     */
    private static boolean isSubtype(Type.Record lhs, Type.Record rhs, WhileFile file) {
        // Support depth subtyping but not width subtyping
        // I.e. {int a, B b} subtypes {real a, B b} but not {int a, B b, C c}

        Map<String, Type> lhsFields = lhs.getFields();
        Map<String, Type> rhsFields = rhs.getFields();

        // Has to at least have all of the fields names
        if (!lhsFields.keySet().equals(rhsFields.keySet())) {
            return false;
        }

        // Check that each fields type is a subtype
        for (Map.Entry<String, Type> field : lhsFields.entrySet()) {
            if (!isSubtype(field.getValue(), rhsFields.get(field.getKey()), file)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Normalises the given union type. A normalised union type simply normalises all inner types.
     * If the size of the union is 1 then just that type is returned.
     *
     * @param type the union type to normalise.
     * @param file the while file for name resolution.
     * @return the normalised union type.
     */
    private static Type normalise(Type.Union type, WhileFile file) {
        List<Type> ntypes = new ArrayList<Type>();

        // TODO: Not taking into account type equivalency here
        for (Type inner : type.getBounds()) {
            Type normalised = normalise(inner, file);

            if (normalised instanceof Type.Union) {
                ntypes.addAll(((Type.Union) normalised).getBounds());
            } else {
                ntypes.add(normalised);
            }
        }

        if (ntypes.size() == 1) {
            return ntypes.get(0);
        }

        return new Type.Union(ntypes);
    }

    /**
     * Normalises the given record type. The purpose of this is to have a consistent form for
     * comparing different types (specifically, subtype relations). A normalised record will have
     * all element types normalised, along with any union types split up. For example, {@code
     * {int|null a, real b}} would be normalised to {@code {int a, real b}|{null a, real b}}. This
     * maintains all properties of the type while making it easy to do subtype comparisons between
     * records.
     *
     * @param type the record type to normalise.
     * @return the normalised record type.
     * @file the while file for name resolution.
     */
    private static Type normalise(Type.Record type, WhileFile file) {
        // We're going to split up all the inner unions of the record
        // An example of how it will be represented:
        // {int|null a, real b} = [{("a", int), ("a", null)}, {("b", real)}]
        // That way when we do a cartesian product on all of the sets, we will get all possible
        // record types
        // The final result will be: {int a, real b}|{null a, real b}

        List<Set<Pair<String, Type>>> split = new ArrayList<Set<Pair<String, Type>>>();
        for (Map.Entry<String, Type> entry : type.getFields().entrySet()) {
            Set<Pair<String, Type>> pairs = new HashSet<Pair<String, Type>>();
            Type normalisedType = normalise(entry.getValue(), file);

            if (normalisedType instanceof Type.Union) {
                for (Type inner : ((Type.Union) entry.getValue()).getBounds()) {
                    pairs.add(new Pair<String, Type>(entry.getKey(), normalise(inner, file)));
                }
            } else {
                pairs.add(new Pair<String, Type>(entry.getKey(), normalisedType));
            }

            split.add(pairs);
        }

        // Cartesian product time!
        Set<List<Pair<String, Type>>> product = cartesianProduct(split);

        // Recreate the union or records now, so we'll have {int a, real b}|{null a, real b}
        List<Type> union = new ArrayList<Type>();
        for (List<Pair<String, Type>> inner : product) {
            Map<String, Type> record = new HashMap<String, Type>();
            for (Pair<String, Type> pair : inner) {
                record.put(pair.first(), pair.second());
            }
            union.add(new Type.Record(record));
        }

        if (union.size() == 1) {
            return union.get(0);
        }

        return new Type.Union(union);
    }
}

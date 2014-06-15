package whilelang.runtime;

import static whilelang.util.RuntimeError.castError;
import static whilelang.util.SyntaxError.internalFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import whilelang.lang.Type;
import whilelang.util.Attribute;
import whilelang.util.Pair;
import whilelang.util.RuntimeError;

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
     * Attempts to cast this object to the given type. If the cast is invalid, an error is thrown.
     *
     * @param obj the object to cast.
     * @param type the type to cast it to.
     * @return a casted version of the object or the object itself.
     * @throws whilelang.util.RuntimeError if the cast is invalid.
     */
    public static Object cast(Object obj, Type type) throws RuntimeError {
        // Check if the object is already a subtype, if it is, return it
        if (isSubtype(obj, type)) {
            return obj;
        }

        // Check if the type is a union, now we need to cast the obj to any one of the inner types
        if (type instanceof Type.Union) {
            // Little hacky, it would be nicer not to have the performance hit by throwing / catching exceptions here

            for (Type inner : ((Type.Union) type).getBounds()) {
                try {
                    return cast(obj, inner);
                } catch (RuntimeError e) {
                    // Couldn't cast it to that inner type, let's try the next
                }
            }
        }

        if (obj instanceof Integer) {
            // We can cast an int to a real
            if (type instanceof Type.Real) {
                return ((Integer) obj).doubleValue();
            }
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;

            // We need to cast every element inside the list
            List<Object> nlist = new ArrayList<Object>();

            for (Object item : list) {
                nlist.add(cast(item, ((Type.List) type).getElement()));
            }

            return nlist;
        } else if (obj instanceof Record) {
            Record record = (Record) obj;
            Map<String, Type> fields = ((Type.Record) type).getFields();

            // We need to cast every element inside the record
            Record nrecord = new Record();

            // Check and cast each element inside the record
            for (Map.Entry<String, Object> entry : record.getData().entrySet()) {
                nrecord.put(entry.getKey(), cast(entry.getValue(), fields.get(entry.getKey())));
            }

            return nrecord;
        }

        // No other cast available, cast must be invalid
        throw castError(obj, type);
    }

    /**
     * Attempts to get the type of the given object. There are some cases where the type cannot be
     * determined, these are when the object is an empty list. In this scenario, a list of type
     * {@code void} is returned.
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
            if (list.isEmpty()) {
                return new Type.List(new Type.Any());
            }

            Map<String, Type> inners = new HashMap<String, Type>();
            for (Object o : list) {
                Type inner = getType(o);

                inners.put(inner.toString(), inner);
            }

            return normalise(new Type.List(new Type.Union(inners.values())));
        } else if (obj == null || obj instanceof Null) {
            return new Type.Null();
        } else if (obj instanceof Double) {
            return new Type.Real();
        } else if (obj instanceof Record) {
            Record record = (Record) obj;
            HashMap<String, Type> fields = new HashMap<String, Type>();

            for (Map.Entry<String, Object> entry : record.getData().entrySet()) {
                fields.put(entry.getKey(), getType(entry.getValue()));
            }

            return normalise(new Type.Record(fields));
        } else if (obj instanceof String) {
            return new Type.Strung();
        } else {
            throw new InternalError("obj instanceof not fully implemented: " + obj.getClass());
        }
    }

    /**
     * Determine whether two given types are equivalent. Identical types are always equivalent.
     * Furthermore, e.g. "int|null" is equivalent to "null|int".
     *
     * @param t1 first type to compare
     * @param t2 second type to compare
     */
    public static boolean isEquivalent(Type t1, Type t2) {
        return isSubtype(t1, t2) && isSubtype(t2, t1);
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
     * @return true if the object is an instance of the given type.
     */
    public static boolean isSubtype(Object obj, Type type) {
        return isSubtype(getType(obj), type);
    }

    /**
     * Checks to see whether the {@code lhs} type is a subtype of the {@code rhs} type. The subtype
     * definition varies between types, but in general the following applies: <ul> <li>each type
     * subtypes itself</li> <li>a type subtypes a union type if it subtypes one of its bounds</li>
     * <li>a record or list type subtypes another if all of its inner types subtype the equivalent
     * inner types</li></ul>
     *
     * @param lhs the left hand side (or subtype) to check.
     * @param rhs the right hand side (or supertype) to check.
     * @return true if {@code lhs} subtypes {@code rhs}.
     */
    public static boolean isSubtype(Type lhs, Type rhs) {
        lhs = normalise(lhs);
        rhs = normalise(rhs);

        if (lhs instanceof Type.Union) {
            // We need every bound to be a subtype of the rhs
            for (Type type : ((Type.Union) lhs).getBounds()) {
                if (!isSubtype(type, rhs)) {
                    return false;
                }
            }

            return true;
        } else if (rhs instanceof Type.Union) {
            // We need to be a subtype of at least one of the rhs bounds
            for (Type type : ((Type.Union) rhs).getBounds()) {
                if (isSubtype(lhs, type)) {
                    return true;
                }
            }

            return false;
        } else if (lhs instanceof Type.Any) {
            return true;
        } else if (lhs instanceof Type.List) {
            // Subtyping checks that the elements are subtypes
            // This is allowed because While has pass-by-value semantics
            // In Java it would not work
            if (rhs instanceof Type.List) {
                return isSubtype(((Type.List) lhs).getElement(), ((Type.List) rhs).getElement());
            }

            return false;
        } else if (lhs instanceof Type.Record) {
            // Support depth subtyping but not width subtyping
            // I.e. {int a, B b} subtypes {real a, B b} but not {int a, B b, C c}

            if (rhs instanceof Type.Record) {
                return isSubtype((Type.Record) lhs, (Type.Record) rhs);
            }

            return false;
        } else {
            // Type.Null | Type.Real | Type.Bool | Type.Char | Type.Void | Type.Strung | Type.Int
            return lhs.getClass() == rhs.getClass();
        }
    }

    /**
     * Attempts to normalise the type to allow for ease of type checks (e.g. equivalence or
     * subtypes).
     *
     * @param type the type to normalise.
     * @return the normalised type.
     */
    public static Type normalise(Type type) {
        if (type instanceof Type.List) {
            return new Type.List(normalise(((Type.List) type).getElement()));
        } else if (type instanceof Type.Union) {
            return normalise((Type.Union) type);
        } else if (type instanceof Type.Record) {
            return normalise((Type.Record) type);
        }

        // Everything else is already normalised
        return type;
    }

    /**
     * Computers the cartesian product of the given sets.
     * <p>
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
     * <p>
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

    /**
     * Checks whether the given record ({@code lhs}) is a subtype of the second record ({@code rhs})
     * . A record is considered to be a subtype if it contains all the fields of the other one and
     * the type of each of them is a subtype in turn. Note that this does not support width
     * subtyping, where a record can subtype a record that has more fields than the former.
     *
     * @param lhs the subtype to check.
     * @param rhs the supertype to check.
     * @return true if {@code lhs} subtypes {@code rhs}.
     */
    private static boolean isSubtype(Type.Record lhs, Type.Record rhs) {
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
            if (!isSubtype(field.getValue(), rhsFields.get(field.getKey()))) {
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
     * @return the normalised union type.
     */
    private static Type normalise(Type.Union type) {
        List<Type> normalisedTypes = new ArrayList<Type>();

        // Normalise each inner type and if it is a union, add all of the inner types to this one
        for (Type inner : type.getBounds()) {
            Type normalised = normalise(inner);

            if (normalised instanceof Type.Union) {
                normalisedTypes.addAll(((Type.Union) normalised).getBounds());
            } else {
                normalisedTypes.add(normalised);
            }
        }

        // Go through all of the normalised types and remove equivalent ones
        List<Type> union = new ArrayList<Type>();
        UNION:
        while (normalisedTypes.size() > 0) {
            Type normalised = normalisedTypes.remove(0);

            // If it contains a "any" type, then just return that
            if (normalised instanceof Type.Any) {
                return new Type.Any(type.attributes().toArray(new Attribute[0]));
            } else if (normalised instanceof Type.Void) {
                throw new InternalError("Type.Void apparent in union type: " + type);
            }

            for (Type inner : normalisedTypes) {
                // If this type is equivalent to another, skip it and let the other be added when it gets round to it
                if (isEquivalent(normalised, inner)) {
                    continue UNION;
                }
            }

            union.add(normalised);
        }

        // Check for only 1 type
        if (union.size() == 1) {
            return union.get(0);
        }

        // Uh oh...
        if (union.size() == 0) {
            internalFailure("attempt to normalise union resulted in a 0 elements: ", null, type);
        }

        return new Type.Union(union, type.attributes().toArray(new Attribute[0]));
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
     */
    private static Type normalise(Type.Record type) {
        // We're going to split up all the inner unions of the record
        // An example of how it will be represented:
        // {int|null a, real b} = [{("a", int), ("a", null)}, {("b", real)}]
        // That way when we do a cartesian product on all of the sets, we will get all possible
        // record types
        // The final result will be: {int a, real b}|{null a, real b}

        // Split up the record as per the example above
        List<Set<Pair<String, Type>>> split = new ArrayList<Set<Pair<String, Type>>>();
        for (Map.Entry<String, Type> entry : type.getFields().entrySet()) {
            Set<Pair<String, Type>> pairs = new HashSet<Pair<String, Type>>();
            Type normalised = normalise(entry.getValue());

            if (normalised instanceof Type.Union) {
                for (Type inner : ((Type.Union) normalised).getBounds()) {
                    pairs.add(new Pair<String, Type>(entry.getKey(), normalise(inner)));
                }
            } else {
                pairs.add(new Pair<String, Type>(entry.getKey(), normalised));
            }

            split.add(pairs);
        }

        // Cartesian product time!
        Set<List<Pair<String, Type>>> product = cartesianProduct(split);

        // Recreate the union of records now, so we'll have {int a, real b}|{null a, real b}
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

        return new Type.Union(union, type.attributes().toArray(new Attribute[0]));
    }
}

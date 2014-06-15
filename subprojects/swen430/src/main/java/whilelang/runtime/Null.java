package whilelang.runtime;

/**
 * A null object instance.
 *
 * @author Henry J. Wylde
 */
public final class Null {

    /**
     * A single instance of a {@link whilelang.runtime.Null}.
     */
    public static final Null INSTANCE = new Null();

    /**
     * This class may only be instantiated locally.
     */
    private Null() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "null";
    }
}

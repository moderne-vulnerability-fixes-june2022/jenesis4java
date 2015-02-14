package net.sourceforge.jenesis4java;

/**
 * Models a resource declaration in a try-with-resources statement.
 */
public interface TryResource {

    /**
     * Getter method for the isFinal flag.
     */
    boolean isFinal();

    /**
     * Setter method for the isFinal flag.
     */
    TryResource isFinal(boolean value);
}

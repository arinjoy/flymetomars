package flymetomars.common;

/**
 * Custom exception to make the circumstance of an Illegal Argument that is also
 * a Null Pointer more clear when examining the unchecked sanity exceptions.
 * 
 * This exception represents the exceptional circumstance where a supplied
 * method argument was an illegal argument due to being a null pointer.
 * 
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class NullArgumentException extends IllegalArgumentException {
    
    /**
     * Default constructor
     */
    public NullArgumentException() { super(); }
    
    /**
     * Throwable-wrapping constructor
     * 
     * @param t The Exception/Error to include within stack trace
     */
    public NullArgumentException(Throwable t) { super(t); }
    
    /**
     * Message-only super-type wrapping constructor
     * 
     * @param msg String message indicating reason for this exception
     */
    public NullArgumentException(String msg) { super(msg); }
    
    /**
     * Full feature constructor accepting message description for exception
     * reason, as well as inner Exception/Error for inclusion in stack trace
     * 
     * @param msg String message indicating reason for this exception
     * @param t The Exception/Error to include within stack trace
     */
    public NullArgumentException(String msg, Throwable t) { super(msg, t); }
    
}

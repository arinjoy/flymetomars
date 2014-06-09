package flymetomars.common;

/**
 * Custom exception representing circumstance of invalid password validation
 * 
 * @author Lawrence Colman
 */
public class InvalidPasswordException extends AbstractStringValidationException {

    private String badPass;
    
    /**
     * Default constuctor
     */
    public InvalidPasswordException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param badPass the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public InvalidPasswordException(String badPass, String message) {
        this(message);
        this.badPass=badPass;
    }
    
    /**
     * Full constructor taking violating-value, message and inner-cause
     * 
     * @param badPass the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     * @param inner  the Error/Exception that directly caused this one
     */
    public InvalidPasswordException(String badPass, String message, Throwable inner) {
        super(message, inner);
        this.badPass=badPass;
    }
    
    /**
     * Inner-cause plus message constructor taking both message and inner-cause
     * 
     * @param message String message describing the circumstances for this issue
     * @param inner  the Error/Exception that directly caused this one
     */
    public InvalidPasswordException(String message, Throwable inner) {
        super(message, inner);
    }

    /**
     * Property accessor for the Password String that failed Password validation
     * 
     * @return The String that was found to violate the Password business rules.
     */
    public String getBadPassword() {
        return this.badPass;
    }
    
}

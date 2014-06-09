package flymetomars.common;

/**
 * Custom exception representing circumstance of invalid Email Address validation
 * 
 * @author Lawrence Colman
 */
public class InvalidEmailException extends AbstractStringValidationException {

    private String badEmail;
    
    /**
     * Default constuctor
     */
    public InvalidEmailException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public InvalidEmailException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param badEmail the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public InvalidEmailException(String badEmail, String message) {
        this(message);
        this.badEmail=badEmail;
    }
    
    /**
     * Full constructor taking violating-value, message and inner-cause
     * 
     * @param badEmail the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     * @param inner  the Error/Exception that directly caused this one
     */
    public InvalidEmailException(String badEmail, String message, Throwable inner) {
        super(message, inner);
        this.badEmail=badEmail;
    }
    
    /**
     * Inner-cause plus message constructor taking both message and inner-cause
     * 
     * @param message String message describing the circumstances for this issue
     * @param inner  the Error/Exception that directly caused this one
     */
    public InvalidEmailException(String message, Throwable inner) {
        super(message, inner);
    }

    /**
     * Property accessor for the E-mail String that failed E-mail validation
     * 
     * @return The String that was found to be in violation of the e-mail rules.
     */
    public String getBadEmail() {
        return this.badEmail;
    }
    
}

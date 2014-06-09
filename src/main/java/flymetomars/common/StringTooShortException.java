package flymetomars.common;

/**
 * Represents the exceptional circumstance of a string being validated too short
 * 
 * @author Lawrence Colman
 */
public class StringTooShortException extends AbstractStringValidationException {

    private Integer minimumLength;
    
    /**
     * Default constuctor
     */
    public StringTooShortException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public StringTooShortException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param inViolation the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public StringTooShortException(String inViolation, String message) {
        super(inViolation, message);
    }

    /**
     * Inner-cause plus message constructor taking both message and inner-cause
     * 
     * @param message String message describing the circumstances for this issue
     * @param error  the Error/Exception that directly caused this one
     */
    public StringTooShortException(String message, Throwable error) {
        super(message, error);
    }
    
    /**
     * Violating-Value constructor accepting Inner-cause
     * 
     * @param badString the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     * @param child  the Error/Exception that directly caused this one
     */
    public StringTooShortException(String badString, String message, Throwable child) {
        super(badString, message, child);
    }
    
    /**
     * Violating-Value plus threshold constructor taking message
     * 
     * @param inViolation the violating String value that caused this Exception
     * @param minimum The minimum length value that inViolation did not meet
     * @param message String message describing the circumstances for this issue
     */
    public StringTooShortException(String inViolation, int minimum, String message) {
        this(inViolation, message);
        this.minimumLength=minimum;
    }
    
    /**
     * Full constructor taking violating-value, threshold, message and inner-cause
     * 
     * @param inViolation the violating String value that caused this Exception
     * @param minimum The minimum length value that inViolation did not meet
     * @param message String message describing the circumstances for this issue
     * @param child  the Error/Exception that directly caused this one
     */
    public StringTooShortException(String inViolation, int minimum, String message, Throwable child) {
        this(inViolation, message, child);
        this.minimumLength=minimum;
    }

     /**
     * Property accessor for the numerical threshold which was violated as cause
     * 
     * @return Integer number representing the minimum length which was violated
     */
    public Integer getMinimumLength() {
        return this.minimumLength;
    }
    
}

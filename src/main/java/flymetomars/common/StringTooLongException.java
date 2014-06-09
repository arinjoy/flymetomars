package flymetomars.common;

/**
 * Represents the exceptional circumstance of a string being validated too long.
 * 
 * @author Lawrence Colman
 */
public class StringTooLongException extends AbstractStringValidationException {

    private Integer maximimLength;

    /**
     * Default constuctor
     */
    public StringTooLongException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public StringTooLongException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param inViolation the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public StringTooLongException(String inViolation, String message) {
        super(inViolation, message);
    }

    /**
     * Inner-cause plus message constructor taking both message and inner-cause
     * 
     * @param message String message describing the circumstances for this issue
     * @param error  the Error/Exception that directly caused this one
     */
    public StringTooLongException(String message, Throwable error) {
        super(message, error);
    }
    
    /**
     * Violating-Value constructor accepting Inner-cause
     * 
     * @param badString the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     * @param child  the Error/Exception that directly caused this one
     */
    public StringTooLongException(String badString, String message, Throwable child) {
        super(badString, message, child);
    }
    
    /**
     * Violating-Value plus threshold constructor taking message
     * 
     * @param inViolation the violating String value that caused this Exception
     * @param maximim The maximim length value that inViolation did not meet
     * @param message String message describing the circumstances for this issue
     */
    public StringTooLongException(String inViolation, int maximim, String message) {
        this(inViolation, message);
        this.maximimLength=maximim;
    }
    
    /**
     * Full constructor taking violating-value, threshold, message and inner-cause
     * 
     * @param inViolation the violating String value that caused this Exception
     * @param maximim The maximim length value that inViolation did not meet
     * @param message String message describing the circumstances for this issue
     * @param child  the Error/Exception that directly caused this one
     */
    public StringTooLongException(String inViolation, int maximim, String message, Throwable child) {
        this(inViolation, message, child);
        this.maximimLength=maximim;
    }

     /**
     * Property accessor for the numerical threshold which was violated as cause
     * 
     * @return Integer number representing the maximum length which was violated
     */
    public Integer getMaximimLength() {
        return this.maximimLength;
    }
    
}

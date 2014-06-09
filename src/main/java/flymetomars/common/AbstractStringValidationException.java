package flymetomars.common;

/**
 * Represents the series of Custom Exceptions pertaining to Invalid Strings
 * 
 * @author Lawrence Colman
 */
/*package-private*/ abstract class AbstractStringValidationException extends FMTMValidationException {

    private String inner;
    
    /**
     * Default constuctor
     */
    protected AbstractStringValidationException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractStringValidationException(String message) {
        super(message);
    }
    
    /**
     * Inner-cause plus message constructor taking both message and inner-cause
     * 
     * @param message String message describing the circumstances for this issue
     * @param inner  the Error/Exception that directly caused this one
     */
    protected AbstractStringValidationException(String message, Throwable child) {
        super(message, child);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param badString the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractStringValidationException(String badString, String message) {
        super(message);
        this.inner=badString;
    }
    
    /**
     * Full Exception constructor taking message, bad value and inner-cause
     * 
     * @param badString the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     * @param child the Error/Exception that directly caused this Exception
     */
    protected AbstractStringValidationException(String badString, String message, Throwable child) {
        super(message, child);
        this.inner=badString;
    }
    
    /**
     * Property accessor for the String against which validation was performed
     * 
     * @return The String that was found to be in violation of the rule(s).
     */
    public String getStringInQuestion() {
        return this.inner;
    }   
    
}

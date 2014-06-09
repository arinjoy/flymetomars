package flymetomars.common;

/**
 * Application specific inheritance root exception for unchecked validation errors
 * 
 * @author Lawrence Colman
 */
public class FMTMValidationException extends IllegalArgumentException {

    /**
     * Default constuctor
     */
    public FMTMValidationException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public FMTMValidationException(String message) {
        super(message);
    }
    
    /**
     * Full Exception constructor taking both message and inner-cause
     * 
     * @param message String message describing the circumstances for this issue
     * @param inner  the Error/Exception that directly caused this one
     */
    public FMTMValidationException(String message, Throwable inner) {
        super(message, inner);
    }
    
    /**
     * Inner-cause constructor
     * 
     * @param inner the Error/Exception that directly caused this one
     */
    public FMTMValidationException(Throwable inner) {
        super(inner);
    }
    
}

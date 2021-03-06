package flymetomars.common;

/**
 * Custom exception representing the exceptional circumstance of a number being
 * validated and being below some predetermined threshold business rule value.
 * 
 * @author Lawrence Colman
 */
public class NumberTooSmallException extends AbstractNumberValidationException {

    private Double threshold;

    /**
     * Default constuctor
     */
    public NumberTooSmallException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public NumberTooSmallException(String message) {
        super(message);
    }

    /**
     * Full Exception constructor taking violating-value, threshold and message
     * 
     * @param threshold the Double precision number threshold that was violated
     * @param number the violating Double precision number that caused Exception
     * @param message String message describing the circumstances for this issue
     */
    public NumberTooSmallException(Double threshold, Double number, String message) {
        super(number, message);
        this.threshold=threshold;
    }
    
    /**
     * Full Exception constructor taking violating-value, threshold and message
     * 
     * @param threshold the Integer numerical value threshold that was violated
     * @param number the violating Integer number that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public NumberTooSmallException(Integer threshold, Integer number, String message) {
        super(number, message);
        this.threshold=threshold.doubleValue();
    }
    
    /**
     * Violating-Value plus Required Threshold constructor - Double
     * 
     * @param threshold the Double precision number threshold that was violated
     * @param number the violating Double precision number that caused Exception
     */
    public NumberTooSmallException(Double threshold, Double number) {
        this(threshold, number, "number too small");
    }
    
    /**
     * Violating-Value plus Required Threshold constructor - Integer
     * 
     * @param threshold the Integer numerical value threshold that was violated
     * @param number the violating Integer number that caused this Exception
     */
    public NumberTooSmallException(Integer threshold, Integer number) {
        this(threshold, number, "number too small");
    }
    
    /**
     * Property accessor for the numeric threshold against which validation was performed
     * 
     * @return The numeric threshold that the bad number was in violation of.
     */
    @Override
    public Double getViolatedThreshold() {
        return this.threshold;
    }

}

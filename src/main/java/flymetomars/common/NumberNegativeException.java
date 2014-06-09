package flymetomars.common;

/**
 * Custom exception representing the validation circumstance when a given
 * numeric value is negative when it is undergoing validation to ensure that
 * it is non-negative.
 * 
 * @author Lawrence Colman
 */
public class NumberNegativeException extends AbstractNumberValidationException {

    /**
     * Default constuctor
     */
    public NumberNegativeException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public NumberNegativeException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor - Double
     * 
     * @param number the violating Double precision value that caused Exception
     * @param message String message describing the circumstances for this issue
     */
    public NumberNegativeException(Double number, String message) {
        super(number, message);
    }
    
    /**
     * Violating-Value constructor - Integer
     * 
     * @param number the violating Integer number value that caused Exception
     * @param message String message describing the circumstances for this issue
     */
    public NumberNegativeException(Integer number, String message) {
        super(number, message);
    }
    
    /**
     * Violating-Value constructor - Long
     * 
     * @param number the violating Long integer number that caused Exception
     * @param message String message describing the circumstances for this issue
     */
    public NumberNegativeException(Long number, String message) {
        super(number, message);
    }
    
    /**
     * Violating-Value constructor - Double (messageless)
     * 
     * @param number the violating Double precision value that caused Exception
     */
    public NumberNegativeException(Double number) {
        super(number, "Number cannot be negative");
    }

/**
     * Violating-Value constructor - Integer (messageless)
     * 
     * @param number the violating Integer number value that caused Exception
     */
    public NumberNegativeException(Integer number) {
        this(number, "Number cannot be negative");
    }
    
    /**
     * Violating-Value constructor - Long (messageless)
     * 
     * @param number the violating Long integer number that caused Exception
     */
    public NumberNegativeException(Long number) {
        this(number, "Number cannot be negative");
    }

    /**
     * Property accessor for the numeric threshold against which validation was performed
     * 
     * @return The numeric threshold that the bad number was in violation of.
     * 
     * Note:
     *          This implementation is hard-coded to always return zero, as this
     *          is the only threshold that a 'negative' number will violate.
     */
    @Override
    public Double getViolatedThreshold() {
        return 0.0d;
    }
    
}

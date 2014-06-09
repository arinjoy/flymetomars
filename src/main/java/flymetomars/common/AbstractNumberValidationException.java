package flymetomars.common;

/**
 *  Represents the series of Custom Exceptions pertaining to Invalid numbers
 * 
 * @author Lawrence Colman
 */
/*package-private*/ abstract class AbstractNumberValidationException extends FMTMValidationException {

    private Class<?> numberClazz=AbstractNumberValidationException.class;
    private Double offendingNumber;
    
    /**
     * Default constuctor
     */
    protected AbstractNumberValidationException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractNumberValidationException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param invalid the violating Double numerical value causing the Exception
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractNumberValidationException(Double invalid, String message) {
        super(message);
        this.numberClazz=Double.class;
        this.offendingNumber=invalid;
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param invalid the violating Integer numerical value causing the Exception
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractNumberValidationException(Integer invalid, String message) {
        this(invalid.doubleValue(), message);
        this.numberClazz=Integer.class;
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param invalid the violating Long numerical value causing the Exception
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractNumberValidationException(Long invalid, String message) {
        this(invalid.doubleValue(), message);
        this.numberClazz=Long.class;
    }
    
    /**
     * Property accessor for the floating point (single or double precision)
     * number that was specified at construction as having caused this exception.
     * 
     * @return The relevant (non-validating) number, or `null' if an Integer was to blame
     */
    public Double getBadDecimal() {
        if(!this.numberClazz.equals(Integer.class)) {
            return this.offendingNumber;
        } else {
            return null;
        }
    }
    
    /**
     * Property accessor for the integer number value that was specified at
     * construction time as having caused this exception to be thrown.
     * 
     * @return Non-validating number, or `null' if it's Float, Double or Long
     */
    public Integer getBadInteger() {
        if(this.numberClazz.equals(Integer.class)) {
            return this.offendingNumber.intValue();
        } else {
            return null;
        }
    }
    
    /**
     * Property accessor for the integer number value that was specified at
     * construction time as having caused this exception to be thrown.
     * 
     * @return Non-validating number, or `null' if it's Float, Double or Integer
     */
    public Long getBadLong() {
        if(this.numberClazz.equals(Long.class)) {
            return this.offendingNumber.longValue();
        } else {
            return null;
        }
    }
    
    /**
     * Property accessor for the numeric threshold against which validation was performed
     * 
     * @return The numeric threshold that the bad number was in violation of.
     */
    public abstract Double getViolatedThreshold();
    
}

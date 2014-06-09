package flymetomars.common;

import java.util.Date;

/**
 * Represents the series of Custom Exceptions pertaining to Invalid Date objects
 * 
 * @author Lawrence Colman
 */
/*package-private*/ abstract class AbstractDateValidationException extends FMTMValidationException {
    
    private Date dateInQuestion;
    
    /**
     * Violating-Value constructor
     * 
     * @param invalidDate the violating Date value that caused this Exception
     * @param detail String message describing the circumstances for this issue
     */
    protected AbstractDateValidationException(Date invalidDate, String detail) {
        this(detail);
        this.dateInQuestion=invalidDate;
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    protected AbstractDateValidationException(String message) {
        super(message);
    }

    /**
     * Default constuctor
     */
    protected AbstractDateValidationException() {
        super();
    }
    
    /**
     * Property accessor for the non-complying Date against which validation was performed
     * 
     * @return The Date object that was found to be in violation of the rules.
     */
    public Date getDate() {
        return this.dateInQuestion;
    }
}

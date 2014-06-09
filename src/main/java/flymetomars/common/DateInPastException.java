package flymetomars.common;

import java.util.Date;

/**
 * Representative DateValidationException sub-type Exception for Dates that were
 * supplied as arguments which should have been in the future but were in past
 * 
 * @author Lawrence Colman
 */
public class DateInPastException extends AbstractDateValidationException {
    
    /**
     * Default constuctor
     */
    public DateInPastException() {
        super();
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param invalidPast the violating Date value that caused this Exception
     * @param detail String message describing the circumstances for this issue
     */
    public DateInPastException(Date invalidPast, String detail) {
        super(invalidPast,detail);
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public DateInPastException(String message) {
        super(message);
    }
    
}

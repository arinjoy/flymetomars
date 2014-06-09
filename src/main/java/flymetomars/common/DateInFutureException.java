package flymetomars.common;

import java.util.Date;

/**
 * Representative DateValidationException sub-type Exception for Dates that were
 * supplied as arguments which should have been in the past but were found not so
 * 
 * @author Lawrence Colman
 */
public class DateInFutureException extends AbstractDateValidationException {
    
    /**
     * Default constuctor
     */
    public DateInFutureException() {
        super();
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param invalidFuture the violating Date value that caused this Exception
     * @param detail String message describing the circumstances for this issue
     */
    public DateInFutureException(Date invalidFuture, String detail) {
        super(invalidFuture,detail);
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public DateInFutureException(String message) {
        super(message);
    }
    
}

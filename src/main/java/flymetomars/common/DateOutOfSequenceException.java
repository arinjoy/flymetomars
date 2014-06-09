package flymetomars.common;

import java.util.Date;

/**
 * Representative DateValidationException sub-type Exception for Dates that were
 * supplied as arguments which should have been in sequential order but were not
 * 
 * @author Lawrence Colman
 */
public class DateOutOfSequenceException extends AbstractDateValidationException {
    
    /**
     * Default constuctor
     */
    public DateOutOfSequenceException() {
        super();
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param invalidSequence the violating Date value that caused this Exception
     * @param detail String message describing the circumstances for this issue
     */
    public DateOutOfSequenceException(Date invalidSequence, String detail) {
        super(invalidSequence,detail);
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public DateOutOfSequenceException(String message) {
        super(message);
    }
    
}


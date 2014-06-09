package flymetomars.common.validation;

import flymetomars.common.DateInPastException;
import flymetomars.common.DateInFutureException;
import flymetomars.common.DateOutOfSequenceException;
import flymetomars.common.NullArgumentException;
import java.util.Date;

/**
 * Utility class containing several methods for validating Date information
 * 
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public final class DateValidator {
    
    private DateValidator() {}
    
    /**
     * Validates that the given date is not a null reference
     * 
     * @param date the Date instance that must not be null
     * @throws NullArgumentException thrown when date is null
     */
    public static void validateDateNotNull(Date date) {
    if(null == date) {
            throw  new NullArgumentException("Date field must not be null");
        }
    }
    
    
   /**
    * Validates that future is greater than now
    * 
    * @param future the date object to be checked
    * @throws DateInPastException thrown to indicate future is in the past
    * @throws NullArgumentException thrown when `future' date is null
    */
    public static void validateFutureDate(Date future) {
        validateDateNotNull(future);
        Date now = new Date();
        if(future.compareTo(now) <= 0) {
            throw new DateInPastException(future,"Date must be later than now");
        }
    }
    
   /**
    * Validates that past is before the current date
    * 
    * @param past the date object to be checked
    * @throws DateInFutureException thrown to indicate past is in the future
    * @throws NullArgumentException thrown when `past' date is null
    */
    public static void validatePastDate(Date past) {
        validateDateNotNull(past);
        Date now = new Date();
        if(past.compareTo(now) > 0) {
            throw new DateInFutureException(past, "Date must be before present");
        }
    }
    
    
   /**
    * Validates that a past Date (before) is before another date (after)
    * 
    * @param before the Date object to be verified as being before after
    * @param after the Date object to be verified as being after before
    * @throws DateOutOfSequenceException thrown to indicate before is ahead of after
    * @throws NullArgumentException thrown when either date argument is null
    */
    public static void validateDateSequence(Date before, Date after) {
        validateDateNotNull(before);
        validateDateNotNull(after);
        if(before.compareTo(after) > 0) {
            throw new DateOutOfSequenceException(after, "supplied after Date is before previous Date");
        }
    }
    
    /**
    * Validates that a Date (when) is in between before and after Dates
    * 
    * @param when the date instance to be validated as being within the range
    * @param before the Date object representing the lower bound
    * @param after the Date object representing the upper bound
    * @throws DateOutOfSequenceException thrown to indicate when is not in range
    * @throws NullArgumentException thrown when any date argument is null
    */
    public static void validateDateInRange(Date when, Date before, Date after) {
        validateDateNotNull(before);
        validateDateNotNull(when);
        validateDateNotNull(after);
        validateDateSequence(before,after);
        validateDateSequence(before,when);
        validateDateSequence(when,after);
    }
    
}

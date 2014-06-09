package flymetomars.common.validation;

import flymetomars.common.DateInPastException;
import flymetomars.common.DateInFutureException;
import flymetomars.common.DateOutOfSequenceException;
import flymetomars.common.NullArgumentException;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class DateValidatorUnitTest {
    
    // <editor-fold desc="not null test cases">
    
    @Test
    public void testValidateDateNotNullSuccess() {
        DateValidator.validateDateNotNull(new Date());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testValidateDateNotNullFailure() {
        DateValidator.validateDateNotNull(null);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="future date test cases">

    @Test
    public void testValidateFutureDateSuccess() {
        DateValidator.validateFutureDate(new Date(new Date().getTime() + 1000L));
    }
    
    @Test(expected = DateInPastException.class)
    public void testValidateFutureDateFailure() {
        DateValidator.validateFutureDate(new Date(new Date().getTime() - 10000L));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testValidateFutureDateNullFailure() {
        DateValidator.validateFutureDate(null);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="past date test cases">

    @Test
    public void testValidatePastDateSuccess() {
        DateValidator.validatePastDate(new Date(new Date().getTime() - 10000L));
    }
    
    @Test(expected = DateInFutureException.class)
    public void testValidatePastDateFailure() {
        DateValidator.validatePastDate(new Date(new Date().getTime() + 1000L));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testValidatePastDateNullFailure() {
        DateValidator.validatePastDate(null);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="date sequence test cases">
    
    @Test
    public void validateDateSequenceSuccess() {
        DateValidator.validateDateSequence(new Date(new Date().getTime() - 1000L), new Date());
    }
    
    @Test(expected = DateOutOfSequenceException.class)
    public void validateDateSequenceFailure() {
        DateValidator.validateDateSequence(new Date(new Date().getTime() + 10000L), new Date());
    }

    @Test(expected = NullArgumentException.class)
    public void validateDateSequenceNullBeforeFailure() {
        DateValidator.validateDateSequence(null,new Date());
    }
    
    @Test(expected = NullArgumentException.class)
    public void validateDateSequenceNullAfterFailure() {
        DateValidator.validateDateSequence(new Date(),null);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="date range test cases">

    @Test
    public void validateDateInRangeSuccess() {
        DateValidator.validateDateInRange(new Date(),new Date(new Date().getTime() - 10000L),new Date(new Date().getTime() + 1000L));
    }
    
    @Test(expected = DateOutOfSequenceException.class)
    public void validateDateInRangeBeforeLaterThanAfterFailure() {
        DateValidator.validateDateInRange(new Date(),new Date(),new Date(new Date().getTime() - 10000L));
    }
    
    @Test(expected = DateOutOfSequenceException.class)
    public void validateDateInRangeAfterEarlierThanBeforeFailure() {
        DateValidator.validateDateInRange(new Date(),new Date(new Date().getTime() + 1000L), new Date());
    }
    
    @Test(expected = DateOutOfSequenceException.class)
    public void validateDateInRangeNotInRangeFailure() {
        DateValidator.validateDateInRange(new Date(new Date().getTime() - 10000L),new Date(),new Date(new Date().getTime() + 1000L));
    }
    
    @Test(expected = NullArgumentException.class)
    public void validateDateInRangeNullBeforeFailure() {
        DateValidator.validateDateInRange(new Date(),null,new Date());
    }
    
    @Test(expected = NullArgumentException.class)
    public void validateDateInRangeNullWhenFailure() {
        DateValidator.validateDateInRange(null,new Date(),new Date());
    }
    
    @Test(expected = NullArgumentException.class)
    public void validateDateInRangeNullAfterFailure() {
        DateValidator.validateDateInRange(new Date(),new Date(),null);
    }
            
    // </editor-fold>
}

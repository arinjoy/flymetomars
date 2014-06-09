package flymetomars.common.validation;

import flymetomars.common.NumberNegativeException;
import flymetomars.common.NumberTooLargeException;
import flymetomars.common.NumberTooSmallException;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class NumberValidatorUnitTest {
    
    // <editor-fold desc="positive integer test cases">
    
    @Test
    public void testValidatePositiveIntegerSuccess() {
        NumberValidator.validatePositiveInteger(63);
    }
    
    @Test(expected = NumberNegativeException.class)
    public void testValidatePositiveIntegerFailure() {
        NumberValidator.validatePositiveInteger(-2);
    }

    // </editor-fold>
    
    // <editor-fold desc="positive long integer test cases">
    
    @Test
    public void testValidatePositiveLongSuccess() {
        NumberValidator.validatePositiveLong(4294967297L);
    }

    @Test(expected = NumberNegativeException.class)
    public void testValidatePositiveLongFailure() {
        NumberValidator.validatePositiveLong(-66L);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="positive floating number test cases">
    
    @Test
    public void testValidatePositiveFloatSuccess() {
        NumberValidator.validatePositiveFloat(3.14159f);
    }
    
    @Test(expected = NumberNegativeException.class)
    public void testValidatePositiveFloatFailure() {
        NumberValidator.validatePositiveFloat(-72.6f);
    }
    
    // </editor-fold>

    // <editor-fold desc="positive double precision test cases">
    
    @Test
    public void testValidatePositiveDoubleSuccess() {
        NumberValidator.validatePositiveDouble(257.821);
    }
    
    @Test(expected = NumberNegativeException.class)
    public void testValidatePositiveDoubleFailure() {
        NumberValidator.validatePositiveDouble(-0.219);
    }

    // </editor-fold>
    
    // <editor-fold desc="integer range test cases">

    @Test
    public void testValidateIntegerInRangeSuccess() {
        NumberValidator.validateIntegerInRange(69, 18, 80);
    }
    
    @Test(expected = NumberTooSmallException.class)
    public void testValidateIntegerInRangeTooSmallFailure() {
        NumberValidator.validateIntegerInRange(12, 18, 80);
    }
    
    @Test(expected = NumberTooLargeException.class)
    public void testValidateIntegerInRangeTooLargeFailure() {
        NumberValidator.validateIntegerInRange(82, 18, 80);
    }
    
    // </editor-fold>
    
}

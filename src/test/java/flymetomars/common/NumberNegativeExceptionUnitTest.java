package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class NumberNegativeExceptionUnitTest {

    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new NumberNegativeException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new NumberNegativeException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = NumberNegativeException.class)
    public void testDoubleValuePlusMessageFailure() {
        throw new NumberNegativeException(-13.37d, "Test exception");
    }
    
    @Test(expected = NumberNegativeException.class)
    public void testIntegerValuePlusMessageFailure() {
        throw new NumberNegativeException(-1337, "Test exception");
    }

    @Test(expected = NumberNegativeException.class)
    public void testLongValuePlusMessageFailure() {
        throw new NumberNegativeException(-1337L, "Test exception");
    }
    
    @Test
    public void testDoubleValueFailure() {
        Double result=-13.37d;
        AbstractNumberValidationException e= new NumberNegativeException(result);
        Assert.assertEquals("Number cannot be negative", e.getMessage());
        Assert.assertEquals(result, e.getBadDecimal());
    }
    
    @Test
    public void testIntegerValueFailure() {
        Integer result=-1337;
        AbstractNumberValidationException e= new NumberNegativeException(result);
        Assert.assertEquals("Number cannot be negative", e.getMessage());
        Assert.assertEquals(result, e.getBadInteger());
    }
    
    @Test
    public void testLongValueFailure() {
        Long result=-1337L;
        AbstractNumberValidationException e=new NumberNegativeException(result);
        Assert.assertEquals("Number cannot be negative", e.getMessage());
        Assert.assertEquals(result, e.getBadLong());
    }
    
    @Test
    public void testGetViolatedThresholdIsZeroSuccess() {
        NumberNegativeException instance = new NumberNegativeException();
        Double result = instance.getViolatedThreshold();
        Assert.assertEquals((Double)0.0d, result);
    }
    
}

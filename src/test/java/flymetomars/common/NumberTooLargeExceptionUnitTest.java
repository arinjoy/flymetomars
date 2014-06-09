package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class NumberTooLargeExceptionUnitTest {

    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new NumberTooLargeException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new NumberTooLargeException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = NumberTooLargeException.class)
    public void testBadValueAndDoubleThresholdFailure() {
        Double result = 5.6d;
        NumberTooLargeException e=new NumberTooLargeException(result, 7.2d);
        Assert.assertEquals("number too large", e.getMessage());
        Assert.assertEquals(result, e.getViolatedThreshold());
        throw e;
    }
    
    @Test(expected = NumberTooLargeException.class)
    public void testBadValueAndIntegerThresholdFailure() {
        Integer result = 6;
        NumberTooLargeException e=new NumberTooLargeException(result, 7);
        Assert.assertEquals("number too large", e.getMessage());
        Assert.assertEquals((Double)result.doubleValue(), e.getViolatedThreshold());
        throw e;
    }
    
    @Test(expected = NumberTooLargeException.class)
    public void testFullDoubleFailure() {
        Double result = 56.89d;
        Double val=137.7d;
        String msg="Oh dear!";
        NumberTooLargeException e=new NumberTooLargeException(result, val, msg);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals((Double)result.doubleValue(), e.getViolatedThreshold());
        Assert.assertEquals(val, e.getBadDecimal());
        throw e;
    }
    
    @Test(expected = NumberTooLargeException.class)
    public void testFullIntegerFailure() {
        Integer result = 1337;
        Integer val=9001;
        String msg="Las parablas!";
        NumberTooLargeException e=new NumberTooLargeException(result, val, msg);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals((Double)result.doubleValue(), e.getViolatedThreshold());
        Assert.assertEquals(val, e.getBadInteger());
        throw e;
    }
    
    @Test
    public void testGetViolatedThresholdSuccess() {
        NumberTooLargeException instance = new NumberTooLargeException(3,4);
        Double result = instance.getViolatedThreshold();
        Assert.assertEquals((Double)3.0d, result);
    }
    
    @Test
    public void testGetViolatedThresholdNullDefaultSuccess() {
        NumberTooLargeException result = new NumberTooLargeException();
        Assert.assertNull(result.getViolatedThreshold());
    }
    
    @Test
    public void testGetBadIntegerNullSuccess() {
        NumberTooLargeException result = new NumberTooLargeException(0.0d, 13.37d);
        Assert.assertNull(result.getBadInteger());
    }
    
}

package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class NumberTooSmallExceptionUnitTest {

    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new NumberTooSmallException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new NumberTooSmallException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = NumberTooSmallException.class)
    public void testBadValueAndDoubleThresholdFailure() {
        Double result = 7.1d;
        NumberTooSmallException e=new NumberTooSmallException(result, 5.5d);
        Assert.assertEquals("number too small", e.getMessage());
        Assert.assertEquals(result, e.getViolatedThreshold());
        throw e;
    }
    
    @Test(expected = NumberTooSmallException.class)
    public void testBadValueAndIntegerThresholdFailure() {
        Integer result = 6;
        NumberTooSmallException e=new NumberTooSmallException(result, 5);
        Assert.assertEquals("number too small", e.getMessage());
        Assert.assertEquals((Double)result.doubleValue(), e.getViolatedThreshold());
        throw e;
    }
    
    @Test(expected = NumberTooSmallException.class)
    public void testFullDoubleFailure() {
        Double result = 900.1d;
        Double val=137.7d;
        String msg="Oh my!";
        NumberTooSmallException e=new NumberTooSmallException(result, val, msg);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals((Double)result.doubleValue(), e.getViolatedThreshold());
        Assert.assertEquals(val, e.getBadDecimal());
        throw e;
    }
    
    @Test(expected = NumberTooSmallException.class)
    public void testFullIntegerFailure() {
        Integer result = 9001;
        Integer val=1337;
        String msg="De Amour";
        NumberTooSmallException e=new NumberTooSmallException(result, val, msg);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals((Double)result.doubleValue(), e.getViolatedThreshold());
        Assert.assertEquals(val, e.getBadInteger());
        throw e;
    }
    
    @Test
    public void testGetViolatedThresholdSuccess() {
        NumberTooSmallException instance = new NumberTooSmallException(4,3);
        Double result = instance.getViolatedThreshold();
        Assert.assertEquals((Double)4.0d, result);
    }
    
    @Test
    public void testGetViolatedThresholdNullDefaultSuccess() {
        NumberTooSmallException result = new NumberTooSmallException();
        Assert.assertNull(result.getViolatedThreshold());
    }
    
    @Test
    public void testGetBadLongNullSuccess() {
        NumberTooLargeException result = new NumberTooLargeException(0.0d, 13.37d);
        Assert.assertNull(result.getBadLong());
    }
    
    @Test
    public void testGetBadDecimalNullSuccess() {
        NumberTooLargeException result = new NumberTooLargeException(0, 1337);
        Assert.assertNull(result.getBadDecimal());
    }
    
}

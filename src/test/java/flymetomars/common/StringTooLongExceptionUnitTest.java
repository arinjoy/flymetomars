package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class StringTooLongExceptionUnitTest {
    
    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new StringTooLongException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new StringTooLongException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = StringTooLongException.class)
    public void testInnerCausePlusMessageFailue() {
        String msg="Exception Testing";
        Exception ex=new Exception(msg);
        StringTooLongException e=new StringTooLongException(msg, ex);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(ex, e.getCause());
        Assert.assertEquals(msg, e.getCause().getMessage());
        Assert.assertNull(e.getStringInQuestion());
        throw e;
    }
    
    @Test(expected = StringTooLongException.class)
    public void testBadValueWithCausePlusMessageFailue() {
        String msg="Exception Testing";
        StringTooLongException e=new StringTooLongException("baz", msg, new Exception(msg));
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(msg, e.getCause().getMessage());
        Assert.assertEquals("baz", e.getStringInQuestion());
        throw e;
    }
    
    @Test(expected = StringTooLongException.class)
    public void testBadValueWithThresholdPlusMessageFailue() {
        String msg="Test Exception";
        Integer max=2;
        StringTooLongException e=new StringTooLongException("bar", max, msg);
        Assert.assertEquals(max, e.getMaximimLength());
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals("bar", e.getStringInQuestion());
        throw e;
    }
    
    @Test(expected = StringTooLongException.class)
    public void testFullFeaturedFailue() {
        String msg="Test Exception";
        Integer max=2;
        StringTooLongException e=new StringTooLongException("foo", max, msg, new Exception(msg));
        Assert.assertEquals(max, e.getMaximimLength());
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(msg, e.getCause().getMessage());
        Assert.assertEquals("foo", e.getStringInQuestion());
        throw e;
    }

    @Test
    public void testGetMaximimLengthSuccess() {
        Integer expected=2;
        StringTooLongException instance = new StringTooLongException("foobar", expected, "Test");
        Integer result = instance.getMaximimLength();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetMaximimLengthNullDefaultSuccess() {
        StringTooLongException instance = new StringTooLongException();
        Integer result = instance.getMaximimLength();
        Assert.assertNull(result);
    }
    
}

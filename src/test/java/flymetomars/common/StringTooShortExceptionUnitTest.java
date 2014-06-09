package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class StringTooShortExceptionUnitTest {
    
    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new StringTooShortException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new StringTooShortException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = StringTooShortException.class)
    public void testInnerCausePlusMessageFailue() {
        String msg="Exception Testing";
        Exception ex=new Exception(msg);
        StringTooShortException e=new StringTooShortException(msg, ex);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(ex, e.getCause());
        Assert.assertEquals(msg, e.getCause().getMessage());
        Assert.assertNull(e.getStringInQuestion());
        throw e;
    }
    
    @Test(expected = StringTooShortException.class)
    public void testBadValueWithCausePlusMessageFailue() {
        String msg="Exception Testing";
        StringTooShortException e=new StringTooShortException("baz", msg, new Exception(msg));
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(msg, e.getCause().getMessage());
        Assert.assertEquals("baz", e.getStringInQuestion());
        throw e;
    }
    
    @Test(expected = StringTooShortException.class)
    public void testBadValueWithThresholdPlusMessageFailue() {
        String msg="Test Exception";
        Integer min=4;
        StringTooShortException e=new StringTooShortException("bar", min, msg);
        Assert.assertEquals(min, e.getMinimumLength());
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals("bar", e.getStringInQuestion());
        throw e;
    }
    
    @Test(expected = StringTooShortException.class)
    public void testFullFeaturedFailue() {
        String msg="Test Exception";
        Integer min=4;
        StringTooShortException e=new StringTooShortException("foo", min, msg, new Exception(msg));
        Assert.assertEquals(min, e.getMinimumLength());
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(msg, e.getCause().getMessage());
        Assert.assertEquals("foo", e.getStringInQuestion());
        throw e;
    }

    @Test
    public void testGetMinimumLengthSuccess() {
        Integer expected=1;
        StringTooShortException instance = new StringTooShortException("", expected, "Test");
        Integer result = instance.getMinimumLength();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetMinimumLengthNullDefaultSuccess() {
        StringTooShortException instance = new StringTooShortException();
        Integer result = instance.getMinimumLength();
        Assert.assertNull(result);
    }
    
}

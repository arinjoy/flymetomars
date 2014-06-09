package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class InvalidEmailExceptionUnitTest {
    
    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new InvalidEmailException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new InvalidEmailException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = InvalidEmailException.class)
    public void testBadValuePlusMessageFailure() {
        String msg="Testing...";
        String bad="l p col@monash";
        InvalidEmailException e=new InvalidEmailException(bad, msg);
        Assert.assertEquals(bad, e.getBadEmail());
        Assert.assertEquals(msg, e.getMessage());
        throw e;
    }
    
    @Test(expected = InvalidEmailException.class)
    public void testFullFailure() {
        String msg="Testing...";
        String bad="l p col@monash";
        Exception inn=new Exception("Foobar!");
        InvalidEmailException e= new InvalidEmailException(bad, msg, inn);
        Assert.assertEquals(bad, e.getBadEmail());
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(inn, e.getCause());
        throw e;
    }
    
    @Test
    public void testInnerPlusMessageSuccess() {
        Exception expected=new Exception("Testing");
        FMTMValidationException e=new InvalidEmailException("Test", expected);
        Assert.assertEquals(expected, e.getCause());
    }

    @Test
    public void testGetBadEmailSuccess() {
        String expected="a bis@was";
        InvalidEmailException instance = new InvalidEmailException(expected, "Test");
        String result = instance.getBadEmail();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetBadEmailNullDefaultSuccess() {
        InvalidEmailException instance = new InvalidEmailException();
        String result = instance.getBadEmail();
        Assert.assertNull(result);
    }
    
}

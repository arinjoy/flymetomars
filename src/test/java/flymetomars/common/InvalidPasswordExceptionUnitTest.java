package flymetomars.common;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class InvalidPasswordExceptionUnitTest {
    
    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new InvalidPasswordException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new InvalidPasswordException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = InvalidPasswordException.class)
    public void testBadValuePlusMessageFailure() {
        String msg="Testing...";
        String bad="password";
        InvalidPasswordException e=new InvalidPasswordException(bad, msg);
        Assert.assertEquals(bad, e.getBadPassword());
        Assert.assertEquals(msg, e.getMessage());
        throw e;
    }
    
    @Test(expected = InvalidPasswordException.class)
    public void testFullFailure() {
        String msg="Testing...";
        String bad="password";
        Exception inn=new Exception("Foobar!");
        InvalidPasswordException e= new InvalidPasswordException(bad, msg, inn);
        Assert.assertEquals(bad, e.getBadPassword());
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(inn, e.getCause());
        throw e;
    }
    
    @Test
    public void testInnerPlusMessageSuccess() {
        Exception expected=new Exception("Testing");
        FMTMValidationException e=new InvalidPasswordException("Test", expected);
        Assert.assertEquals(expected, e.getCause());
    }

    @Test
    public void testGetBadPasswordSuccess() {
        String expected="myPass";
        InvalidPasswordException instance = new InvalidPasswordException(expected, "Test");
        String result = instance.getBadPassword();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetBadPasswordNullDefaultSuccess() {
        InvalidPasswordException instance = new InvalidPasswordException();
        String result = instance.getBadPassword();
        Assert.assertNull(result);
    }
    
}

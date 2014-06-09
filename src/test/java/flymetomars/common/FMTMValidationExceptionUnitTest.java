package flymetomars.common;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class FMTMValidationExceptionUnitTest {

    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new FMTMValidationException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        FMTMValidationException e=new FMTMValidationException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test
    public void testInnerSuccess() {
        Exception expected=new Exception("Testing");
        FMTMValidationException e=new FMTMValidationException(expected);
        Assert.assertEquals(expected, e.getCause());
    }
    
    @Test(expected = FMTMValidationException.class)
    public void testFullFeaturedFailue() {
        throw new FMTMValidationException("Testing...",new Exception("Test!"));
    }
}

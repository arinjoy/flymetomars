package flymetomars.common;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class NullArgumentUnitTest {

    @Test
    public void testDefaultConstructorSuccess() {
        Assert.assertNotNull(new NullArgumentException());
    }
    
    @Test
    public void testInnerCauseConstructorSuccess() {
        Assert.assertNotNull(new NullArgumentException(new Exception("Test")));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testBuildAndThrowWorksFailure() {
        throw new NullArgumentException("testing");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testFullConstructorFailure() {
        throw new NullArgumentException("testing", new Exception("Test test"));
    }
    
}

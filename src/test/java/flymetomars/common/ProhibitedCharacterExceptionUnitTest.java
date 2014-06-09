package flymetomars.common;

import java.util.Set;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class ProhibitedCharacterExceptionUnitTest {
    
    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new ProhibitedCharacterException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        ProhibitedCharacterException e=new ProhibitedCharacterException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = ProhibitedCharacterException.class)
    public void testValuePlusMessageFailure() {
        throw new ProhibitedCharacterException("foobar", "Test exception");
    }

    @Test
    public void testGetProhibitedCharsEmptyDefaultSuccess() {
        ProhibitedCharacterException instance = new ProhibitedCharacterException();
        Set result = instance.getProhibitedChars();
        Assert.assertEquals(0, result.size());
    }
    
    @Test
    public void testGetProhibitedCharsSuccess() {
        ProhibitedCharacterException instance = new ProhibitedCharacterException("abc123", "abc", "test");
        Set result = instance.getProhibitedChars();
        Assert.assertTrue(result.contains('a'));
        Assert.assertTrue(result.contains('b'));
        Assert.assertTrue(result.contains('c'));
    }
    
}

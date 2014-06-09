package flymetomars.common;

import java.util.Set;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Lawrence Colman
 */
public class CharactersMissingExceptionUnitTest {
    
    @Test
    public void testDefaultSuccess() {
        FMTMValidationException e=new CharactersMissingException();
        Assert.assertNotNull(e);
    }
    
    @Test
    public void testMessageSuccess() {
        String expected="This is a test exception message";
        CharactersMissingException e=new CharactersMissingException(expected);
        Assert.assertEquals(expected, e.getMessage());
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testValuePlusMessageFailure() {
        throw new CharactersMissingException("foobar", "Test exception");
    }

    @Test
    public void testGetRequiredCharsEmptyDefaultSuccess() {
        CharactersMissingException instance = new CharactersMissingException();
        Set result = instance.getRequiredChars();
        Assert.assertEquals(0, result.size());
    }
    
    @Test
    public void testGetRequiredCharsSuccess() {
        CharactersMissingException instance = new CharactersMissingException("foo", "abc", "test");
        Set result = instance.getRequiredChars();
        Assert.assertTrue(result.contains('a'));
        Assert.assertTrue(result.contains('b'));
        Assert.assertTrue(result.contains('c'));
    }
    
}

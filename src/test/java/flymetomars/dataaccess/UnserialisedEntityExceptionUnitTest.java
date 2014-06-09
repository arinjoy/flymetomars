package flymetomars.dataaccess;

import flymetomars.common.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class UnserialisedEntityExceptionUnitTest {
    
    @Test
    public void testUnserialisedEntityExceptionSuccess() {
        Exception e = new Exception();
        UnserialisedEntityException ex=new UnserialisedEntityException("bar","foo.bar",e);
        Assert.assertEquals("bar",ex.getEntity());
        String real="bar entity supplied to foo.bar was not a persisted instance!  Inner Error: [" + e.getMessage() + ']';
        Assert.assertEquals(real,ex.getMessage());
        Assert.assertSame(ex.getCause(), e);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testUnserialisedEntityExceptionNullModelFaulure() throws UnserialisedEntityException  {
        Exception e = new Exception();
        throw new UnserialisedEntityException(null,"testUnserialisedEntityExceptionNullModelFaulure",e);
    }
    
    @Test
    public void testUnserialisedEntityExceptionNullOperationSuccess() {
        Exception e = new Exception("BarRamYou");
        UnserialisedEntityException ex=new UnserialisedEntityException("Foobar",null,e);
        String real="Foobar entity supplied to NULL was not a persisted instance!  Inner Error: [" + e.getMessage() + ']';
        Assert.assertEquals(real,ex.getMessage());
    }
    
    @Test
    public void testUnserialisedEntityExceptionNullCauseSuccess() {
        UnserialisedEntityException ex=new UnserialisedEntityException("Test","testUnserialisedEntityExceptionNullCauseFailure",null);
        String real="Test entity supplied to testUnserialisedEntityExceptionNullCauseFailure was not a persisted instance!  Inner Error: [NULL]";
        Assert.assertEquals(real,ex.getMessage());
    }
    
    @Test
    public void testUnserialisedEntityExceptionMessageOnlySuccess() {
        UnserialisedEntityException ex=new UnserialisedEntityException("Testing 123");
        Assert.assertEquals("Testing 123", ex.getMessage());
    }
    
}

package flymetomars.common;

import flymetomars.common.DateInFutureException;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the date being in the future
 * 
 * @author Lawrence Colman
 */
public class DateInFutureExceptionUnitTest {
    
    @Test(expected = DateInFutureException.class)
    public void testDateInFutureExceptionDefaultFailure() {
        throw new DateInFutureException();
    }
    
    @Test
    public void testDateInFutureExceptionSuccess() {
        Date d=new Date();
        String msg="Something!";
        DateInFutureException ex=new DateInFutureException(d,msg);
        Assert.assertSame(d, ex.getDate());
        Assert.assertEquals(ex.getMessage(), msg);
    }
    
    @Test
    public void testDateInFutureExceptionNullsSuccess() {
        DateInFutureException ex=new DateInFutureException(null,null);
        Assert.assertNull(ex.getDate());
        Assert.assertNull(ex.getMessage());
    }
    
    @Test
    public void testDateInFutureExceptionEmptyMessageSuccess() {
        DateInFutureException ex=new DateInFutureException(new Date(),"");
        Assert.assertEquals(ex.getMessage(),"");
    }
    
    @Test
    public void testDateInFutureExceptionJustMessageSuccess() {
        DateInFutureException ex=new DateInFutureException("stuff happened");
        Assert.assertEquals(ex.getMessage(),"stuff happened");
    }
    
}

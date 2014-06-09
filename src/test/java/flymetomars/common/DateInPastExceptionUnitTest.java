package flymetomars.common;

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the date being in the past
 * 
 * @author Lawrence Colman
 */
public class DateInPastExceptionUnitTest {
    
    @Test(expected = DateInPastException.class)
    public void testDateInPastExceptionDefaultFailure() {
        throw new DateInPastException();
    }
    
    @Test
    public void testDateInPastExceptionSuccess() {
        Date d=new Date();
        String msg="Something!";
        DateInPastException ex=new DateInPastException(d,msg);
        Assert.assertSame(d, ex.getDate());
        Assert.assertEquals(ex.getMessage(), msg);
    }
    
    @Test
    public void testDateInPastExceptionNullsSuccess() {
        DateInPastException ex=new DateInPastException(null,null);
        Assert.assertNull(ex.getDate());
        Assert.assertNull(ex.getMessage());
    }
    
    @Test
    public void testDateInPastExceptionEmptyMessageSuccess() {
        DateInPastException ex=new DateInPastException(new Date(),"");
        Assert.assertEquals(ex.getMessage(),"");
    }
    
    @Test
    public void testDateInPastExceptionJustMessageSuccess() {
        DateInPastException ex=new DateInPastException("stuff happened");
        Assert.assertEquals(ex.getMessage(),"stuff happened");
    }
    
}

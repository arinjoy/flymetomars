package flymetomars.common;

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the date being out of sequence
 * 
 * @author Lawrence Colman
 */
public class DateOutOfSequenceExceptionUnitTest {
    
    @Test(expected = DateOutOfSequenceException.class)
    public void testDateOutOfSequenceExceptionDefaultFailure() {
        throw new DateOutOfSequenceException();
    }
    
    @Test
    public void testDateOutOfSequenceExceptionSuccess() {
        Date d=new Date();
        String msg="Something!";
        DateOutOfSequenceException ex=new DateOutOfSequenceException(d,msg);
        Assert.assertSame(d, ex.getDate());
        Assert.assertEquals(ex.getMessage(), msg);
    }
    
    @Test
    public void testDateOutOfSequenceExceptionNullsSuccess() {
        DateOutOfSequenceException ex=new DateOutOfSequenceException(null,null);
        Assert.assertNull(ex.getDate());
        Assert.assertNull(ex.getMessage());
    }
    
    @Test
    public void testDateOutOfSequenceExceptionEmptyMessageSuccess() {
        DateOutOfSequenceException ex=new DateOutOfSequenceException(new Date(),"");
        Assert.assertEquals(ex.getMessage(),"");
    }
    
    @Test
    public void testDateOutOfSequenceExceptionJustMessageSuccess() {
        DateOutOfSequenceException ex=new DateOutOfSequenceException("stuff happened");
        Assert.assertEquals(ex.getMessage(),"stuff happened");
    }
    
}

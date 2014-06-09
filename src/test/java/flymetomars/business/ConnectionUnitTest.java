package flymetomars.business;

import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class ConnectionUnitTest {

    @Test
    public void testMakeConnectionFromMissionBlankMissionSuccess() {
        Mission miss=new Mission();
        Connection result = Connection.makeConnectionFromMission(miss);
        Assert.assertNotNull(result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMakeConnectionFromMissionNullMissionFailure() {
        Connection.makeConnectionFromMission(null);
    }

    @Test
    public void testMakeConnectionFromPersonBlankPersonSuccess() {
        Person per = new Person();
        Connection result = Connection.makeConnectionFromPerson(per);
        Assert.assertNotNull(result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMakeConnectionFromPersonNullPersonFailure() {
        Connection.makeConnectionFromPerson(null);
    }

    @Test
    public void testGetInnerModelForMissionSuccess() {
        Mission m=new Mission();
        Connection instance=Connection.makeConnectionFromMission(m);
        Assert.assertEquals(m.hashCode(), instance.getInnerModel().hashCode());
    }

    @Test
    public void getInnerModelForPersonSuccess() {
        Person p=new Person();
        Connection instance=Connection.makeConnectionFromPerson(p);
        Assert.assertEquals(p, instance.getInnerModel());
    }

    @Test
    public void getInnerTypeForMissionSuccess() {
        Connection instance=Connection.makeConnectionFromMission(new Mission());
        Assert.assertEquals(Mission.class, instance.getInnerType());
    }
    
    @Test
    public void getInnerTypeForPersonSuccess() {
        Connection instance=Connection.makeConnectionFromPerson(new Person());
        Assert.assertEquals(Person.class, instance.getInnerType());
    }

}

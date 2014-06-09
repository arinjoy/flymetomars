package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.validation.DateValidator;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the invitation model
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class InvitationIntegrationTest {

    private Person makePerson() {
        Person p = new Person();
        p.setFirstName("Apoorva");
        p.setLastName("Singh");
        p.setUserName("asin91");
        SaltedPassword pass=new SaltedPassword();
        pass.setId(9001L);
        Salt salt=new Salt();
        pass.setSalt(salt);  //the setSalt call must happen BEFORE setPassword.
        pass.setPassword("Ajoy_36#Bis%joy");
        p.setPassword(pass);
        p.setEmail("ap.singh@test.com.au");
        return p;
    }
    
    // <editor-fold desc="mission test cases">
 
    @Test
    public void testHasNoMissionByDefaultSuccess() {
        Invitation i = new Invitation();
        Mission m = i.getMission();
        Assert.assertNull(m);
    }
    
    @Test
    public void testSetMissionSuccess() {
        Invitation i = new Invitation();
        Mission m = new Mission();
        Person p = makePerson();
        Date d=new Date();
        d.setTime(d.getTime()+7257600000L);
        m.setCaptain(p);
        m.setName("Mars Wonderer II");
        m.setTime(d);
        Location l = new Location();
        l.setFloor("Level 2");
        l.setStreetNo("221");
        l.setStreet("Dandenong Road");
        l.setRegion("Victoria");
        l.setPostcode("3031");
        l.setLandmark("Near Racecourse");
        l.setTown("Caulfield");  
        l.setCountry("Australia");
        m.setLocation(l);
        m.setDescription("The best mission of all Mars expeditions... Ever!");
        i.setMission(m);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetMissionNullFailure() {
        Invitation i = new Invitation();
        i.setMission(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMissionNoNameFailure() {
        Invitation i = new Invitation();
        Mission m = new Mission();
        Person p = makePerson();
        Date d=new Date();
        d.setTime(d.getTime()+7257600000L);
        m.setCaptain(p);
        m.setTime(d);
        Location l = new Location();
        l.setFloor("Ground Floor");
        l.setStreetNo("221");
        l.setStreet("Dandenong Road");
        l.setRegion("Victoria");
        l.setPostcode("3031");
        l.setLandmark("Near Racecourse");
        l.setTown("Caulfield");  
        l.setCountry("Australia");
        m.setLocation(l);
        m.setDescription("The best mission of all Mars expeditions... Ever!");
        i.setMission(m);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetMissionWithoutCaptainFailure() {
        Invitation i = new Invitation();
        Mission m = new Mission();
        Date d=new Date();
        d.setTime(d.getTime()+7257600000L);
        m.setName("Mars Great Adventure");
        m.setTime(d);
        Location l = new Location();
        l.setFloor("3rd");
        l.setStreetNo("221");
        l.setStreet("Dandenong Road");
        l.setRegion("Victoria");
        l.setPostcode("3031");
        l.setLandmark("Near Racecourse");
        l.setTown("Caulfield");  
        l.setCountry("Australia");
        m.setLocation(l);
        m.setDescription("The leading space exploration and science team");
        i.setMission(m);
    }
    
    // </editor-fold>

    // <editor-fold desc="creator test cases">
    
    @Test
    public void testHasNoCreatorByDefaultSuccess() {
        Invitation i = new Invitation();
        Assert.assertNull(i.getCreator());
    }
    
    @Test
    public void testSetCreatorSuccess() {
        Invitation i = new Invitation();
        Person p = new Person();
        p.setFirstName("Arinjoy");
        p.setLastName("Biswas");
        p.setUserName("abis_23");
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setSalt(salt);  //the setSalt call must happen BEFORE setPassword.
        pass.setPassword("Ajoy_36#Bis%joy");
        p.setPassword(pass);
        p.setEmail("arin@icloud.com");
        i.setCreator(p);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetCreatorNullFail() {
        Invitation i = new Invitation();
        i.setCreator(null);
    }
    
    // </editor-fold>

    // <editor-fold desc="status test cases">
    
    @Test
    public void testHasNotNullInvitationStatusByDefaultSuccess() {
        Invitation i = new Invitation();
        Assert.assertNotNull(i.getStatus());
    }
    
    @Test
    public void testHasCreatedInvitationStatusByDefaultSuccess() {
        Invitation i = new Invitation();
        Assert.assertEquals(i.getStatus(),flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED);
    }
    
    @Test
    public void testSetStatusSuccess() {
        Invitation i = new Invitation();
        i.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStatusBadValueFailure() {
        Invitation i = new Invitation();
        i.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.valueOf("xyzyx"));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetStatusNullFailure() {
        Invitation i = new Invitation();
        i.setStatus(null);
    }
    
    // </editor-fold>

    // <editor-fold desc="recipient test cases">
    
    @Test
    public void testHasNoRecipientByDefaultSuccess() {
        Invitation i = new Invitation();
        Assert.assertNull(i.getRecipient());
    }
    
    @Test
    public void testSetRecipientSuccess() {
        Invitation i = new Invitation();
        Person p = makePerson();
        i.setRecipient(p);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetRecipientNullFailure() {
        Invitation i = new Invitation();
        i.setRecipient(null);
    }
    
    // </editor-fold>

    // <editor-fold desc="lastUpdated test cases">

    @Test
    public void testGetLastUpdatedInPastSuccess() {
        Invitation i = new Invitation();
        DateValidator.validatePastDate(i.getLastUpdated());
    }
    
    @Test
    public void testSetLastUpdatedSuccess() {
        Invitation i = new Invitation();
        Date d=new Date();
        d=new Date(d.getTime()-(60*100));
        i.setLastUpdated(d);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastUpdatedInFutureFailure() {
        Invitation i = new Invitation();
        Date d=new Date();
        d=new Date(d.getTime()+(long)(60*60*100));
        i.setLastUpdated(d);
    }
    
    // </editor-fold>
    
}

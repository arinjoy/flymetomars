package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the mission model
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * @author Apoorva Singh
 */
public class MissionIntegrationTest {

    // <editor-fold desc="setName test cases">
    
    @Test
    public void testSetNameSuccess() {
        Mission m = new Mission();
        m.setName("Mars Adeventure");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameEmptyFailure() {
        Mission m = new Mission();
        m.setName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameLengthLessThanMinFailure() {
        Mission m = new Mission();
        m.setName("XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameLengthGreaterThanMaxFailure() {
        Mission m = new Mission();
        //79 character string:
        m.setName("Super Great Awesome Mars Special Fantastic Expedition with three hundred people");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWithSpecialCharacterFailure() {
        Mission m = new Mission();
        m.setName("#Mar$ % Ad^enture*");
    }
    
    // </editor-fold>

    // <editor-fold desc="setTime test cases">
    
    @Test
    public void testSetTimeSuccess() {
        Mission m = new Mission();
        Calendar missionDate = Calendar.getInstance();
        missionDate.add(Calendar.DAY_OF_YEAR, 150);
        Date d;
        d = new Date(missionDate.getTimeInMillis());
        m.setTime(d);
    }
    
    // </editor-fold>

    // <editor-fold desc="setLocation test cases">
    
    @Test
    public void testSetLocationSuccess() {
        Mission m = new Mission();
        Location l = new Location();
        l.setFloor("Basement");
        l.setStreetNo("221");
        l.setStreet("Dandenong Road");
        l.setRegion("Victoria");
        l.setPostcode("3031");
        l.setLandmark("Near Racecourse");
        l.setTown("Caulfield");  
        l.setCountry("Australia");
        m.setLocation(l);
    }
  
    @Test
    public void testSetLocationWithMinimalInfoSuccess() {
        Mission m = new Mission();
        Location l = new Location();
        l.setTown("Caulfield");  
        l.setCountry("Australia");
        m.setLocation(l);
    }
    
   
    @Test
    public void testResetLocationSuccess() {
        Mission m = new Mission();
        Location l1 = new Location(), l2, result;
        l1.setFloor("H2.22");
        l1.setStreetNo("221");
        l1.setStreet("Dandenong Road");
        l1.setRegion("Victoria");
        l1.setPostcode("3031");
        l1.setLandmark("Near Racecourse");
        l1.setTown("Caulfield");
        l1.setCountry("Australia");
        m.setLocation(l1);
        
        l2 = new Location();
        l2.setFloor("Level 2");
        l2.setStreetNo("212");
        l2.setStreet("Queens Road");
        l2.setRegion("NSW");
        l2.setPostcode("4020");
        l2.setLandmark("Opera House");
        l2.setTown("Sydney");
        l2.setCountry("Australia");
        m.setLocation(l2);
        
        result = m.getLocation();     
        Assert.assertEquals(l2, result);
         
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetLocationLengthLessThanMinFailure() {
        Mission m = new Mission();
        m.setName("Tul");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLocationLengthGreaterThanMaxFailure() {
        Mission m = new Mission();
        m.setName("NASA Rocket Launching station, Chicago, United States of America, The World.");
    }
    
    

    // </editor-fold>
    
    // <editor-fold desc="setDescription test cases">
    
    @Test
    public void testSetDescriptionSuccess() {
        Mission m = new Mission();
        m.setDescription("The best thing of your life. You will never forget this journey.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionEmptyFailure() {
        Mission m = new Mission();
        m.setDescription("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionLegthLessThanMinFailure() {
        Mission m = new Mission();
        m.setDescription("The best");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionLengthGreaterThanMaxFailure() {
        Mission m = new Mission();
        m.setDescription("That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition. That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition. That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition.That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition. That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition. That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition. That will be such thrilling journey fo my life so that in my next life I will keep thinking of this great expedition.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionContainsBadCharactersFailure() {
        Mission m = new Mission();
        m.setDescription("abc2  d $57 032 $#(::) >:' ");
    }
    
    // </editor-fold>

    // <editor-fold desc="setCaptain test cases">
    
    private Person makeCaptain() {
        Person result = new Person();
        result.setFirstName("Lawrence");
        result.setLastName("Colman");
        result.setUserName("lpcol_22");
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setSalt(salt);  //the setSalt call must happen BEFORE setPassword.
        pass.setPassword("Ajoy_36#Bis%joy");
        result.setPassword(pass);
        result.setEmail("law.rence@spacemail.com.au");
        return result;
    }
    
    @Test
    public void testSetCaptainSuccess() {
        Mission m = new Mission();
        Person p = makeCaptain();
        m.setCaptain(p);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetCaptainNullFailure() {
        Mission m = new Mission();
        m.setCaptain(null);
    }

    // </editor-fold>
    
    // <editor-fold desc="invitationSet test cases">
    
    @Test
    public void testHasNoInvitationsByDefaultSuccess() {
        Mission m = new Mission();
        Set<Invitation> invited = m.getInvitationSet();
        Assert.assertTrue(invited.isEmpty());
    }
        
    @Test
    public void testSetInvitationSetSuccess() {
        Mission m = new Mission();
        Invitation i = new Invitation();
        Person creator = makeCaptain();
        Person recipient = new Person();
        recipient.setFirstName("Arinjoy");
        recipient.setLastName("Biswas");
        recipient.setUserName("bisaw81");
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setSalt(salt);  //the setSalt call must happen BEFORE setPassword.
        pass.setPassword("Ajoy_36#Bis%joy");
        recipient.setPassword(pass);
        recipient.setEmail("arin.bis@yahoo.com");
        Set<Invitation> invitations = new HashSet<Invitation>();
        invitations.add(i);
        i.setCreator(creator);
        i.setRecipient(recipient);
        i.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED);
        m.setInvitationSet(invitations);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetInvitationSetNullFailure() {
        Mission m = new Mission();
        m.setInvitationSet(null);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="participantSet test cases">
    
    @Test
    public void testHasNoParticipantsByDefaultSuccess() {
        Mission m = new Mission();
        Set<Person> participants = m.getParticipantSet();
        Assert.assertTrue(participants.isEmpty());
    }
    
    @Test
    public void testSetParticipantSetSuccess() {
        Set<Person> people = new HashSet<Person>();
        Mission m = new Mission();
        m.setParticipantSet(people);
    }
    
    @Test
    public void testSetParticipantSetWithParticipantsSuccess() {
        Set<Person> people = new HashSet<Person>();
        Person p1 =  makeCaptain();
        Person p2 =  makeCaptain();
        p1.setFirstName("Apoorva");
        p2.setFirstName("Arinjoy");
        p2.setUserName("spaceXplor3r");
        people.add(p1);
        people.add(p2);
        Mission m = new Mission();
        m.setParticipantSet(people);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetParticipantSetNullFailure() {
        Mission m = new Mission();
        m.setParticipantSet(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetParticipantSetWithNullPersonFailure() {
        Set<Person> people = new HashSet<Person>();
        people.add(null);
        Mission m = new Mission();
        m.setParticipantSet(people);
    }
    
    // </editor-fold>
    
}

package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the person model
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class PersonIntegrationTest {

    private Mission makeMission() {
        Mission m = new Mission();
        m.setName("Unite the Federation");
        m.setTime(new Date(new Date().getTime() + 10000L));
        Location l = new Location();        
        l.setFloor("H2.22");
        l.setStreetNo("221");
        l.setStreet("Dandenong Road");
        l.setRegion("Victoria");
        l.setPostcode("3031");
        l.setLandmark("Near Racecourse");
        l.setTown("Caulfield");  
        l.setCountry("Australia");
        m.setLocation(l);
        m.setDescription("to boldly go where no one has gone before");
        return m;
    }
    
    // <editor-fold desc="firstName test cases">
	
    @Test
    public void testHasEmptyFirstNameByDefaultSuccess() {
        Person p = new Person();
        Assert.assertTrue(p.getFirstName().isEmpty());
    }
   
    @Test
    public void testSetFirstNameSuccess() {
        Person p = new Person();
        p.setFirstName("Barrack");
    }
    
    @Test
    public void testSetAndRetrieveFirstNameSuccess() {
        Person p = new Person();
        String n1="George",n2;
        p.setFirstName(n1);
        n2=p.getFirstName();
        Assert.assertEquals(n1,n2);
    }
    
    @Test
    public void testResetFirstNameSuccess() {
        Person p = new Person();
        String n1="Bill",n2="Ronald",result;
        p.setFirstName(n1);
        p.setFirstName(n2);
        result=p.getFirstName();
        Assert.assertEquals(n2,result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameEmptyFailure() {
        Person p = new Person();
        p.setFirstName("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testResetFirstNameEmptyFailure() {
        Person p = new Person();
        p.setFirstName("Jimmy");
        p.setFirstName("");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetFirstNameNullFailure() {
        Person p = new Person();
        p.setFirstName(null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testResetFirstNameNullFailure() {
        Person p = new Person();
        p.setFirstName("Gerald");
        p.setFirstName(null);
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameTooShortFailure() {
        Person p = new Person();
        p.setFirstName("A");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameTooLongFailure() {
        Person p = new Person();
        p.setFirstName("Kdfjalsjdfiouwerkjzcvfsdfs");  //26 chars
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameContainsNumberFailure() {
        Person p = new Person();
        p.setFirstName("A1654dcx");
    }
	
    @Test(expected=IllegalArgumentException.class)
    public void testSetFirstNameContainsSpaceFailure() {
        Person p = new Person();
        p.setFirstName("John Hood");
    }
    
    @Test
    public void testSetFirstNameContainsExtenedLatinSuccess() {
        Person p = new Person();
        p.setFirstName("L\u0086wr\u0088n\u0063\u0089"); //Låwrênċë
    }
    
    @Test
    public void testSetFirstNameContainsHyphenSuccess() {
        Person p = new Person();
        p.setFirstName("Mary-Kate");
    }
	
    // </editor-fold>
	
    // <editor-fold desc="lastName test cases">
	
    @Test
    public void testHasEmptyLastNameByDefaultSuccess() {
        Person p = new Person();
        Assert.assertTrue(p.getLastName().isEmpty());
    }
    
    @Test
    public void testSetLastNameSuccess() {
        Person p = new Person();
        p.setLastName("Obama");
    }
    
    @Test
    public void testSetAndRetrieveLastNameSuccess() {
        Person p = new Person();
        String n1="Bush",n2;
        p.setLastName(n1);
        n2=p.getLastName();
        Assert.assertEquals(n1,n2);
    }
    
    @Test
    public void testResetLastNameSuccess() {
        Person p = new Person();
        String n1="Clinton",n2="Regan",result;
        p.setLastName(n1);
        p.setLastName(n2);
        result=p.getLastName();
        Assert.assertEquals(n2,result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameEmptyFailure() {
        Person p = new Person();
        p.setLastName("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testResetLastNameEmptyFailure() {
        Person p = new Person();
        p.setLastName("Carter");
        p.setLastName("");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetLastNameNullFailure() {
        Person p = new Person();
        p.setLastName(null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testResetLastNameNullFailure() {
        Person p = new Person();
        p.setLastName("Ford");
        p.setLastName(null);
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameTooShortFailure() {
        Person p = new Person();
        p.setLastName("B");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameTooLongFailure() {
        Person p = new Person();
        p.setLastName("Azvolssksijeleajfidsksjnsmjssjkslsa"); //36 chars
    }
	
    @Test
    public void testSetLastNameContainsSpaceSuccess() {
        Person p = new Person();
        p.setLastName("von Neuuman");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameAllWhitespaceFailure() {
        Person p = new Person();
        p.setLastName("          ");  //10 spaces
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameContainsNumberFailure() {
        Person p = new Person();
        p.setLastName("Exasdk3");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameContainsUnderscoreFailure() {
        Person p = new Person();
        p.setLastName("Colm_n");
    }
    
    @Test
    public void testSetLastNameContainsExtenedLatinSuccess() {
        Person p = new Person();
        p.setLastName("C\u0094lm\u00c6n"); //Cölmãn
    }
    
    @Test
    public void testSetLastNameContainsHyphenSuccess() {
        Person p = new Person();
        p.setLastName("Bernes-Lee");
    }
	
    // </editor-fold>
	
    // <editor-fold desc="userName test cases">
	
    @Test
    public void testHasEmptyUsernameByDefaultSuccess() {
        Person p = new Person();
        Assert.assertTrue(p.getUserName().isEmpty());
    }
    
    @Test
    public void testSetUserNameSuccess() {
        Person p = new Person();
        p.setUserName("MisterFoo");
    }
    
    @Test
    public void testSetAndRetrieveUserNameSuccess() {
        Person p = new Person();
        String un1="MissBar",un2;
        p.setUserName(un1);
        un2=p.getUserName();
        Assert.assertEquals(un1,un2);
    }
    
    @Test
    public void testResetUserNameSuccess() {
        Person p = new Person();
        String un1="FooBar",un2="FooBarFoo",result;
        p.setUserName(un1);
        p.setUserName(un2);
        result=p.getUserName();
        Assert.assertEquals(un2,result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameEmptyFailure() {
        Person p = new Person();
        p.setUserName("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testResetUserNameEmptyFailure() {
        Person p = new Person();
        p.setUserName("MisterBar");
        p.setUserName("");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetUserNameNullFailure() {
        Person p = new Person();
        p.setUserName(null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testResetUserNameNullFailure() {
        Person p = new Person();
        p.setUserName("MissFoo");
        p.setUserName(null);
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameTooShortFailure() {
        Person p = new Person();
        p.setUserName("jk");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameTooLongFailure() {
        Person p = new Person();
        p.setUserName("4C617772656E636520436F6C6D616Eand4172696E6A6F7920426973776173");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameContainsSpacesFailure() {
        Person p = new Person();
        p.setUserName("Arin joy");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameContainsSpecialCharactersFailure() {
        Person p = new Person();
        p.setUserName("#Ap%or^a!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameContainsExtendedLatinFailure() {
        Person p = new Person();
        p.setUserName("ÃpÔÕrvã");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetUserNameContainsHyphenFailure() {
        Person p = new Person();
        p.setUserName("Yuan-Fang");
    }

    @Test
    public void testSetUserNameContainsUnderscoreSuccess() {
        Person p = new Person();
        p.setUserName("Yuan_Fang");
    }
    
    @Test
    public void testSetUserNameContainsDotSuccess() {
        Person p = new Person();
        p.setUserName("Lawrence.Colman");
    }
	
	
    // </editor-fold>
    
    // <editor-fold desc="email test cases">
	
   @Test
    public void testHasEmptyEmailByDefaultSuccess() {
        Person p = new Person();
        Assert.assertTrue(p.getEmail().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetBlankEmailFailure() {
        Person p = new Person();
        p.setEmail("");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetNullEmailFailure() {
        Person p = new Person();
        p.setEmail(null);
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailTooShortFailure() {
        Person p = new Person();
        p.setEmail("a@bc.it"); //7 chars
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailTooLongFailure() {
        Person p = new Person();
        //82 char email:
        p.setEmail("Arinjoy+Apoorva+Lawrence@Biswas-Singh-and-Colman.are.totally-awesome.net.au.co.nz");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidEmailFailure() {
        Person p = new Person();
        p.setEmail("qwertyuiop");  //no at symbol
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetBadEmailFailure() {
        Person p = new Person();
        p.setEmail("+@.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailWithExtendedLatinFailure() {
        Person p = new Person();
        p.setEmail("Tõný.Soprãno@legitimage.biz");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailWithoutDotFailure() {
        Person p = new Person();  //Business Rule is for public TLDs only,
        p.setEmail("webmaster@localhost");  //no private domains registerable
    }
    
    @Test
    public void testSetEmailWithPlusSuccess() {
        Person p = new Person();
        p.setEmail("jerry+seinfeld@nbc.com");
    }
    
    @Test
    public void testSetEmailWithUnderscoreSuccess() {
        Person p = new Person();
        p.setEmail("jerry_seinfeld@nbc.com");
    }
    
    @Test
    public void testSetEmailWithHyphenSuccess() {
        Person p = new Person();
        p.setEmail("jerry-seinfeld@nbc.com");
    }
    
    @Test
    public void testSetEmailWithInternalDomainSuccess() {
        Person p = new Person();
        p.setEmail("newman+seinfeld.net@nbc.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailWithInvalidDomainFailure() {
        Person p = new Person();
        p.setEmail("cramer@apt 5 dot org");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailWithSpacesBeforeAtFailure() {
        Person p = new Person();
        p.setEmail("the costanzas@new.jersey.net");
    }
    
    // </editor-fold>
	
    // <editor-fold desc="password test cases">

    @Test
    public void testSetPasswordWithSaltSuccess() {
        Person p = new Person();
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        salt.setPureSalt("I like to party");
        pass.setSalt(salt); 
        pass.setPassword("Ajoy_36#Bis%joy");
        p.setPassword(pass);
        Assert.assertEquals("1E7B3DAB78BC0D6E73BB45D3069CBBBF",pass.getDigest());
        Assert.assertTrue(p.isPassword("Ajoy_36#Bis%joy"));
    }
    
    @Test
    public void testUpdatePasswordSuccess() {
        Person p = new Person();
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setSalt(salt); 
        pass.setPassword("Ajoy_36#Bis%joy");
        p.setPassword(pass);       
        Assert.assertTrue(p.isPassword("Ajoy_36#Bis%joy"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePasswordEmptyFailure() {
        Person p = new Person();
        p.updatePassword("");
    }
	
    @Test(expected = NullArgumentException.class)
    public void testUpdatePasswordNullFailure() {
        Person p = new Person();
        p.updatePassword(null);
    }
    
    @Test
    public void testSetPasswordSuccess() {
        Person p = new Person();
        p.setPassword(new SaltedPassword());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetPasswordNullFailure() {
        Person p = new Person();
        p.setPassword(null);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="missions test cases">
	
    @Test
    public void testHasNoMissionsRegisteredByDefaultSuccess() {
        Person p = new Person();
        Set<Mission> m = p.getMissionsRegistered();
        Assert.assertTrue(m.isEmpty());
    }
	
    @Test
    public void testSetMissionsRegisteredSuccess() {
        Person p = new Person();
        Set<Mission> missions = new HashSet<Mission>();
        p.setMissionsRegistered(missions);
    }
	
    @Test
    public void testSetMissionRegisteredWithMissionSuccess() {
        Person p = new Person();
        p.setFirstName("LtCommander");
        p.setLastName("Data");
        p.setUserName("Android");
        Mission m = makeMission();
        m.setCaptain(p);
        Set<Mission> missions = new HashSet<Mission>();
        missions.add(m);
        p.setMissionsRegistered(missions);
    }
	
    @Test(expected = NullArgumentException.class)
    public void testSetMissionsRegisteredWithNullMissionFailure() {
        Set<Mission> missions = new HashSet<Mission>();
        missions.add(null);
        Person p = new Person();
        p.setMissionsRegistered(missions);
    }
	
    // </editor-fold>
    
    // <editor-fold desc="invitations test cases">
    
    @Test
    public void testHasNoInvitationsByDefaultSuccess() {
        Person p = new Person();
        Set<Invitation> i = p.getInvitationsReceived();
        Assert.assertTrue(i.isEmpty());
    }
	
    @Test
    public void testSetInvitationsReceivedSuccess() {
        Person p = new Person();
        Set<Invitation> i=new HashSet<Invitation>();
        p.setInvitationsReceived(i);
    }
	
    @Test
    public void testSetInvitationsReceivedWithInvitationSuccess() {
        //object preamble:
        Person creator = new Person();
        creator.setFirstName("James");
        creator.setLastName("Kirk");
        creator.setUserName("JTkrik");
        creator.setEmail("JTkrik@USSenterprise.federation.org");
        Person recipient = new Person();
        recipient.setFirstName("Leonard");
        recipient.setLastName("Nemoy");
        recipient.setUserName("DrSpock");
        recipient.setEmail("Spock@actors.star-trek.com");
        Invitation i = new Invitation();
	Mission m=makeMission();
        m.setCaptain(creator);
        i.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.ACCEPTED);
        i.setLastUpdated(new Date());
        i.setMission(m);
        i.setCreator(creator);
        i.setRecipient(recipient);
        //actual test:
        Set<Invitation> invitations = new HashSet<Invitation>();
        invitations.add(i);
        
        recipient.setInvitationsReceived(invitations);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetInvitationsReceivedNullFailure() {
        Person p = new Person();
        p.setInvitationsReceived(null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetInvitationsReceivedWithNullInvitationFailure() {
        Person p = new Person();
        Set<Invitation> invitations = new HashSet<Invitation>();
        invitations.add(null);
        p.setInvitationsReceived(invitations);
    }
	
    // </editor-fold>
	
    // <editor-fold desc="expertise test cases">
	
    @Test
    public void testExpertiseGainedSuccess() {
        Person p = new Person();
        Set<Expertise> ex=new HashSet<Expertise>();
        p.setExpertiseGained(ex);
    }
     
    @Test
    public void testSetExpertiseGainedWithExprtiseSuccess() {
        Person p = new Person();
        p.setPassword(new SaltedPassword());
        p.getPassword().setId(1337L);
        Set<Expertise> expertiseGained = new HashSet();
        Expertise ex1 = new Expertise();
        ex1.setName("Singing");
        ex1.setDescription("Somone who has ability to produce vocal sounds");
        ex1.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.AMATURE);
        Expertise ex2 = new Expertise();
        ex2.setName("Rock Climbing");
        ex2.setDescription("Somone who can climb rock eleveated in more than 60 degree");
        ex2.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        expertiseGained.add(ex1);
        expertiseGained.add(ex2);
        p.setExpertiseGained(expertiseGained);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetExpertiseGainedTooManyFailure() {
        Person p = new Person();
        Expertise ex1 = new Expertise();
        ex1.setName("Singing");
        ex1.setDescription("Somone who has ability to produce vocal sounds");
        ex1.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.AMATURE);
        Expertise ex2 = new Expertise();
        ex2.setName("Rock Climbing");
        ex2.setDescription("Somone who can climb rock eleveated in more than 60 degree");
        ex2.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        Expertise ex3 = new Expertise();
        ex3.setName("Astrology");
        ex3.setDescription("Somone who has ability to read palm prints and read the future");
        ex3.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.AMATURE);
        Expertise ex4 = new Expertise();
        ex4.setName("Mountain Biking");
        ex4.setDescription("Somone who can ride bikes over the mountians and desert.");
        ex4.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        Expertise ex5 = new Expertise();
        ex5.setName("Math Solving");
        ex5.setDescription("Somone who has ability to solve any math problem");
        ex5.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.SEMINAL);
        Expertise ex6 = new Expertise();
        ex6.setName("Piano Playing");
        ex6.setDescription("Somone who can play piano with rhythm and charisma");
        ex6.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        Expertise ex7 = new Expertise();
        ex7.setName("Software Development");
        ex7.setDescription("Somone who can build high quality software");
        ex7.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.EMINENT);
        Expertise ex8 = new Expertise();
        ex8.setName("Computer Science");
        ex8.setDescription("Somone who can explan FSM and NP problem");
        ex8.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.INTRODUCTORY);
        Expertise ex9 = new Expertise();
        ex9.setName("Scuba Diving");
        ex9.setDescription("Somone who can dive into the deep ocean with confidence");
        ex9.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        Expertise ex10 = new Expertise();
        ex10.setName("Boogy Jumping");
        ex10.setDescription("Somone who can jump from any hieght wihtout any worries");
        ex10.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.EMINENT);
        Expertise ex11 = new Expertise();
        ex11.setName("Computer Science");
        ex11.setDescription("Somone who can explan FSM and NP problem");
        ex11.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);   
        Set<Expertise> expertiseGained = new HashSet();
        expertiseGained.add(ex1);
        expertiseGained.add(ex2);
        expertiseGained.add(ex3);
        expertiseGained.add(ex4);
        expertiseGained.add(ex5);
        expertiseGained.add(ex6);
        expertiseGained.add(ex7);
        expertiseGained.add(ex8);
        expertiseGained.add(ex9);
        expertiseGained.add(ex10);
        expertiseGained.add(ex11);       
        p.setExpertiseGained(expertiseGained);
    }
	
    @Test(expected = NullArgumentException.class)
    public void testSetExpertiseGainedNullFailure() {
        Person p = new Person();
        p.setExpertiseGained(null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetExpertiseGainedWithNullExpertsiseFailure() {
        Person p = new Person();
        Set<Expertise> expertiseGained = new HashSet<Expertise>();
        expertiseGained.add(null);
        p.setExpertiseGained(expertiseGained);
    }

    // </editor-fold>
	
}

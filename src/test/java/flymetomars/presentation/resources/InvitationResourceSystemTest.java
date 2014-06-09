package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jwebunit.exception.TestingEngineResponseException;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 *
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class InvitationResourceSystemTest extends AbstractDataDependantResourceSystemTest {

    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;


    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    public InvitationResourceSystemTest() {
        super(0);
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<InvitationResourceSystemTest> me=InvitationResourceSystemTest.class;
        testCount=countTests(me);
    }
    @Before
    public void preTestTicker() {
        beforeTestTick();
    }
    @After
    public void postTestTicker() {
        afterTestTick();
    }
    @Override
    public void doBeforeFirstTest() {
        this.setTestCounter(1);
        this.setLoader(loader);
        this.setDeleter(deleter);
        doSystemInit();
    }
    @Override
    public void doAfterLastTest() {
        doSystemWindup();
    }
    //</editor-fold>
    
    private static SaltedPassword saltedPass;
    
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        cal=Calendar.getInstance();
        try {
            removeInvites();
            removeMissions();
            removeLocations();
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to purge user data", ex);
            throw new UnsupportedOperationException(ex);
        }
        try {
            saltedPass=factory.createSaltedPassword(new Salt());
            saver.saveSalt(saltedPass.getSalt());
            saltedPass.setPassword(VALID_PASSWORD);
            saver.saveSaltedPassword(saltedPass);
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist password errata", e);
            throw new IllegalStateException(e);
        }
        String port = getProperty("port.number");
        JWebUnit.setBaseUrl("http://localhost:" + port + "/flymetomars");
        JWebUnit.setScriptingEnabled(false);
        saveData();
    }
    
    //@AfterInstance (effectivly)
    public void doSystemWindup() {
        try {
            removeInvites();
            removeMissions();
            removeLocations();
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception durig System Test Windup", ex);
            throw new UnsupportedOperationException(ex);
        }
    }
    
    @After
    public void tearDown() throws DependencyException {
        //removeInvites();
        //removeMissions();
        //removeLocations();
    }
    
    private static Calendar cal;
    private static final String VALID_PASSWORD = "Hahah3!45";
    private static Person captain;
    private static Person recipient;
    private static Location location;
    private static Mission mission;
   
    private void createCaptain() {
        captain = factory.createPerson(saltedPass);
        captain.setEmail("smiddy@foobar.org");
        captain.setUserName("george");
        captain.getPassword().setPassword(VALID_PASSWORD);
        captain.setFirstName("George");
        captain.setLastName("Frankly");
    }

    private void createRecipient() {
        recipient = factory.createPerson(saltedPass);
        recipient.setEmail("test2@gmail.com");
        recipient.setUserName("john21");
        recipient.getPassword().setPassword(VALID_PASSWORD);
        recipient.setFirstName("John");
        recipient.setLastName("Smith");
    }

    private void createLocation() {
        location = factory.createLocation();
        location.setFloor("B3.43");
        location.setStreetNo("900");
        location.setStreet("Dandenong Road");
        location.setRegion("Victoria");
        location.setTown("Caulfield");
        location.setCountry("Australia");
    }
    
    
    private void createMission() {
        mission = factory.createMission(captain, location);
        mission.setTime(new Date(cal.getTimeInMillis()+900001));
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
    }
    
    private void saveData() {
        createCaptain();
        createRecipient();
        try {
            recipient=saver.savePerson(recipient);
            captain=saver.savePerson(captain);
        } catch (DependencyException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist person data", ex);
            throw new UnsupportedOperationException(ex);
        }
        createLocation();
        try {
            location=saver.saveLocation(location);
        } catch (DependencyException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist location data", ex);
            throw new UnsupportedOperationException(ex);
        }
        createMission();
        try {
            mission=saver.saveMission(mission);
        } catch (DependencyException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist mission data", ex);
            throw new UnsupportedOperationException(ex);
        }
    }

    private Invitation doInvite() {
        Invitation invitation = factory.createInvitation(captain, mission);
        invitation.setRecipient(recipient);
        invitation.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.SENT);
        invitation.setLastUpdated(new Date(cal.getTimeInMillis()-1000));
        try {
            saver.saveInvitation(invitation);
        } catch (DependencyException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist invitation data", ex);
            throw new UnsupportedOperationException(ex);
        }
        List<Invitation> invs = loader.loadInvitationsByCreator(captain);
        if(!invs.isEmpty()) { invitation=invs.get(0); }
        return invitation;
    }

    /**
     * index -> login -> base -> user -> invitation
     */
    @Test
    public void testUserPageContainsCreatedInvitation() {
        // creating a complete invitation with all creator, mission and recipient
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", captain.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        // login
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + captain.getFirstName() + " " + captain.getLastName());
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + captain.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + captain.getFirstName() + " " + captain.getLastName());
        JWebUnit.assertTextPresent("User Name: " + captain.getUserName());
        JWebUnit.assertTextPresent("Email Address: " + captain.getEmail());
        JWebUnit.assertTextPresent("First Name: " + captain.getFirstName());
        JWebUnit.assertTextPresent("Last Name: " + captain.getLastName());
        JWebUnit.assertTextPresent("Invitations created by me");
        JWebUnit.assertTextPresent("Invitation for " + mission.getName() + " to " + recipient.getUserName());
        }
    
    /**
     * index -> login -> base -> user -> invitation
     */
    @Test
    public void testGoToInvitationFromUserPage() {
        // creating a complete invitation with all creator, mission and recipient
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", captain.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        // login
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + captain.getFirstName() + " " + captain.getLastName());
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + captain.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + captain.getFirstName() + " " + captain.getLastName());
        JWebUnit.assertTextPresent("User Name: " + captain.getUserName());
        JWebUnit.assertTextPresent("Email Address: " + captain.getEmail());
        JWebUnit.assertTextPresent("First Name: " + captain.getFirstName());
        JWebUnit.assertTextPresent("Last Name: " + captain.getLastName());
        JWebUnit.assertTextPresent("Invitations created by me");
        JWebUnit.assertTextPresent("Invitation for " + mission.getName() + " to " + recipient.getUserName());
        // go to the created invitation page
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        //verify details
        JWebUnit.assertTextPresent("Invitation Details");
        JWebUnit.assertTextPresent("Recipient: " + invitation.getRecipient().getFirstName() + " " + invitation.getRecipient().getLastName());
        }

    /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * invitation page > assert it has been sent to him >
     * then accept the invitation
     */
    @Test
    public void testAcceptInvitationFromInvitationPageSuccess() {
        // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickLinkWithText("Invitation to "+invitation.getMission().getName());
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: sent");
        JWebUnit.clickButtonWithText("Accept");
        JWebUnit.assertTextPresent("Status: accepted");
        
    }
    
    /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * assert it has been sent to him >
     * then accept the invitation
     */
    @Test
    public void testAcceptInvitationFromHomePageSuccess() {
        // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickButtonWithText("Accept Invitation");
  
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: accepted");
        
    }
    
    /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * assert it has been sent to him >
     * then decline the invitation
     */
    @Test
    public void testDeclineInvitationFromHomePageSuccess() {
        // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickButtonWithText("Decline Invitation");
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: declined");
        
    }
    
   /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * invitation page > assert it has been sent to him >
     * then decline the invitation
     */
    @Test
    public void testDeclineInvitationFromInvitationPageSuccess() {
       // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickLinkWithText("Invitation to "+invitation.getMission().getName());
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: sent");
        JWebUnit.clickButtonWithText("Decline");
        JWebUnit.assertTextPresent("Status: declined");
    }
    
    
    /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * invitation page > assert it has been sent to him > Decline from user page >
     * come back to user page > change my mind
     * 
     */
    @Test
    public void testDeclineInvitationFromUserPageAndChangeMindFromUserPageSuccess() {
       // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickButtonWithText("Decline Invitation");
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: declined");
        // back to user page
        JWebUnit.clickLinkWithText(recipient.getUserName()+" - Settings");
        JWebUnit.clickButtonWithText("I change my mind! Accept Invitation");
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        // assert that the status chnaged
        JWebUnit.assertTextPresent("Status: accepted");
        
    }
    
      /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * invitation page > assert it has been sent to him > Accpt form user page >
     * back to user page > change my mind
     * 
     */
    @Test
    public void testAcceptInvitationFromUserPageAndChangeMindFromUserPageSuccess() {
       // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickButtonWithText("Accept Invitation");
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: accepted");
        // back to user page
        JWebUnit.clickLinkWithText(recipient.getUserName()+" - Settings");
        JWebUnit.clickButtonWithText("I've change my mind! Decline Invitation");
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        // assert that the status chnaged
        JWebUnit.assertTextPresent("Status: declined");    
    }
    
    
    /**
     * 
     * Verify invitation id does not exist
     */
    @Test
    public void testUnkownInvitation() {
        int invitationId = 001;
        try {
            JWebUnit.beginAt("/invitation/" + invitationId);
            JWebUnit.assertTextPresent("Cannot find invitation with id: " + invitationId);
        } catch (TestingEngineResponseException te) {
            if(!te.getMessage().startsWith("unexpected status code [404]")) { throw te; }
        }
    }
    
    /**
     * Send the invitation by the creator and then > recipient login > base > user > 
     * invitation page > assert it has been sent to him >
     * then accept the invitation > back to user page > verify mission registered
     *
    //@Test
    public void testAcceptInvitationAndVerifyMissionRegistertedSuccess() {
        // do the task of create and send the invitation by a creator
        Invitation invitation=doInvite();
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        // user page
        JWebUnit.assertTextPresent("Welcome, " + recipient.getFirstName() + " " + recipient.getLastName());
        // assert that the invitation has been sent from creator
        JWebUnit.assertTextPresent("Invitation to "+invitation.getMission().getName()+" from "+invitation.getCreator().getUserName()+", status: sent");
        
        JWebUnit.clickButtonWithText("Accept Invitation");
        
        // verify the status
        JWebUnit.gotoPage("/invitation/" + invitation.getId().toString());
        JWebUnit.assertTextPresent("Status: accepted");
        // go back to user page
        JWebUnit.clickLinkWithText(recipient.getUserName()+" - Settings");
        JWebUnit.gotoPage("/user/" + recipient.getUserName());
        //verify that this recipient has registered for that invitation's mission
        JWebUnit.assertTextPresent("Missions registered");
        // assert that the mission name appears in the list of registered missions
        JWebUnit.assertTextPresent(invitation.getMission().getName());  
        
    }*/
    


}

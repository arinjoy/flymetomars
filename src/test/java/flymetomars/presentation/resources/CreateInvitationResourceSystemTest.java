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
import flymetomars.business.model.SaltedPassword;
import flymetomars.dataaccess.ExpertiseDAO;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.After;
import org.junit.Assert;
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
public class CreateInvitationResourceSystemTest extends AbstractDataDependantResourceSystemTest {
    
    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;
    
    @Autowired
    private ExpertiseDAO expertiseDao;

    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    public CreateInvitationResourceSystemTest() {
        super(0);
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<CreateInvitationResourceSystemTest> me=CreateInvitationResourceSystemTest.class;
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
        this.setTestCounter(testCount);
        this.setLoader(loader);
        this.setDeleter(deleter);
        doSystemInit();
    }
    @Override
    public void doAfterLastTest() {
        doSystemWindup();
    }
    //</editor-fold>
    
    @After
    public void tearDown() throws DependencyException {
        removeInvites();
        removeMissions();
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

    private static SaltedPassword saltedPass;
    
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        try {
            removeInvites();
            removeMissions();
            removeLocations();
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to purge existing data", ex);
            throw new UnsupportedOperationException(ex);
        }
        try {
            saltedPass=factory.createSaltedPassword(factory.createSalt(""));
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
        makeDataForTest();
    }
    
    private static final String VALID_PASSWORD = "Hahah3!45";
    
    private static Location location;
    private static Person alpha;
    private static Person bravo;

    private void makeDataForTest() {
        //first person
        alpha = factory.createPerson(saltedPass);
        alpha.setEmail("test@gmail.com");
        alpha.setUserName("foobar");
        alpha.setFirstName("Foo");
        alpha.setLastName("Goo");
        //second person
        bravo = factory.createPerson(saltedPass);
        bravo.setEmail("test2@gmail.com");
        bravo.setUserName("john21");
        bravo.setFirstName("John");
        bravo.setLastName("Smith");
        //location
        location = factory.createLocation();
        location.setStreetNo("123");
        location.setStreet("Singh Avenue");
        location.setRegion("Castro");
        location.setLandmark("Near Averi");
        location.setTown("Columbo");
        location.setCountry("Sri Lanka");
        //save
        try {
            saveTheData();
        } catch (DependencyException de) {
            throw new UnsupportedOperationException("System Test Infrastructure Error",de);
        }
    }
    
    private void saveTheData() throws DependencyException {
        DependencyException res=null;
        try {
            saver.savePerson(alpha);
            saver.savePerson(bravo);
        } catch (DependencyException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to persist person test data", e);
            res=e;
        }
        try {
            saver.saveLocation(location);
        } catch (DependencyException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to persist location test data", e);
            res=e;
        }
        if(res!=null) { throw res; }
    }
    
    /**
     * index > login > base > user > create mission > mission > create invitation > invitation
     */
    @Test
    public void testCreateInvitationWithoutSendingSuccess() {
        Person creator = alpha;
        Mission mission = factory.createMission(creator, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        Person recipient = bravo;
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", creator.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + creator.getUserName());           
        // go to : create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        JWebUnit.gotoPage("/mission/create");
        // go to: create mission page
        JWebUnit.assertTextPresent("Mission Details");
        //populate mission data
        JWebUnit.setTextField("missionName", mission.getName());
        JWebUnit.setTextField("time", "20/08/2015, 9 PM");  //TODO: adjust format
        JWebUnit.setTextField("location", location.getId().toString());
        JWebUnit.setTextField("description", mission.getDescription());
        //save mission
        JWebUnit.clickButtonWithText("Create New Mission");
       // mission page
        JWebUnit.assertTextPresent("Mission Details");
        JWebUnit.assertTextPresent("Name: " + mission.getName());
        JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
        JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        // go to : create invitation
        JWebUnit.clickLinkWithText("Create invitation");
        // create invitation but not sending now
        JWebUnit.assertTextPresent("Create invitation");
        JWebUnit.assertTextPresent("Recipient:");
        JWebUnit.assertTextPresent("Send now?:");
        JWebUnit.setTextField("receipient", recipient.getUserName());
        // creating but not sending
        JWebUnit.clickRadioOption("toSend", "CREATED");
        JWebUnit.clickButtonWithText("Create New Invitation");
        //check its in db
        List<Invitation> invs=loader.loadInvitationsByCreator(creator);
        Assert.assertFalse(invs.isEmpty());
        Assert.assertEquals(1, invs.size());
        Invitation result=loader.loadInvitationById(invs.get(0).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCreator().getId(),creator.getId());
        Assert.assertEquals(result.getStatus(),flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED);
        //check next page renders it too
        JWebUnit.gotoPage("/invitation/" + result.getId().toString());
        JWebUnit.assertTextPresent("Invitation Details");
        JWebUnit.assertTextPresent("Recipient: " + recipient.getFirstName() + " " + recipient.getLastName());
        JWebUnit.assertTextPresent("Status: created");
    }
    
     /**
     * index > login > base > user > create mission > mission > create and send invitation > invitation
     */
    @Test
    public void testCreateAndSendInvitationSuccess() {
        Person creator = alpha;
        Mission mission = factory.createMission(creator, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        Person recipient = bravo;
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", creator.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + creator.getUserName());           
        // go to : create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        JWebUnit.gotoPage("/mission/create");
        // go to: create mission page
        JWebUnit.assertTextPresent("Mission Details");
        //populate mission data
        JWebUnit.setTextField("missionName", mission.getName());
        JWebUnit.setTextField("time", "20/08/2015, 9 PM");  //TODO: adjust
        JWebUnit.setTextField("location", location.getId().toString());
        JWebUnit.setTextField("description", mission.getDescription());
        //save mission
        JWebUnit.clickButtonWithText("Create New Mission");
       // mission page
        JWebUnit.assertTextPresent("Mission Details");
        JWebUnit.assertTextPresent("Name: " + mission.getName());
        JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
        JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        // go to : create invitation
        JWebUnit.clickLinkWithText("Create invitation");
        // create invitation but not sending now
        JWebUnit.assertTextPresent("Create invitation");
        JWebUnit.assertTextPresent("Recipient:");
        JWebUnit.assertTextPresent("Send now?:");
        JWebUnit.setTextField("receipient", recipient.getUserName());
        // sending along with creating
        JWebUnit.clickRadioOption("toSend", "SENT");
        JWebUnit.clickButtonWithText("Create New Invitation");
        //check its in db
        List<Invitation> invs=loader.loadInvitationsByCreator(creator);
        Assert.assertFalse(invs.isEmpty());
        Assert.assertEquals(1, invs.size());
        Invitation result=loader.loadInvitationById(invs.get(0).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCreator().getId(),creator.getId());
        Assert.assertEquals(result.getStatus(),flymetomars.common.datatransfer.Invitation.InvitationStatus.SENT);
        //check next page renders it too
        JWebUnit.gotoPage("/invitation/" + result.getId().toString());
        JWebUnit.assertTextPresent("Invitation Details");
        JWebUnit.assertTextPresent("Recipient: " + recipient.getFirstName() + " " + recipient.getLastName());
        JWebUnit.assertTextPresent("Status: sent");
    }
    
    /**
     * index > login > base > user > create mission > mission > create and send invitation > invitation
     * > log out > login for recipient > recipient check whether it has the invitation received in its user page
     */
    @Test
    public void testCreateAndSendInvitationVerifyFromRecipientSideSuccess() {
        Person creator = alpha;
        Mission mission = factory.createMission(creator, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        Person recipient = bravo;
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page (alpha)
        JWebUnit.setTextField("j_username", creator.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + creator.getUserName());           
        // go to : create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        JWebUnit.gotoPage("/mission/create");
        // go to: create mission page
        JWebUnit.assertTextPresent("Mission Details");
        //populate mission data
        JWebUnit.setTextField("missionName", mission.getName());
        JWebUnit.setTextField("time", "20/08/2015, 9 PM");  //TODO: adjust
        JWebUnit.setTextField("location", location.getId().toString());
        JWebUnit.setTextField("description", mission.getDescription());
        //save mission
        JWebUnit.clickButtonWithText("Create New Mission");
       // mission page
        JWebUnit.assertTextPresent("Mission Details");
        JWebUnit.assertTextPresent("Name: " + mission.getName());
        JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
        JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        // go to : create invitation
        JWebUnit.clickLinkWithText("Create invitation");
        // create invitation but not sending now
        JWebUnit.assertTextPresent("Create invitation");
        JWebUnit.assertTextPresent("Recipient:");
        JWebUnit.assertTextPresent("Send now?:");
        JWebUnit.setTextField("receipient", recipient.getUserName());
        // sending along with creating
        JWebUnit.clickRadioOption("toSend", "SENT");
        JWebUnit.clickButtonWithText("Create New Invitation");
        //check its in db
        List<Invitation> invs=loader.loadInvitationsByCreator(creator);
        Assert.assertFalse(invs.isEmpty());
        Assert.assertEquals(1, invs.size());
        Invitation result=loader.loadInvitationById(invs.get(0).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCreator().getId(),creator.getId());
        Assert.assertEquals(result.getStatus(),flymetomars.common.datatransfer.Invitation.InvitationStatus.SENT);
        //check next page renders it too
        JWebUnit.gotoPage("/invitation/" + result.getId().toString());
        JWebUnit.assertTextPresent("Invitation Details");
        JWebUnit.assertTextPresent("Recipient: " + recipient.getFirstName() + " " + recipient.getLastName());
        JWebUnit.assertTextPresent("Status: sent");
        
        // finished sending the invitation by the sender (alpha)
        JWebUnit.gotoPage("j_spring_security_logout");
        JWebUnit.closeBrowser();
        JWebUnit.beginAt("");
        // now login by the recipient (bravo) and check his user page whther invitation receievd for this mission
        
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page (bravo)
        JWebUnit.setTextField("j_username", recipient.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + recipient.getUserName()); 
        // verify the invitation received
        JWebUnit.assertTextPresent("Invitations received:");
        // invitation came from the mission has mission name displayed
        JWebUnit.assertTextPresent("Invitation to "+mission.getName());
    }
    
    
    /**
     * index > login > base > user page > create mission > mission > create and send invitation > 
     * invitation > verify details > user page > mission link > Invitation added for that mission
     */
    @Test
    public void testCreateAndSendInvitationAndCheckDetailsViaMissionPage() {
        Person creator = alpha;
        Mission mission = factory.createMission(creator, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        Person recipient = bravo;
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page (alpha)
        JWebUnit.setTextField("j_username", creator.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + creator.getUserName());           
        // go to : create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        JWebUnit.gotoPage("/mission/create");
        // go to: create mission page
        JWebUnit.assertTextPresent("Mission Details");
        //populate mission data
        JWebUnit.setTextField("missionName", mission.getName());
        JWebUnit.setTextField("time", "20/08/2015, 9 PM");  //TODO: adjust
        JWebUnit.setTextField("location", location.getId().toString());
        JWebUnit.setTextField("description", mission.getDescription());
        //save mission
        JWebUnit.clickButtonWithText("Create New Mission");
       // mission page
        JWebUnit.assertTextPresent("Mission Details");
        JWebUnit.assertTextPresent("Name: " + mission.getName());
        JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
        JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        // go to : create invitation
        JWebUnit.clickLinkWithText("Create invitation");
        // create invitation but not sending now
        JWebUnit.assertTextPresent("Create invitation");
        JWebUnit.assertTextPresent("Recipient:");
        JWebUnit.assertTextPresent("Send now?:");
        JWebUnit.setTextField("receipient", recipient.getUserName());
        // sending along with creating
        JWebUnit.clickRadioOption("toSend", "SENT");
        JWebUnit.clickButtonWithText("Create New Invitation");
        //check its in db
        List<Invitation> invs=loader.loadInvitationsByCreator(creator);
        Assert.assertFalse(invs.isEmpty());
        Assert.assertEquals(1, invs.size());
        Invitation result=loader.loadInvitationById(invs.get(0).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCreator().getId(),creator.getId());
        Assert.assertEquals(result.getStatus(),flymetomars.common.datatransfer.Invitation.InvitationStatus.SENT);
        //check next page renders it too
        JWebUnit.gotoPage("/invitation/" + result.getId().toString());
        JWebUnit.assertTextPresent("Invitation Details");
        JWebUnit.assertTextPresent("Recipient: " + recipient.getFirstName() + " " + recipient.getLastName());
        JWebUnit.assertTextPresent("Status: sent");
        // go back to user page
        JWebUnit.clickLinkWithText(creator.getUserName()+" - Settings");
        JWebUnit.gotoPage("/user/" + creator.getUserName());
        //verify that the mission name appears
        JWebUnit.assertTextPresent("Missions created by me:");
        JWebUnit.clickLinkWithText(mission.getName());
       // JWebUnit.gotoPage("/mission/" + mission.getId().toString()); 
        // assert that the invitaion created for this mission
        JWebUnit.assertTextPresent(result.getId().toString());
        
       
    }
    
     /**
     * index > login > base > user > create mission > mission > create and send invitation  to himself fails
     */
    @Test
    public void testCreateAndSendToHimSelfFailure() {
        Person creator = alpha;
        Mission mission = factory.createMission(creator, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        Person recipient = alpha;
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", creator.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + creator.getUserName());           
        // go to : create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        JWebUnit.gotoPage("/mission/create");
        // go to: create mission page
        JWebUnit.assertTextPresent("Mission Details");
        //populate mission data
        JWebUnit.setTextField("missionName", mission.getName());
        JWebUnit.setTextField("time", "20/08/2015, 9 PM");  //TODO: adjust
        JWebUnit.setTextField("location", location.getId().toString());
        JWebUnit.setTextField("description", mission.getDescription());
        //save mission
        JWebUnit.clickButtonWithText("Create New Mission");
       // mission page
        JWebUnit.assertTextPresent("Mission Details");
        JWebUnit.assertTextPresent("Name: " + mission.getName());
        JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
        JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        // go to : create invitation
        JWebUnit.clickLinkWithText("Create invitation");
        // create invitation but not sending now
        JWebUnit.assertTextPresent("Create invitation");
        JWebUnit.assertTextPresent("Recipient:");
        JWebUnit.assertTextPresent("Send now?:");
        JWebUnit.setTextField("receipient", recipient.getUserName());
        // sending along with creating
        JWebUnit.clickRadioOption("toSend", "SENT");
        JWebUnit.clickButtonWithText("Create New Invitation");
        // sending to myself that alspha is sending to alpha. hence failure will occur
        JWebUnit.assertTextPresent("You cannot invite to yourself! Please send invite to someone else.");
    }
    
    
     /**
     * index > login > base > user > create mission > mission > create invitation > invitation
     *
    @Test
    public void testCreateInvitationRecipientHasExpertiseSuccess() {
        Person creator = alpha;
        Mission mission = factory.createMission(creator, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        Person recipient = bravo;
        
         Expertise expertise=factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        flymetomars.common.datatransfer.Expertise expDTO = expertise.toDTO();
        expDTO.setHeldBy(recipient.toDTO());
        // saving the expertise1 first
        expertiseDao.save(expDTO);
        // getting the serilazable id from DTO
        expertise.setId(expDTO.getId());
        
        
        // index page
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        // go to : login page
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.gotoPage("/login");
        // login page
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        // go to : base page
        JWebUnit.setTextField("j_username", creator.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        // base page
        JWebUnit.assertTextPresent("Places to go to: User page");
        // go to : user page
        JWebUnit.clickLinkWithText("User page");
        JWebUnit.gotoPage("/user/" + creator.getUserName());           
        // go to : create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        JWebUnit.gotoPage("/mission/create");
        // go to: create mission page
        JWebUnit.assertTextPresent("Mission Details");
        //populate mission data
        JWebUnit.setTextField("missionName", mission.getName());
        JWebUnit.setTextField("time", "20/08/2015, 9 PM");  //TODO: adjust format
        JWebUnit.setTextField("location", location.getId().toString());
        JWebUnit.setTextField("description", mission.getDescription());
        //save mission
        JWebUnit.clickButtonWithText("Create New Mission");
       // mission page
        JWebUnit.assertTextPresent("Mission Details");
        JWebUnit.assertTextPresent("Name: " + mission.getName());
        JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
        JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        // go to : create invitation
        JWebUnit.clickLinkWithText("Create invitation");
        // create invitation but not sending now
        JWebUnit.assertTextPresent("Create invitation");
        JWebUnit.assertTextPresent("Recipient:");
        JWebUnit.assertTextPresent("Send now?:");
        JWebUnit.setTextField("receipient", recipient.getUserName());
        // creating but not sending
        JWebUnit.clickRadioOption("toSend", "CREATED");
        JWebUnit.clickButtonWithText("Create New Invitation");
        //check its in db
        List<Invitation> invs=loader.loadInvitationsByCreator(creator);
        Assert.assertFalse(invs.isEmpty());
        Assert.assertEquals(1, invs.size());
        Invitation result=loader.loadInvitationById(invs.get(0).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCreator().getId(),creator.getId());
        Assert.assertEquals(result.getStatus(),flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED);
        //check next page renders it too
        JWebUnit.gotoPage("/invitation/" + result.getId().toString());
        JWebUnit.assertTextPresent("Invitation Details");
        JWebUnit.assertTextPresent("Recipient: " + recipient.getFirstName() + " " + recipient.getLastName());
        JWebUnit.assertTextPresent("Status: created");
        
        expertiseDao.delete(expDTO);
    }*/

}

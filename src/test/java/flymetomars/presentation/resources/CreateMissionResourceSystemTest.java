package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
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
@FixMethodOrder
public class CreateMissionResourceSystemTest extends AbstractDataDependantResourceSystemTest {

    @Autowired
    private EntityLoader loader;
    @Autowired
    private EntitySaver saver;
    @Autowired
    private EntityDeleter deleter;
    @Autowired
    private EntityFactory factory;
    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    private static int constructCount = 0;

    public CreateMissionResourceSystemTest() {
        super(constructCount++);
    }
    private static int testCount;

    @BeforeClass
    public static void getTestCount() {
        Class<CreateLocationResourceSystemTest> me = CreateLocationResourceSystemTest.class;
        testCount = countTests(me);
    }

    @Before
    public void preTestTicker() {
        this.setTestCounter(testCount);
        this.setLoader(loader);
        this.setDeleter(deleter);
        makeLocation();
        beforeTestTick();
    }

    @After
    public void postTestTicker() {
        afterTestTick();
    }

    @Override
    public void doBeforeFirstTest() {
        doSystemInit();
    }

    @Override
    public void doAfterLastTest() {
        doSystemWindup();
    }
    //</editor-fold>

    //@AfterInstance (effectivly)
    public void doSystemWindup() {
        try {
            removeMissions();
            removePeople();
            removePasswords();
            removeSalts();
            removeLocations();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception durig System Test Windup", ex);
            throw new UnsupportedOperationException(ex);
        }
    }
    private static SaltedPassword saltedPass;
    private static Person person;
    private static Location location;

    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        try {
            removeMissions();
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to remove existing missions", e);
            throw new IllegalStateException(e);
        }
        try {
            saltedPass = factory.createSaltedPassword(new Salt());
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
        if (null == person) {
            //person
            person = createAPerson();
            //save
            try {
                person = saver.savePerson(person);
            } catch (DependencyException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to persist person test data", e);
                throw new UnsupportedOperationException("System Test Infrastructure Error", e);
            }
        }
        location = this.makeLocation();
        try {
            location = saver.saveLocation(location);
        } catch (DependencyException ex) {
            Logger.getLogger(CreateMissionResourceSystemTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static final String VALID_PASSWORD = "Hahah3!45";

    private Person createAPerson() {
        Person p = factory.createPerson(saltedPass);
        p.setEmail("test@gmail.com");
        p.setUserName("foobar");
        p.getPassword().setPassword(VALID_PASSWORD);
        p.setFirstName("Foo");
        p.setLastName("Goo");
        return p;
    }

    private Location makeLocation() {
        Location l = factory.createLocation();
        l.setFloor("H2.22");
        l.setStreetNo("221");
        l.setStreet("Dandenong Road");
        l.setRegion("Victoria");
        l.setPostcode("3031");
        l.setLandmark("Near Racecourse");
        l.setTown("Caulfield");
        l.setCountry("Australia");
        return l;
    }

    /**
     * Test creating mission from user login page (baseUrl/login) --> user page
     * then Verify Mission details
     */
    @Test
    public void testMissionStartingFromLoginPage() throws DependencyException {
        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");
            JWebUnit.setTextField("missionName", mission.getName());
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify mission details
            JWebUnit.gotoPage("/mission/" + mission.getId());
            JWebUnit.assertTextPresent("Mission Details");
            JWebUnit.assertTextPresent("Name: " + mission.getName());
            JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
            JWebUnit.assertTextPresent("Description: " + mission.getDescription());

        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * name is not set
     */
    @Test
    public void testMissionNameNotSetFailure() throws DependencyException {
        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("Mission name must be between five and fifty characters!");

        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * name is too short
     */
    @Test
    public void testSetNameLengthLessThanMinFailure() throws DependencyException {

        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");

            // seting too short mission name
            JWebUnit.setTextField("missionName", "XX");
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("Mission name must be between five and fifty characters!");

        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * name is too long
     */
    @Test
    public void testSetNameLengthGreaterThanMaxFailure() throws DependencyException {

        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");

            // seting too short mission name
            JWebUnit.setTextField("missionName", "XXsgdsadsggsakjdgaskjdgsakjdgsakjdgasjkdgsakdgsakdgsakdgsakdgask");
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("Mission name must be between five and fifty characters!");
        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * time is not set
     */
    @Test
    public void testMissionTimeNotSetFailure() throws DependencyException {

        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");

            JWebUnit.setTextField("missionName", mission.getName());
            //JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("Wrong date format: : Unparseable date");

        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * location is not set
     */
    @Test
    public void testMissionLocationNotSetFailure() throws DependencyException {

        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");

            JWebUnit.setTextField("missionName", mission.getName());
            JWebUnit.setTextField("time", mission.getTime().toString());

            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("The location id must be entered!");

        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * location ID is not existing failure
     */
    @Test
    public void testMissionLocationNonExisitingFailure() throws DependencyException {
        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");

            JWebUnit.setTextField("missionName", mission.getName());
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", "111");
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("The location id does not exist. Check your input!");

        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Verify mission
     * description is not set
     */
    @Test
    public void testMissionDescriptionNotSetFailure() throws DependencyException {

        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");

            JWebUnit.setTextField("missionName", mission.getName());
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            // JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify error message of mission name
            JWebUnit.assertTextPresent("Mission description must be at least twenty-five and at most three-hundred characters.");
        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from register page (baseUrl/register) --> login
     * page --> user page Verify mission details
     */
    @Test
    public void testValidMissionStartingFromRegisterPageSuccess() throws DependencyException {

        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/register");
            JWebUnit.setTextField("userName", person.getUserName());
            JWebUnit.setTextField("email", person.getEmail());
            JWebUnit.setTextField("password", VALID_PASSWORD);
            JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
            JWebUnit.setTextField("firstName", person.getFirstName());
            JWebUnit.setTextField("lastName", person.getLastName());
            JWebUnit.clickButtonWithText("Create New User");

            //go to login page
            JWebUnit.gotoPage("/login");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //    create mission
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");
            JWebUnit.setTextField("missionName", mission.getName());
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getId().toString());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify mission details
            JWebUnit.gotoPage("/mission/" + mission.getId());
            JWebUnit.assertTextPresent("Mission Details");
            JWebUnit.assertTextPresent("Name: " + mission.getName());
            JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
            JWebUnit.assertTextPresent("Description: " + mission.getDescription());
        } finally {
            deleter.deleteMission(mission);
        }
    }

    /**
     * Test creating mission from user login page (baseUrl/login) Click on user
     * - setting link Verify Mission details and mission is displayed under
     * "missions created by me" section
     */
    @Test
    public void testValidMissionWithLoginUserSettingPageSuccess() throws DependencyException {
        Mission mission = factory.createMission(person, location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Wonderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  create mission
            JWebUnit.clickLinkWithText(person.getUserName() + " - Settings");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.clickLinkWithText("Create Mission");
            JWebUnit.gotoPage("/mission/create");
            JWebUnit.setTextField("missionName", mission.getName());
            JWebUnit.setTextField("time", mission.getTime().toString());
            JWebUnit.setTextField("location", mission.getLocation().getTown());
            JWebUnit.setTextField("description", mission.getDescription());
            JWebUnit.clickButtonWithText("Create New Mission");
            //verify mission details
            JWebUnit.gotoPage("/mission/" + mission.getId());
            JWebUnit.assertTextPresent("Mission Details");
            JWebUnit.assertTextPresent("Name: " + mission.getName());
            JWebUnit.assertTextPresent("Location: " + mission.getLocation().getTown());
            JWebUnit.assertTextPresent("Description: " + mission.getDescription());
            //go back to user/test page
            JWebUnit.clickLinkWithText(person.getUserName() + " - Settings");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Missions created by me:");
            JWebUnit.assertTextPresent(mission.getName());
        } finally {
            deleter.deleteMission(mission);
        }
    }
}

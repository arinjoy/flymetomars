package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.SaltedPassword;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class MissionResourceSystemTest extends AbstractDataDependantResourceSystemTest {
    
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

    public MissionResourceSystemTest() {
        super(constructCount++);
    }
    private static int testCount;

    @BeforeClass
    public static void getTestCount() {
        Class<MissionResourceSystemTest> me = MissionResourceSystemTest.class;
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
            removeLocations();
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to remove existing locations", e);
            throw new IllegalStateException(e);
        }
        try {
            saltedPass = factory.createSaltedPassword(factory.createSalt(""));
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
            person = factory.createPerson(saltedPass);
            person.setEmail("barfoo@gmail.com");
            person.setUserName("foobar_22");
            person.setFirstName("HooGoo");
            person.setLastName("FooGoo");
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
     * Test creating a mission from Home page (baseUrl) --> login page --> user page
     * then Verify Mission details
     */
    @Test
    public void testMissionPageStartingFromHomePage() throws DependencyException {
        Mission mission = factory.createMission(person, location);
        mission.setCaptain(person);
        mission.setLocation(location);
        mission.setTime(Calendar.getInstance().getTime());
        mission.setDescription("Woderful Mission of your life");
        mission.setName("Greatest Mission");
        mission = saver.saveMission(mission);
        //test
        JWebUnit.beginAt("");
        JWebUnit.assertTextPresent("Fly me to Mars - a mission registration system.");
        JWebUnit.clickLinkWithText("Login");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        //JWebUnit.gotoPage("");
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        //create mission
        JWebUnit.clickLinkWithText("User page");
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
    }
    
    /**
     * 
     * Verify that mission id does not exist
     */
    @Test
    public void testUnkownMission() {
        int missionId = 001;
        JWebUnit.beginAt("/mission/" + missionId);
        JWebUnit.assertTextPresent("Cannot find mission with id: " + missionId);
    }
    
}

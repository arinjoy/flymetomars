package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
 * Extension to the UserResourceSystemTest which specifically tests some of the
 * mining functionality introduced via business layer implementations
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class MiningSystemTest extends AbstractDataDependantResourceSystemTest {
    
    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityUpdater updater;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;

    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    public static int cadinal=0;
    public MiningSystemTest() {
        super(cadinal);
        cadinal++;
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<MiningSystemTest> me=MiningSystemTest.class;
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
       
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        String port = getProperty("port.number");
        JWebUnit.setBaseUrl("http://localhost:" + port + "/flymetomars");
        JWebUnit.setScriptingEnabled(false);
        try {
            removePeople();
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist password errata", e);
            throw new IllegalStateException(e);
        }
        saveData();
    }
    
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
    
    private static final String VALID_PASSWORD = "ABcd@1234";

    private Person createPerson1() {
        Person p = factory.createPerson(makeSalt());
        p.setEmail("mister_hills@harry.net");
        p.setUserName("harry_21");
        p.setFirstName("Harry");
        p.setLastName("Hills");
        return p;
    }

    private Person createPerson2() {
        Person p = factory.createPerson(makeSalt());
        p.setEmail("merryanne.miller@myspace.me");
        p.setUserName("merry_21");
        p.setFirstName("Merry");
        p.setLastName("Mills");
        return p;
    }

    private Person createPerson3() {
        Person p = factory.createPerson(makeSalt());
        p.setEmail("kelly@kills.u");
        p.setUserName("kelly_21");
        p.setFirstName("Kelly");
        p.setLastName("Kills");
        return p;
    }

    private Person createPerson4() {
        Person p = factory.createPerson(makeSalt());
        p.setEmail("medicated-perry@iLovePills.org");
        p.setUserName("perry_21");
        p.setFirstName("Perry");
        p.setLastName("Pills");
        return p;
    }

    private Person createPerson5() {
        Person p = factory.createPerson(makeSalt());
        p.setEmail("Tom@TomandJerry.net.nz");
        p.setUserName("terry_21");
        p.setFirstName("Terry");
        p.setLastName("Toms");
        return p;
    }
    
    private SaltedPassword makeSalt() {
        SaltedPassword pass=null;
        try {
            pass=factory.createSaltedPassword(new Salt());
            saver.saveSalt(pass.getSalt());
            pass.setPassword(VALID_PASSWORD);
            saver.saveSaltedPassword(pass);           
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist password errata", e);
            throw new IllegalStateException(e);
        }
        return pass;
    }

    private Location createLocation() {
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
    
    private static Location location;
    private static Mission missionA;
    private static Mission missionB;
    private static Person[] people;
    
    public void saveData() {if(people==null) {
        //make location
        try {
            location = saver.saveLocation(createLocation());
        } catch(DependencyException de) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist location data", de);
            throw new IllegalStateException(de);
        }
        //make people
        makeSalt();
        people=new Person[] {
            createPerson1(),
            createPerson2(),
            createPerson3(),
            createPerson4(),
            createPerson5()
        };
        //save people
        try {
            for(int o=0;o<people.length;o++) {
                people[o]=saver.savePerson(people[o]);
            }
        } catch(DependencyException de) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist required persons", de);
            throw new IllegalStateException(de);
        }
        //make missions
        Calendar cal=Calendar.getInstance();
        // p1 is the captain of two missions m1 and m2
        missionA = factory.createMission(people[0], location);
        missionA.setTime(new Date(cal.getTimeInMillis()+2000L));
        missionA.setDescription("Woderful Mission of your life");
        missionA.setName("Greatest Mission");
        missionB = factory.createMission(people[0], location);
        missionB.setTime(new Date(cal.getTimeInMillis()+10000L));
        missionB.setDescription("The best mission that you can ever imagine");
        missionB.setName("The greater one");
        //save missions
        try {
            missionA=saver.saveMission(missionA);
            missionB=saver.saveMission(missionB);
        } catch(DependencyException de) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist required missions", de);
            throw new IllegalStateException(de);
        }
        //participation
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(missionA);
        mSet.add(missionB);
        //p2 registered in m1 and m2
        people[1].setMissionsRegistered(mSet);
        //participants
        Set<Person> pSetA = new HashSet<Person>();
        pSetA.add(people[1]);
        pSetA.add(people[2]);
        pSetA.add(people[4]);
        Set<Person> pSetB = new HashSet<Person>();
        pSetB.add(people[1]);
        pSetB.add(people[2]);
        pSetB.add(people[3]);
        missionA.setParticipantSet(pSetA);
        missionB.setParticipantSet(pSetB);
        //update missions
        try {
            missionA=updater.updateMission(missionA);
            missionB=updater.updateMission(missionB);
        } catch(DependencyException de) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to update required missions", de);
            throw new IllegalStateException(de);
        }
    }}

    @Test
    public void testVerifyCelebritiesSucsess() throws DependencyException {
        // p2 will searched for his friends for p3, p4 and p5
        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", people[1].getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + people[1].getUserName());
        JWebUnit.assertTextPresent("Welcome, " + people[1].getFirstName() + " " + people[1].getLastName());
        JWebUnit.assertTextPresent("User Details");
        JWebUnit.clickLinkWithText(people[1].getUserName() + " - Settings");
        JWebUnit.gotoPage("/user/" + people[1].getUserName());
        JWebUnit.assertTextPresent("The Celebrities are:");
        JWebUnit.assertTextPresent(people[0].getFirstName() + " " + people[0].getLastName());
        JWebUnit.assertTextPresent(people[1].getFirstName() + " " + people[1].getLastName());
        JWebUnit.assertTextPresent(people[2].getFirstName() + " " + people[2].getLastName());
    }

}

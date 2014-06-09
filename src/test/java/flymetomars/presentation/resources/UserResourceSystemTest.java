package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import flymetomars.business.model.Person;
import flymetomars.business.model.SaltedPassword;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jwebunit.exception.TestingEngineResponseException;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 * 
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class UserResourceSystemTest extends AbstractDataDependantResourceSystemTest {
    
    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;

    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    public UserResourceSystemTest() {
        super(0);
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<UserResourceSystemTest> me=UserResourceSystemTest.class;
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
    
    private SaltedPassword saltedPass;
    
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        try {
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to purge existing user data", ex);
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
    }
    
    //@AfterInstance (effectivly)
    public void doSystemWindup() {
        try {
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
        removePeople();
    }

    private static final String VALID_PASSWORD="Hahah3!45";
    
    private Person createAPerson() {
        Person p = factory.createPerson(saltedPass);
        p.setEmail("test@gmail.com");
        p.setUserName("foobar");
        p.setFirstName("Foo");
        p.setLastName("Goo");
        return p;
    }
    
    @Test
    public void testValidUserWithLogin() throws DependencyException {
        Person person = createAPerson();
        person=saver.savePerson(person);
        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");
    }

    @Test
    public void testValidUserWithoutLogin() throws DependencyException {
        Person person = createAPerson();
        person=saver.savePerson(person);
        JWebUnit.beginAt("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextNotPresent("User Details");
    }

    @Test
    public void testUnknownUser() {
        String userName = "henry" + new Random().nextLong();
        try {
            JWebUnit.beginAt("/user/" + userName);
            Assert.fail("JWebUnit call did not recieve expected HTTP error");
        } catch (TestingEngineResponseException e) {
            Assert.assertTrue(e.getMessage().contains("unexpected status code [404]"));
            Assert.assertTrue(e.getMessage().contains(userName));
        }
        //JWebUnit.assertTextPresent("Cannot find user with name: " + userName);
    }
    
}

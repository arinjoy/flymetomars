package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Person;
import flymetomars.business.model.SaltedPassword;
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
public class RegisterResourceSystemTest extends AbstractDataDependantResourceSystemTest {
     
    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;
    
    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    public RegisterResourceSystemTest() {
        super(0);
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<RegisterResourceSystemTest> me=RegisterResourceSystemTest.class;
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
        removePeople();
    }
    
    //@AfterInstance (effectivly)
    public void doSystemWindup() {
        try {
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception durig System Test Windup", ex);
            throw new UnsupportedOperationException(ex);
        }
    }
    
    private SaltedPassword saltedPass;
    
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        try {
            saltedPass=factory.createSaltedPassword(factory.createSalt("salty"));
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
    
    private static final String VALID_PASSWORD="Hahah3!45";
    
    private Person createAPerson() {
        Person p = factory.createPerson(saltedPass);
        p.setEmail("test@gmail.com");
        p.setUserName("foobar21");
        p.setFirstName("Foo");
        p.setLastName("Goo");
        return p;
    }
    
    /**
     * Testing the success case for registration
     */
    
    @Test
    public void testRegistrationSuccess() {
        Person person = this.createAPerson();
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
    }
    
     /**
     * Person already present in DB
     */
    @Test
    public void testRegistrationAlreadyRegisteredFailure() {
        Person person = this.createAPerson();
        try {
            person=saver.savePerson(person);
        } catch (DependencyException ex) {
            String msg="System Test Infrastructure issue - unable to save Person";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg, ex);
            Assert.fail(msg);
        }
        //TODO: implement me
    }
    
    /**
     * Invalid email address format
     */
    @Test
    public void testRegistrationInvalidEmailFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", "abc def@&*&*.com");
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Given E-mail address does not appear to be valid, please check the value entered and try again");
    }
    
    /**
     * Email address is too short
     */
    @Test
    public void testRegistrationTooShortEmailFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", "a@b.c");
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Invalid E-mail address: length must be at least eight andless than eighty characters to be valid. Thank you.");
    }
    
    /**
     * Email address empty
     */
    @Test
    public void testRegistrationEmptyEmailFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
       // JWebUnit.setTextField("email", "a@b.c");
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Invalid E-mail address: length must be at least eight andless than eighty characters to be valid. Thank you.");
    }
    
    /**
     * User name is empty
     */
    @Test
    public void testRegistrationEmptyUserNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        //JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("User name must be at least six and no more than twenty characters in length. Thank you for your compliance.");
    }
    
    /**
     * User name is too short
     */
    @Test
    public void testRegistrationTooShortUserNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", "ac_2");
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("User name must be at least six and no more than twenty characters in length. Thank you for your compliance.");
    }
    
    /**
     * User name is too long
     */
    @Test
    public void testRegistrationTooLongUserNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", "arinjoy_biswasnorth21arinjoy");
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("User name must be at least six and no more than twenty characters in length. Thank you for your compliance.");
    }
    
    /**
     * First name contains illegal character
     */
    @Test
    public void testRegistrationInvalidFirstNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName","a2k%sl ");
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
       // JWebUnit.assertTextPresent("Illegal characters present!");
    }
    
    /**
     * First name is empty
     */
    @Test
    public void testRegistrationEmptyFirstNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
       
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("First name of a Person cannot be empty, and must be between two and twenty-five characters in length. Thank you.");
    }
    
    /**
     * First name contains illegal character
     */
    @Test
    public void testRegistrationInvalidLastNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", "12");
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Illegal characters present! Permitted characters for the surname of a Person are upper and lower case letters, hyphen symbols and extended latin alphabet characters.");
    }
    
    /**
     * First name is empty
     */
    @Test
    public void testRegistrationEmptyLastNameFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", VALID_PASSWORD);
        JWebUnit.setTextField("firstName", person.getFirstName());
       // JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Last name of a Person cannot be empty, and must be between two and twenty-five characters in length. Thank you.");
    }
    
    /**
     * Passwords don't match
     */
    @Test
    public void testRegistrationConfirmPasswordDoesNotMatchFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", VALID_PASSWORD);
        JWebUnit.setTextField("confirmPassword", "Abcd@1234");
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Password confirmation mismatch");
    }
    
    
     /**
     * Invalid password
     */
    @Test
    public void testRegistrationInvalidPasswordFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", "12345678");
        JWebUnit.setTextField("confirmPassword", "12345678");
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Invalid Password: Password must contain at least one alphabetical character, one numeric character, and one symbol. Password must not contain spaces. Valid Symbols are:!@#$%^&*");
    }

    
    
    /**
     * Password field is too short
     */
    @Test
    public void testRegistrationTooShortPasswordFailure() {
        Person person = this.createAPerson();
        JWebUnit.beginAt("/register");
        JWebUnit.setTextField("userName", person.getUserName());
        JWebUnit.setTextField("email", person.getEmail());
        JWebUnit.setTextField("password", "ab1");
        JWebUnit.setTextField("confirmPassword", "ab1");
        JWebUnit.setTextField("firstName", person.getFirstName());
        JWebUnit.setTextField("lastName", person.getLastName());
        JWebUnit.clickButtonWithText("Create New User");      
        JWebUnit.assertTextPresent("Invalid Password: length must be at least eight and no more than twenty characters in order to be valid. Thank you.");
    }

}

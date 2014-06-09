package flymetomars.business.services;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import java.util.prefs.BackingStoreException;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 
 * 
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class RegisterServiceIntegrationClassTest {

    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;
    
    //System under test
    @Autowired
    private RegisterService serv;
    
    @Before
    public void checkOperators() throws BackingStoreException {
        Object[] core=new Object[] {
            loader, saver, deleter, factory, serv
        };
        for(Object op : core) {
            if(null==op) { throw new BackingStoreException(new NullPointerException()); }
        }
    }
    
    @After
    public void cleanUp() throws DependencyException {
        for(Person p : loader.loadAllPersons()) {
            deleter.deletePerson(p);
        }
        for(SaltedPassword sp : loader.loadAllSaltedPasswords()) {
            deleter.deleteSaltedPassword(sp);
        }
        for(Salt s : loader.loadAllSalts()) {
            deleter.deleteSalt(s);
        }
    }
    
    private static final String VALID_PASS = "P4ssw0rd!";
    private static final String USER_NAME = "freddy";
    private static final String FIRST_NAME = "Fred";
    private static final String LAST_NAME = "Smith";
    private static final String EMAIL_ADDR = "fred@example.net";
    
    @Test
    public void testRegisterServiceRegisterBasicSuccess() throws PersonAlreadyExistsException, DependencyException {
        Person result=this.serv.register(
            USER_NAME, EMAIL_ADDR,
            VALID_PASS, VALID_PASS,
            FIRST_NAME, LAST_NAME
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getFirstName(), FIRST_NAME);
        Assert.assertEquals(result.getLastName(), LAST_NAME);
        Assert.assertEquals(result.getUserName(), USER_NAME);
        Assert.assertTrue(result.isPassword(VALID_PASS));
        Assert.assertNotNull(result.getId());
        Person verify=loader.loadPersonByEmail(EMAIL_ADDR);
        Assert.assertNotNull(verify);
        Assert.assertEquals(result, verify);
        verify=loader.loadPersonByUserName(USER_NAME);
        Assert.assertNotNull(verify);
        Assert.assertEquals(result, verify);
        Assert.assertEquals(verify.getFirstName(), FIRST_NAME);
        Assert.assertEquals(verify.getLastName(), LAST_NAME);
        Assert.assertEquals(verify.getUserName(), USER_NAME);
        Assert.assertTrue(verify.isPassword(VALID_PASS));
    }
    
    @Test(expected = PersonAlreadyExistsException.class)
    public void testRegisterServiceAlreadyRegisteredFailure() throws DependencyException, PersonAlreadyExistsException {
        Person here=factory.createPerson(factory.createSaltedPassword(factory.createSalt("Rock Sal7")));
        saver.saveSalt(here.getPassword().getSalt());
        here.getPassword().setPassword(VALID_PASS);
        saver.saveSaltedPassword(here.getPassword());
        here.setFirstName(FIRST_NAME);
        here.setLastName(LAST_NAME);
        here.setUserName(USER_NAME);
        here.setEmail(EMAIL_ADDR);
        here=saver.savePerson(here);
        Assert.assertNotNull(here);
        Assert.assertEquals(here.getFirstName(), FIRST_NAME);
        Assert.assertEquals(here.getLastName(), LAST_NAME);
        Assert.assertEquals(here.getUserName(), USER_NAME);
        Assert.assertTrue(here.isPassword(VALID_PASS));
        Person there=this.serv.register(
            USER_NAME, EMAIL_ADDR,
            VALID_PASS, VALID_PASS,
            FIRST_NAME, LAST_NAME
        );
        Assert.assertNull(there);
    }
    
}

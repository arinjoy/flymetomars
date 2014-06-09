package flymetomars.business.services;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.PasswordHandler;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.common.NullArgumentException;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for the different modes of operation for the AuthService
 * 
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class
})
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AuthServiceIntegrationClassTest {
    
    //<editor-fold defaultstate="collapsed" desc="DAO dependencies for Loader">
    @Autowired
    private PersonDAO personDao;
    @Autowired
    private SaltedPasswordDAO passwordDao;
    @Autowired
    private SaltDAO saltDao;
    //</editor-fold>
    
    //Loader dependency for Service
    private EntityLoader loader;
    
    //Saver dependency for Testing
    private EntitySaver saver;
    
    //Factory dependency for Testing
    private EntityFactory factory;
    
    //Updater dependency for Testing
    private EntityUpdater updater;
    
    //Handler dependency for Service
    private PasswordHandler handler;
    
    private int runCount=0;
    
    @Before
    public void init() {
        if(null==loader) {
            loader=new EntityLoader(personDao,null,null,null,null,passwordDao,saltDao);
        }
        if(null==saver) {
            saver=new EntitySaver(personDao,null,null,null,null,passwordDao,saltDao);
        }
        if(null==handler) {
            handler=new PasswordHandler(loader);
        }
        if(null==factory) {
            factory=new EntityFactory();
        }
        if(null==updater) {
            updater=new EntityUpdater(personDao,null,null,null,null,passwordDao,saltDao);
        }
        if(runCount++==0) { clearSalts(); }
    }
    @After
    public void clearSalts() {
        for(flymetomars.common.datatransfer.Salt s : saltDao.getAll()) {
            saltDao.delete(s);
        }
    }
    
    private Person user;
    private String passwordText="aBc12#_f12";
    
    @Before
    public void setUp() throws DependencyException {
        if(null==factory) { init(); }
        Salt salt=saver.saveSalt(handler.randomSalt());
        SaltedPassword pass=factory.createSaltedPassword(salt);
        pass.setPassword(passwordText);
        pass=saver.saveSaltedPassword(pass);
        user=factory.createPerson(pass);
        user.setEmail("user@test.net");
        user.setUserName("thatGuy");
        user.setFirstName("that");
        user.setLastName("guy");
        user=saver.savePerson(user);
        user=loader.loadPersonByUserName(user.getUserName());
    }
    
    @Test
    public void testAuthenticateWithLevelingSuccess() throws NoSuchPersonExistsException {
        AuthService instance = new AuthServiceImpl(loader, saver, updater, handler);
        Assert.assertTrue(instance.isLevelling());
        boolean result=instance.authenticate(user.getUserName(), passwordText);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testAuthenticateWithLevelingInvalidSuccess() throws NoSuchPersonExistsException {
        AuthService instance = new AuthServiceImpl(loader, saver, updater, handler);
        Assert.assertTrue(instance.isLevelling());
        boolean result=instance.authenticate(user.getUserName(), "cheeseybacon");
        Assert.assertFalse(result);
    }
    
    @Test(expected = NoSuchPersonExistsException.class)
    public void testAuthenticateWithLevelingBadUserFailure() throws NoSuchPersonExistsException {
        AuthService instance = new AuthServiceImpl(loader, saver, updater, handler);
        Assert.assertTrue(instance.isLevelling());
        boolean result=instance.authenticate("fred", passwordText);
        Assert.assertFalse(result);
    }
    
    //<editor-fold defaultstate="collapsed" desc="constructor argument null-check test cases">
    @Test(expected = NullArgumentException.class)
    public void testConstructWithIllogicalNullSaverFailure() {
        AuthService dummy = new AuthServiceImpl(loader, null, updater, handler);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructWithIllogicalNullUpdaterFailure() {
        AuthService dummy = new AuthServiceImpl(loader, saver, null, handler);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructWithIllogicalNullHandlerFailure() {
        AuthService dummy = new AuthServiceImpl(loader, saver, updater, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructWithOnlyHandlerFailure() {
        AuthService dummy = new AuthServiceImpl(loader, null, null, handler);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructWithOnlySaverFailure() {
        AuthService dummy = new AuthServiceImpl(loader, saver, null, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructWithOnlyUpdaterFailure() {
        AuthService dummy = new AuthServiceImpl(loader, null, updater, null);
    }
    //</editor-fold>
    
    @Test
    public void testAuthenticateNoLevelingSuccess() throws NoSuchPersonExistsException {
        AuthService instance = new AuthServiceImpl(loader);
        Assert.assertFalse(instance.isLevelling());
        boolean result=instance.authenticate(user.getUserName(), passwordText);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testAuthenticateNoLevelingInvalidSuccess() throws NoSuchPersonExistsException {
        AuthService instance = new AuthServiceImpl(loader);
        Assert.assertFalse(instance.isLevelling());
        boolean result=instance.authenticate(user.getUserName(), "cheeseybacon");
        Assert.assertFalse(result);
    }
    
    @Test(expected = NoSuchPersonExistsException.class)
    public void testAuthenticateNoLevelingBadUserFailure() throws NoSuchPersonExistsException {
        AuthService instance = new AuthServiceImpl(loader);
        Assert.assertFalse(instance.isLevelling());
        boolean result=instance.authenticate("fred", passwordText);
        Assert.assertFalse(result);
    }
    
}

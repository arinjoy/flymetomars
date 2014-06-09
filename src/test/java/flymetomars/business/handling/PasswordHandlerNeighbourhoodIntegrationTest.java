package flymetomars.business.handling;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
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
 * Neighbourhood style integration test for the features of the PasswordHandler
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
public class PasswordHandlerNeighbourhoodIntegrationTest {
    
    //<editor-fold defaultstate="collapsed" desc="DAO dependencies for Loader">
    @Autowired
    private PersonDAO personDao;
    @Autowired
    private SaltedPasswordDAO passwordDao;
    @Autowired
    private SaltDAO saltDao;
    //</editor-fold>
    
    //Loader dependency for Handler
    private EntityLoader loader;
    
    //Saver dependency for Testing
    private EntitySaver saver;
    
    //Factory dependency for Testing
    private EntityFactory factory;
    
    //Updater dependency for Testing
    private EntityUpdater updater;
    
    //System under test - Handler
    private PasswordHandler handler;
    
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
        //clearSalts();
    }
    @After
    public void clearSalts() {
        for(flymetomars.common.datatransfer.Salt s : saltDao.getAll()) {
            saltDao.delete(s);
        }
    }
    
    private Salt alreadyInUse;
    private int saltUseLimit=6; //should always be 1 plus MAX_SHARING_SAME_SALT
    private Person user;
    private String passwordText="aBc12#_f12";
    
    @Before
    public void setUp() throws DependencyException {
        if(null==factory) { init(); }
        alreadyInUse=saver.saveSalt(handler.randomSalt());
        SaltedPassword pass=null;
        for(int s=0;s<saltUseLimit;s++) {
            pass=factory.createSaltedPassword(alreadyInUse);
            pass.setPassword(passwordText);
            pass=saver.saveSaltedPassword(pass);
        }
        user=factory.createPerson(pass);
        user.setEmail("user@test.net");
        user.setUserName("thatGuy");
        user.setFirstName("that");
        user.setLastName("guy");
        user=saver.savePerson(user);
    }

    //<editor-fold desc="changePassword test cases">
    
    @Test
    public void testChangePasswordSuccess() throws DependencyException {
        String pass="n3wP4ssw0rd!";
        handler.changePassword(user, passwordText, pass);
        saltDao.saveOrUpdate(user.getPassword().getSalt().toDTO());
        updater.updateSaltedPassword(user.getPassword());
        Assert.assertTrue(user.isPassword(pass));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testChangePasswordNullPersonFailure() {
        handler.changePassword(null, passwordText, passwordText);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testChangePasswordNullCurrentPasswordFailure() {
        handler.changePassword(user, null, passwordText);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testChangePasswordNullNewPasswordFailure() {
        handler.changePassword(user, passwordText, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordNullPasswordSaltFailure() {
        handler.changePassword(new Person(), passwordText, passwordText);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordWrongPasswordFailure() {
        handler.changePassword(user, "p4sSw0r@", passwordText);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordIllegalNewPasswordFailure() {
        handler.changePassword(user, passwordText, "cheese");
    }
    
    //</editor-fold>

    
    //<editor-fold desc="levelSalt test cases">
    
    @Test
    public void testLevelSaltLevelingRequiredSuccess() {
        Assert.assertTrue(handler.levelSalt(user, passwordText));
        Assert.assertNotEquals(alreadyInUse, user.getPassword().getSalt());
    }
    
    @Test
    public void testLevelSaltNoActualLevelingSuccess() throws DependencyException {
        Salt mysteriousSalt=factory.createSalt("Pancakes!");
        handler.changeSalt(user, passwordText, mysteriousSalt);
        saver.saveSalt(mysteriousSalt);
        updater.updateSaltedPassword(user.getPassword());
        Assert.assertFalse(handler.levelSalt(user, passwordText));
        Assert.assertEquals(mysteriousSalt, user.getPassword().getSalt());
        Assert.assertNotEquals(mysteriousSalt, alreadyInUse);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testLevelSaltNullPersonFailure() {
        handler.levelSalt(null, passwordText);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testLevelSaltNullPasswordFailure() {
        handler.levelSalt(user, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLevelSaltNullPersonSaltFailure() {
        handler.levelSalt(new Person(), passwordText);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLevelSaltWrongPasswordFailure() {
        handler.levelSalt(user, "pizza");
    }
    
    //</editor-fold>

    
    //<editor-fold desc="changeSalt test cases">
    
    @Test
    public void testChangeSaltSuccess() {
        Salt salt=handler.randomSalt();
        handler.changeSalt(user, passwordText, salt);
        Assert.assertTrue(user.isPassword(passwordText));
        Assert.assertEquals(salt, user.getPassword().getSalt());
    }
    
    @Test
    public void testChangeSaltSameSaltSuccess() {
        handler.changeSalt(user, passwordText, user.getPassword().getSalt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChangeSaltWrongPasswordFailure() {
        Salt salt=handler.randomSalt();
        handler.changeSalt(user, "mustard", salt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChangeSaltNoSaltSetFailure() {
        handler.changeSalt(new Person(), passwordText, alreadyInUse);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testChangeSalNullSaltFailure() {
        handler.changeSalt(user, passwordText, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testChangeSalNullPasswordFailure() {
        handler.changeSalt(user, null, alreadyInUse);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testChangeSalNullPersonFailure() {
        handler.changeSalt(null, passwordText, alreadyInUse);
    }
    
    //</editor-fold>
    

    //<editor-fold desc="randomSalt test cases">
    
    @Test
    public void testRandomSaltNewSaltSuccess() {
        Salt salt=handler.randomSalt();
        Assert.assertNotNull(salt);
        Assert.assertFalse(loader.loadAllSalts().contains(salt));
    }
    
    @Test
    public void testRandomSaltLoadSaltSuccess() throws DependencyException {
        Salt mysteriousSalt=factory.createSalt("Pancakes!");
        saver.saveSalt(mysteriousSalt);
        Salt salt=handler.randomSalt();
        Assert.assertNotNull(salt.getHashedSaltKey());
        Assert.assertNotNull(salt);
        Assert.assertEquals(mysteriousSalt, salt);
    }
    
    //</editor-fold>
    
}

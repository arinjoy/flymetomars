package flymetomars.business.services;

import flymetomars.business.handling.PasswordService;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.PasswordHandler;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import org.junit.Test;
import org.junit.Assert;
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
public class ServiceLocatorNeighbourhoodIntegrationClassTest {
    
    
    //<editor-fold defaultstate="collapsed" desc="DAO dependencies for Core operators">
    @Autowired
    private PersonDAO personDao;
    @Autowired
    private MissionDAO missionDao;
    @Autowired
    private InvitationDAO invitationDao;
    @Autowired
    private LocationDAO locationDao;
    @Autowired
    private ExpertiseDAO expertiseDao;
    @Autowired
    private SaltedPasswordDAO passwordDao;
    @Autowired
    private SaltDAO saltDao;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Core operator dependencies for services">
    private EntityLoader loader=new EntityLoader(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao);
    private EntitySaver saver=new EntitySaver(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao);
    private EntityUpdater updater=new EntityUpdater(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao);
    private EntityDeleter deleter=new EntityDeleter(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao);
    private EntityFactory factory=new EntityFactory();
    //</editor-fold>
    
    @Test
    public void testFetchAuthServiceSuccess() {
        AuthService result = ServiceLocator.fetch(
            AuthService.class,
            loader,saver,updater,deleter,factory
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AuthServiceImpl);
    }
    
    @Test
    public void testFetchPassServiceSuccess() {
        PasswordService result = ServiceLocator.fetch(
            PasswordService.class,
            loader,saver,updater,deleter,factory
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof PasswordHandler);
    }
    
    @Test
    public void testFetchRegServiceSuccess() {
        RegisterService result = ServiceLocator.fetch(
            RegisterService.class,
            loader,saver,updater,deleter,factory
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof RegisterServiceImpl);
    }
    
}

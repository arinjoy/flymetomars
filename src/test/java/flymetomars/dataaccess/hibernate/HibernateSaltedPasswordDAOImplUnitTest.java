package flymetomars.dataaccess.hibernate;

import flymetomars.common.datatransfer.Salt;
import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    DependencyInjectionTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@FixMethodOrder
@Transactional
public class HibernateSaltedPasswordDAOImplUnitTest {
    
    @Autowired
    private SaltedPasswordDAO dao;
    
    @Autowired
    private SaltDAO saltDao;
    
    private SaltedPassword pass;
    
    @MockitoAnnotations.Mock
    private Salt salt;
    
    @Before
    public void configureMocks() {
        MockitoAnnotations.initMocks(this);
        Mockito.stub(this.salt.getObfuscatedSalt()).toReturn("VGhpcyBpcyBzb21lIHBhc3N3b3JkIHNhbHQgNTY3OA==");  //This is some password salt 5678
        Mockito.stub(this.salt.getHashedSaltKey()).toReturn("414243"); //ABC
    }
    
    @Before
    public void configureDTOs() throws ClassNotFoundException {
        try {
            this.pass=DTOFactoryImpl.getInstance().createDTO(SaltedPassword.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernateSaltedPasswordDAOImplUnitTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        }
    }
    
    @Before
    public void setUp() {
        for(Salt s : saltDao.getAll()) {
            saltDao.delete(s);
        }
    }
    
    @After
    public void tearDown() {
        for(Salt s : saltDao.getAll()) {
            saltDao.delete(s);
        }
    }

    @Test
    public void testGetSaltedPasswordsSharingSameSaltEmptySuccess() {
        List result = dao.getSaltedPasswordsSharingSameSalt(salt);
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testGetSaltedPasswordsSharingSameSaltSuccess() {
        pass.setSaltId(salt.getHashedSaltKey());
        pass.setDigest("755f7f1fa8ccab14eabe69a7112732f3"); //p@sswOrd1This is some password salt 5678
        //pass.setDigest("69a6b310223aaf308c72f10503b06eda"); //p@sswOrd1
        dao.save(pass);
        List result = dao.getSaltedPasswordsSharingSameSalt(salt);
        Assert.assertTrue(result.contains(pass));
        Mockito.stub(this.salt.getHashedSaltKey()).toReturn("1234");
        result = dao.getSaltedPasswordsSharingSameSalt(salt);
        Assert.assertFalse(result.contains(pass));
    }
    
    @Test
    public void testSaveAndLoadSuccess() {
        saltDao.save(salt);
        pass.setSaltId(salt.getHashedSaltKey());
        pass.setDigest("56bc6bf0d0fd0521fe9269d022e1b93f"); //5up3R_S3cret!
        dao.save(pass);
        Assert.assertTrue(dao.getAll().contains(pass));
        SaltedPassword loadPass=dao.load(pass.getId());
        Assert.assertEquals(pass,loadPass);
    }
}

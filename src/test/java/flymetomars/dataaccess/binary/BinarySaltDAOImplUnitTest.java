package flymetomars.dataaccess.binary;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Salt;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.SaltDAO;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class
})
@FixMethodOrder
public class BinarySaltDAOImplUnitTest {
    
    @Autowired
    private SaltDAO saltDao;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private Salt makeSalt() {
        try {
            Salt result=this.dtoFactory.createDTO(Salt.class);
            result.setHashedSaltKey("AABBCC11449321");
            result.setObfuscatedSalt("VGhpcyBpcyBhIHBhc3N3b3JkIHNhbHQu"); //This is a password salt.
            return result;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BinarySaltDAOImplUnitTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
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

    //<editor-fold desc="save test cases">
    
    @Test
    public void testSaveSuccess() {
        Salt ajax=this.makeSalt();
        ajax.setHashedSaltKey("AABBCC");
        ajax.setObfuscatedSalt("");
        try {
            saltDao.save(ajax);
        } finally {
            saltDao.delete(ajax);
        }
    }
    
    @Test(expected = RejectedExecutionException.class)
    public void testSaveAlreadySavedFailure() {
        Salt ajax=this.makeSalt();
        ajax.setHashedSaltKey("AABBCC");
        ajax.setObfuscatedSalt("");
        try {
            saltDao.save(ajax);
            saltDao.save(ajax);
        } finally {
            saltDao.delete(ajax);
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveNullFailure() {
        saltDao.save(null);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="load test cases">
    
    @Test
    public void testLoadSuccess() {
        Salt theSalt=this.makeSalt();
        theSalt.setHashedSaltKey("AABBCC");
        saltDao.save(theSalt);
        Salt foundMe = saltDao.load("AABBCC");
        Assert.assertNotNull(foundMe);
        Assert.assertEquals("AABBCC", foundMe.getHashedSaltKey());
        saltDao.delete(theSalt);
    }
    
    @Test
    public void testLoadNotExistantSuccess() {
        Assert.assertNull(saltDao.load("foobar"));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testLoadNullFailure() {
        saltDao.load(null);
    }
    
    //</editor-fold>

    //<editor-fold desc="getAll test cases">
    
    @Test
    public void testGetAllSuccess() {
        Salt s1=this.makeSalt();
        s1.setHashedSaltKey("AABBCC");
        saltDao.save(s1);
        Salt s2=this.makeSalt();
        s2.setHashedSaltKey("ABCDEF");
        s2.getObfuscatedSalt();
        saltDao.save(s2);
        List<Salt> result = saltDao.getAll();
        Assert.assertTrue(result.contains(s1));
        Assert.assertTrue(result.contains(s2));
    }
    
    @Test
    public void testGetAllEmptySuccess() {
        List<Salt> result = saltDao.getAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(0,result.size());
    }
    
    //</editor-fold>

    //<editor-fold desc="update test cases">
    
    @Test
    public void testUpdateSuccess() {
        Salt worax=this.makeSalt();
        worax.setHashedSaltKey("FEDCBA");
        try {
            saltDao.save(worax);
        } catch(RejectedExecutionException t) {
            throw new RuntimeException("error occured in required test infrastructure - save threw throwable: "+t.getMessage(),t);
        }
        worax.setObfuscatedSalt("Rmxhc2ggR29yZG9uIGFwcHJvYWNoaW5nLi4u"); //Flash Gordon approaching...
        try {
            saltDao.update(worax);
            Salt flash=saltDao.load(worax.getHashedSaltKey());
            Assert.assertEquals(flash.getObfuscatedSalt(), worax.getObfuscatedSalt());
        } finally {
            saltDao.delete(worax);
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testUpdateNullFailure() {
        saltDao.update(null);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="saveOrUpdate test cases">
    
    @Test
    public void testSaveOrUpdateSuccess() {
        Salt borax=this.makeSalt();
        borax.setHashedSaltKey("FECDAD");
        try {
            //save test:
            saltDao.saveOrUpdate(borax);
            Salt kleenex=saltDao.load(borax.getHashedSaltKey());
            Assert.assertNotNull(kleenex);
            Assert.assertEquals(borax.getHashedSaltKey(), kleenex.getHashedSaltKey());
            Assert.assertEquals(borax.getObfuscatedSalt(), kleenex.getObfuscatedSalt());
            kleenex=null;
            //update test:
            borax.setObfuscatedSalt("Rmxhc2ggR29yZG9uIGFwcHJvYWNoaW5nLi4u"); //Flash Gordon approaching...
            saltDao.saveOrUpdate(borax);
            kleenex=saltDao.load(borax.getHashedSaltKey());
            Assert.assertEquals(borax.getObfuscatedSalt(), kleenex.getObfuscatedSalt());
        } finally {
            saltDao.delete(borax);
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveOrUpdateNullFailure() {
        saltDao.saveOrUpdate(null);
    }
    
    //</editor-fold>

    //<editor-fold desc="delete test cases">

    @Test
    public void testDeleteSuccess() {
        Salt chicken = this.makeSalt();
        chicken.setHashedSaltKey("CACA");
        chicken.setObfuscatedSalt("SSBsYXllZCBhbiBlZ2ch"); //I layed an egg!
        try {
            saltDao.save(chicken);
        } catch(Throwable t) {
            throw new IllegalStateException("error occured in required test infrastructure - save threw throwable: "+t.getMessage(),t);
        }
        Salt rooster=saltDao.load(chicken.getHashedSaltKey());
        Assert.assertNotNull(rooster);
        Assert.assertEquals(chicken.getHashedSaltKey(), rooster.getHashedSaltKey());
        Assert.assertEquals(chicken.getObfuscatedSalt(), rooster.getObfuscatedSalt());
        saltDao.delete(chicken);
        Assert.assertEquals(0, saltDao.getAll().size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteNotExistFailure() {
        saltDao.delete(this.makeSalt());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDeleteNullFailure() {
        saltDao.delete(null);
    }
    
    //</editor-fold>
}

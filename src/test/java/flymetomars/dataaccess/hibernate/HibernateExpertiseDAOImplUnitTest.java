package flymetomars.dataaccess.hibernate;

import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({TransactionalTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@FixMethodOrder
public class HibernateExpertiseDAOImplUnitTest {
    
    @Autowired
    private ExpertiseDAO expertiseDao;
    
    @Autowired
    private PersonDAO personDao;
    
    @Autowired
    private SaltedPasswordDAO passwordDao;
    
    private Person p;
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    @Before
    @Transactional
    public void setUp() throws ClassNotFoundException {
        p=dtoFactory.createDTO(Person.class);
        p.setEmail("john@skilled.org");
        p.setUserName("CleverJohn88");
        p.setFirstName("John");
        p.setLastName("RamboSon");
        SaltedPassword pass = dtoFactory.createDTO(SaltedPassword.class);
        pass.setSaltId("AABBCCDD");
        pass.setDigest("1234567890ABCDEF1234567890ABCDEF");
        passwordDao.saveOrUpdate(pass);
        p.setPassword(pass.getId());
        personDao.save(p);
    }
    
    private Expertise makeExpertise() {
        try {
            return this.dtoFactory.createDTO(Expertise.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernateExpertiseDAOImplUnitTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    
    @Test
    @Transactional
    public void testInsertExpertiseSuccess() {
        Expertise e = makeExpertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setHeldBy(p);
        try {
            expertiseDao.save(e);
            Expertise exp = expertiseDao.getExpertiseListByName(e.getName()).get(0);
            Assert.assertNotNull(exp);
            Assert.assertEquals(e.getName(),exp.getName());
            Assert.assertEquals(e.getDescription(),exp.getDescription());
            Assert.assertEquals(e.getLevel(),exp.getLevel());
        } finally {
            if(null!=e && null!=e.getId()) { expertiseDao.delete(e); }
       }
    }
     
    @Test
    @Transactional
    public void testUpdateExpertiseNameSuccess() {
        Expertise e = makeExpertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setHeldBy(p);
        expertiseDao.save(e);
        try {
            e.setName("Snorkelling");
            expertiseDao.save(e);
            Expertise exp = expertiseDao.getExpertiseListByName(e.getName()).get(0);
            Assert.assertNotNull(exp);
            Assert.assertEquals(e.getName(),exp.getName());
            Assert.assertEquals(e.getLevel(),exp.getLevel());      
        } finally {
            if(null!=e && null!=e.getId()) { expertiseDao.delete(e); }
        }
    }
    
    @Test
    @Transactional
    public void testUpdateExpertiseLevelSuccess() {
        Expertise e = makeExpertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setHeldBy(p);
        try {
            expertiseDao.save(e);
            e.setLevel(Expertise.ExpertiseLevel.AMATURE);
            expertiseDao.save(e);         
            Set<Expertise> named=new HashSet<Expertise>(expertiseDao.getExpertiseListByName(e.getName()));
            Set<Expertise> level=new HashSet<Expertise>(expertiseDao.getExpertiseListByLevel(e.getLevel()));
            Assert.assertFalse(Collections.disjoint(named, level));
            Set<Expertise> namedLevel=new HashSet<Expertise>(named);
            namedLevel.retainAll(level);
            Assert.assertFalse(namedLevel.isEmpty());
            Expertise exp = namedLevel.iterator().next();
            Assert.assertNotNull(exp);
            Assert.assertEquals(e.getName(),exp.getName());
            Assert.assertEquals(e.getLevel(),exp.getLevel());      
        } finally {
            if(null!=e && null!=e.getId()) { expertiseDao.delete(e); }
        }
    }

    @Test
    @Transactional
    public void testDeleteExpertiseSuccess() {
        Expertise e = makeExpertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setHeldBy(p);
        try {
            expertiseDao.save(e);
            expertiseDao.delete(e);
            Assert.assertFalse(expertiseDao.getExpertiseListByName(e.getName()).contains(e));      
        } finally {
            if(null!=e && null!=e.getId()) { expertiseDao.delete(e); }
       }
    }
    
    
    @Test
    @Transactional
    public void testGetExpertiseListByNameSuccess() {
        Expertise e1 = makeExpertise();
        Expertise e2 = makeExpertise();
        Expertise e3 = makeExpertise();
        try{
        e1.setName("Scuba Diving");
        e1.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e1.setLevel(Expertise.ExpertiseLevel.AMATURE);
        e1.setHeldBy(p);
        expertiseDao.save(e1);
        e2.setName("Scuba Diving");
        e2.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e2.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
        e2.setHeldBy(p);
        expertiseDao.save(e2);   
        e3.setName("Mountain Biking");
        e3.setDescription("Someone who can run bikes over rough and touch mountain surfaces and jump all over");
        e3.setLevel(Expertise.ExpertiseLevel.SEMINAL);
        e3.setHeldBy(p);
        expertiseDao.save(e3); 
        List<Expertise> list = expertiseDao.getExpertiseListByName(e1.getName());
        Assert.assertEquals(list.get(0).getName(), e1.getName());
        Assert.assertEquals(list.get(1).getName(), e2.getName());
        } finally {
            if(e1!=null && null!=e1.getId()) { expertiseDao.delete(e1); }
            if(e2!=null && null!=e2.getId()) { expertiseDao.delete(e2); }
            if(e3!=null && null!=e3.getId()) { expertiseDao.delete(e3); }
        }    
     }

    @Test
    @Transactional
    public void testGetExpertiseListByNameDoesNotExistSuccess() {
        Expertise e1 = makeExpertise();
        Expertise e2 = makeExpertise();
        Expertise e3 = makeExpertise();
        try{
            e1.setName("Scuba Diving");
            e1.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
            e1.setLevel(Expertise.ExpertiseLevel.AMATURE);
            e1.setHeldBy(p);
            expertiseDao.save(e1);
            e2.setName("Scuba Diving");
            e2.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
            e2.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
            e2.setHeldBy(p);
            expertiseDao.save(e2);   
            e3.setName("Mountain Biking");
            e3.setDescription("Someone who can run bikes over rough and touch mountain surfaces and jump all over");
            e3.setLevel(Expertise.ExpertiseLevel.SEMINAL);
            e3.setHeldBy(p);
            expertiseDao.save(e3); 
            List<Expertise> list = expertiseDao.getExpertiseListByName("XYZAW");
            Assert.assertEquals(list.size(), 0); // asserting the size is zero
        } finally {
            if(e1!=null && null!=e1.getId()) { expertiseDao.delete(e1); }
            if(e2!=null && null!=e2.getId()) { expertiseDao.delete(e2); }
            if(e3!=null && null!=e3.getId()) { expertiseDao.delete(e3); }
        }
    }
    
    @Test
    @Transactional
    public void testGetExpertiseListByLevelSuccess() {
        Expertise e1 = makeExpertise();
        e1.setName("Scuba Diving");
        e1.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e1.setLevel(Expertise.ExpertiseLevel.AMATURE);
        e1.setHeldBy(p);
        expertiseDao.save(e1);
        Expertise e2 = makeExpertise();
        e2.setName("Math Solving");
        e2.setDescription("Someone who can solve any math problem and eligible for mathc Olympics");
        e2.setLevel(Expertise.ExpertiseLevel.PROFESSIONAL);
        e2.setHeldBy(p);
        expertiseDao.save(e2);    
        Expertise e3 = makeExpertise();
        e3.setName("Mountain Biking");
        e3.setDescription("Someone who can run bikes over rough and touch mountain surfaces and jump all over");
        e3.setLevel(Expertise.ExpertiseLevel.AMATURE);
        e3.setHeldBy(p);
        expertiseDao.save(e3); 
        List<Expertise> list = expertiseDao.getExpertiseListByLevel(e1.getLevel());
        Assert.assertEquals(list.get(0).getLevel(), e1.getLevel());
        Assert.assertEquals(list.get(1).getLevel(), e3.getLevel());
    }
    
}

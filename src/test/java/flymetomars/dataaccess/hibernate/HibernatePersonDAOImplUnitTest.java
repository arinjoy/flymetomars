package flymetomars.dataaccess.hibernate;

import flymetomars.common.datatransfer.Person;
import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author Yuan-Fang Li
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
public class HibernatePersonDAOImplUnitTest {

    private Person person;
    
    private SaltedPassword pass;
    
    @Autowired
    private PersonDAO personDao;
  
    @Autowired
    private ExpertiseDAO expertiseDao;
    
    @Autowired
    private SaltedPasswordDAO passwordDao;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    @Before
    public void setUp() throws ClassNotFoundException {
        person = this.makePersonDTO();
        person.setEmail("test@gmail.com");
        person.setUserName("fAo_23");
        person.setFirstName("Foolish");
        person.setLastName("Zhao");
        try {
            pass=this.dtoFactory.createDTO(SaltedPassword.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernatePersonDAOImplUnitTest.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        pass.setDigest("35179a54ea587953021400eb0cd23201");
        pass.setSaltId("656667");
        passwordDao.save(pass);
        person.setPassword(pass.getId());
    }
    
    // <editor-fold desc="general success test cases">
    
    @Test
    @Transactional
    public void testInsertPersonSuccess() {
        try {
            personDao.save(person);
            Person result = personDao.getPersonByUserName(this.person.getUserName());
            Assert.assertNotNull(result);
            Assert.assertEquals(this.person.getEmail(), result.getEmail());
            Assert.assertEquals(this.person.getFirstName(), result.getFirstName());
            Assert.assertEquals(this.person.getLastName(), result.getLastName());
            Assert.assertEquals(this.person.getUserName(), result.getUserName());
        } finally {
            doDelete(person);
        }
    }

    
    @Test
    @Transactional
    public void testUpdatePersonSuccess() {
        personDao.save(person);
        Person fellow = personDao.load(person.getId());
        Assert.assertNotNull(fellow);
        personEquals(person, fellow);
        fellow.setEmail("biswas.arinjoy@hotmale.com");
        fellow.setUserName("abis8");
        fellow.setFirstName("Arinjoy");
        fellow.setLastName("Biswas");
        fellow.setId(null);
        personDao.save(fellow);
        Person result = personDao.load(person.getId());
        Assert.assertNotNull(result);
        personEquals(person, result);
    }
     
    @Test
    @Transactional
    public void testDeletePersonSuccess() {
        try {
            personDao.save(person);
            Person alive = personDao.getPersonByEmail(this.person.getEmail());
            Assert.assertEquals(person.hashCode(), alive.hashCode());
            personDao.delete(person);
            Person dead = personDao.getPersonByEmail(this.person.getEmail());
            Assert.assertNull(dead);  //person is now null as they have been deleted
        } finally {
            doDelete(person);
        }
    }

    @Test
    @Transactional
    public void testFindPersonByEmailSuccess() {
        try {
            personDao.save(person);
            Person result = personDao.getPersonByEmail(this.person.getEmail());
            Assert.assertNotNull(result);
            personEquals(this.person, result);
        } finally {
            doDelete(person);
        }
    }

    @Test
    @Transactional
    public void testFindPersonByUserNameSuccess() {
        Assert.assertNotNull(person);
        personDao.save(person);
        try {
            Person result = personDao.getPersonByUserName(this.person.getUserName());
            Assert.assertNotNull(result);
            personEquals(this.person, result);
        } finally {
            doDelete(person);
        }
    }
    
    // </editor-fold>
    
    // <editor-fold desc="find non-existant test cases">
    
    @Test
    @Transactional
    public void testFindPersonByEmailWhoDoesNotExistSuccess() {
        Person result = null;
        try {
            result = personDao.getPersonByEmail("abraham.licon@aolmail.com");
            Assert.assertNull(result);
        } finally {
            doDelete(result);
        }
    }

    @Test
    @Transactional
    public void testFindPersonByUserNameWhoDoesNotExistSuccess() {
        Person result = personDao.getPersonByUserName("abcdefG");
        Assert.assertNull(result);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="duplicate/non-unique test cases">

    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertDuplicateEmailFailure() throws Exception {
        person.setEmail("duplicate@email.net");
        personDao.save(person);
        try {
            Person p2 = this.makePersonDTO();
            p2.setEmail(person.getEmail());
            p2.setFirstName("Davis");
            p2.setLastName("Jones");
            p2.setUserName("tssj_34");
            p2.setPassword(pass.getId());
            try {
                personDao.save(p2);
            } finally {
                doDelete(p2);
            }
        } finally {
            doDelete(person);
        }
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertDuplicateUserNameFailure() throws Exception {
        personDao.save(person);
        Person p2 = this.makePersonDTO();
        p2.setFirstName("Colman");
        p2.setLastName("Lawrence");
        p2.setEmail("monash.biswas@yahoo.com");
        p2.setUserName(person.getUserName());
        p2.setPassword(pass.getId());
        try {
            personDao.save(p2);
        } finally {
            doDelete(person);
            doDelete(p2);
        }
    }
    
    // </editor-fold>
    
    // <editor-fold desc="private utility methods">
    
    private Person makePersonDTO() {
        try {
            Person result=this.dtoFactory.createDTO(Person.class);
            //result.setPassword(this.dtoFactory.createDTO(SaltedPassword.class));
            return result;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernatePersonDAOImplUnitTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
    }

    Comparator<Person> engine=new Comparator<Person>() {
        private List<Comparable<String>> left;
        private List<String> right;
        @Override
        public int compare(Person p1, Person p2) {
            left=new ArrayList<Comparable<String>>();
            right=new ArrayList<String>();
            left.add(p1.getFirstName());
            right.add(p2.getFirstName());
            left.add(p1.getLastName());
            right.add(p2.getLastName());
            left.add(p1.getUserName());
            right.add(p2.getUserName());
            left.add(p1.getEmail());
            right.add(p2.getEmail());
            for(int counterVariable=0;counterVariable<right.size();counterVariable++) {
                int c=realCompare(left.get(counterVariable),right.get(counterVariable));
                if(c!=0) { return c; }
            }
            return 0;
        }
        private <T> int realCompare(Comparable<T> a, T b) {
            return a.compareTo(b);
        }
    };
    private void personEquals(Person x, Person y) {
        Assert.assertEquals(0,this.engine.compare(x, y));
    }

    private void doDelete(Person person) {
        if (null != person && null!=person.getId() && null==person) {
            personDao.delete(person);
        }
    }
    
    // </editor-fold>
    
}

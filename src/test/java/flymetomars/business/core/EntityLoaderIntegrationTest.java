package flymetomars.business.core;

import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntityLoaderIntegrationTest {

    private EntityLoader loader;
    private EntityFactory factory;
    private Location loc;

    /**
     * This runs before each test cases
     */
    @Before
    public void beforeEachTestCase() {
        factory = new EntityFactory();
        loc = new Location();
        loc.setFloor("H2.22");
        loc.setStreetNo("221");
        loc.setStreet("Dandenong Road");
        loc.setRegion("Victoria");
        loc.setPostcode("3031");
        loc.setLandmark("Near Racecourse");
        loc.setTown("Caulfield");
        loc.setCountry("Australia");
    }

    /**
     * Test case loading all the persons via core when there is only one
     * existing in the database
     */
    @Test
    public void testLoadAllPersonsWhenOnlyOneExistsSuccess() {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Person p = factory.createPerson(factory.createSaltedPassword(factory.createSalt("sjsk1")));
        p.setEmail("test@gmail.com");
        p.setUserName("fAo_23");
        p.setFirstName("Foolish");
        p.setLastName("Zhao");
        p.setPassword(new SaltedPassword());
        p.getPassword().setId(1337L);
        p.getPassword().setDigest("35179A54EA587953021400EB0CD23201");
        List<flymetomars.common.datatransfer.Person> pList = new ArrayList<flymetomars.common.datatransfer.Person>();
        pList.add(p.toDTO());
        //mocking the behaviour of person DAO to give all the persons from database
        Mockito.when(pDao.getAll()).thenReturn(pList);
        loader = new EntityLoader(pDao, Mockito.mock(InvitationDAO.class), null, null, null, null, null);
        List<Person> result = this.loader.loadAllPersons();
        // assert that only one person, i.e. p is returned
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), p);
    }

    /**
     * Test case loading all the persons via core when there are more that one
     * persons existing in the database
     */
    @Test
    public void testLoadAllPersonsWhenMoreThanOneExistSuccess() {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        List<flymetomars.common.datatransfer.Person> pList = new ArrayList<flymetomars.common.datatransfer.Person>();
        Person p1 = factory.createPerson(factory.createSaltedPassword(factory.createSalt("sjsk1")));
        p1.setEmail("test@gmail.com");
        p1.setUserName("fAo_23");
        p1.setFirstName("Foolish");
        p1.setLastName("Zhao");
        p1.setPassword(new SaltedPassword());
        p1.getPassword().setId(123L);
        p1.getPassword().setDigest("35179A54EA587953021400EB0CD23201");
        pList.add(p1.toDTO());
        Person p2 = factory.createPerson(factory.createSaltedPassword(factory.createSalt("sjsk1")));
        p2.setEmail("abc@gmail.com");
        p2.setUserName("sso_23");
        p2.setFirstName("Apoorva");
        p2.setLastName("Zhao");
        p2.setPassword(new SaltedPassword());
        p2.getPassword().setId(321L);
        p2.getPassword().setDigest("35179A54EA587953021400EB0CD23201");
        pList.add(p2.toDTO());
        //mocking the behaviour of person DAO to give all the persons from database
        Mockito.when(pDao.getAll()).thenReturn(pList);
        loader = new EntityLoader(pDao, Mockito.mock(InvitationDAO.class), null, null, null, null, null);
        List<Person> result = this.loader.loadAllPersons();
        // assert that p1 and p2 are returned
        Assert.assertEquals(result.size(), 2);
        Assert.assertTrue(result.contains(p1));
        Assert.assertTrue(result.contains(p2));

    }

    /**
     * Test case loading all the persons via core when there are no person
     * existing in the database
     */
    @Test
    public void testLoadAllPersonsWhenNoOneExistsSuccess() {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        List<flymetomars.common.datatransfer.Person> pList = new ArrayList<flymetomars.common.datatransfer.Person>();
        //mocking the behaviour of person DAO to give all the persons from database
        Mockito.when(pDao.getAll()).thenReturn(pList);
        loader = new EntityLoader(pDao, null, null, null, null, null, null);
        List<Person> result = this.loader.loadAllPersons();
        // assert that the list of person returned in empty
        Assert.assertEquals(result.size(), 0);
    }

    /**
     * Test case for loading all the missions via core when only one mission is existing
     */
    @Test
    public void testLoadAllMisionsWhenOnlyOneExistsSuccess() {
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        List<flymetomars.common.datatransfer.Mission> mList = new ArrayList<flymetomars.common.datatransfer.Mission>();
        Person cap = factory.createPerson(factory.createSaltedPassword(factory.createSalt("sjsk1")));
        cap.setEmail("test@gmail.com");
        cap.setUserName("fAo_23");
        cap.setFirstName("Foolish");
        cap.setLastName("Zhao");
        cap.setPassword(new SaltedPassword());
        cap.getPassword().setId(123L);
        cap.getPassword().setDigest("35179A54EA587953021400EB0CD23201");
        Mission m = factory.createMission(cap, loc);
        m.setName("nice Mision");
        m.setDescription("It would be a great mission in your life");
        m.setTime(new Date());
        mList.add(m.toDTO());
        Mockito.when(mDao.getAll()).thenReturn(mList);
        loader = new EntityLoader(null, null, mDao, null, null, null, null);
        List<Mission> result = this.loader.loadAllMissions();
        Assert.assertEquals(result.size(), 1);
        Assert.assertTrue(result.contains(m));
    }
    
     /**
     * Test case for loading all the missions via core when more that one missions are existing
     */
    @Test
    public void testLoadAllMisionsWhenMoreThanOneExistSuccess() {
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        List<flymetomars.common.datatransfer.Mission> mList = new ArrayList<flymetomars.common.datatransfer.Mission>();
        Person cap = factory.createPerson(factory.createSaltedPassword(factory.createSalt("sjsk1")));
        cap.setEmail("test@gmail.com");
        cap.setUserName("fAo_23");
        cap.setFirstName("Foolish");
        cap.setLastName("Zhao");
        cap.setPassword(new SaltedPassword());
        cap.getPassword().setId(123L);
        cap.getPassword().setDigest("35179A54EA587953021400EB0CD23201");
        Mission m1 = factory.createMission(cap, loc);
        m1.setName("Fight Mision");
        m1.setDescription("It would be a great mission in your life");
        m1.setTime(new Date());
        mList.add(m1.toDTO());   
        Mission m2 = factory.createMission(cap, loc);
        m2.setName("Night Mision");
        m2.setDescription("It would a wonderful mission in your life");
        m2.setTime(new Date());
        mList.add(m2.toDTO());     
        Mockito.when(mDao.getAll()).thenReturn(mList);
        loader = new EntityLoader(null, null, mDao, null, null, null, null);
        List<Mission> result = this.loader.loadAllMissions();
        Assert.assertEquals(result.size(), 2);
        Assert.assertTrue(result.contains(m1));
        Assert.assertTrue(result.contains(m2));   
    }
    
     /**
     * Test case for loading all the missions via core when more that one missions are existing
     */
    @Test
    public void testLoadAllMisionsWhenNoMissionExistSuccess() {
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        List<flymetomars.common.datatransfer.Mission> mList = new ArrayList<flymetomars.common.datatransfer.Mission>();
        Mockito.when(mDao.getAll()).thenReturn(mList);
        loader = new EntityLoader(null, null, mDao, null, null, null, null);
        List<Mission> result = this.loader.loadAllMissions();
        Assert.assertEquals(result.size(), 0); 
    }
    
    @Test
    public void testLoadAllSaltedPasswordsSharingSaltSuccess() {
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Salt shared=new Salt();
        List<flymetomars.common.datatransfer.SaltedPassword> sList = new ArrayList<flymetomars.common.datatransfer.SaltedPassword>();
        Mockito.when(spDao.getSaltedPasswordsSharingSameSalt(shared.toDTO())).thenReturn(sList);
        this.loader = new EntityLoader(null, null, null, null, null, spDao, null);
        List<SaltedPassword> result = loader.loadAllSaltedPasswordsSharingSalt(shared);
        Assert.assertEquals(result.size(), 0);
    }
}

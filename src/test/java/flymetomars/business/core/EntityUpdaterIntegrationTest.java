
package flymetomars.business.core;

import flymetomars.business.model.Expertise;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import flymetomars.common.NullArgumentException;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import org.mockito.Mockito;

/**
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntityUpdaterIntegrationTest {
   
    EntityUpdater updater;
    
    Location loc;
    
    @Before
    public void beforeEachTestCase() {
        loc=new Location();
        loc.setFloor("H2.22");
        loc.setStreetNo("221");
        loc.setStreet("Dandenong Road");
        loc.setRegion("Victoria");
        loc.setPostcode("3031");
        loc.setLandmark("Near Racecourse");
        loc.setTown("Caulfield");
        loc.setCountry("Australia");
        loc.setId(24L);
    }
    
    private EntityFactory factory=null;
    private SaltedPassword dummypass=new SaltedPassword();
    @Before
    public void beforeFirstRun() {
        if(null==factory) {
            factory=new EntityFactory();
            dummypass=factory.createSaltedPassword(factory.createSalt(""));
            dummypass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
            dummypass.setId(1337L);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Person update tests">
    
    /**
     * Testing of a update person when it succeeds
     * @throws DependencyException
     */
    @Test
    public void testUpdatePersonSuccess() throws DependencyException {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        updater = new EntityUpdater(pDao, null, null,null,null,null,null);
        
        Person p = factory.createPerson(dummypass);
        p.setId(2020L);        
        p.setEmail("myemail@gmail.com.au");
        p.setUserName("my_Name");
        // mock such that these updated email and user name does not seem like existing in the database
        Mockito.when(pDao.getPersonByEmail("myemail@gmail.com.au")).thenReturn(null);
        Mockito.when(pDao.getPersonByUserName("my_Name")).thenReturn(null);
        // mock the load behaviour of persondao such that it thinks person 'p' already exists
        Mockito.when(pDao.load(p.getId())).thenReturn(p.toDTO());
        Person updateP = updater.updatePerson(p);
        
        Assert.assertEquals("my_Name", updateP.getUserName());
        Assert.assertEquals("myemail@gmail.com.au", updateP.getEmail());
        Assert.assertEquals(p, updateP);
    }
    /**
     * Testing of update person when the person does not already exist in the database
     * @throws DependencyException 
     */
    @Test(expected = DependencyException.class)
    public void testUpdatePersonDoesNotExistSuccess() throws DependencyException {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        updater = new EntityUpdater(pDao, null, null,null,null,null,null);
        Person p = factory.createPerson(dummypass);
        p.setId(2020L);        
        p.setEmail("myemail@gmail.com.au");
        p.setUserName("my_Name");
        // mock such that these updated email and user name does not seem like existing in the database
        Mockito.when(pDao.getPersonByEmail("myemail@gmail.com.au")).thenReturn(null);
        Mockito.when(pDao.getPersonByUserName("my_Name")).thenReturn(null);
        // mock the load behaviour of persondao such that it thinks person 'p' does not exist
        Mockito.when(pDao.load(p.getId())).thenReturn(null);
        Person updateP = updater.updatePerson(p);
        Assert.assertEquals("my_Name", updateP.getUserName());
        Assert.assertEquals("myemail@gmail.com.au", updateP.getEmail());
        Assert.assertEquals(p, updateP);
    }
    
    /**
     * Testing of person update when duplicate email detected
     * @throws DependencyException
     */
    @Test(expected = DependencyException.class)
    public void testUpdatePersonDuplicateEmailFailure() throws DependencyException {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        updater = new EntityUpdater(pDao, null, null,null,null,null,null);
        Person p = factory.createPerson(dummypass);
        p.setId(2020L);        
        p.setEmail("myoldemail@gmail.com.au");
        p.setUserName("my_Name");
        Person p2 = factory.createPerson(dummypass);
        p2.setId(2030L);        
        p2.setEmail("myemail@gmail.com.au");
        // mock such that a person with this updated email exists in the database
        Mockito.when(pDao.getPersonByEmail("myoldemail@gmail.com.au")).thenReturn(p.toDTO());
        Mockito.when(pDao.getPersonByEmail("myemail@gmail.com.au")).thenReturn(p2.toDTO());
        Mockito.when(pDao.getPersonByUserName("my_Name")).thenReturn(p.toDTO());
        // mock the load behaviour of persondao such that it thinks person 'p' already exists
        Mockito.when(pDao.load(p.getId())).thenReturn(p.toDTO());
        p.setEmail("myemail@gmail.com.au");  //actually update the email to a taken value
        updater.updatePerson(p);   
    }
    
    /**
     * Testing of person update when duplicate user name detected
     * @throws DependencyException
     */
    @Test(expected = DependencyException.class)
    public void testUpdatePersonDuplicateUserNameFailure() throws DependencyException {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        updater = new EntityUpdater(pDao, null, null,null,null,null,null);
        Person p = factory.createPerson(dummypass);
        p.setId(2020L);        
        p.setEmail("myemail@gmail.com.au");
        p.setUserName("my_oldName");
        Person p2 = factory.createPerson(dummypass);
        p2.setId(2030L);        
        p2.setUserName("my_Name");
        Mockito.when(pDao.getPersonByEmail("myemail@gmail.com.au")).thenReturn(p.toDTO());
        Mockito.when(pDao.getPersonByUserName("my_oldName")).thenReturn(p.toDTO());
        // mock such that a person with this updated user name exists in the database
        Mockito.when(pDao.getPersonByUserName("my_Name")).thenReturn(p2.toDTO());
        // mock the load behaviour of persondao such that it thinks person 'p' already exists
        Mockito.when(pDao.load(p.getId())).thenReturn(p.toDTO());
        p.setUserName("my_Name");  //actually update the user name value
        updater.updatePerson(p);   
    }
    
    @Test (expected = NullArgumentException.class)
    public void testUpdatePersonNullInputFailure() throws DependencyException {       
        updater = new EntityUpdater(null, null, null, null, null, null, null);
        updater.updatePerson(null);
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Invitation update tests">
    
    /**
     * Testing of invitation update test cases
     */
    @Test
    public void testUpdateInvitationNoDependencySuccess() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(dummypass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the World");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setRecipient(crecap);
        inv.setMission(mis);
        inv.setId(222L);
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(crecap.toDTO());
        Mockito.when(personDAO.load(inv.getRecipient().getId())).thenReturn(crecap.toDTO());
        Mockito.when(personDAO.load(mis.getCaptain().getId())).thenReturn(crecap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO iDao=Mockito.mock(InvitationDAO.class);
        Mockito.when(iDao.load(inv.getId())).thenReturn(inv.toDTO());
        updater = new EntityUpdater(personDAO,iDao,mDao,null,null,null,null);
        Invitation result = updater.updateInvitation(inv);
        Assert.assertEquals(result, inv);
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateInvitationNotExistFailure() throws DependencyException {
        //invitation
        Invitation inv=new Invitation();
        inv.setId(1L);
        //dao
        InvitationDAO iDao=Mockito.mock(InvitationDAO.class);
        Mockito.when(iDao.load(inv.getId())).thenReturn(null);
        updater = new EntityUpdater(null,iDao,null,null,null,null,null);
        updater.updateInvitation(inv); //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateInvitationUnsavedMissionFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(dummypass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the World");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setRecipient(crecap);
        inv.setMission(mis);
        inv.setId(222L);
        //daos
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(null);
        InvitationDAO iDao=Mockito.mock(InvitationDAO.class);
        Mockito.when(iDao.load(inv.getId())).thenReturn(inv.toDTO());
        updater = new EntityUpdater(null,iDao,mDao,null,null,null,null);
        updater.updateInvitation(inv); //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateInvitationUnsavedCreatorFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(dummypass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the World");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setRecipient(crecap);
        inv.setMission(mis);
        inv.setId(222L);
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(null);
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO iDao=Mockito.mock(InvitationDAO.class);
        Mockito.when(iDao.load(inv.getId())).thenReturn(inv.toDTO());
        updater = new EntityUpdater(personDAO,iDao,mDao,null,null,null,null);
        updater.updateInvitation(inv); //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateInvitationUnsavedRecipientFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person cre = factory.createPerson(dummypass);
        cre.setEmail("law@ren.ce");
        cre.setId(1337L);
        Person rep = factory.createPerson(dummypass);
        rep.setEmail("apo@or.va");
        rep.setId(9002L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(cre);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the World");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(cre);
        inv.setRecipient(rep);
        inv.setMission(mis);
        inv.setId(444L);
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(cre.toDTO());
        Mockito.when(personDAO.load(inv.getRecipient().getId())).thenReturn(null);
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO iDao=Mockito.mock(InvitationDAO.class);
        Mockito.when(iDao.load(inv.getId())).thenReturn(inv.toDTO());
        updater = new EntityUpdater(personDAO,iDao,mDao,null,null,null,null);
        updater.updateInvitation(inv); //throws exception
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Mission update tests">
    
    /**
     * Test cases for Updating a mission
     * @throws DependencyException
     */
    @Test
    public void testUpdateMissionNoDependencySuccess() throws DependencyException, UnserialisedEntityException {
        //real captain
        Person cap = factory.createPerson(dummypass);
        cap.setEmail("john.monash@scotch.edu");
        //real mission
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setTime(new Date());
        m.setLocation(loc);
        m.setId(343L);
        //mocked person dao
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.getPersonByUserName(m.getCaptain().getUserName())).thenReturn(cap.toDTO());
        //mocked location dao
        LocationDAO locationDAO = Mockito.mock(LocationDAO.class);
        Mockito.when(locationDAO.load(m.getLocation().getId())).thenReturn(loc.toDTO());
        //mocked mission dao
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(m.getName())).thenReturn(m.toDTO());
        //actual test logic
        updater = new EntityUpdater(personDAO, null,mDao,locationDAO,null,null,null);
        Mission result = updater.updateMission(m);
        Assert.assertEquals(result, m);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testUpdateMissionNullInputFailure() throws DependencyException {
        updater = new EntityUpdater(null, null, null, null, null, null, null);
        updater.updateMission(null);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateMissionDoesNotExistFailure() throws DependencyException, UnserialisedEntityException {
        Mission m = new Mission();
        m.setId(2378L);
        m.setName("Lemon Party Explorers");
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(m.getName())).thenReturn(null);
        updater = new EntityUpdater(null, null, mDao, null, null, null, null);
        updater.updateMission(m);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateMissionWithNullLocationFailure() throws DependencyException, UnserialisedEntityException {
        //real captain
        Person cap = factory.createPerson(dummypass);
        cap.setEmail("john.monash@scotch.edu");
        //real mission
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setTime(new Date());
        m.setLocation(loc);
        m.setId(343L);
        //mocked person dao
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.getPersonByUserName(m.getCaptain().getUserName())).thenReturn(cap.toDTO());
        //mocked location dao
        LocationDAO locationDAO = Mockito.mock(LocationDAO.class);
        Mockito.when(locationDAO.load(m.getLocation().getId())).thenReturn(null);
        //mocked mission dao
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(m.getName())).thenReturn(m.toDTO());
        //actual test logic
        updater = new EntityUpdater(personDAO, null,mDao,locationDAO,null,null,null);
        updater.updateMission(m);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateMissionWithNullTimeFailure() throws DependencyException {
        //real captain
        Person cap = factory.createPerson(dummypass);
        cap.setEmail("john.monash@scotch.edu");
        //real mission
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setLocation(loc);
        m.setId(343L);
        m.setName("Foobar mission");
        //mocked person dao
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.getPersonByUserName(m.getCaptain().getUserName())).thenReturn(cap.toDTO());
        //mocked location dao
        LocationDAO locationDAO = Mockito.mock(LocationDAO.class);
        Mockito.when(locationDAO.load(m.getLocation().getId())).thenReturn(loc.toDTO());
        //mocked mission dao
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        // Mockito.when(mDao.getMissionByName(m.getName())).thenReturn(m.toDTO());
        //actual test logic
        updater = new EntityUpdater(personDAO, null,mDao,locationDAO,null,null,null);
        updater.updateMission(m);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateMissionDuplicateNameFailure() throws DependencyException, UnserialisedEntityException {
        //real captain
        Person cap = factory.createPerson(dummypass);
        cap.setEmail("john.monash@scotch.edu");
        //real mission
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setLocation(loc);
        m.setId(343L);
        m.setName("Foobar mission");
        m.setTime(new Date());
        Mission m2=new Mission();
        m2.setCaptain(cap);
        m2.setLocation(loc);
        m2.setId(345L);
        m2.setTime(new Date());
        m2.setName("Foobar mission");
        //mocked person dao
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.getPersonByUserName(m.getCaptain().getUserName())).thenReturn(cap.toDTO());
        //mocked location dao
        LocationDAO locationDAO = Mockito.mock(LocationDAO.class);
        Mockito.when(locationDAO.load(m.getLocation().getId())).thenReturn(loc.toDTO());
        //mocked mission dao
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(m.getName())).thenReturn(m2.toDTO());
        //actual test logic
        updater = new EntityUpdater(personDAO, null,mDao,locationDAO,null,null,null);
        updater.updateMission(m);  //throws exception
    }
    
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="Location update tests">
    
    @Test
    public void testUpdateLocationNoDependencySuccess() throws DependencyException, UnserialisedEntityException {
        LocationDAO lDao = Mockito.mock(LocationDAO.class);
        loc.setId(234L);
        List<flymetomars.common.datatransfer.Mission> misSet = new ArrayList<flymetomars.common.datatransfer.Mission>();
        Mockito.when(lDao.load(loc.getId())).thenReturn(loc.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionsByLocation(loc.toDTO())).thenReturn(misSet);
        updater = new EntityUpdater(null,null,mDao,lDao,null,null,null);
        Location result = updater.updateLocation(loc); 
        Assert.assertEquals(result, loc);
    }
    
    @Test(expected=DependencyException.class)
    public void testUpdateLocationMissionLessOrphanFailure() throws DependencyException, UnserialisedEntityException {
        LocationDAO lDao = Mockito.mock(LocationDAO.class);
        loc.setId(234L);
       // List<flymetomars.common.datatransfer.Mission> misSet = new ArrayList<flymetomars.common.datatransfer.Mission>();
        Mockito.when(lDao.load(loc.getId())).thenReturn(loc.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionsByLocation(loc.toDTO())).thenReturn(null);
        updater = new EntityUpdater(null,null,mDao,lDao,null,null,null);
        Location result = updater.updateLocation(loc); 
        Assert.assertEquals(result, loc);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testUpdateLocationNullInputFailure() throws DependencyException {
       updater = new EntityUpdater(null, null, null, null, null, null, null);
       Location dead  = updater.updateLocation(null);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Expertise update tests">
    
    @Test
    public void testUpdateExpertiseSuccess() throws DependencyException {
        Expertise e = new Expertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setId(121L);
        ExpertiseDAO expDao = Mockito.mock(ExpertiseDAO.class);
        // mock the behaviour so that it thinks it already exists
        Mockito.when(expDao.load(e.getId())).thenReturn(e.toDTO());
        updater = new EntityUpdater(null, null,null,null,expDao,null,null);
        Expertise dead = updater.updateExpertise(e);
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateExpertiseDoesNotExistFailure() throws DependencyException {
        Expertise e = new Expertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setId(121L);
        ExpertiseDAO expDao = Mockito.mock(ExpertiseDAO.class);
        // mock the behaviour so that it thinks it already does not exist
        Mockito.when(expDao.load(e.getId())).thenReturn(null);
        updater = new EntityUpdater(null, null,null,null,expDao,null,null);
        Expertise dead = updater.updateExpertise(e);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testUpdateExpertiseNullInputFailure() throws DependencyException {
       updater = new EntityUpdater(null, null, null, null, null, null, null);
       Expertise dead = updater.updateExpertise(null);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="SaltedPassword update tests">
    @Test
    public void testUpdateSaltedPasswordNoDependencySuccess() throws DependencyException {
        SaltedPassword pass = new SaltedPassword();
        pass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        pass.setId(9001L);
        Salt salt=new Salt();
        salt.setHashedSaltKey("414243");
        pass.setSalt(salt);
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pass.getId())).thenReturn(pass.toDTO());
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(salt.toDTO());
        updater = new EntityUpdater(null,null,null,null,null,spDao,sDao);
        SaltedPassword result = updater.updateSaltedPassword(pass);
        Assert.assertEquals(pass, result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testUpdateSaltedPasswordNullInputFailure() throws DependencyException {
        updater = new EntityUpdater(null,null,null,null,null,null,null);
        updater.updateSaltedPassword(null);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateSaltedPasswordNotSavedFailure() throws DependencyException {
        SaltedPassword pass = new SaltedPassword();
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        updater = new EntityUpdater(null,null,null,null,null,spDao,null);
        updater.updateSaltedPassword(pass);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testUpdateSaltedPasswordUnsavedSaltFailure() throws DependencyException {
        SaltedPassword pass = new SaltedPassword();
        pass.setId(9001L);
        pass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        Salt salt=new Salt();
        pass.setSalt(salt);
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pass.getId())).thenReturn(pass.toDTO());
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(null);
        updater = new EntityUpdater(null,null,null,null,null,spDao,sDao);
        updater.updateSaltedPassword(pass);  //throws exception
    }
    //</editor-fold>
    
    //TODO: Salt update tests
    
}

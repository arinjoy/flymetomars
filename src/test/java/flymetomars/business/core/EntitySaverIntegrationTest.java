package flymetomars.business.core;

import flymetomars.business.model.Expertise;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.common.NullArgumentException;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import org.mockito.Mockito;

/**
 * 
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntitySaverIntegrationTest {
    
    EntitySaver saver;
    
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
    }
    
    private EntityFactory factory=null;
    private SaltedPassword pass=new SaltedPassword();
    @Before
    public void beforeFirstRun() {
        if(null==factory) {
            factory=new EntityFactory();
            pass=factory.createSaltedPassword(factory.createSalt(""));
            pass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
            pass.setId(1337L);
        }
    }
   
    //<editor-fold defaultstate="collapsed" desc="Person save tests">
    
    /**
     * Testing of Person saver where no dependency exists
     * @throws Exception
     */
    @Test
    public void testSavePersonNoDepenencySuccess() throws DependencyException {       
        Person p = factory.createPerson(pass);
        p.setEmail("test@test.com.au");
        p.setUserName("person_1");       
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(null);
        Mockito.when(pDao.getPersonByUserName(p.getUserName())).thenReturn(null);      
        saver = new EntitySaver(pDao, null,null,null,null,null,null);
        Person p1 = saver.savePerson(p);   
        Assert.assertEquals(p, p1);
    }
    /**
     * Testing Person saver when a person already exists
     * @throws DependencyException 
     */
    @Test(expected = DependencyException.class)
    public void testSavePersonAlreadyExistsFailure() throws DependencyException {    
        Person p = factory.createPerson(pass);
        p.setEmail("test@test.com.au");
        p.setUserName("person_1");
        p.setId(1010L);
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(null);
        Mockito.when(pDao.getPersonByUserName(p.getUserName())).thenReturn(null);       
        //tell that this p already exists when asked by load
        Mockito.when(pDao.load(p.getId())).thenReturn(p.toDTO());  
        saver = new EntitySaver(pDao, null,null,null,null,null,null);
        Person p1 = saver.savePerson(p);   
        Assert.assertEquals(p, p1);
    }
    
    /**
     * Testing of person saver where duplicate user name was detected
     * @throws Exception
     */
    @Test(expected = DependencyException.class)
    public void testSavePersonDuplicateUserNameFailure() throws DependencyException {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Person p = factory.createPerson(pass);
        Person p1 = factory.createPerson(pass);
        Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(null);
        Mockito.when(pDao.getPersonByUserName(p.getUserName())).thenReturn(p1.toDTO());
        saver = new EntitySaver(pDao, null,null,null,null,null,null);
        saver.savePerson(p);
    }
    
    /**
     * Testing of person saver where duplicate email was detected
     * @throws Exception
     */
    @Test(expected = DependencyException.class)
    public void testSavePersonDuplicateEmailFailure() throws DependencyException {
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Person p = factory.createPerson(pass);
        Person p1 = factory.createPerson(pass);
        Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(p1.toDTO());
        Mockito.when(pDao.getPersonByUserName(p1.getUserName())).thenReturn(null);
        saver = new EntitySaver(pDao, null,null,null,null,null,null);
        saver.savePerson(p);
    }
    
    @Test (expected = NullArgumentException.class)
    public void testSavePersonNullInputFailure() throws DependencyException {
        saver = new EntitySaver(null, null, null, null, null, null, null);
        saver.savePerson(null);
    }
    
    //</editor-fold>
           
    //<editor-fold defaultstate="collapsed" desc="Invitation save tests">
    
    @Test
    public void testSaveInvitationNoDependencySuccess() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(pass);
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
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(crecap.toDTO());
        Mockito.when(personDAO.load(inv.getRecipient().getId())).thenReturn(crecap.toDTO());
        Mockito.when(personDAO.load(mis.getCaptain().getId())).thenReturn(crecap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        Assert.assertNotNull(mDao);
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(personDAO,invitationDao,mDao,null,null,null,null);
        Invitation result = saver.saveInvitation(inv);
        Assert.assertEquals(result, inv);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveInvitationNullInputFailure() throws DependencyException {
        saver = new EntitySaver(null,null,null,null,null,null,null);
        saver.saveInvitation(null);
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveInvitationAlreadyExistsFailure() throws DependencyException {
        //person
        Person crecap = factory.createPerson(pass);
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
        inv.setId(123L);
        //dao
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        flymetomars.common.datatransfer.Invitation iDTO=Mockito.any();
        Mockito.when(invitationDao.load(inv.getId())).thenReturn(iDTO);
        MissionDAO mDao=Mockito.mock(MissionDAO.class);
        //test
        saver = new EntitySaver(null, invitationDao,mDao,null,null,null,null);
        saver.saveInvitation(inv);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveInvitationNullMissionFailure() throws DependencyException {
        //person
        Person crecap = factory.createPerson(pass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setRecipient(crecap);
        //dao
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(null, invitationDao,null,null,null,null,null);
        saver.saveInvitation(inv);  //should throw exception
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveInvitationUnsavedMissionFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(pass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the Worlds");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setRecipient(crecap);
        inv.setMission(mis);
        inv.setId(123L);
        //daos
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(null);
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(null,invitationDao,mDao,null,null,null,null);
        saver.saveInvitation(inv);  //should throw exception
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveInvitationNullCreatorFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(pass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the Worlds");
        //invitation
        Invitation inv=new Invitation();
        inv.setRecipient(crecap);
        inv.setMission(mis);
        //daos
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(null,invitationDao,mDao,null,null,null,null);
        saver.saveInvitation(inv);  //should throw exception
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveInvitationUnsavedCreatorFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(pass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the Worlds");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setRecipient(crecap);
        inv.setMission(mis);
        inv.setId(123L);
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(null);
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(personDAO,invitationDao,mDao,null,null,null,null);
        saver.saveInvitation(inv);  //should throw exception
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveInvitationNullRecipientFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(pass);
        crecap.setEmail("law@ren.ce");
        crecap.setId(1337L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(crecap);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the Worlds");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(crecap);
        inv.setMission(mis);
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(crecap.toDTO());
        Mockito.when(personDAO.load(mis.getCaptain().getId())).thenReturn(crecap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(personDAO,invitationDao,mDao,null,null,null,null);
        saver.saveInvitation(inv);  //should throw exception
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveInvitationUnsavedRecipientFailure() throws DependencyException, UnserialisedEntityException {
        //persons
        Person cre = factory.createPerson(pass);
        cre.setEmail("law@ren.ce");
        cre.setId(1337L);
        Person rep = factory.createPerson(pass);
        rep.setEmail("arin@joy.org");
        rep.setId(9001L);
        //mission
        Mission mis = new Mission();
        mis.setCaptain(cre);
        mis.setTime(new Date());
        mis.setLocation(loc);
        mis.setName("The end of the Worlds");
        //invitation
        Invitation inv=new Invitation();
        inv.setCreator(cre);
        inv.setRecipient(rep);
        inv.setMission(mis);
        inv.setId(123L);
        //daos
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mockito.when(personDAO.load(inv.getCreator().getId())).thenReturn(cre.toDTO());
        Mockito.when(personDAO.load(inv.getRecipient().getId())).thenReturn(null);
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(inv.getMission().getName())).thenReturn(mis.toDTO());
        InvitationDAO invitationDao=Mockito.mock(InvitationDAO.class);
        //test
        saver = new EntitySaver(personDAO,invitationDao,mDao,null,null,null,null);
        saver.saveInvitation(inv);  //should throw exception
    }
        
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mission save tests">
    
    @Test
    public void testSaveMisionNoDependencySuccess() throws Exception {
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setTime(new Date());
        m.setLocation(loc);
        Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(cap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
        Assert.assertEquals(result, m);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveMissionNullInputFailure() throws Exception {
        saver = new EntitySaver(null, null, null, null, null, null, null);
        Mission born = saver.saveMission(null);
    }
    
    @Test(expected=NullArgumentException.class)
    public void testSaveMisionWithNullLocationFailure() throws Exception {   
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setTime(new Date());
        Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(cap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
    }
    
    @Test(expected=DependencyException.class)
    public void testSaveMisionAlreadyExistsFailure() throws Exception {
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setName("Nice Mission");
        m.setCaptain(cap);
        m.setTime(new Date());
        m.setLocation(loc);
        Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(cap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
        mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionByName(result.getName())).thenReturn(m.toDTO());
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission savedAgain = saver.saveMission(result);
    }
    
    @Test(expected=NullArgumentException.class)
    public void testSaveMissionWithNullCaptainFailure() throws Exception {
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setName("Nice Mission");
        m.setTime(new Date());
        m.setLocation(loc);
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
    }
    
    @Test(expected=NullArgumentException.class)
    public void testSaveMissionWithNullTimeFailure() throws Exception {
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setName("Nice Mission");
        m.setCaptain(cap);
        m.setLocation(loc);
        Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(cap.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
    }
    
    @Test(expected=DependencyException.class)
    public void testSaveMisionCaptainDoesNotExistFailure() throws Exception {
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setTime(new Date());
        m.setLocation(loc);
        Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(null);
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
    }
    
    @Test(expected=DependencyException.class)
    public void testSaveMisionWithNullParticiantFailure() throws Exception {
        Person cap = factory.createPerson(pass);
        cap.setEmail("arin@joy.com");
        PersonDAO personDAO = Mockito.mock(PersonDAO.class);
        Mission m = new Mission();
        m.setCaptain(cap);
        m.setTime(new Date());
        m.setLocation(loc);
        Person part = factory.createPerson(pass);
        part.setEmail("abc@def.com");
        Set<Person> parts = new HashSet<Person>();
        parts.add(part);
        m.setParticipantSet(parts);
        Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(cap.toDTO());
        Mockito.when(personDAO.getPersonByEmail(m.getParticipantSet().iterator().next().getEmail())).thenReturn(null);
        
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        saver = new EntitySaver(personDAO, null,mDao,null,null,null,null);
        Mission result = saver.saveMission(m);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Location save tests">

    @Test
    public void testSaveLocationSuccess() throws DependencyException {
        LocationDAO lDao = Mockito.mock(LocationDAO.class);
        saver = new EntitySaver(null, null,null,lDao,null,null,null);
        Location born = saver.saveLocation(loc);
        Assert.assertEquals(born, loc);
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveLocationDoesNotExistFailure() throws DependencyException {  
        loc.setId(292L);
        LocationDAO lDao = Mockito.mock(LocationDAO.class);
        Mockito.when(lDao.load(loc.getId())).thenReturn(loc.toDTO());
        saver = new EntitySaver(null, null,null,lDao,null,null,null);
        Location born = saver.saveLocation(loc);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveLocationNullInputFailure() throws DependencyException {
        saver = new EntitySaver(null,null,null,null,null,null,null);
        Location l = saver.saveLocation(null);
    }
    
   //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Expertise save tests">
    @Test
    public void testSaveExpertiseSuccess() throws DependencyException {
        Expertise e = new Expertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        ExpertiseDAO expDao = Mockito.mock(ExpertiseDAO.class);
        // make sure the same name and level not already exists
        Mockito.when(expDao.getExpertiseListByLevel(e.getLevel())).thenReturn(null);
        Mockito.when(expDao.getExpertiseListByName(e.getName())).thenReturn(null);
        saver = new EntitySaver(null, null,null,null,expDao,null,null);
        Expertise born = saver.saveExpertise(e);
        Assert.assertEquals(born, e);
    }
    
    @Test (expected=DependencyException.class)
    public void testSaveExpertiseAlreadySavedFailure() throws DependencyException {
        Expertise e = new Expertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setId(202L);
        ExpertiseDAO expDao = Mockito.mock(ExpertiseDAO.class);
   
        Mockito.when(expDao.load(e.getId())).thenReturn(e.toDTO());
        saver = new EntitySaver(null, null,null,null,expDao,null,null);
        Expertise born = saver.saveExpertise(e);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveExpertiseNullInputFailure() throws DependencyException {
        saver = new EntitySaver(null,null,null,null,null,null,null);
        Expertise ex = saver.saveExpertise(null);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="SaltedPassword save tests">
    
    @Test
    public void testSaveSaltedPasswordNoDependencySuccess() throws DependencyException {
        SaltedPassword pw=new SaltedPassword();
        Salt salt=new Salt();
        pw.setSalt(salt);
        salt.setHashedSaltKey("FACEBEAD");
        pw.setDigest("D41D8CD98F00B204E9800998ECF8427E");
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(salt.toDTO());
        saver = new EntitySaver(null, null,null,null,null,spDao,sDao);
        SaltedPassword result= saver.saveSaltedPassword(pw);
        Assert.assertEquals(pw, result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveSaltedPasswordNullInputFailure() throws DependencyException {
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        saver = new EntitySaver(null, null,null,null,null,spDao,null);
        saver.saveSaltedPassword(null);
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveSaltedPasswordAlreadyExistsFailure() throws DependencyException {
        SaltedPassword pw=new SaltedPassword();
        pw.setId(3L);
        pw.setDigest("D41D8CD98F00B204E9800998ECF8427E");
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pw.getId())).thenReturn(pw.toDTO());
        saver = new EntitySaver(null, null,null,null,null,spDao,null);
        saver.saveSaltedPassword(pw);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveSaltedPasswordNullSaltFailure() throws DependencyException {
        SaltedPassword pw=new SaltedPassword();
        pw.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        saver = new EntitySaver(null, null,null,null,null,spDao,null);
        SaltedPassword result= saver.saveSaltedPassword(pw);
        Assert.assertEquals(pw, result);
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveSaltedPasswordUnsavedSaltFailure() throws DependencyException {
        SaltedPassword pw=new SaltedPassword();
        pw.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        Salt salt=new Salt();
        pw.setSalt(salt);
        salt.setHashedSaltKey("FACEBEAD");
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(null);
        saver = new EntitySaver(null, null,null,null,null,spDao,sDao);
        SaltedPassword result= saver.saveSaltedPassword(pw);
        Assert.assertEquals(pw, result);
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveSaltedPasswordNoPasswordSetFailure() throws DependencyException {
        SaltedPassword pw=new SaltedPassword();
        Salt salt=new Salt();
        pw.setSalt(salt);
        salt.setHashedSaltKey("FACEBEAD");
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(salt.toDTO());
        saver = new EntitySaver(null, null,null,null,null,spDao,sDao);
        SaltedPassword result= saver.saveSaltedPassword(pw);
        Assert.assertEquals(pw, result);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Salt save tests">
    
    @Test
    public void testSaveSaltNoDependencySuccess() throws DependencyException {
        Salt salt=new Salt();
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        saver = new EntitySaver(null, null,null,null,null,null,sDao);
        Salt result= saver.saveSalt(salt);
        Assert.assertEquals(salt, result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSaveSaltNullFailure() throws DependencyException {
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        saver = new EntitySaver(null, null,null,null,null,null,sDao);
        saver.saveSalt(null);
    }
    
    @Test(expected = DependencyException.class)
    public void testSaveSaltAlreadySavedFailure() throws DependencyException {
        Salt salt=new Salt();
        salt.setHashedSaltKey("FACEBEAD");
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(salt.toDTO());
        saver = new EntitySaver(null, null,null,null,null,null,sDao);
        Salt result= saver.saveSalt(salt);
        Assert.assertEquals(salt, result);
    }
    
    //</editor-fold>
    
}

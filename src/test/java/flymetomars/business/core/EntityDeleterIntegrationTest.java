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
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntityDeleterIntegrationTest {

    EntitySaver saver;
    EntityDeleter deleter;
    
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
    private SaltedPassword glopass=new SaltedPassword();
    @Before
    public void beforeFirstRun() {
        if(null==factory) {
            factory=new EntityFactory();
            glopass=factory.createSaltedPassword(factory.createSalt(""));
            glopass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
            glopass.setId(1337L);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Person delete tests">
     /**
      * Testing of Deleting a person where no dependency
      * @throws Exception 
      */
     @Test
     public void testDeletePersonNoDependencySuccess() throws UnserialisedEntityException, DependencyException {
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         InvitationDAO iDao = Mockito.mock(InvitationDAO.class);
         PersonDAO pDao = Mockito.mock(PersonDAO.class);
         Person p = factory.createPerson(glopass);
         p.setEmail("aabcd@yahoo.com");
         p.setUserName("user_1");
         p.setId(244L);
         flymetomars.common.datatransfer.Person pDTO=Mockito.any(flymetomars.common.datatransfer.Person.class);
         Mockito.when(mDao.getMissionsByCaptain(pDTO)).thenReturn(null);
         Mockito.when(iDao.getInvitationsByCreator(pDTO)).thenReturn(null);
         Mockito.when(iDao.getInvitationsByRecipient(pDTO)).thenReturn(null);
         // mocking the stub so that it thinks the peron 'p' already exist in the database
         Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(p.toDTO());
         deleter = new EntityDeleter(pDao,iDao,mDao,null,null,null,null);
         Person result = deleter.deletePerson(p);       
         Assert.assertEquals(result, p);
     }

     @Test(expected=DependencyException.class)
     public void testDeletePersonDoesNotExistFailure() throws DependencyException, UnserialisedEntityException {
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         InvitationDAO iDao = Mockito.mock(InvitationDAO.class);
         PersonDAO pDao = Mockito.mock(PersonDAO.class);
         Person p = factory.createPerson(glopass);
         Mockito.when(mDao.getMissionsByCaptain(p.toDTO())).thenReturn(null);
         Mockito.when(iDao.getInvitationsByCreator(p.toDTO())).thenReturn(null);
         Mockito.when(iDao.getInvitationsByRecipient(p.toDTO())).thenReturn(null);
         // mocking the stub so that it thinks the peron 'p' does not exist in the database
         Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(null);
         deleter = new EntityDeleter(pDao,iDao,mDao,null,null,null,null);
         Person result = deleter.deletePerson(p);       
     }

     @Test(expected = DependencyException.class)
     public void testDeletePersonWhoIsParticipantFailure() throws UnserialisedEntityException, DependencyException {
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         InvitationDAO iDao = Mockito.mock(InvitationDAO.class);
         PersonDAO pDao = Mockito.mock(PersonDAO.class);
         Person p = factory.createPerson(glopass);
         Mission m = new Mission();
         Set<Mission> mSet = new HashSet<Mission>();
         mSet.add(m);
         p.setMissionsRegistered(mSet);
         Mockito.when(mDao.getMissionsByCaptain(p.toDTO())).thenReturn(null);
         Mockito.when(iDao.getInvitationsByCreator(p.toDTO())).thenReturn(null);
         Mockito.when(iDao.getInvitationsByRecipient(p.toDTO())).thenReturn(null);
         // mocking the stub so that it thinks the peron 'p' already exist in the database
         Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(p.toDTO());
         deleter = new EntityDeleter(pDao, iDao, mDao, null, null, null, null);
         Person result = deleter.deletePerson(p);

     }

     @Test(expected = DependencyException.class)
     public void testDeletePersonCaptainHimselfFailure() throws DependencyException, UnserialisedEntityException {
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         InvitationDAO iDao = Mockito.mock(InvitationDAO.class);
         PersonDAO pDao = Mockito.mock(PersonDAO.class);
         Person p = factory.createPerson(glopass);
         List<flymetomars.common.datatransfer.Mission> missionSet = new ArrayList<flymetomars.common.datatransfer.Mission>();
         flymetomars.common.datatransfer.Person pDTO=Mockito.any(flymetomars.common.datatransfer.Person.class);
         Mockito.when(mDao.getMissionsByCaptain(pDTO)).thenReturn(missionSet);
         Mockito.when(iDao.getInvitationsByCreator(pDTO)).thenReturn(null);
         Mockito.when(iDao.getInvitationsByRecipient(pDTO)).thenReturn(null);
         // mocking the stub so that it thinks the peron 'p' already exist in the database
         Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(pDTO);
         deleter = new EntityDeleter(pDao, iDao, mDao, null, null, null, null);
         Person result = deleter.deletePerson(p);
     }

    @Test(expected = DependencyException.class)
    public void testDeletePersonInvitationCreatorDependencyFailure() throws UnserialisedEntityException, DependencyException {
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        InvitationDAO iDao = Mockito.mock(InvitationDAO.class);
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Person p = factory.createPerson(glopass);
        List<flymetomars.common.datatransfer.Invitation> invSet = new ArrayList<flymetomars.common.datatransfer.Invitation>();
        flymetomars.common.datatransfer.Person pDTO = Mockito.any(flymetomars.common.datatransfer.Person.class);
        Mockito.when(mDao.getMissionsByCaptain(pDTO)).thenReturn(null);
        Mockito.when(iDao.getInvitationsByCreator(pDTO)).thenReturn(invSet);
        Mockito.when(iDao.getInvitationsByRecipient(pDTO)).thenReturn(null);
        // mocking the stub so that it thinks the peron 'p' already exist in the database
        Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(pDTO);
        deleter = new EntityDeleter(pDao, iDao, mDao, null, null, null, null);
        Person result = deleter.deletePerson(p);
    }

     @Test(expected = DependencyException.class)
     public void testDeletePersonInvitationRecipientDependencyFailure() throws DependencyException, UnserialisedEntityException {
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         InvitationDAO iDao = Mockito.mock(InvitationDAO.class);
         PersonDAO pDao = Mockito.mock(PersonDAO.class);
         Person p = factory.createPerson(glopass);
         List<flymetomars.common.datatransfer.Invitation> invSet = new ArrayList<flymetomars.common.datatransfer.Invitation>();
         flymetomars.common.datatransfer.Person pDTO=Mockito.any(flymetomars.common.datatransfer.Person.class);
         Mockito.when(mDao.getMissionsByCaptain(pDTO)).thenReturn(null);
         Mockito.when(iDao.getInvitationsByCreator(pDTO)).thenReturn(null);
         Mockito.when(iDao.getInvitationsByRecipient(pDTO)).thenReturn(invSet);
         // mocking the stub so that it thinks the peron 'p' already exist in the database
         Mockito.when(pDao.getPersonByEmail(p.getEmail())).thenReturn(pDTO);
         deleter = new EntityDeleter(pDao, iDao, mDao, null, null, null, null);
         Person result = deleter.deletePerson(p);
     }

    @Test (expected = NullArgumentException.class)
     public void testDeletePersonNullInputFailure() throws DependencyException {
        deleter = new EntityDeleter(null, null, null, null, null, null, null);
         Person dead  = deleter.deletePerson(null);
     }
     //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Invitation delete tests">
    /**
     * Delete Invitation test cases
     */
    @Test
    public void testDeleteInvitationNoDependencySuccess() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(glopass);
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
        deleter = new EntityDeleter(personDAO,iDao,mDao,null,null,null,null);
        // delete the mission
        Invitation result = deleter.deleteInvitation(inv);
        Assert.assertEquals(result, inv);
    }
    
    @Test(expected = DependencyException.class)
    public void testDeleteInvitationDoesNotExistFailure() throws DependencyException, UnserialisedEntityException {
        //person
        Person crecap = factory.createPerson(glopass);
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
        Mockito.when(iDao.load(inv.getId())).thenReturn(null);
        deleter = new EntityDeleter(personDAO,iDao,mDao,null,null,null,null);
        // delete the mission
        Invitation result = deleter.deleteInvitation(inv);
        Assert.assertEquals(result, inv);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDeleteInvitationNullInputFailure() throws DependencyException {
       deleter = new EntityDeleter(null, null, null, null, null, null, null);
       Invitation dead  = deleter.deleteInvitation(null);
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mission delete tests">
    /**
     * Delete mission test cases
     */
     @Test
     public void testDeleteMissionNoDependencySuccess() throws DependencyException {
         Person cap = factory.createPerson(glopass);
         cap.setEmail("arin@joy.com");
         PersonDAO personDAO = Mockito.mock(PersonDAO.class);
         Mission m = new Mission();
         m.setCaptain(cap);
         m.setId(1L);
         m.setTime(new Date());
         m.setLocation(loc);
         Mockito.when(personDAO.getPersonByEmail(m.getCaptain().getEmail())).thenReturn(cap.toDTO());
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         Mockito.when(mDao.load(1L)).thenReturn(m.toDTO());   
         deleter = new EntityDeleter(null, null, mDao, null, null, null, null);
         Mission dead = deleter.deleteMission(m);
         Assert.assertEquals(dead, m);
     }

     @Test(expected = NullArgumentException.class)
     public void testDeleteMissionNullInputFailure() throws DependencyException {
        deleter = new EntityDeleter(null, null, null, null, null, null, null);
         Mission dead  = deleter.deleteMission(null);
     }

     @Test(expected = DependencyException.class)
     public void testDeleteMissionWhenMissionDoesNotExistFailure() throws DependencyException {
         Person cap = factory.createPerson(glopass);
         cap.setEmail("arin@joy.com");
         Mission m = new Mission();
         m.setCaptain(cap);
         m.setId(1L);
         m.setTime(new Date());
         m.setLocation(loc);
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         Mockito.when(mDao.load(1L)).thenReturn(null);   
         deleter = new EntityDeleter(null, null, mDao, null, null, null, null);
         Mission dead = deleter.deleteMission(m);
         Assert.assertEquals(dead, m);
     }

     @Test(expected = DependencyException.class)
     public void testDeleteMissionWhenParticipantExitsFailure() throws DependencyException {
         Person cap = factory.createPerson(glopass);
         cap.setEmail("arin@joy.com");
         Mission m = new Mission();
         m.setCaptain(cap);
         m.setId(1L);
         m.setTime(new Date());
         m.setLocation(loc);        
         Person part = factory.createPerson(glopass);
         part.setEmail("abc@def.com");
         Set<Person> parts = new HashSet<Person>();
         parts.add(part);
         m.setParticipantSet(parts);
         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         Mockito.when(mDao.load(1L)).thenReturn(m.toDTO());   
         deleter = new EntityDeleter(null, null, mDao, null, null, null, null);
         Mission dead = deleter.deleteMission(m);
         Assert.assertEquals(dead, m);
     }

     @Test(expected = IllegalArgumentException.class)
     public void testDeleteMissionWhenInvitationsExitFailure() throws DependencyException {
         Person cap = factory.createPerson(glopass);
         cap.setEmail("arin@joy.com");    
         Mission m = new Mission();
         m.setCaptain(cap);
         m.setId(1L);
         m.setTime(new Date());
         m.setLocation(loc);
         Invitation inv1 = new Invitation();

         inv1.setCreator(cap);
         inv1.setRecipient(cap);
         inv1.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.SENT);
         inv1.setLastUpdated(new Date());
         inv1.setMission(m);      
         Set<Invitation> invs = new HashSet<Invitation>();
         invs.add(inv1);
         m.setInvitationSet(invs);

         MissionDAO mDao = Mockito.mock(MissionDAO.class);
         Mockito.when(mDao.load(1L)).thenReturn(m.toDTO());   
         deleter = new EntityDeleter(null, null, mDao, null, null, null, null);
         Mission dead = deleter.deleteMission(m);
         Assert.assertEquals(dead, m);
     }

     //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Location delete tests">
    
    @Test
    public void testDeleteLocatioNoDependencySuccess() throws UnserialisedEntityException, DependencyException {
        LocationDAO lDao = Mockito.mock(LocationDAO.class);
        loc.setId(234L);
        Mockito.when(lDao.load(loc.getId())).thenReturn(loc.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionsByLocation(loc.toDTO())).thenReturn(null);
        deleter = new EntityDeleter(null,null,mDao,lDao,null,null,null);
        Location result = deleter.deleteLocation(loc);
        Assert.assertEquals(result, loc);
    }
    
    @Test(expected=DependencyException.class)
    public void testDeleteLocationHasMissionFailure() throws DependencyException, UnserialisedEntityException {
        LocationDAO lDao = Mockito.mock(LocationDAO.class);
        loc.setId(234L);
        List<flymetomars.common.datatransfer.Mission> misSet = new ArrayList<flymetomars.common.datatransfer.Mission>();
        Mission m=new Mission();
        m.setLocation(loc);
        misSet.add(m.toDTO());
        Mockito.when(lDao.load(loc.getId())).thenReturn(loc.toDTO());
        MissionDAO mDao = Mockito.mock(MissionDAO.class);
        Mockito.when(mDao.getMissionsByLocation(loc.toDTO())).thenReturn(misSet);
        deleter = new EntityDeleter(null,null,mDao,lDao,null,null,null);
        deleter.deleteLocation(loc);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDeleteLocationNullInputFailure() throws DependencyException {
       deleter = new EntityDeleter(null, null, null, null, null, null, null);
       deleter.deleteLocation(null);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Expertise delete tests">
    
    @Test
    public void testDeleteExpertiseSuccess() throws DependencyException {
        Expertise e = new Expertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setId(121L);
        ExpertiseDAO expDao = Mockito.mock(ExpertiseDAO.class);
        // mock the behaviour so that it thinks it already exists
        Mockito.when(expDao.load(e.getId())).thenReturn(e.toDTO());
        deleter = new EntityDeleter(null, null,null,null,expDao,null,null);
        Expertise dead = deleter.deleteExpertise(e);
    }
    
    @Test(expected = DependencyException.class)
    public void testDeleteExpertiseDoesNotExistFailure() throws DependencyException {
        Expertise e = new Expertise();
        e.setName("Scuba Diving");
        e.setDescription("Someone who has knows diving well with oxigen cylinder for hours and play with ocean creatures");
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);
        e.setId(121L);
        ExpertiseDAO expDao = Mockito.mock(ExpertiseDAO.class);
        // mock the behaviour so that it thinks it already does not exist
        Mockito.when(expDao.load(e.getId())).thenReturn(null);
        deleter = new EntityDeleter(null, null,null,null,expDao,null,null);
        Expertise dead = deleter.deleteExpertise(e);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDeleteExpertiseNullInputFailure() throws DependencyException {
       deleter = new EntityDeleter(null, null, null, null, null, null, null);
       Expertise dead  = deleter.deleteExpertise(null);
    }
    
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="SaltedPassword delete tests">
    @Test
    public void testDeleteSaltedPasswordNoDependencySuccess() throws DependencyException {
        SaltedPassword pass=new SaltedPassword();
        pass.setId(1337L);
        pass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pass.getId())).thenReturn(pass.toDTO());
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        deleter = new EntityDeleter(pDao,null,null,null,null,spDao,null);
        SaltedPassword result = deleter.deleteSaltedPassword(pass);
        Assert.assertEquals(pass, result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDeleteSaltedPasswordNullInputFailure() throws DependencyException {
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        deleter = new EntityDeleter(null,null,null,null,null,spDao,null);
        deleter.deleteSaltedPassword(null);  //throws exception
    }
    
    @Test(expected = DependencyException.class)
    public void testDeleteSaltedPasswordNotSavedFailure() throws DependencyException {
        SaltedPassword sp=new SaltedPassword();
        sp.setDigest("D41D8CD98F00B204E9800998ECF8427E");
        flymetomars.common.datatransfer.SaltedPassword pass=sp.toDTO();
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pass.getId())).thenReturn(pass);
        deleter = new EntityDeleter(null,null,null,null,null,spDao,null);
        deleter.deleteSaltedPassword(sp);
    }
    
    @Test(expected = DependencyException.class)
    public void testDeleteSaltedPasswordInUseFailure() throws DependencyException {
        SaltedPassword sp=new SaltedPassword();
        sp.setId(1337L);
        sp.setDigest("D41D8CD98F00B204E9800998ECF8427E");
        flymetomars.common.datatransfer.SaltedPassword pass=sp.toDTO();
        flymetomars.common.datatransfer.Person p1=new flymetomars.common.datatransfer.PersonImpl();
        p1.setPassword(pass.getId());
        p1.setId(123L);
        flymetomars.common.datatransfer.Person p2=new flymetomars.common.datatransfer.PersonImpl();
        p2.setPassword(pass.getId());
        p2.setId(456L);
        List<flymetomars.common.datatransfer.Person> pList=new ArrayList<flymetomars.common.datatransfer.Person>();
        pList.add(p1);
        pList.add(p2);
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pass.getId())).thenReturn(pass);
        PersonDAO pDao = Mockito.mock(PersonDAO.class);
        Mockito.when(pDao.getAll()).thenReturn(pList);
        deleter = new EntityDeleter(pDao,null,null,null,null,spDao,null);
        deleter.deleteSaltedPassword(sp);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Salt delete tests">
    @Test
    public void testDeleteSaltNoDependencySuccess() throws DependencyException {
        SaltedPassword pass = new SaltedPassword();
        pass.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        Salt salt=new Salt();
        pass.setId(1337L);
        pass.setSalt(salt);
        salt.setHashedSaltKey("F00CDE");
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(salt.toDTO());
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.load(pass.getId())).thenReturn(pass.toDTO());
        deleter = new EntityDeleter(null,null,null,null,null,spDao,sDao);
        Salt result = deleter.deleteSalt(salt);
        Assert.assertEquals(salt, result);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDeleteSaltNullInputFailure() throws DependencyException {
        deleter = new EntityDeleter(null,null,null,null,null,null,null);
        deleter.deleteSalt(null);
    }
    
    @Test(expected = DependencyException.class)
    public void testDeleteSaltNotExistFailure() throws DependencyException {
        Salt salt=new Salt();
        salt.setHashedSaltKey("F00CDE");
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(null);
        deleter = new EntityDeleter(null,null,null,null,null,null,sDao);
        deleter.deleteSalt(salt);
    }
    
    @Test(expected = DependencyException.class)
    public void testDeleteSaltInUseDependencyFailure() throws DependencyException {
        SaltedPassword pass = new SaltedPassword();
        pass.setDigest("D41D8CD98F00B204E9800998ECF8427E");
        Salt salt=new Salt();
        pass.setId(1337L);
        pass.setSalt(salt);
        salt.setHashedSaltKey("F00CDE");
        SaltDAO sDao = Mockito.mock(SaltDAO.class);
        Mockito.when(sDao.load(salt.getHashedSaltKey())).thenReturn(salt.toDTO());
        SaltedPasswordDAO spDao = Mockito.mock(SaltedPasswordDAO.class);
        Mockito.when(spDao.getSaltedPasswordsSharingSameSalt(Mockito.any(flymetomars.common.datatransfer.Salt.class))).thenReturn(Collections.singletonList(pass.toDTO()));
        deleter = new EntityDeleter(null,null,null,null,null,spDao,sDao);
        Salt result = deleter.deleteSalt(salt);
        Assert.assertEquals(salt, result);
    }
    //</editor-fold>
}
